package br.ufpb.dcx.appalpha.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

class ChallengeFacadeTest {
    ChallengeFacade challengeFacade;

    @BeforeEach
    void setUp() {
        Theme kitchenTheme = new Theme("cozinha",  Integer.toString(R.drawable.cozinha), Integer.toString(R.raw.cozinha), null);
        Challenge spoonChallenge = new Challenge("colher", Integer.toString(R.raw.colher), null, Integer.toString(R.drawable.colher));
        Challenge knifeChallenge = new Challenge("faca", Integer.toString(R.raw.faca), null, Integer.toString(R.drawable.faca));
        challengeFacade = ChallengeFacade.getInstance();
        challengeFacade.init(Arrays.asList(spoonChallenge, knifeChallenge), kitchenTheme);
    }

    @DisplayName("Given user attempt a right letter for the word 'colher', " +
            "when they click on it, " +
            "then it must return ATTEMPT_ACCEPTED")
    @Test
    void checkAttemptWithRightLetterTest() {
        // Given
        String letter = "o";
        // When
        int result = challengeFacade.checkAttempt(letter);
        // Then
        assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @DisplayName("Given user attempt a wrong letter for the word 'colher', " +
            "when they click on it, " +
            "then it must return ATTEMPT_REJECTED")
    @Test
    void checkAttemptWithWrongLetterTest() {
        String letter = "z";
        int result = challengeFacade.checkAttempt(letter);
        assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);
    }

    @DisplayName("Given user attempt a right letter, " +
            "when they click on it, " +
            "" +
            "then the underlined word must be updated with that letter")
    @Test
    void updateWordByAttemptWithRightLetterTest() {
        char letter = 'r';
        String wordWithExpectedUnderline = "_____r";
        String wordWithReturnedUnderline = challengeFacade.updateWordByAttempt(letter);
        assertEquals(wordWithExpectedUnderline, wordWithReturnedUnderline);
    }

    @DisplayName("Given user attempt a wrong letter, " +
            "when they click on it, " +
            "then the underlined word must not be updated with that letter")
    @Test
    void updateWordByAttemptWithWrongLetterTest() {
        char letter = 'z';
        String wordWithExpectedUnderline = "______";
        String wordWithReturnedUnderline = challengeFacade.updateWordByAttempt(letter);
        assertEquals(wordWithExpectedUnderline, wordWithReturnedUnderline);
    }

    @DisplayName("Given user attempt the same letter more than one time, " +
            "when they click on it, " +
            "then it must return that the attempt already was made")
    @Test
    void checkAttemptExistsTest() {
        String letter = "o";
        int firstResult = challengeFacade.getAttempResult(letter);
        assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, firstResult);
        assertTrue(challengeFacade.checkAttemptExists('o'));
    }

    @DisplayName("Given user attempt a letter that is in the word, " +
            "when they click on it, " +
            "then it must return that the attempt was accepted")
    @Test
    void checkAttemptIsAcceptedTest() {
        String letter = "o";
        int result = challengeFacade.checkAttempt(letter);
        assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @DisplayName("Given user attempt letter that is not in the word, " +
            "when they click on it, " +
            "then it must return that the attempt was rejected")
    @Test
    void checkAttemptIsRejectedTest() {
        String letter = "j";
        int result = challengeFacade.checkAttempt(letter);
        assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);
    }

    @DisplayName("Given user already attempted almost all the letters in the word, " +
            "when they click on the last one missing, " +
            "then it must return that the word is already finished")
    @Test
    void checkWordAcceptedIsFinished() {
        String underlinedWordFinished = "co_her";
        challengeFacade.setCurrentUnderlinedWord(underlinedWordFinished);

        challengeFacade.getAttempResult("l");
        assertTrue(challengeFacade.checkWordAccepted());
    }

    @DisplayName("Given user already attempted almost all the letters in the word, " +
            "when they click on one but , " +
            "then it must return that the word is already finished")
    @Test
    void checkWordAcceptedIsNotFinished() {
        String underlinedWordFinished = "co__er";
        challengeFacade.setCurrentUnderlinedWord(underlinedWordFinished);

        challengeFacade.getAttempResult("l");
        assertFalse(challengeFacade.checkWordAccepted());
    }

    @Test
    void getAttemptResultIsAccepted() {
        String letter = "h";
        int result = challengeFacade.getAttempResult(letter);
        assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @DisplayName("Given user attempt a wrong letter, " +
            "when they click on it, " +
            "then it must return ATTEMPT_REJECTED and increase the error count")
    @Test
    void getAttemptResultIsRejected() {
        int errorCount = challengeFacade.getErroCount();
        String letter = "k";
        int result = challengeFacade.getAttempResult(letter);

        int errorCountExpected = errorCount + 1;
        int errorCountReturned = challengeFacade.getErroCount();

        assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);
        assertEquals(errorCountExpected, errorCountReturned);
    }

    @Test
    void getUnderscoreWithSpaces() {
        String underlinedWordWithSpacesExpected = "_ _ _ _ _ _ ";
        assertEquals(underlinedWordWithSpacesExpected, challengeFacade.getUnderlinedWordWithSpaces());
    }

    @DisplayName("Given that challenge was already finished," +
            "When it is requested a new one," +
            "Then a new Challenge must be set as current challenge")
    @Test
    void nextChallenge() {
        challengeFacade.nextChallenge();

        assertEquals("faca", challengeFacade.getCurrentChallenge().getWord());
    }

    @DisplayName("Given the last challenge was already finished" +
            "When it is requested a new one" +
            "Then the current Challenge must be set to null to signal that the Challenges are over")
    @Test
    void nextChallengeWhenTheyAreFinished() {
        challengeFacade.nextChallenge();
        challengeFacade.nextChallenge();

        assertNull(challengeFacade.getCurrentChallenge());
    }
}