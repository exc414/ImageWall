package io.bluephoenix.imagewall.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public class PresenterDef
{
    @IntDef({ FEED, FRIENDS, FAVOURITE, LOGIN, REGISTER, PROFILE, POST, DETAIL, WALL, DEFAULT })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PresenterType { }
    public static final int FEED = 0;
    public static final int FRIENDS = 1;
    public static final int FAVOURITE = 2;
    public static final int PROFILE = 3;
    public static final int LOGIN = 4;
    public static final int REGISTER = 5;
    public static final int POST = 6;
    public static final int DETAIL = 7;
    public static final int WALL = 8;
    public static final int DEFAULT = 9;
}
