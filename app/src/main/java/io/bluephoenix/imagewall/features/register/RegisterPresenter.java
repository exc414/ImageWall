package io.bluephoenix.imagewall.features.register;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.bluephoenix.imagewall.core.common.RegistrationFailureDef;
import io.bluephoenix.imagewall.core.data.model.User;
import io.bluephoenix.imagewall.core.data.repo.IRepository;
import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.util.Constant;
import io.bluephoenix.imagewall.util.Util;

/**
 * @author Carlos A. Perez Zubizarreta
 */
@SuppressWarnings("ConstantConditions")
public class RegisterPresenter extends BasePresenter<IRegisterContract.PublishToView>
        implements IRegisterContract.Presenter
{
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private IRepository.Storage storage;
    private IRepository.Preferences preferences;
    private User user;
    private int errorMessage = RegistrationFailureDef.GENERAL_ERROR;

    public RegisterPresenter(FirebaseAuth firebaseAuth, FirebaseDatabase firebaseDatabase,
                             IRepository.Storage storage, IRepository.Preferences preferences)
    {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
        this.preferences = preferences;
        this.storage = storage;
    }

    /**
     * Try creating a user with email/password credentials.
     *
     * @param userInfo  An object with the user's  details.
     * @param password  A string with the user's password.
     */
    @Override
    public void createUserWithEmail(User userInfo, String password)
    {
        this.user = userInfo;
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    //If successful set the user id to the user object.
                    //As before this it was null.
                    if(task.isSuccessful())
                    {
                        user.setUserid(firebaseAuth.getUid());
                        sendVerificationEmail();
                    }
                    else
                    {
                        //If the email address already exist let the user know.
                        if(task.getException() instanceof FirebaseAuthUserCollisionException)
                        {
                            errorMessage = RegistrationFailureDef.EMAIL_ALREADY_IN_USED;
                        }
                    }

                    //Set the auth listener so that the user details can be inserted into
                    //the real time database.
                    authStateListener.onAuthStateChanged(firebaseAuth);

                    Log.v(Constant.TAG, "Created user with user id : " + user.getUserid());
                    Log.v(Constant.TAG, "Created user with email : " + user.getEmail());
                }
            });
    }

    @Override
    public void createUserWithGoogle(String email, String password) { }

    @Override
    public void createUserWithFacebook(String email, String password) { }

    /**
     * Listen for any authentication changes.
     */
    private FirebaseAuth.AuthStateListener authStateListener =
        new FirebaseAuth.AuthStateListener()
    {
        @Override
        public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth)
        {
            Log.i(Constant.TAG, "onAuthStateChanged : " + getClass().getName());

            firebaseDatabase.getReference().addListenerForSingleValueEvent(
                    new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Log.i(Constant.TAG, "onDataChange : " + getClass().getName());
                    //If the username chosen exists already add random characters to it.
                    //The user can later change this in their profile.
                    if(checkIfUsernameExist(dataSnapshot))
                    {
                        String username = user.getUsername() + "." + Util.generateToken(6);
                        user.setUsername(username);
                        Log.i(Constant.TAG, "Newly generated username : " + user.getUsername());
                    }

                    //Store the user settings, remove the listener (important), then publish
                    //result to the view.
                    storage.createUserProfile(user);
                    preferences.setProfileInformation(user);
                    firebaseAuth.removeAuthStateListener(authStateListener);

                    //Make sure the user gets sign out since they should not be able
                    //to access the app without first login in once after registration.
                    firebaseAuth.signOut();

                    publishToView.userCreated();
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e(Constant.TAG, "onCancelled : " + getClass().getName());
                    firebaseAuth.removeAuthStateListener(authStateListener);
                    publishToView.userCreationFailed(errorMessage);
                }
            });
        }
    };

    /**
     * Send a verification email to the user after successfully registering an account.
     * If this fails log it.
     */
    private void sendVerificationEmail()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    //If the email sending is not successful log it.
                    if(!task.isSuccessful())
                    {
                        Log.e(Constant.TAG, "Email verification failed : " + task.getException());
                    }
                }
            });
        }
    }

    /**
     * Check if the supplied username already exists in the database.
     *
     * @param  dataSnapshot Contains all the database nodes.
     * @return a boolean whether a username exists or not.
     */
    @SuppressWarnings({ "LoopStatementThatDoesntLoop", "StatementWithEmptyBody" })
    private boolean checkIfUsernameExist(DataSnapshot dataSnapshot)
    {
        //Iterated the database looking to see if the username already exists.
        for(DataSnapshot dataItem : dataSnapshot.getChildren())
        {
            String usernameToCompare = dataItem.getValue(User.class).getUsername();
            if(usernameToCompare != null && usernameToCompare.equals(user.getUsername()));
            { return true; }
        }
        return false;
    }
}