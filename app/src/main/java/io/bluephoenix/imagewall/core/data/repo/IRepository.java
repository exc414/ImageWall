package io.bluephoenix.imagewall.core.data.repo;

import io.bluephoenix.imagewall.core.data.model.User;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public interface IRepository
{
    interface Preferences
    {
        boolean hasProfileBeenSaved();
        void setProfileInformation(User user);
        User getProfileInformation();
    }

    interface Storage
    {
        void createUserProfile(User user);
    }
}
