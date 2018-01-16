package io.bluephoenix.imagewall;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.bluephoenix.imagewall.features.post.PostActivity;
import io.bluephoenix.imagewall.features.wall.WallActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Carlos A. Perez Zubizarreta
 */
@RunWith(AndroidJUnit4.class)
public class WallActivityIntentTest
{
    @Rule
    public IntentsTestRule<WallActivity> intentsTestRule =
            new IntentsTestRule<>(WallActivity.class);

    @Test
    public void addPostClick()
    {
        onView(withId(R.id.addImageMenu)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), PostActivity.class)));
    }
}
