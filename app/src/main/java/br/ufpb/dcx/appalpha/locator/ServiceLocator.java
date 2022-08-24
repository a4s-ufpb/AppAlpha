package br.ufpb.dcx.appalpha.locator;

import br.ufpb.dcx.appalpha.control.ChallengeFacade;

public class ServiceLocator {
    private static ServiceLocator soleInstance;
    private ChallengeFacade challengeFacade;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (soleInstance == null) {
            soleInstance = new ServiceLocator();
        }

        return soleInstance;
    }

    public static void load(ServiceLocator arg) {
        soleInstance = arg;
    }

    public void setChallengeFacade(ChallengeFacade challengeFacade) {
        this.challengeFacade = challengeFacade;
    }

    public ChallengeFacade getChallengeFacade() {
        return challengeFacade;
    }
}
