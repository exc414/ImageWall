package io.bluephoenix.imagewall.features.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Constructor;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bluephoenix.imagewall.app.App;
import io.bluephoenix.imagewall.core.common.PresenterActivityDef;
import io.bluephoenix.imagewall.core.common.PresenterActivityDef.PresenterActivityType;
import io.bluephoenix.imagewall.core.data.repo.IRepository;
import io.bluephoenix.imagewall.core.data.repo.Preferences;
import io.bluephoenix.imagewall.core.data.repo.Storage;
import io.bluephoenix.imagewall.util.Util;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public abstract class BaseActivity extends AppCompatActivity
{
    protected Unbinder unbinder;
    private BasePresenter presenter;
    private Activity activity = this;

    /**
     * Set the default view for the activity and bind ButterKnife to the activity.
     *
     * @param layoutResID A resource id for the layout to be set.
     */
    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    /**
     * Retain the present through configuration (screen rotation) changes. The
     * presenter is persisted through this function.
     *
     * @return An object.
     */
    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        return presenter;
    }

    /**
     * Destroy/Unbind/Release everything. This might not get called.
     */
    @Override
    protected void onDestroy()
    {
        if(presenter != null) { presenter.detachView(); }
        unbinder.unbind();
        super.onDestroy();
    }

    /**
     * If the back button on the UI (not the key on the phone itself) is pressed
     * return to previous activity.
     *
     * @param item A menu item which was clicked.
     * @return     A menu item which was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home) { finish(); }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attach the based context for use with the custom fonts (Calligraphy Lib).
     *
     * @param baseContext Interface to global information about an application environment.
     */
    @Override
    protected void attachBaseContext(Context baseContext)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(baseContext));
    }

    /**
     * Start an activity with no result returned.
     *
     * @param activity A class where all user interactions are held.
     */
    protected void start(Class<? extends BaseActivity> activity)
    {
        startActivity(new Intent(this, activity));
    }

    /**
     * Start an activity not expecting a result.
     *
     * @param intent The intent to start.
     */
    protected void start(Intent intent)
    {
        startActivity(intent);
    }

    /**
     * Start an activity expecting a result when returning to it.
     *
     * @param intent     The intent to start.
     * @param resultCode An int that is returned to the activity when the application
     *                   that was started is finished.
     */
    protected void start(Intent intent, int resultCode)
    {
        startActivityForResult(intent, resultCode);
    }

    /**
     * Start a new activity and remove the previous activity from the history stack.
     * Basically if the user presses the back button they wont see the executing activity.
     *
     * @param activity  A class where all user interactions are held.
     * @param classType A class type which is used to get the name of the class
     *                  and its type.
     */
    protected void startClearPrevious(Activity activity, Class classType)
    {
        Intent intent = new Intent(activity, classType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Start an activity expecting a result when returning to it.
     *
     * @param activity A class where all user interactions are held.
     * @param result   A key used to filter for the correct return values.
     */
    protected void start(Class<? extends BaseActivity> activity, int result)
    {
        startActivityForResult(new Intent(this, activity), result);
    }

    @SuppressWarnings("ConstantConditions")
    protected void setToolbar(Toolbar toolbar, String title)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
    }

    @SuppressWarnings("ConstantConditions")
    protected void setToolbarBackButton(Toolbar toolbar, String title)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Create the presenter, if it is already created get the previously
     * made presenter. Cast the object into the BasePresenter used in the BaseActivity
     * to detach the presenter from the view and to retain after screen rotation.
     *
     * @param classType     A class type which is used to get the name of the class
     *                      and its type.
     * @param <V>           A generic object use for the retrieved/new object.
     * @param presenterType An int which corresponds to the type of presenter.
     * @return              an object casted into the correct class type.
     */
    protected<V> V attachPresenter(Class<V> classType, @PresenterActivityType int presenterType)
    {
        if(getLastCustomNonConfigurationInstance() == null)
        {
            try
            {
                //Get the correct presenter by its class name.
                Class<?> genClass = Class.forName(classType.getName());
                Object newPresenter;

                switch(presenterType)
                {
                    case PresenterActivityDef.REGISTER:

                        Constructor<?> constructorRegister = genClass.getConstructor(
                                FirebaseAuth.class,
                                FirebaseDatabase.class,
                                IRepository.Storage.class,
                                IRepository.Preferences.class);

                        newPresenter = constructorRegister.newInstance(
                                FirebaseAuth.getInstance(),
                                FirebaseDatabase.getInstance(),
                                new Storage(FirebaseDatabase.getInstance()),
                                new Preferences(App.getPrefs()));
                        break;

                    case PresenterActivityDef.LOGIN:

                        Constructor<?> constructorLogin = genClass.getConstructor(
                                FirebaseAuth.class,
                                FirebaseDatabase.class,
                                IRepository.Preferences.class);

                        newPresenter = constructorLogin.newInstance(
                                FirebaseAuth.getInstance(),
                                FirebaseDatabase.getInstance(),
                                new Preferences(App.getPrefs()));
                        break;

                    case PresenterActivityDef.WALL:

                        Constructor<?> constructorWall = genClass.getConstructor(
                                FirebaseAuth.class);

                        newPresenter = constructorWall.newInstance(FirebaseAuth.getInstance());
                        break;

                    case PresenterActivityDef.EDIT_PROFILE:

                        Constructor<?> constructorEditProfile = genClass.getConstructor(
                                FirebaseAuth.class,
                                DatabaseReference.class,
                                IRepository.Preferences.class);

                        newPresenter = constructorEditProfile.newInstance(
                                FirebaseAuth.getInstance(),
                                FirebaseDatabase.getInstance().getReference(),
                                new Preferences(App.getPrefs()));
                        break;

                    case PresenterActivityDef.DETAIL:
                    case PresenterActivityDef.POST:
                    default: newPresenter = genClass.newInstance(); break;
                }

                //Cast to BasePresenter to be used in this activity.
                presenter = (BasePresenter) newPresenter;

                //Cast to the correct presenter dynamically based on the classType arg.
                return classType.cast(newPresenter);
            }
            catch(Exception ex) { ex.printStackTrace(); }
        }
        else
        {
            Object lastPresenter =  getLastCustomNonConfigurationInstance();
            presenter = (BasePresenter) lastPresenter;
            return classType.cast(lastPresenter);
        }

        return null;
    }

    /**
     * Adds a listener to the passed edit text to listen for the DONE (enter) key.
     *
     * @param editText An edit text that needs the focus removed.
     */
    protected void listenForDoneKeyOnEditBox(final EditText editText, final View view)
    {
        //The last edit text and the only one that will get the DONE enter key. All others
        //get NEXT as the enter key.
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    Util.hideKeyboard(activity);
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(true);
                    view.requestFocus();
                    return true;
                }
                else { return false; }
            }
        });
    }

}