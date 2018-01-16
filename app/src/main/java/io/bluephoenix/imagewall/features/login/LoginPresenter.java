package io.bluephoenix.imagewall.features.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.bluephoenix.imagewall.common.LoginResponseDef;
import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.util.Constant;

import static io.bluephoenix.imagewall.common.LoginResponseDef.CORRECT_CREDENTIALS;
import static io.bluephoenix.imagewall.common.LoginResponseDef.EMAIL_NOT_VERIFIED;
import static io.bluephoenix.imagewall.common.LoginResponseDef.INVALID_CREDENTIALS;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class LoginPresenter extends BasePresenter<ILoginContract.PublishToView>
    implements ILoginContract.Presenter
{
    private FirebaseAuth firebaseAuth;

    public LoginPresenter(FirebaseAuth firebaseAuth)
    {
        this.firebaseAuth = firebaseAuth;
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
                    { publishToView.userCredentialsResponse(CORRECT_CREDENTIALS); }
                }
                else { publishToView.userCredentialsResponse(INVALID_CREDENTIALS); }
            }
        });
    }
}

