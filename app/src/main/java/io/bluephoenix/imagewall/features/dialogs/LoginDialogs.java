package io.bluephoenix.imagewall.features.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Patterns;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import io.bluephoenix.imagewall.R;

/**
 * @author Carlos A. Perez Zubizarreta
 */
@SuppressWarnings("ConstantConditions")
public class LoginDialogs
{
    private Activity activity;

    public LoginDialogs(Activity activity)
    {
        this.activity = activity;
    }

    /**
     * Display a dialog with an edit text box. The user can enter their email
     * and receive instructions in that email on how to recover their password.
     * The email used must be the same email used to register.
     */
    public void forgotPassword()
    {
        new MaterialDialog.Builder(activity)
            .title(R.string.dialog_forgot_password_title)
            .content(R.string.dialog_forgot_password_content)
            .inputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            .theme(Theme.LIGHT)
            .positiveText(R.string.dialog_forgot_password_positive_button)
            .negativeText(R.string.dialog_forgot_password_negative_button)
            .widgetColorRes(R.color.colorPrimary)
            .positiveColorRes(R.color.colorPrimary)
            .negativeColorRes(R.color.colorPrimary)
            .autoDismiss(false)
            .cancelable(false)
            .onAny(new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which)
                {
                    if(which == DialogAction.POSITIVE)
                    {
                        try
                        {
                            //Check if the email is valid.
                            String email = dialog.getInputEditText().getText().toString();
                            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                            {
                                //TODO send recovery instructions to reset password.
                                dialog.dismiss();
                            }
                            else
                            {
                                dialog.getInputEditText().setText("");
                                dialog.getInputEditText().setError(
                                        activity.getResources().getString
                                        (R.string.dialog_forgot_password_invalid_email));
                            }
                        }
                        catch(NullPointerException ex)
                        {
                            ex.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                    else if(which == DialogAction.NEGATIVE) { dialog.dismiss(); }
                }
            })
            .input(R.string.dialog_edit_text_forgot_password_hint,
                    R.string.dialog_edit_text_forgot_password_prefill,
                    new MaterialDialog.InputCallback()
                    {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog,
                                            CharSequence input) { }
                    }).show();
    }
}
