package io.bluephoenix.imagewall.features.register;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.core.common.PresenterActivityDef;
import io.bluephoenix.imagewall.core.common.RegistrationFailureDef;
import io.bluephoenix.imagewall.core.common.RegistrationFailureDef.RegistrationFailureType;
import io.bluephoenix.imagewall.core.data.model.User;
import io.bluephoenix.imagewall.features.base.BaseActivity;
import io.bluephoenix.imagewall.features.base.FocusListener;
import io.bluephoenix.imagewall.features.dialogs.CountryDialog;
import io.bluephoenix.imagewall.features.dialogs.IDialogCallback;
import io.bluephoenix.imagewall.features.dialogs.RegisterDialogs;
import io.bluephoenix.imagewall.features.login.LoginActivity;
import io.bluephoenix.imagewall.util.Constant;
import io.bluephoenix.imagewall.util.Util;
import io.bluephoenix.imagewall.views.CustomBtn;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class RegisterActivity extends BaseActivity implements IRegisterContract.PublishToView,
        IDialogCallback.Registration, IDialogCallback.Country
{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.constraintRegisterLayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.editTextRegisterEmail)
    EditText editTextEmail;

    @BindView(R.id.txtRegisterEmailError)
    TextView txtRegisterEmailError;

    @BindView(R.id.editTextRegisterPassword)
    EditText editTextPassword;

    @BindView(R.id.txtRegisterPasswordError)
    TextView txtRegisterPasswordError;

    @BindView(R.id.txtRegisterPasswordRetypeError)
    TextView txtRegisterPasswordRetypeError;

    @BindView(R.id.editTextRegisterRetypePassword)
    EditText editTextPasswordRetype;

    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.txtRegisterNameError)
    TextView txtRegisterNameError;

    @BindView(R.id.editTextWebsite)
    EditText editTextWebsite;

    @BindView(R.id.countryBtn)
    Button countryBtn;

    @BindView(R.id.registerBtn)
    CustomBtn registerBnt;

    private final Handler handler = new Handler();
    private RegisterPresenter registerPresenter;

    private RegisterDialogs registerDialogs;
    private MaterialDialog registerProgressDialog;
    private CountryDialog countryDialog;

    private String countryName = "Unknown";
    private int errorCount = 0;
    private long registrationStartTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setToolbarBackButton(toolbar, getString(R.string.register_activity_name));

        registerPresenter = attachPresenter(RegisterPresenter.class,
                PresenterActivityDef.REGISTER);
        registerPresenter.attachView(this);
        registerDialogs = new RegisterDialogs(this);
        countryDialog = new CountryDialog(this);

        //Once the user presses done dismiss the keyboard and remove
        //focus from the edit text and add it to the parent layout.
        listenForDoneKeyOnEditBox(editTextWebsite, constraintLayout);
        setFocusListener();

        Log.d(Constant.TAG, "onCreate : " + getClass().getName());
    }

    /**
     * Handle the selection of the user's country. When this is clicked a dialog appears
     * with a list of countries. The user selects one and then its 3 digits ISO code is
     * displayed in this button.
     */
    @OnClick(R.id.countryBtn)
    public void countryOnClick()
    {
        countryDialog.selectCountry();
    }

    /**
     * Handle when the Register button is clicked. Check all edit text for errors or invalid
     * input. Then send a request to create a new user if no errors were found.
     */
    @OnClick(R.id.registerBtn)
    public void registerOnClick()
    {
        //If anything has focus before checking the edit texts remove it.
        constraintLayout.requestFocus();

        errorCount = 0;

        //Any spaces in the name, replace with a dot. That will be the user's username.
        //They can later change it if they want.
        String username = Util.condenseUsername(editTextName.getText().toString());
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordRetype = editTextPasswordRetype.getText().toString();
        String name = editTextName.getText().toString();
        String website = editTextWebsite.getText().toString();

        //Email cannot be empty and must be validated using Apache Commons.
        if(email.trim().isEmpty())
        {
            errorCount = Util.setError(txtRegisterEmailError, R.string.error_empty);
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            errorCount = Util.setError(txtRegisterEmailError, R.string.error_email);
        }

        //Password cannot be empty and must meet at 8 chars in length, 2 numbers and
        //1 cap letter.
        if(password.trim().isEmpty())
        {
            errorCount = Util.setError(txtRegisterPasswordError, R.string.error_empty);
        }
        else if(!Util.doesPasswordMeetCriteria(password))
        {
            errorCount = Util.setError(txtRegisterPasswordError,
                    R.string.error_password_does_not_meet_criteria);
        }

        //Retype password cannot be empty and it has to match with the
        //previously typed password.
        if(passwordRetype.trim().isEmpty())
        {
            errorCount = Util.setError(txtRegisterPasswordRetypeError, R.string.error_empty);
        }
        else if(!Util.doPasswordsMatch(password, passwordRetype))
        {
            errorCount = Util.setError(txtRegisterPasswordRetypeError,
                    R.string.error_retype_password);
        }

        //Name cannot be empty.
        if(name.trim().isEmpty())
        {
            errorCount = Util.setError(txtRegisterNameError, R.string.error_empty);
        }

        //If no errors then create the user.
        //Clear focus from any edit text that may have them and clear the keyboard.
        if(errorCount == 0)
        {
            User user = new User(email, username, name, website, countryName);

            //Show the loading dialog
            registerProgressDialog = registerDialogs.progressDialog();
            registrationStartTime = SystemClock.elapsedRealtime();
            registerPresenter.createUserWithEmail(user, password);
        }

        //Hide keyboard, clear focus.
        Util.hideKeyboard(this);
        constraintLayout.requestFocus();
    }

    /**
     * When a user is successfully created take them to the login screen. Let the
     * user know that they must check their email for a verification email. Then
     * they can sign in for the first time.
     */
    @Override
    public void userCreated()
    {
        //Get the difference in time between when the dialog was started and now.
        int lapseTime =  (int) (SystemClock.elapsedRealtime() - registrationStartTime);

        //Check if the lapse time is over the force time to wait. If it is
        //the set the handler's start time at 0. If its not then set the handler's
        //start time to the difference between the two.
        int FORCE_WAIT = 1500;
        int awaitTime = (lapseTime > FORCE_WAIT) ? 0 : (FORCE_WAIT - lapseTime);

        //If at least one second has passed then dismiss the dialog.
        //This makes sure that the dialog does not flash in and out of the screen
        //if the registration happened to quickly.
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                registerProgressDialog.dismiss();
                registerDialogs.registrationSuccess(editTextEmail.getText().toString());
            }
        }, awaitTime);
    }

    /**
     * Display a failed message to the user.
     */
    @Override
    public void userCreationFailed(@RegistrationFailureType int errorType)
    {
        String errorMessage;

        switch(errorType)
        {
            case RegistrationFailureDef.EMAIL_ALREADY_IN_USED:
                 errorMessage = getString(
                         R.string.error_creating_account_email_already_in_used);
                errorCount = Util.setError(txtRegisterEmailError,
                          R.string.error_creating_account_email_already_in_used);
                 break;

            case RegistrationFailureDef.GENERAL_ERROR:
            default:
                 errorMessage = getString(R.string.error_creating_account);
                 break;
        }

        Snackbar.make(constraintLayout, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    /**
     * On positive user click take the newly register user back to the login page.
     */
    @Override
    public void onPositiveClick()
    {
        startClearPrevious(this, LoginActivity.class);
    }

    /**
     * Set the user's country.
     *
     * @param country3LetterISOCode A string with the selected country's 3 letter iso code.
     * @param countryName           A string with the country's full name.
     */
    @Override
    public void onCountrySelection(String country3LetterISOCode, String countryName)
    {
        //Set the chosen full country name to its 3 letter iso code.
        countryBtn.setText(country3LetterISOCode);
        //The color at this point is set to be like the hint text in the
        //edit text boxes. This must be change here manually to normal text
        //color as this is button.
        countryBtn.setTextColor(getResources().getColor(R.color.editTextText));
        this.countryName = countryName;
    }

    /**
     * Set focus listener on all the edit text. When the edit text gain focus, the error
     * message displayed will be hidden.
     */
    private void setFocusListener()
    {
        int[] editTextsId = { R.id.editTextRegisterEmail,  R.id.editTextRegisterPassword,
                R.id.editTextRegisterRetypePassword, R.id.editTextName };
        List<TextView> textViewsError = new ArrayList<>();

        textViewsError.add(txtRegisterEmailError);
        textViewsError.add(txtRegisterPasswordError);
        textViewsError.add(txtRegisterPasswordRetypeError);
        textViewsError.add(txtRegisterNameError);

        FocusListener focusListener = new FocusListener(editTextsId, textViewsError);

        editTextEmail.setOnFocusChangeListener(focusListener);
        editTextPassword.setOnFocusChangeListener(focusListener);
        editTextPasswordRetype.setOnFocusChangeListener(focusListener);
        editTextName.setOnFocusChangeListener(focusListener);
    }
}