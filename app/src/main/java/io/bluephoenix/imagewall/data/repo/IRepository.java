package io.bluephoenix.imagewall.data.repo;

import io.bluephoenix.imagewall.data.model.User;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public interface IRepository
{
    interface Preferences
    {
        void getProfileInformation();
    }

    interface Storage
    {
        void createUserProfile(User user);
    }
}
