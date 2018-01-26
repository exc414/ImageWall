package io.bluephoenix.imagewall.features.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.bluephoenix.imagewall.core.data.model.User;
import io.bluephoenix.imagewall.core.data.repo.IRepository;
import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.util.Constant;

import static io.bluephoenix.imagewall.core.common.LoginResponseDef.CORRECT_CREDENTIALS;
import static io.bluephoenix.imagewall.core.common.LoginResponseDef.EMAIL_NOT_VERIFIED;
import static io.bluephoenix.imagewall.core.common.LoginResponseDef.INVALID_CREDENTIALS;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class LoginPresenter extends BasePresenter<ILoginContract.PublishToView>
    implements ILoginContract.Presenter
{
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private IRepository.Preferences preferences;

    public LoginPresenter(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase,
                          IRepository.Preferences preferences)
    {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
        this.preferences = preferences;
    }

    /**
     * Check the credentials that the user submitted on the login form.
     */
    @Override
    public void checkUserCredentials(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                boolean isUserEmailVerified = false;

                try
                {
                    isUserEmailVerified = firebaseAuth.getCurrentUser().isEmailVerified();
                    Log.d(Constant.TAG, "Is the email verified for user : "
                            + firebaseAuth.getCurrentUser().getEmail()
                            + " - " + isUserEmailVerified);
                }
                catch(NullPointerException ex)
                {
                    Log.e(Constant.TAG, "Tried to retrieve a user that does not exists."
                            + " " + ex.getMessage());
                }

                if(task.isSuccessful())
                {
                    //Extra check in case the user needs to verify their email.
                    if(!isUserEmailVerified)
                    {
                        publishToView.userCredentialsResponse(EMAIL_NOT_VERIFIED);
                    }
                    else
                    {
                        //Check whether the profile information has been saved in the
                        //preferences. If not downloaded and save it.
                        loadUserProfileInformation(firebaseAuth.getUid());
                        publishToView.userCredentialsResponse(CORRECT_CREDENTIALS);
                    }
                }
                else { publishToView.userCredentialsResponse(INVALID_CREDENTIALS); }
            }
        });
    }

    /**
     * Retrieves profile information for the user currently logged in if no profile
     * information has been saved already. The profile information is saved when the
     * user registers. However, if the user register on another device or they delete
     * it the app's data, then the information needs to be downloaded from firebase.
     *
     * @param userID The id for the currently logged in user.
     */
    private void loadUserProfileInformation(String userID)
    {
        if(!preferences.hasProfileBeenSaved())
        {
            DatabaseReference usersRef = firebaseDatabase.getReference(Constant.ROOT_NODE_USERS);
            usersRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    try
                    {
                        Log.d(Constant.TAG, "User Data snapshot : " + dataSnapshot);
                        //If the user is found save the profile information.
                        User user = dataSnapshot.getValue(User.class);
                        preferences.setProfileInformation(user);
                    }
                    catch(NullPointerException ex)
                    {
                        Log.e(Constant.TAG, "Supplied User ID does not exists or could " +
                                "not be retrieved. " + ex.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e(Constant.TAG, "Profile information could not be retrieved.");
                    Log.e(Constant.TAG, "Message : " + databaseError.getMessage());
                }
            });
        }
    }
}