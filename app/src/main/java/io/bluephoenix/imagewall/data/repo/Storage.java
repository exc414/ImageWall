package io.bluephoenix.imagewall.data.repo;

import io.bluephoenix.imagewall.app.Components;
import io.bluephoenix.imagewall.data.model.User;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class Storage implements IRepository.Storage
{
    @Override
    public void createUserProfile(User user)
    {
        Components.getFDBReference("users").child(user.getUserid()).setValue(user);
    }
}
