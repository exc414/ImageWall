package io.bluephoenix.imagewall.features.profile;

import io.bluephoenix.imagewall.core.common.PasswordUpdateResponseRef.PasswordUpdateResponseType;
import io.bluephoenix.imagewall.core.common.UniqueDef.*;
import io.bluephoenix.imagewall.core.data.model.User;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public interface IProfileContract
{
    interface PublishToView
    {
        void populateProfile(User user);
        void passwordUpdateResponse(@PasswordUpdateResponseType int responseType);
        void isUnique(@UniqueType int uniqueType);
    }

    interface Presenter
    {
        void populateProfile();
        void saveProfile(final User user, int flags);
        void changePassword(final String oldPassword, final String newPassword);
    }
}
