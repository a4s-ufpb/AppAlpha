package br.ufpb.dcx.appalpha.view.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.locator.ServiceLocator;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ForcaActivityTest {

    @Rule
    public ActivityTestRule<ForcaActivity> forcaActivityActivityTestRule = new ActivityTestRule<>(ForcaActivity.class);
    private ForcaActivity launchedForcaActivity;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchedForcaActivity = forcaActivityActivityTestRule.launchActivity(intent);

        Theme mockedTheme = new Theme("cozinha",  Integer.toString(R.drawable.cozinha), Integer.toString(R.raw.cozinha), null);
        Challenge mockedChallenge = new Challenge("colher", null, Integer.toString(R.raw.colher), null, Integer.toString(R.drawable.colher));
        ChallengeFacade.getInstance().init(Collections.singletonList(mockedChallenge), mockedTheme);
        ServiceLocator.getInstance().setChallengeFacade(ChallengeFacade.getInstance());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldAppearImageAndUnderscoreWordOfChallenge() {
        String underscoreWord = "_ _ _ _ _ _";
        onView(withId(R.id.txt_underscore))
                .check(matches(withText(underscoreWord)));
    }
}
