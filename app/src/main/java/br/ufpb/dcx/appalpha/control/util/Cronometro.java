package br.ufpb.dcx.appalpha.control.util;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

import br.ufpb.dcx.appalpha.control.ChallengeFacade;

/**
 * Class to manage stopwatch for count points
 */
public class Cronometro {
    private Chronometer cronometro;
    private Context context;

    public Cronometro(View txt, Context context) {
        this.cronometro = (Chronometer) txt;
        this.context = context;

        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                cronometro = chronometer;

                String tempo = (String) cronometro.getText();

                if(verificarHoraDaDica(tempo)) {
                    darDicaSonora();
                }
            }
        });
    }

    /**
     * Verify Time for play sound help
     * @param tempo
     * @return
     */
    private boolean verificarHoraDaDica(String tempo) {
        boolean hora_chegou = false;
        String tempo_formatado = tempo.replace(":", ".");

        double tempo_final = Double.parseDouble(tempo_formatado);

        int t = (int) tempo_final;


        if(t != 0) {

            if(tempo_final % 2 == 0) {
                hora_chegou = true;
            }
        }

        return hora_chegou;
    }

    /**
     * PLay help sound
     */
    private void darDicaSonora() {
        String soundUrl = ChallengeFacade.getInstance().getCurrentChallenge().getSoundUrl();
        if (soundUrl != null && !soundUrl.equals("")) {
            if (soundUrl.startsWith("http")) {
                AudioUtil.getInstance(context).playSoundURL(soundUrl);
            } else if (TextUtil.isAllInteger(soundUrl)) {
                AudioUtil.getInstance(context).playSound(Integer.parseInt(soundUrl));
            } else {
                AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
            }
        } else {
            AudioUtil.getInstance().speakWord(ChallengeFacade.getInstance().getCurrentChallenge().getWord());
        }
    }

    /**
     * Start stopwatch count
     */
    public void comecarCronometro() {
        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start();
    }

    /**
     * Stop stopwatch and return time
     * @return
     */
    public double parandoCronometroEPegandoTempo() {
        cronometro.stop();

        String tempo = (String) cronometro.getText();
        String tempo_form = tempo.replace(":", ".");

        return Double.parseDouble(tempo_form);

    }
}
