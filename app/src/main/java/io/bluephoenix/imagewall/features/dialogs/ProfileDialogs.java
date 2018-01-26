package io.bluephoenix.imagewall.features.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.ArrayList;
import java.util.List;

import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.features.base.FocusListener;
import io.bluephoenix.imagewall.util.Util;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class ProfileDialogs implements IDialogPublish
{
    private Activity activity;
    private IDialogCallback.EditProfile dialogCallback;

    private ConstraintLayout constraintLayout;

    private TextView oldPasswordError;
    private TextView newPasswordError;
    private TextView newPasswordRetypeError;

    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtNewPasswordRetype;

    private boolean isSetUpDone = false;

    private MaterialDialog changePasswordDialog;

    public ProfileDialogs(Activity activity)
    {
        this.activity = activity;
        this.dialogCallback = (IDialogCallback.EditProfile) activity;
    }

    /**
     * Let the user know that their profile is being updated.
     */
    public MaterialDialog updatingProfile()
    {
        return new MaterialDialog.Builder(activity)
                .title(R.string.edit_profile_update_dialog_title)
                .titleColorRes(R.color.colorAccent)
                .content(R.string.edit_profile_update_dialog_content)
                .autoDismiss(false)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    /**
     * Let the user know that there is information that will be lost
     * if they do not save it.
     */
    public void infoWillBeLostIfNotSaved()
    {

    }

    /**
     * Change the user's password
     */
    public void changePassword()
    {
        //currentPasswordCheck.passwordCheck();
        MaterialDialog materialDialog = new MaterialDialog.Builder(activity)
            .title(R.string.edit_profile_password_change_dialog_title)
            .titleColorRes(R.color.colorAccent)
            .positiveColorRes(R.color.colorPrimary)
            .negativeColorRes(R.color.colorPrimary)
            .customView(R.layout.dialog_password_view, true)
            .positiveText(R.string.edit_profile_password_change_positive_button)
            .negativeText(R.string.edit_profile_password_change_dialog_negative_button)
            .theme(Theme.LIGHT)
            .autoDismiss(false)
            .cancelable(false)
            .onPositive(new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which)
                {
                    if(!isSetUpDone) { setUp(dialog); }
                    process();
                }
            })
            .onNegative(new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which)
                {
                    //If not done then dialog could be called again but setUp() method
                    //wont be called again.
                    isSetUpDone = false;
                    dialog.dismiss();
                }
            }).build();

        materialDialog.show();
    }

    /**
     * Set up all the views inside of the custom dialog.
     *
     * @param dialog The dialog from where the views can be extracted.
     */
    @SuppressWarnings("ConstantConditions")
    private void setUp(MaterialDialog dialog)
    {
        changePasswordDialog = dialog;
        View dialogView = dialog.getCustomView();

        //Only need to come into setup once.
        isSetUpDone = true;

        constraintLayout = dialogView.findViewById(R.id.constraintEditProfileLayout);

        edtOldPassword = dialogView.findViewById(R.id.oldPassword);
        edtNewPassword = dialogView.findViewById(R.id.newPassword);
        edtNewPasswordRetype = dialogView.findViewById(R.id.newPasswordRetype);

        oldPasswordError = dialogView.findViewById(
                R.id.txtEditTextDialogOldPasswordError);

        newPasswordError = dialogView.findViewById(
                R.id.txtEditTextDialogNewPasswordError);

        newPasswordRetypeError = dialogView.findViewById(
                R.id.txtEditTextDialogNewPasswordRetypeError);

        setFocusListener();
    }

    /**
     * Check that the fields are not empty, pass password criteria. Then if there are
     * no errors post it to to the activity for firebase processing.
     */
    private void process()
    {
        constraintLayout.requestFocus();
        int errorCount = 0;

        String oldPassword = edtOldPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String newPasswordRetype = edtNewPasswordRetype.getText().toString();

        if(oldPassword.isEmpty())
        {
            errorCount = Util.setError(oldPasswordError, R.string.error_empty);
        }
        else if(!Util.doesPasswordMeetCriteria(oldPassword))
        {
            errorCount = Util.setError(oldPasswordError,
                    R.string.error_password_does_not_meet_criteria);
        }

        if(newPassword.trim().isEmpty())
        {
            errorCount = Util.setError(newPasswordError, R.string.error_empty);
        }
        else if(!Util.doesPasswordMeetCriteria(newPassword))
        {
            errorCount = Util.setError(newPasswordError,
                    R.string.error_password_does_not_meet_criteria);
        }

        if(newPasswordRetype.trim().isEmpty())
        {
            errorCount = Util.setError(newPasswordRetypeError, R.string.error_empty);
        }
        else if(!Util.doPasswordsMatch(newPassword, newPasswordRetype))
        {
            errorCount = Util.setError(newPasswordRetypeError,
                    R.string.error_retype_password);
        }

        //Send password if all checks pass.
        if(errorCount == 0)
        {
            dialogCallback.updatePassword(oldPassword, newPassword);
        }

        Util.hideKeyboard(activity);
        constraintLayout.requestFocus();
    }

    /**
     * Set focus listener on all the edit text. When the edit text gain focus, the error
     * message displayed will be hidden.
     */
    private void setFocusListener()
    {
        int[] editTextsId = { R.id.oldPassword, R.id.newPassword, R.id.newPasswordRetype };
        List<TextView> textViewsError = new ArrayList<>();

        textViewsError.add(oldPasswordError);
        textViewsError.add(newPasswordError);
        textViewsError.add(newPasswordRetypeError);

        FocusListener focusListener = new FocusListener(editTextsId, textViewsError);

        edtOldPassword.setOnFocusChangeListener(focusListener);
        edtNewPassword.setOnFocusChangeListener(focusListener);
        edtNewPasswordRetype.setOnFocusChangeListener(focusListener);
    }

    @Override
    public void authSuccess() { changePasswordDialog.dismiss(); }

    /**
     * Let the user know that their password (old password) was incorrect.
     */
    @Override
    public void authFailed()
    {
        Util.setError(oldPasswordError, R.string.error_auth_failed);
    }
}