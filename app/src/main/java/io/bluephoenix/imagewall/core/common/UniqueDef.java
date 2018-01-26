package io.bluephoenix.imagewall.core.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public class UniqueDef
{
    @IntDef({ EMAIL, USERNAME, ITS_UNIQUE })
    @Retention(RetentionPolicy.SOURCE)
    public @interface UniqueType { }
    public static final int EMAIL = 0;
    public static final int USERNAME = 1;
    public static final int ITS_UNIQUE = 2;
}
