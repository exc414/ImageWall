package io.bluephoenix.imagewall.features.login;

import io.bluephoenix.imagewall.common.LoginResponseDef.LoginResponseType;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public interface ILoginContract
{
    interface PublishToView
    {
        /**
         * Publish the result of checking the user's credentials.
         *
         * @param responseType An int with the result of the cred check.
         */
        void userCredentialsResponse(@LoginResponseType int responseType);
    }

    interface Presenter
    {
        /**
         * Check the credentials that the user submitted on the login form.
         */
        void checkUserCredentials(String email, String password);
    }
}
