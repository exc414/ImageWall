package io.bluephoenix.imagewall;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.bluephoenix.imagewall.features.login.LoginActivity;
import io.bluephoenix.imagewall.features.register.RegisterActivity;
import io.bluephoenix.imagewall.features.wall.WallActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * These two test fail with "Wanted to match 1 intents. Actually matched 0 intents."
 * I don't understand why as this same code works perfectly fine for LoginActivity and
 * WallActivity intent test.
 *
 * @author Carlos A. Perez Zubizarreta
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityIntentTest
{
    @Rule
    public IntentsTestRule<RegisterActivity> intentsTestRule =
            new IntentsTestRule<>(RegisterActivity.class);

    @Test
    public void registerButtonClick()
    {
        onView((withId(R.id.editTextRegisterEmail))).perform(clearText(),
                typeText("example@gmail.com"));
        onView((withId(R.id.editTextRegisterPassword))).perform(clearText(),
                typeText("?Password1234"));
        onView((withId(R.id.editTextRegisterRetypePassword))).perform(clearText(),
                typeText("?Password1234"));
        onView((withId(R.id.editTextName))).perform(scrollTo(), clearText(),
                typeText("Testing"));
        onView((withId(R.id.editTextAge))).perform(scrollTo(), clearText(),
                typeText("30"));

        onView((withId(R.id.registerBtn))).perform(scrollTo(), click());
        intended(hasComponent(new ComponentName(getTargetContext(), WallActivity.class)));
    }

    @Test
    public void menuBackButtonClick()
    {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));
    }
}