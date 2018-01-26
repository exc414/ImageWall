package io.bluephoenix.imagewall.features.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import io.bluephoenix.imagewall.R;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public class CountryDialog
{
    private Activity activity;
    private IDialogCallback.Country dialogCallback;

    public CountryDialog(Activity activity)
    {
        this.activity = activity;
        this.dialogCallback = (IDialogCallback.Country) activity;
    }

    /**
     * Handle the selection of the user's country. When this is clicked a dialog appears
     * with a list of countries. The user selects one and then its 3 digits ISO code is
     * displayed in this button.
     */
    public void selectCountry()
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
