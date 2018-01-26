package io.bluephoenix.imagewall.features.dialogs;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public interface IDialogCallback
{
    interface Login { }

    interface Registration
    {
        /**
         * Positive click refers to action such as OK or SEND.
         */
        void onPositiveClick();
    }

    interface EditProfile
    {
        void updatePassword(String oldPassword, String newPassword);
    }

    interface Country
    {
        /**
         * Set the user's country.
         *
         * @param country3LetterISOCode A string with the selected country's 3 letter iso code.
         * @param countryName           A string with the country's full name.
         */
        void onCountrySelection(String country3LetterISOCode, String countryName);
    }
}
