package io.bluephoenix.imagewall.core.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class PasswordUpdateResponseRef
{
    @IntDef({ UPDATE_SUCCESS, UPDATE_FAILED, AUTH_FAILED })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PasswordUpdateResponseType { }
    public static final int UPDATE_SUCCESS = 1;
    public static final int UPDATE_FAILED = 2;
    public static final int AUTH_FAILED = 3;
}
