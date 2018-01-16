package io.bluephoenix.imagewall;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;
import io.bluephoenix.imagewall.features.wall.WallActivity;

/**
 * @author Carlos A. Perez Zubizarreta
 */
@RunWith(AndroidJUnit4.class)
public class WallActivityTest
{
    @Rule
    public ActivityTestRule<WallActivity> activityTestRule =
            new ActivityTestRule<>(WallActivity.class);
}
