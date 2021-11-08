package br.ufpb.dcx.appalpha.control;

import static org.junit.Assert.assertEquals;

import android.os.Environment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowEnvironment;

import java.util.Collections;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.locator.ServiceLocator;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;
import br.ufpb.dcx.appalpha.view.activities.ForcaActivity;

@RunWith(RobolectricTestRunner.class)
public class ForcaActivityRoboletricTest {
    private ForcaActivity forcaActivity;


    @Before
    public void setUp() throws Exception {
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED);
        //ShadowEnvironment.setExternalStorageDirectory();

        Theme mockedTheme = new Theme("cozinha",  Integer.toString(R.drawable.cozinha), Integer.toString(R.raw.cozinha), null);
        Challenge mockedChallenge = new Challenge("colher", null, Integer.toString(R.raw.colher), null, Integer.toString(R.drawable.colher));
        ChallengeFacade.getInstance().init(Collections.singletonList(mockedChallenge), mockedTheme);
        ServiceLocator.getInstance().setChallengeFacade(ChallengeFacade.getInstance());

        forcaActivity = Robolectric.buildActivity(ForcaActivity.class)
                .create()
                .start()
                .get();
    }

    @Test
    public void shouldAppearImageAndUnderscoreWordOfChallenge() {
        String underscoreWord = "_ _ _ _ _ _";
        String underscoreFromActivity = forcaActivity.findViewById(R.id.txt_underscore).toString();
        assertEquals(underscoreWord, underscoreFromActivity);
    }
}
