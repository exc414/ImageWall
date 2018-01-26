package io.bluephoenix.imagewall.features.profile;

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
import io.bluephoenix.imagewall.core.common.PasswordUpdateResponseRef;
import io.bluephoenix.imagewall.core.common.PasswordUpdateResponseRef.PasswordUpdateResponseType;
import io.bluephoenix.imagewall.core.common.PresenterActivityDef;
import io.bluephoenix.imagewall.core.common.UniqueDef;
import io.bluephoenix.imagewall.core.common.UniqueDef.*;
import io.bluephoenix.imagewall.core.data.model.User;
import io.bluephoenix.imagewall.features.base.BaseActivity;
import io.bluephoenix.imagewall.features.base.FocusListener;
import io.bluephoenix.imagewall.features.dialogs.CountryDialog;
import io.bluephoenix.imagewall.features.dialogs.IDialogCallback;
import io.bluephoenix.imagewall.features.dialogs.ProfileDialogs;
import io.bluephoenix.imagewall.util.Constant;
import io.bluephoenix.imagewall.util.Util;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class EditProfileActivity extends BaseActivity implements IProfileContract.PublishToView,
        IDialogCallback.EditProfile, IDialogCallback.Country
{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.constraintEditProfileLayout)
    ConstraintLayout constraintEditProfileLayout;

    @BindView(R.id.edtEditProfileName)
    EditText edtEditProfileName;

    @BindView(R.id.edtEditProfileUserName)
    EditText edtEditProfileUserName;

    @BindView(R.id.edtEditProfileWebsite)
    EditText edtEditProfileWebsite;

    @BindView(R.id.btnEditProfileCountry)
    Button btnEditProfileCountry;

    @BindView(R.id.edtEditProfileEmail)
    EditText edtEditProfileEmail;

    @BindView(R.id.btnEditProfilePassword)
    Button btnEditProfilePassword;

    @BindView(R.id.txtEditProfileEmailError)
    TextView txtEditProfileEmailError;

    @BindView(R.id.txtEditProfileNameError)
    TextView txtEditProfileNameError;

    @BindView(R.id.txtEditProfileWebsiteError)
    TextView txtEditProfileWebsiteError;

    @BindView(R.id.txtEditProfileUserNameError)
    TextView txtEditProfileUsernameError;

    private ProfilePresenter profilePresenter;
    private ProfileDialogs profileDialogs;
    private CountryDialog countryDialog;

    private User user;

    private String name;
    private String username;
    private String website;
    private String country;
    private String email;

    int flags = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setToolbarBackButton(toolbar, getString(R.string.edit_profile_activity_name));

        profilePresenter = attachPresenter(ProfilePresenter.class,
                PresenterActivityDef.EDIT_PROFILE);
        profilePresenter.attachView(this);
        profileDialogs = new ProfileDialogs(this);
        countryDialog = new CountryDialog(this);

        setFocusListener();
        profilePresenter.populateProfile();
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
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
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

        if(id == R.id.saveMenu)
        {
            saveProfile();
            return true;
        }
        else if(item.getItemId() == android.R.id.home)
        {
            //Before leaving check that the fields have not change. If they have
            //ask the user if they really want to leave. Let the user know that their
            //changes will not be saved.
            if(checkIfUserProfileHasChanged())
            {
                //Show dialog
                profileDialogs.infoWillBeLostIfNotSaved();
            }
            else { finish(); }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnEditProfileCountry)
    public void countryOnClick()
    {
        countryDialog.selectCountry();
    }

    @OnClick(R.id.btnEditProfilePassword)
    public void passwordOnClick()
    {
        profileDialogs.changePassword();
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
        btnEditProfileCountry.setText(countryName);
    }

    /**
     * Update the user's password.
     *
     * @param oldPassword The user's old/current password
     * @param newPassword The password the user wants to change to.
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword)
    {
        profilePresenter.changePassword(oldPassword, newPassword);
    }

    /**
     * On start up populate the user's profile.
     *
     * @param user An object with all the user's profile information.
     */
    @Override
    public void populateProfile(User user)
    {
        //Save the loaded user.
        this.user = user;

        edtEditProfileName.setText(user.getName());
        edtEditProfileUserName.setText(user.getUsername());
        edtEditProfileWebsite.setText(user.getWebsite());
        btnEditProfileCountry.setText(user.getCountry());
        edtEditProfileEmail.setText(user.getEmail());
    }


    /**
     * Save the user's profile information only if the information has changed and
     * there are no errors.
     */
    private void saveProfile()
    {
        //If anything has focus before checking the edit texts remove it.
        constraintEditProfileLayout.requestFocus();

        int errorCount = 0;

        if(checkIfUserProfileHasChanged())
        {
            if(username.trim().isEmpty())
            { errorCount = Util.setError(txtEditProfileUsernameError, R.string.error_empty); }

            //Name cannot be empty.
            if(name.trim().isEmpty())
            { errorCount = Util.setError(txtEditProfileNameError, R.string.error_empty); }

            if(!Patterns.WEB_URL.matcher(website).matches() && !website.isEmpty())
            { errorCount = Util.setError(txtEditProfileWebsiteError, R.string.error_website); }

            //Email cannot be empty and must be validated using Apache Commons.
            if(email.trim().isEmpty())
            {
                errorCount = Util.setError(txtEditProfileEmailError, R.string.error_empty);
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                errorCount = Util.setError(txtEditProfileEmailError, R.string.error_email);
            }

            if(errorCount == 0)
            {
                user.setName(name);
                user.setUsername(username);
                user.setWebsite(website);
                user.setCountry(country);
                user.setEmail(email);

                profilePresenter.saveProfile(user, flags);
            }
        }

        //Hide keyboard, clear focus.
        Util.hideKeyboard(this);
        constraintEditProfileLayout.requestFocus();
    }

    @Override
    public void isUnique(@UniqueType int uniqueType)
    {
        switch(uniqueType)
        {
            case UniqueDef.EMAIL:

                txtEditProfileEmailError.setText(R.string.snack_bar_email_already_in_used);
                Snackbar.make(constraintEditProfileLayout,
                        R.string.snack_bar_email_already_in_used, Snackbar.LENGTH_SHORT).show();
                break;

            case UniqueDef.USERNAME:

                txtEditProfileUsernameError.setText(R.string.snack_bar_username_already_in_used);
                Snackbar.make(constraintEditProfileLayout,
                        R.string.snack_bar_username_already_in_used,
                        Snackbar.LENGTH_SHORT).show();
                break;

            case UniqueDef.ITS_UNIQUE:
            default:

                Snackbar.make(constraintEditProfileLayout,
                        getString(R.string.snack_bar_successfully_updated_profile),
                        Snackbar.LENGTH_SHORT);
                break;
        }
    }

    /**
     * @param responseType An int which detonates if the updating process succeed/failed.
     */
    @Override
    public void passwordUpdateResponse(@PasswordUpdateResponseType int responseType)
    {
        switch(responseType)
        {
            case PasswordUpdateResponseRef.UPDATE_SUCCESS:

                 profileDialogs.authSuccess();
                 Snackbar.make(constraintEditProfileLayout,
                         getString(R.string.snack_bar_successfully_update_password),
                         Snackbar.LENGTH_SHORT).show();
                 break;

            case PasswordUpdateResponseRef.UPDATE_FAILED:

                 //Unknown error.
                 Snackbar.make(constraintEditProfileLayout,
                         getString(R.string.snack_bar_failed_update_password),
                         Snackbar.LENGTH_SHORT).show();
                 break;

            case PasswordUpdateResponseRef.AUTH_FAILED:

                 //Let the user know that their password (old password) was incorrect.
                 profileDialogs.authFailed();
                 break;

            default: break;
        }
    }

    /**
     * Check whether the user has change some/all of the information.
     * This is done to one not have to constantly save if nothing has changed. Two, so that
     * if the user taps the back arrow a dialog will pop up and remind the user that they
     * have not saved their information.
     *
     * @return A boolean on whether something has changed.
     */
    private boolean checkIfUserProfileHasChanged()
    {
        flags = 0;

        name = edtEditProfileName.getText().toString();
        username = edtEditProfileUserName.getText().toString();
        website = edtEditProfileWebsite.getText().toString();
        country = btnEditProfileCountry.getText().toString();
        email = edtEditProfileEmail.getText().toString();

        //Old email compare to newly typed email by the user.
        //Username and email must be unique so they have to be check separately.
        if(!user.getEmail().equalsIgnoreCase(email))
        {
            flags ++;
        }

        if(!user.getUsername().equalsIgnoreCase(username))
        {
            flags = (flags == 1) ? 3 : 2;
        }

        //If any fields has been changed send back true
        if(!user.getName().equalsIgnoreCase(name)
            || !user.getWebsite().equalsIgnoreCase(website)
            || !user.getCountry().equalsIgnoreCase(country))
        {
            //flags += 4;
        }

        Log.i(Constant.TAG, "Flag value is : " + flags);

        return (flags != 0);
    }

    /**
     * Set focus listener on all the edit text. When the edit text gain focus, the error
     * message displayed will be hidden.
     */
    private void setFocusListener()
    {
        int[] editTextsId = { R.id.edtEditProfileName, R.id.edtEditProfileUserName,
                R.id.edtEditProfileWebsite, R.id.edtEditProfileEmail };
        List<TextView> textViewsError = new ArrayList<>();

        textViewsError.add(txtEditProfileNameError);
        textViewsError.add(txtEditProfileUsernameError);
        textViewsError.add(txtEditProfileWebsiteError);
        textViewsError.add(txtEditProfileEmailError);

        FocusListener focusListener = new FocusListener(editTextsId, textViewsError);

        edtEditProfileName.setOnFocusChangeListener(focusListener);
        edtEditProfileUserName.setOnFocusChangeListener(focusListener);
        edtEditProfileWebsite.setOnFocusChangeListener(focusListener);
        edtEditProfileEmail.setOnFocusChangeListener(focusListener);
    }
}