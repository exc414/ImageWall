package io.bluephoenix.imagewall.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;

import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.core.jobs.SyncJobCreator;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public class App extends Application
{
    //Global context
    private static App instance;
    private static SharedPreferences sharedPreferences;

    public static App getInstance() { return instance; }
    public static SharedPreferences getPrefs() { return sharedPreferences; }

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Stetho.initializeWithDefaults(this);

        //Create job scheduler
        JobManager.create(this).addJobCreator(new SyncJobCreator());
    }
}
