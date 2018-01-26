package io.bluephoenix.imagewall.core.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public class PresenterFragmentDef
{
    @IntDef({ FEED, FRIENDS, FAVOURITE, PROFILE })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PresenterFragmentType { }

    public static final int FEED = 0;
    public static final int FRIENDS = 1;
    public static final int FAVOURITE = 2;
    public static final int PROFILE = 3;
}
