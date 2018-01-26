package io.bluephoenix.imagewall.util;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.security.SecureRandom;

import io.bluephoenix.imagewall.app.App;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public class Util
{
    private static DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

    /**
     * Calculates a dp value into pixels for the current device.
     * @param dp A virtual pixel unit that you should use when defining UI layout.
     * @return a float with the number of pixels.
     */
    public static int getPixelFromDP(int dp)
    {
        return (int) (dp * metrics.density);
    }

    /**
     * Helper - Get color-int from resource.
     * @param resource A particular resource ID.
     * @return a color associated with a particular resource ID.
     */
    public static int getColorFromResource(int resource)
    {
        return ContextCompat.getColor(App.getInstance(), resource);
    }

    /**
     * Check the password input of the user to make sure it passes the
     * impose criteria. 8 chars in length, only letters and digits and
     * at least 2 digits.
     *
     * @param password A string to check against the criteria
     * @return a boolean whether the password passed or failed the check.
     */
    public static boolean doesPasswordMeetCriteria(String password)
    {
        if(password.length() < 8) { return false; }
        else
        {
            char singleChar;
            int count = 0;
            for (int i = 0; i < password.length(); i++)
            {
                singleChar = password.charAt(i);
                if (!Character.isLetterOrDigit(singleChar))
                {
                    return false;
                }
                else if(Character.isDigit(singleChar)) { count++; }
            }

            if(count < 2) { return false; }
        }

        return true;
    }

    /**
     * Set visibility of errors text view and their message.
     *
     * @param generic         A text view object.
     * @param errorMessageRes A int which points to a string resource.
     */
    public static int setError(TextView generic, int errorMessageRes)
    {
        generic.setText(errorMessageRes);
        generic.setVisibility(View.VISIBLE);
        return 1;
    }

    /**
     * Check if the keyboard is visible. If so, hide the keyboard.
     */
    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(INPUT_METHOD_SERVICE);

        //Verify if the soft keyboard is open and not null
        if(inputMethodManager != null)
        {
            //noinspection ConstantConditions
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Compares the text inside of the password and retype password edit text.
     *
     * @return a boolean on whether the passwords match or not.
     */
    public static boolean doPasswordsMatch(String password, String retypePassword)
    {
        return password.equals(retypePassword);
    }

    /**
     * @param numberOfChars An int with the length that the token should have.
     * @return a random string of characters.
     */
    public static String generateToken(int numberOfChars)
    {
        SecureRandom random = new SecureRandom();
        long longToken = Math.abs(random.nextLong());
        return Long.toString(longToken, numberOfChars).substring(0, numberOfChars);
    }

    public static String condenseUsername(String username)
    {
        return username.replace(" ", ".");
    }
}