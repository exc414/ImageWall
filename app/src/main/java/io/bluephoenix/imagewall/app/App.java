package io.bluephoenix.imagewall.app;

import android.app.Application;

import io.bluephoenix.imagewall.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Carlos A. Perez Zubizarreta
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

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
