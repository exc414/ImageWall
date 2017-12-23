package io.bluephoenix.imagewall.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * @author Carlos A. Perez
 */

public class App extends Application
{
    //Global context
    private static App instance;
    public static App getInstance() { return instance; }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }
}
