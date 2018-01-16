package io.bluephoenix.imagewall.features.wall;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.util.Constant;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class WallPresenter extends BasePresenter<IWallContract.PublishToView>
        implements IWallContract.Presenter
{
    private FirebaseAuth firebaseAuth;

    public WallPresenter(FirebaseAuth firebaseAuth)
    {
        this.firebaseAuth = firebaseAuth;
    }

    /**
     * At the start of the application check if the user has been previously logged in.
     */
    @Override
    public void isUserAlreadyLoggedIn()
    {
       authStateListener.onAuthStateChanged(firebaseAuth);
    }

    //Set a listener for any authentication changes
    private FirebaseAuth.AuthStateListener authStateListener =
            new FirebaseAuth.AuthStateListener()
    {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
        {
            //Remove listener (important)
            firebaseAuth.removeAuthStateListener(authStateListener);

            Log.i(Constant.TAG, "onAuthStateChanged : " + getClass().getName());
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if(firebaseUser != null)
            {
                publishToView.isUserLoggedInResponse(true);
            }
            else { publishToView.isUserLoggedInResponse(false); }

        }
    };
}
