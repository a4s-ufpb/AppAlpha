package br.ufpb.dcx.appalpha.control;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufpb.dcx.appalpha.control.util.TextUtil;
import br.ufpb.dcx.appalpha.model.bean.Challenge;
import br.ufpb.dcx.appalpha.model.bean.Theme;

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
    private String currentUnderscore;
    private Set<String> triedLetters = new HashSet<>();


    private ChallengeFacade() {
    }

    public static ChallengeFacade getInstance() {
        if (instance == null) {
            instance = new ChallengeFacade();
        }

        return instance;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void init(List<Challenge> challenges, Theme selectedTheme) {
        this.challenges = challenges;
        this.currentChallenge = challenges.get(0);
        this.selectedTheme = selectedTheme;
        this.erroCount = 0;
        this.progressCount = 0;
        this.time = 0.0;
        this.sumError = 0;
        this.currentUnderscore = "";
        setUnderscore();

    }

    public Theme getSelectedTheme() {
        return selectedTheme;
    }

    public Challenge getCurrentChallenge() {
        return currentChallenge;
    }

    public void nextChallenge() {
        try {
            this.sumError += this.erroCount;
            this.erroCount = 0;
            this.progressCount++;
            this.currentChallenge = challenges.get(challenges.indexOf(currentChallenge) + 1);
            this.triedLetters.clear();
            setUnderscore();
        } catch (IndexOutOfBoundsException e) {
            this.currentChallenge = null;
        }
    }

    public void setCurrentChallenge(Challenge currentChallenge) {
        this.currentChallenge = currentChallenge;
    }

    public void increaseError() {
        this.erroCount++;
    }

    /**
     * Deixa a palavra em underscore
     */
    public void setUnderscore() {
        this.currentUnderscore = TextUtil.getInstance().getUnderscoreOfThis(this.currentChallenge.getWord());
    }

    public void setCurrentUnderscore(String currentUnderscore) {
        this.currentUnderscore = currentUnderscore;
    }

    /**
     * Verifica se o chute do usuário foi certo ou errado
     *
     * @param letter chute do usuário
     * @return um int indicando se houve acerto ou erro
     */
    public int checkAttempt(String letter) {

        String newUnderscoreAfterAttempt = updateWordByAttempt(letter.charAt(0));

        if (currentUnderscore.equalsIgnoreCase(newUnderscoreAfterAttempt)) {
            return ATTEMPT_REJECTED;

        } else {
            currentUnderscore = newUnderscoreAfterAttempt;
            return ATTEMPT_ACEPTED;
        }

    }

    /**
     * Verifica se o chute se encontra na palavra. Caso sim, a adiciona no underscore
     *
     * @param letter letra que o usuário chutou
     * @return o underscore modificado
     */
    public String updateWordByAttempt(char letter) {
        StringBuilder newUnderscore = new StringBuilder(currentUnderscore);
        String currentChallengeWord = this.currentChallenge.getWord();

        for (int indexInWord = 0; indexInWord < currentChallengeWord.length(); indexInWord++) {
            char upperCasedLetterAttempt = Character.toUpperCase(letter);
            char upperCasedCurrentChallengeWordLetter = Character.toUpperCase(currentChallengeWord.charAt(indexInWord));

            if (upperCasedLetterAttempt == upperCasedCurrentChallengeWordLetter) {
                newUnderscore.setCharAt(indexInWord, Character.toLowerCase(upperCasedCurrentChallengeWordLetter));
            }
        }

        return newUnderscore.toString();
    }

    /**
     * Verifica se a letra já foi chutada antes
     *
     * @param letter letra que o usuário chutou
     * @return boolean indicando se já foi chutada antes
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
     * Verifica se o usuário acertou a palavra, se o sublinhado estiver por completo igual a palavra (sem traços) é porque a palavra foi completa.
     *
     * @return um boolean indicando se acertou ou não
     */
    public boolean checkWordAccepted() {
        return this.currentChallenge.getWord().equalsIgnoreCase(currentUnderscore);
    }

    /**
     * Verifica se o chute do usuário foi vazio, se ele já chutou aquela letra ou se o chute foi errado
     *
     * @param letter letra chutada pelo usuário
     * @return um inteiro indicando se o chute foi vazio, já existente ou errado
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

    public String getCurrentUnderscore() {
        return currentUnderscore;
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
}
