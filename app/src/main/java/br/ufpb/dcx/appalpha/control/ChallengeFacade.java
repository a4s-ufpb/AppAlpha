package br.ufpb.dcx.appalpha.control;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufpb.dcx.appalpha.control.util.TextUtil;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

/**
 * Shared class to manage current Challenge information
 */
public class ChallengeFacade {
    public static final int ATTEMPT_ACEPTED = 0;
    public static final int ATTEMPT_REJECTED = 1;
    public static final int ATTEMPT_EXISTS = 2;
    private static ChallengeFacade instance;
    private List<Challenge> challenges;
    private Challenge currentChallenge;
    private Theme selectedTheme;
    private int erroCount;
    private int progressCount;
    private double time;
    private int sumError;
    private String currentUnderlinedWord;
    private Set<String> triedLetters = new HashSet<>();


    private ChallengeFacade() {
    }

    /**
     * Get shared instance
     *
     * @return
     */
    public static ChallengeFacade getInstance() {
        if (instance == null) {
            instance = new ChallengeFacade();
        }

        return instance;
    }

    /**
     * Setup an Challenge with an selected Theme
     *
     * @param challenges
     * @param selectedTheme
     */
    public void init(List<Challenge> challenges, Theme selectedTheme) {
        this.challenges = challenges;
        this.currentChallenge = challenges.get(0);
        this.selectedTheme = selectedTheme;
        this.erroCount = 0;
        this.progressCount = 0;
        this.time = 0.0;
        this.sumError = 0;
        this.currentUnderlinedWord = "";
        setUnderlinedWord();

    }

    /**
     * Return the current Challenge on the progress
     *
     * @return
     */
    public Challenge getCurrentChallenge() {
        return currentChallenge;
    }

    /**
     * Go to next challenge in the list
     */
    public void nextChallenge() {
        try {
            this.sumError += this.erroCount;
            this.erroCount = 0;
            this.progressCount++;
            this.currentChallenge = challenges.get(challenges.indexOf(currentChallenge) + 1);
            resetTryLetterAttempts();
            setUnderlinedWord();
        } catch (IndexOutOfBoundsException e) {
            this.currentChallenge = null;
        }
    }

    /**
     * Clean up retry letter tha user attempted
     */
    public void resetTryLetterAttempts() {
        this.triedLetters.clear();
    }

    public void setCurrentChallenge(Challenge currentChallenge) {
        this.currentChallenge = currentChallenge;
    }

    public void increaseError() {
        this.erroCount++;
    }

    /**
     * Set word in underline
     */
    public void setUnderlinedWord() {
        this.currentUnderlinedWord = TextUtil.getInstance().getUnderlineOfThis(this.currentChallenge.getWord());
    }

    public void setCurrentUnderlinedWord(String currentUnderlinedWord) {
        this.currentUnderlinedWord = currentUnderlinedWord;
    }

    /**
     * Check if attempt was correct or wrong
     *
     * @param letter attempt by user
     * @return an int indicate attempt status
     */
    public int checkAttempt(String letter) {

        String newUnderscoreAfterAttempt = updateWordByAttempt(letter.charAt(0));

        if (currentUnderlinedWord.equals(newUnderscoreAfterAttempt)) {
            return ATTEMPT_REJECTED;

        } else {
            currentUnderlinedWord = newUnderscoreAfterAttempt;
            return ATTEMPT_ACEPTED;
        }

    }

    /**
     * Check if attempt letter was found in the word, if found add to underscore
     *
     * @param letter user attempt
     * @return an updated underscore
     */
    public String updateWordByAttempt(char letter) {
        StringBuilder newUnderscore = new StringBuilder(currentUnderlinedWord);
        String currentChallengeWord = this.currentChallenge.getWord();

        for (int indexInWord = 0; indexInWord < currentChallengeWord.length(); indexInWord++) {
            if (TextUtil.getInstance().treatCharacter(letter) == TextUtil.getInstance().treatCharacter(currentChallengeWord.charAt(indexInWord))) {
                newUnderscore.setCharAt(indexInWord, currentChallengeWord.charAt(indexInWord));
            }
        }

        return newUnderscore.toString();
    }

    /**
     * Check if letter was attempted before
     *
     * @param letter user attempt
     * @return an boolean thats return result of check
     */
    public boolean checkAttemptExists(char letter) {
        for (String let : this.triedLetters) {
            if (let.equals(String.valueOf(letter))) {
                this.triedLetters.add(String.valueOf(letter));
                return true;
            }
        }
        this.triedLetters.add(String.valueOf(letter));
        return false;
    }

    /**
     * Check if user was got correct word, if the underline word are equal to real word.
     *
     * @return an boolean thats return result of check
     */
    public boolean checkWordAccepted() {
        return this.currentChallenge.getWord().equals(currentUnderlinedWord);
    }

    /**
     * Check if the letter attempt was empty, wrong or correct
     *
     * @param letter of user attempt
     * @return an status of attempt letter
     */
    public int getAttempResult(String letter) {

        if (checkAttemptExists(letter.charAt(0))) {
            return ATTEMPT_EXISTS;
        } else if (checkAttempt(letter) == ATTEMPT_REJECTED) {
            increaseError();
            return ATTEMPT_REJECTED;
        } else {
            return ATTEMPT_ACEPTED;
        }

    }

    /**
     * Add spaces betwen letters of word
     *
     * @return o underscore with spaces betwen letters of word
     */
    public String getUnderlinedWordWithSpaces() {
        StringBuilder novaString = new StringBuilder();

        for (int i = 0; i < getCurrentUnderlinedWord().length(); i++) {
            novaString.append(getCurrentUnderlinedWord().charAt(i));
            novaString.append(" ");
        }

        return novaString.toString();
    }

    public String getCurrentUnderlinedWord() {
        return currentUnderlinedWord;
    }

    public int getProgressCount() {
        return progressCount;
    }

    public void setProgressCount(int progressCount) {
        this.progressCount = progressCount;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void increaseTime(double time) {
        this.time += time;
    }

    public int getErroCount() {
        return erroCount;
    }

    public int getSumError() {
        return sumError;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }
}
