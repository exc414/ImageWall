package io.bluephoenix.imagewall.data.repo;

import com.google.firebase.database.FirebaseDatabase;

import io.bluephoenix.imagewall.data.model.User;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class Storage implements IRepository.Storage
{
    private FirebaseDatabase firebaseDatabase;

    public Storage(FirebaseDatabase firebaseDatabase)
    {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void createUserProfile(User user)
    {
        firebaseDatabase.getReference("users").child(user.getUserid()).setValue(user);
    }
}
