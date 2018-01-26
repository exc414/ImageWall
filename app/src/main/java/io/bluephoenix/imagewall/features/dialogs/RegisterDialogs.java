package io.bluephoenix.imagewall.features.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import io.bluephoenix.imagewall.R;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class RegisterDialogs
{
    private Activity activity;
    private IDialogCallback.Registration dialogCallback;

    public RegisterDialogs(Activity activity)
    {
        this.activity = activity;
        this.dialogCallback = (IDialogCallback.Registration) activity;
    }

    /**
     * Show an infinite progress animation while the user's account is being created.
     * @return a material dialog.
     */
    public MaterialDialog progressDialog()
    {
        return new MaterialDialog.Builder(activity)
                .title(R.string.dialog_registration_progress_title)
                .content(R.string.dialog_registration_progress_content)
                .titleColorRes(R.color.colorAccent)
                .positiveColorRes(R.color.colorPrimary)
                .widgetColorRes(R.color.colorPrimary)
                .theme(Theme.LIGHT)
                .progress(true, 0).show();
    }

    /**
     * If the user was created successfully then show this success dialog explaining
     * that they need to check their mail box for a verification email.
     */
    public void registrationSuccess(String email)
    {
        String content = activity.getString(R.string.dialog_registration_content, email);

        new MaterialDialog.Builder(activity)
            .title(R.string.dialog_registration_success_title)
            .titleColorRes(R.color.colorAccent)
            .positiveColorRes(R.color.colorPrimary)
            .content(content)
            .theme(Theme.LIGHT)
            .positiveText(R.string.dialog_registration_positive_button)
            .autoDismiss(false)
            .cancelable(false)
            .onPositive(new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which)
                {
                    dialogCallback.onPositiveClick();
                    dialog.dismiss();
                }
            }).show();
    }
}
