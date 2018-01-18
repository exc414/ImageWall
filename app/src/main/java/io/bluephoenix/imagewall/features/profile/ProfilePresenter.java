package io.bluephoenix.imagewall.features.profile;

import android.util.Log;

import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.util.Constant;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class ProfilePresenter extends BasePresenter<IProfileContract.PublishToView>
    implements IProfileContract.Presenter
{
    public ProfilePresenter()
    {

    }

    @Override
    public void populateProfile()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int max = 20;

                for(int i = 0; i < max; i++)
                {
                    try
                    {
                        Log.d(Constant.TAG, "Debugging application in profile presenter : " + i);
                        Thread.sleep(2000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
