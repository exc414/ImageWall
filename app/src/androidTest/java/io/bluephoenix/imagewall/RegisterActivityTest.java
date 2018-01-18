package io.bluephoenix.imagewall;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.bluephoenix.imagewall.features.register.RegisterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Carlos A. Perez Zubizarreta
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest
{
    @Rule
    public ActivityTestRule<RegisterActivity> activityTestRule =
            new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void registerClickWithEmptyFields()
    {
        onView((withId(R.id.registerBtn))).perform(click());
        onView((withId(R.id.txtRegisterEmailError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtRegisterPasswordError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtRegisterPasswordRetypeError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtRegisterNameError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtRegisterAgeError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void registerClickWithInvalidFields()
    {
        onView((withId(R.id.editTextRegisterEmail))).perform(clearText(),
                typeText("example.com"));
        onView((withId(R.id.editTextRegisterPassword))).perform(clearText(),
                typeText("pawdawdfg"));
        onView((withId(R.id.editTextRegisterRetypePassword))).perform(clearText(),
                typeText("Pawda1a23"));
        onView((withId(R.id.editTextAge))).perform(scrollTo(), clearText(),
                typeText("140"));

        Espresso.closeSoftKeyboard();

        onView((withId(R.id.registerBtn))).perform(scrollTo(), click());

        onView((withId(R.id.txtRegisterEmailError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView((withId(R.id.txtRegisterEmailError))).check(
                matches(withText(R.string.error_email)));

        onView((withId(R.id.txtRegisterPasswordError))).check(
                matches(withText(R.string.error_password_does_not_meet_criteria)));
        onView((withId(R.id.txtRegisterPasswordError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView((withId(R.id.txtRegisterPasswordRetypeError))).check(
                matches(withText(R.string.error_retype_password)));
        onView((withId(R.id.txtRegisterPasswordRetypeError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView((withId(R.id.txtRegisterAgeError))).check(
                matches(withText(R.string.error_age)));
        onView((withId(R.id.txtRegisterAgeError))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
