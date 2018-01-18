package io.bluephoenix.imagewall.features.register;

import io.bluephoenix.imagewall.common.RegistrationFailureDef.RegistrationFailureType;
import io.bluephoenix.imagewall.data.model.User;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public interface IRegisterContract
{
    interface PublishToView
    {
        void userCreated();
        void userCreationFailed(@RegistrationFailureType int type);
    }

    interface Presenter
    {
        /**
         * Try creating a user with email/password credentials.
         *
         * @param userInfo  An object with the user's  details.
         * @param password  A string with the user's password.
         */
        void createUserWithEmail(User userInfo, String password);

        void createUserWithGoogle(String email, String password);
        void createUserWithFacebook(String email, String password);
    }
}
