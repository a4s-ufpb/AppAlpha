package br.ufpb.dcx.appalpha.view.activities;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Collections;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.control.ChallengeFacade;
import br.ufpb.dcx.appalpha.locator.ServiceLocator;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

@RunWith(AndroidJUnit4.class)
public class ForcaActivityTest {

    @Rule
    public ActivityTestRule<ForcaActivity> forcaActivityActivityTestRule = new ActivityTestRule<>(ForcaActivity.class);

    @Before
    public void setUp() throws Exception {
        Theme mockedTheme = new Theme("cozinha",  Integer.toString(R.drawable.cozinha), Integer.toString(R.raw.cozinha), null);
        Challenge mockedChallenge = new Challenge("colher", null, Integer.toString(R.raw.colher), null, Integer.toString(R.drawable.colher));
        ChallengeFacade.getInstance().init(Collections.singletonList(mockedChallenge), mockedTheme);
        ServiceLocator.setChallengeFacade(ChallengeFacade.getInstance());
    }

    @After
    public void tearDown() throws Exception {
    }
}