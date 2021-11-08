package br.ufpb.dcx.appalpha.control;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import br.ufpb.dcx.appalpha.R;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

class ChallengeFacadeTest {
    ChallengeFacade challengeFacade;

    @BeforeEach
    void setUp() {
        Theme kitchenTheme = new Theme("cozinha",  Integer.toString(R.drawable.cozinha), Integer.toString(R.raw.cozinha), null);
        Challenge spoonChallenge = new Challenge("colher", null, Integer.toString(R.raw.colher), null, Integer.toString(R.drawable.colher));
        challengeFacade = ChallengeFacade.getInstance();
        challengeFacade.init(Collections.singletonList(spoonChallenge), kitchenTheme);
    }

    @AfterEach
    void tearDown() {
    }


    @DisplayName("Given user attempt a right letter for the word 'colher', when they choose it, then it must return ATTEMPT_ACCEPTED")
    @Test
    void checkAttemptWithRightLetterTest() {
        String letter = "o";
        int result = challengeFacade.checkAttempt(letter);
        Assertions.assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @DisplayName("Given user attempt a wrong letter for the word 'colher', when they choose it, then it must return ATTEMPT_REJECTED")
    @Test
    void checkAttemptWithWrongLetterTest() {
        String letter = "z";
        int result = challengeFacade.checkAttempt(letter);
        Assertions.assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);
    }

    @DisplayName("Given user attempt a right letter, when they choose it, the underlined word must be updated with that letter")
    @Test
    void updateWordByAttemptWithRightLetterTest() {
        char letter = 'r';
        String wordWithExpectedUnderline = "_____r";
        String wordWithReturnedUnderline = challengeFacade.updateWordByAttempt(letter);
        Assertions.assertEquals(wordWithExpectedUnderline, wordWithReturnedUnderline);
    }

    @DisplayName("Given user attempt a wrong letter, when they choose it, the underlined word must not be updated with that letter")
    @Test
    void updateWordByAttemptWithWrongLetterTest() {
        char letter = 'z';
        String wordWithExpectedUnderline = "______";
        String wordWithReturnedUnderline = challengeFacade.updateWordByAttempt(letter);
        Assertions.assertEquals(wordWithExpectedUnderline, wordWithReturnedUnderline);
    }

    @DisplayName("Given user attempt the same letter more than one time, when they do that, it must return that the attempt already was made")
    @Test
    void checkAttemptExistsTest() {
        String letter = "o";
        int firstResult = challengeFacade.getAttempResult(letter);
        Assertions.assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, firstResult);
        Assertions.assertTrue(challengeFacade.checkAttemptExists('o'));
    }

    @DisplayName("Given user attempt a letter that is in the word, when they do that, it must return that the attempt already was accepted")
    @Test
    void checkAttemptIsAcceptedTest() {
        String letter = "o";
        int result = challengeFacade.checkAttempt(letter);
        Assertions.assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @DisplayName("Given user attempt that is not in the word, when they do that, it must return that the attempt already was rejected")
    @Test
    void checkAttemptIsRejectedTest() {
        String letter = "j";
        int result = challengeFacade.checkAttempt(letter);
        Assertions.assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);
    }

    @DisplayName("Given user already attempted all the letters in the word, when they attempt the last one missing, then it must return that the word is already finished")
    @Test
    void checkWordAcceptedIsFinished() {
        String underlinedWordFinished = "colher";
        challengeFacade.setCurrentUnderlinedWord(underlinedWordFinished);
        Assertions.assertTrue(challengeFacade.checkWordAccepted());
    }

    @DisplayName("Given user already attempted all the letters in the word, when they attempt the last one missing, then it must return that the word is already finished")
    @Test
    void checkWordAcceptedIsNotFinished() {
        String underlinedWordFinished = "co_her";
        challengeFacade.setCurrentUnderlinedWord(underlinedWordFinished);
        Assertions.assertFalse(challengeFacade.checkWordAccepted());
    }

    @Test
    void getAttemptResultIsAccepted() {
        String letter = "h";
        int result = challengeFacade.getAttempResult(letter);
        Assertions.assertEquals(ChallengeFacade.ATTEMPT_ACEPTED, result);
    }

    @Test
    void getAttemptResultIsRejected() {
        String letter = "k";
        int result = challengeFacade.getAttempResult(letter);
        Assertions.assertEquals(ChallengeFacade.ATTEMPT_REJECTED, result);
    }

    @Test
    void getUnderscoreWithSpaces() {
        String underlinedWordWithSpacesExpected = "_ _ _ _ _ _ ";
        Assertions.assertEquals(underlinedWordWithSpacesExpected, challengeFacade.getUnderlinedWordWithSpaces());
    }
}