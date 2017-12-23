package io.bluephoenix.imagewall.util;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import io.bluephoenix.imagewall.app.App;

/**
 * @author Carlos A. Perez
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
     * Calculates a dp value into pixels for the current device.
     * @param dp A virtual pixel unit that you should use when defining UI layout.
     * @return a float with the number of pixels.
     */
    public static float getPixelFromDP(float dp)
    {
        return (dp * metrics.density);
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
}
