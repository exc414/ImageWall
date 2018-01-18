package io.bluephoenix.imagewall;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.bluephoenix.imagewall.features.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Carlos A. Perez Zubizarreta
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest
{
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginClickWithEmptyFields()
    {
        onView((withId(R.id.loginBtn))).perform(click());
        onView((withId(R.id.txtLoginEmailError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtLoginPasswordError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void loginClickWithInvalidFields()
    {
        //Insert invalid email and password
        onView((withId(R.id.editTextLoginEmail))).perform(clearText(),
                typeText("example.com"));
        onView((withId(R.id.editTextLoginPassword))).perform(clearText(),
                typeText("p234"));

        onView((withId(R.id.loginBtn))).perform(click());

        //Check that the error is visible and that the error message matches.
        onView((withId(R.id.txtLoginEmailError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtLoginEmailError))).check(matches(withText(R.string.error_email)));

        onView((withId(R.id.txtLoginPasswordError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtLoginPasswordError))).check(
                matches(withText(R.string.error_password)));
    }

    @Test
    public void forgotPasswordClick()
    {
        onView((withId(R.id.forgotPasswordBtn))).perform(click());
        onView(withText(R.string.dialog_title)).inRoot(isDialog())
                .check(matches(isDisplayed()));
    }
}
