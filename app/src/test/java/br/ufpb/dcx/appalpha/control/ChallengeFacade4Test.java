package br.ufpb.dcx.appalpha.control;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

public class ChallengeFacade4Test {
    ChallengeFacade challengeFacade;

    @Before
    public void setUp() {
        Theme kitchenTheme = new Theme("cozinha",  Integer.toString(R.drawable.cozinha), Integer.toString(R.raw.cozinha), null);
        Challenge spoonChallenge = new Challenge("colher", Integer.toString(R.raw.colher), null, Integer.toString(R.drawable.colher));
        challengeFacade = ChallengeFacade.getInstance();
        challengeFacade.init(Collections.singletonList(spoonChallenge), kitchenTheme);
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
        String wordWithExpectedUnderline;
        String underscoreExpected = "_____r";
        String wordWithReturnedUnderline;
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
    public void shouldReturnThatAttemptWasAlreadyMade() {
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

    @Test
    public void shouldReturnUnderscoreWithSpaces() {
        String underscoreWithSpacesExpected = "_ _ _ _ _ _ ";
        String actualUnderscoreWithSpaces = challengeFacade.getUnderlinedWordWithSpaces();
        assertEquals(underscoreWithSpacesExpected, actualUnderscoreWithSpaces);
    }
}