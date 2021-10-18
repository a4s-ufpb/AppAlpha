package br.ufpb.dcx.appalpha.locator;

import br.ufpb.dcx.appalpha.control.ChallengeFacade;

public class ServiceLocator {
    private static ServiceLocator soleInstance;
    private static ChallengeFacade challengeFacade;

    public static void load(ServiceLocator arg) {
        soleInstance = arg;
    }

    public static void setChallengeFacade(ChallengeFacade challengeFacade) {
        challengeFacade = challengeFacade;
    }

    public static ChallengeFacade getChallengeFacade() {
        return challengeFacade;
    }
}
