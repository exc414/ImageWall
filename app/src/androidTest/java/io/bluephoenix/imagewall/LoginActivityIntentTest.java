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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Carlos A. Perez Zubizarreta
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityIntentTest
{
    @Rule
    public IntentsTestRule<LoginActivity> intentTestRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Test
    public void loginButtonClick()
    {
        onView((withId(R.id.editTextLoginEmail))).perform(clearText(),
                typeText("example@gmail.com"));
        onView((withId(R.id.editTextLoginPassword))).perform(clearText(),
                typeText("?Password1234"));
        onView((withId(R.id.loginBtn))).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), WallActivity.class)));
    }

    @Test
    public void createAccountTextButtonClick()
    {
        onView(withId(R.id.createAccountBtn)).perform(scrollTo(), click());
        intended(hasComponent(new ComponentName(getTargetContext(), RegisterActivity.class)));
    }

    @Test
    public void createAccountMenuItemClick()
    {
        onView(withId(R.id.createAccount)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), RegisterActivity.class)));
    }
}
