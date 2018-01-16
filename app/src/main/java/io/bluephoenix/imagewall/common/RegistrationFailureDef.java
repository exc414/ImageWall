package io.bluephoenix.imagewall.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class RegistrationFailureDef
{
    @IntDef({ GENERAL_ERROR, EMAIL_ALREADY_IN_USED })
    @Retention(RetentionPolicy.SOURCE)
    public @interface RegistrationFailureType { }
    public static final int GENERAL_ERROR = 0;
    public static final int EMAIL_ALREADY_IN_USED = 1;
}
