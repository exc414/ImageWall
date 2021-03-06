package io.bluephoenix.imagewall.features.login;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.core.common.LoginResponseDef;
import io.bluephoenix.imagewall.core.common.LoginResponseDef.LoginResponseType;
import io.bluephoenix.imagewall.core.common.PresenterActivityDef;
import io.bluephoenix.imagewall.features.base.BaseActivity;
import io.bluephoenix.imagewall.features.base.FocusListener;
import io.bluephoenix.imagewall.features.dialogs.IDialogCallback;
import io.bluephoenix.imagewall.features.dialogs.LoginDialogs;
import io.bluephoenix.imagewall.features.register.RegisterActivity;
import io.bluephoenix.imagewall.features.wall.WallActivity;
import io.bluephoenix.imagewall.util.Constant;
import io.bluephoenix.imagewall.util.Util;
import io.bluephoenix.imagewall.views.CustomBtn;

public class LoginActivity extends BaseActivity implements ILoginContract.PublishToView,
        IDialogCallback.Login
{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loginBtn)
    CustomBtn loginBtn;

    @BindView(R.id.forgotPasswordBtn)
    Button forgotPasswordBtn;

    @BindView(R.id.editTextLoginEmail)
    EditText editTextEmail;

    @BindView(R.id.editTextLoginPassword)
    EditText editTextPassword;

    @BindView(R.id.createAccountBtn)
    Button createAccountBtn;

    @BindView(R.id.constraintLoginLayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.txtLoginEmailError)
    TextView txtEmailError;

    @BindView(R.id.txtLoginPasswordError)
    TextView txtPasswordError;

    private LoginPresenter loginPresenter;
    private LoginDialogs loginDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setToolbar(toolbar, getString(R.string.login_activity_name));

        loginPresenter = attachPresenter(LoginPresenter.class, PresenterActivityDef.LOGIN);
        loginPresenter.attachView(this);
        loginDialogs = new LoginDialogs(this);

        //Once the user presses done dismiss the keyboard and remove
        //focus from the edit text and add it to the parent layout.
        listenForDoneKeyOnEditBox(editTextPassword, constraintLayout);
        setFocusListener();

        Log.d(Constant.TAG, "onCreate : " + getClass().getName());
    }

    /**
     * Create the menu.
     *
     * @param menu A menu to inflate.
     * @return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    /**
     * Handles what happens when a button in the toolbar gets clicked.
     *
     * @param item A MenuItem object with the id of the pressed button.
     * @return whether the item was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.createAccount)
        {
            start(RegisterActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle when the Login button is clicked. Check all edit text for errors or invalid
     * input. Then check their credentials if no errors were found.
     */
    @OnClick(R.id.loginBtn)
    public void loginOnClick()
    {
        //If anything has focus before checking the edit texts remove it.
        constraintLayout.requestFocus();

        int errorCount = 0;
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        //Check if the email field is empty. If not then check if its a valid email
        //address.
        if(email.trim().isEmpty())
        {
            errorCount = Util.setError(txtEmailError, R.string.error_empty);
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            errorCount = Util.setError(txtEmailError, R.string.error_email);
        }

        //Check if the password field is empty. If not then check if it meets the minimum
        //length.
        if(password.trim().isEmpty())
        {
            errorCount = Util.setError(txtPasswordError, R.string.error_empty);
        }
        else if(password.length() < 8)
        {
            errorCount = Util.setError(txtPasswordError, R.string.error_password);
        }

        //If there are no errors with the user's input then check his/her credentials.
        //Clear focus from any edit text that may have them and clear the keyboard.
        if(errorCount == 0)
        { loginPresenter.checkUserCredentials(email, password); }

        //Hide keyboard, clear focus.
        Util.hideKeyboard(this);
        constraintLayout.requestFocus();
    }

    /**
     * Display a dialog with an edit text box. The user can enter their email
     * and receive instructions in that email on how to recover their password.
     * The email used must be the same email used to register.
     */
    @OnClick(R.id.forgotPasswordBtn)
    public void forgotPasswordOnClick()
    {
        loginDialogs.forgotPassword();
    }

    /**
     * Start the register activity if the user wishes to make an account.
     */
    @OnClick(R.id.createAccountBtn)
    public void createAccountOnClick()
    {
        start(RegisterActivity.class);
    }

    /**
     * Based on the responseType either take the user to the correct activity or
     * show an error message.
     *
     * @param responseType An int with the result of the cred check.
     */
    @Override
    public void userCredentialsResponse(@LoginResponseType int responseType)
    {
        Log.i(Constant.TAG, "userCredentialsResponse : " + getClass().getName());
        switch(responseType)
        {
            case LoginResponseDef.CORRECT_CREDENTIALS:
                startClearPrevious(LoginActivity.this, WallActivity.class);
                break;

            case LoginResponseDef.INVALID_CREDENTIALS:
                Snackbar.make(constraintLayout, R.string.invalid_credentials_error_message,
                        Snackbar.LENGTH_SHORT).show();
                break;

            case LoginResponseDef.EMAIL_NOT_VERIFIED:
                Snackbar.make(constraintLayout, R.string.email_is_not_verified,
                        Snackbar.LENGTH_SHORT).show();
                break;

            case LoginResponseDef.GENERAL_ERROR:
            default:
                break;
        }
    }

    /**
     * Set focus listener on all the edit text. When the edit text gain focus, the error
     * message displayed will be hidden.
     */
    private void setFocusListener()
    {
        int[] editTextsId = { R.id.editTextLoginEmail,  R.id.editTextLoginPassword };
        List<TextView> textViews = new ArrayList<>();

        textViews.add(txtEmailError);
        textViews.add(txtPasswordError);

        FocusListener focusListener = new FocusListener(editTextsId, textViews);

        editTextEmail.setOnFocusChangeListener(focusListener);
        editTextPassword.setOnFocusChangeListener(focusListener);
    }
}