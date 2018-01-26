package io.bluephoenix.imagewall.core.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class LoginResponseDef
{
    @IntDef({ CORRECT_CREDENTIALS, INVALID_CREDENTIALS, GENERAL_ERROR, EMAIL_NOT_VERIFIED })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoginResponseType { }
    public static final int CORRECT_CREDENTIALS = 1;
    public static final int INVALID_CREDENTIALS = 2;
    public static final int GENERAL_ERROR = 3;
    public static final int EMAIL_NOT_VERIFIED = 4;
}
