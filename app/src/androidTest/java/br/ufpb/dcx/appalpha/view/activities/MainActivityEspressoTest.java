package br.ufpb.dcx.appalpha.view.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;


import br.ufpb.dcx.appalpha.R;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityEspressoTest {

    @Rule
    public IntentsTestRule<MainActivity> activityTestRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void done() {
        Intents.release();
    }

    @Test
    public void clickingRecordsShouldGoToRecordsActivity() {
        onView(withId(R.id.btnRecords))
                .perform(click());

        intended(hasComponent(RecordesActivity.class.getName()));
    }

    /*@Test
    public void clickingThemesShouldGoToThemesActivity() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).get();
        mainActivity.findViewById(R.id.btnPlay).performClick();

        Intent expectedIntent = new Intent(mainActivity, ThemeActivity.class);
        Intent actualIntent = Shadows.shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void clickingAboutShouldGoToAboutActivity() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).get();
        mainActivity.findViewById(R.id.btnInfo).performClick();

        Intent expectedIntent = new Intent(mainActivity, SobreActivity.class);
        Intent actualIntent = Shadows.shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void clickingConfigShouldGoToConfigActivity() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).get();
        mainActivity.findViewById(R.id.btnConfigs).performClick();

        Intent expectedIntent = new Intent(mainActivity, ConfigActivity.class);
        Intent actualIntent = Shadows.shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }*/
}