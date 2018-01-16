package io.bluephoenix.imagewall.features.register;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.features.base.IDialogCallback;

/**
 * @author Carlos A. Perez Zubizarreta
 */
class RegisterDialogs
{
    private Activity activity;
    private IDialogCallback.Registration dialogCallback;

    RegisterDialogs(Activity activity)
    {
        this.activity = activity;
        this.dialogCallback = (IDialogCallback.Registration) activity;
    }

    /**
     * Show an infinite progress animation while the user's account is being created.
     * @return a material dialog.
     */
    MaterialDialog progressDialog()
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
    void registrationSuccess(String email)
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

    /**
     * Handle the selection of the user's country. When this is clicked a dialog appears
     * with a list of countries. The user selects one and then its 3 digits ISO code is
     * displayed in this button.
     */
    void registrationSelectCountry()
    {
        //3 letter ISO country code array
        final String[] isoCode = activity.getResources().getStringArray(R.array.iso_3_letter);

        new MaterialDialog.Builder(activity)
            .title(R.string.countries_dialog_title)
            .titleColorRes(R.color.colorAccent)
            .positiveColorRes(R.color.colorPrimary)
            .theme(Theme.LIGHT)
            .items(R.array.countries)
            .choiceWidgetColor(activity.getResources().getColorStateList(
                    R.color.radio_btn_color_state_list))
            .positiveText(R.string.countries_dialog_positive_button)
            .autoDismiss(false)
            .cancelable(false)
            .onPositive(new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which)
                { dialog.dismiss(); }
            })
            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice()
            {
                @Override
                public boolean onSelection(MaterialDialog dialog, View view, int which,
                                           CharSequence countryName)
                {
                    //Check that the user selected something
                    if(countryName != null)
                    {
                        dialogCallback.onCountrySelection(isoCode[which],
                                countryName.toString());
                    }
                    return true;
                }
            }).show();
    }
}
