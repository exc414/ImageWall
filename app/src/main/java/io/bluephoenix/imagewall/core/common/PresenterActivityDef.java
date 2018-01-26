package io.bluephoenix.imagewall.core.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class PresenterActivityDef
{
    @IntDef({ LOGIN, REGISTER, POST, DETAIL, WALL, EDIT_PROFILE })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PresenterActivityType { }

    public static final int LOGIN = 0;
    public static final int REGISTER = 1;
    public static final int POST = 2;
    public static final int DETAIL = 3;
    public static final int WALL = 4;
    public static final int EDIT_PROFILE = 5;
}
