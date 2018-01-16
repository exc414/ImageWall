package io.bluephoenix.imagewall.features.wall;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public interface IWallContract
{
    interface PublishToView
    {
        /**
         * Publish the result of checking if the user is already logged in.
         *
         * @param isUserLoggedIn A boolean with true if user already logged in.
         */
        void isUserLoggedInResponse(boolean isUserLoggedIn);
    }

    interface Presenter
    {
        /**
         * At the start of the application check if the user has been previously logged in.
         * This adds the auth listener.
         */
        void isUserAlreadyLoggedIn();
    }
}
