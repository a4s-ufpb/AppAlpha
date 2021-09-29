package br.ufpb.dcx.appalpha.control;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

public class ChallengeFacadeTest {
    ChallengeFacade challengeFacade;

    @Before
    public void setUp() {
        Theme mockedTheme = new Theme("cozinha",  Integer.toString(R.drawable.cozinha), Integer.toString(R.raw.cozinha), null);
        Challenge mockedChallenge = new Challenge("colher", null, Integer.toString(R.raw.colher), null, Integer.toString(R.drawable.colher));
        ChallengeFacade.getInstance().init(Collections.singletonList(mockedChallenge), mockedTheme);
        challengeFacade = ChallengeFacade.getInstance();
    }

    @Test
    public void shouldReturnRightAttempt() {
        String letter = "o";
        int result = challengeFacade.checkAttempt(letter);
        assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @Test
    public void shouldReturnWrongAttempt() {
        String letter = "a";
        int result = challengeFacade.checkAttempt(letter);
        assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);
    }

    @Test
    public void shouldReturnUnderscoreWithRightAttempt() {
        char letter = 'r';
        String underscoreExpected = "_____r";
        String underscoreReturned = challengeFacade.updateWordByAttempt(letter);
        assertEquals(underscoreExpected, underscoreReturned);
    }

    @Test
    public void shouldReturnUnderscoreWithoutWrongAttempt() {
        char letter = 'z';
        String underscoreExpected = "______";
        String underscoreReturned = challengeFacade.updateWordByAttempt(letter);
        assertEquals(underscoreExpected, underscoreReturned);
    }

    @Test
    public void shouldReturnThatAttemptExists() {
        String letter = "o";
        challengeFacade.getAttempResult(letter);
        assertTrue(challengeFacade.checkAttemptExists(letter.charAt(0)));

    }

    @Test
    public void shouldReturnThatAttemptDoesNotExists() {
        String letter = "o";
        challengeFacade.getAttempResult(letter);
        assertFalse(challengeFacade.checkAttemptExists('b'));

    }

    @Test
    public void shouldReturnThatAttemptIsAccepted() {
        String letter = "o";
        int result = challengeFacade.getAttempResult(letter);
        assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @Test
    public void shouldReturnThatAttemptIsRejected() {
        String letter = "z";
        int result = challengeFacade.getAttempResult(letter);
        assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);

    }

    @Test
    public void shouldReturnThatAttemptAlreadyExists() {
        String letter = "o";
        challengeFacade.getAttempResult(letter);
        int result = challengeFacade.getAttempResult(letter);
        assertEquals(ChallengeFacade.ATTEMPT_EXISTS, result);

    }
}