package io.bluephoenix.imagewall.features.profile;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.bluephoenix.imagewall.core.common.PasswordUpdateResponseRef;
import io.bluephoenix.imagewall.core.data.model.User;
import io.bluephoenix.imagewall.core.data.repo.IRepository;
import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.util.Constant;
import java8.util.concurrent.CompletableFuture;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class ProfilePresenter extends BasePresenter<IProfileContract.PublishToView>
    implements IProfileContract.Presenter
{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private IRepository.Preferences preferences;

    //Ref to the main thread.
    private final Handler profile = new Handler();

    public ProfilePresenter(FirebaseAuth firebaseAuth, DatabaseReference databaseReference,
                            IRepository.Preferences preferences)
    {
        this.firebaseAuth = firebaseAuth;
        this.databaseReference = databaseReference;
        this.preferences = preferences;
    }

    @Override
    public void populateProfile()
    {
        //Get profile on a background thread then post it to the main thread.
        CompletableFuture.runAsync(new Runnable()
        {
            @Override
            public void run()
            {
                final User user = preferences.getProfileInformation();

                //Anything in here runs on the main thread. Or rather the thread where
                //the handler was made.
                profile.post(new Runnable()
                {
                    @Override public void run() { publishToView.populateProfile(user); }
                });
            }
        });
    }

    /**
     * Save the user's profile.
     * @param user An object with the user's information.
     */
    @Override
    public void saveProfile(final User user, int flags)
    {
        if((flags & 1) == 1)
        {
            Log.i(Constant.TAG, "Email was changed.");
        }

        if((flags & 2) == 2)
        {
            Log.i(Constant.TAG, "Username was changed.");
        }

        if((flags & 4) == 4)
        {
            Log.i(Constant.TAG, "Both were changed.");
        }

        if((flags & 8) == 8)
        {
            Log.i(Constant.TAG, "Other options were changed.");
        }

        if((flags & 15) == 15)
        {
            Log.i(Constant.TAG, "All options were changed.");
        }
    }

    /**
     * Update the user's password. The user must provide the old password to re-authenticate.
     *
     * @param oldPassword The user's old/current password
     * @param newPassword The password the user wants to change to.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void changePassword(final String oldPassword, final String newPassword)
    {
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        try
        {
            //You must authenticate first before trying to change the password.
            AuthCredential credential = EmailAuthProvider.getCredential(
                    firebaseUser.getEmail(), oldPassword);

            firebaseUser.reauthenticate(credential).addOnCompleteListener(
                new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        firebaseUser.updatePassword(newPassword).addOnCompleteListener(
                            new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    publishToView.passwordUpdateResponse(
                                            PasswordUpdateResponseRef.UPDATE_SUCCESS);
                                }
                                else
                                {
                                    publishToView.passwordUpdateResponse(
                                            PasswordUpdateResponseRef.UPDATE_FAILED);
                                }
                            }
                        });
                    }
                    else
                    {
                        Log.e(Constant.TAG, "Auth failed in : " + getClass().getName());
                        publishToView.passwordUpdateResponse(
                                PasswordUpdateResponseRef.AUTH_FAILED);
                    }
                }
            });
        }
        catch(NullPointerException ex)
        {
            Log.e(Constant.TAG, "Current user was null in : " + getClass().getName());
            ex.printStackTrace();
        }
    }
}