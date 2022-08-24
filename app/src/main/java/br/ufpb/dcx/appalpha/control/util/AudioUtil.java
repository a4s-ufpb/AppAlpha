package br.ufpb.dcx.appalpha.control.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class AudioUtil implements TextToSpeech.OnInitListener {
    private static AudioUtil instance;
    private MediaPlayer mediaPlayer;
    private int duration;
    private TextToSpeech textToSpeak;
    private Context context;

    public static AudioUtil getInstance(Context context) {
        if (instance == null) {
            instance = new AudioUtil(context);
        }

        return instance;
    }

    public static AudioUtil getInstance() {
        if (instance == null) {
            throw new RuntimeException("Não há instância do serviço de áudio! Alguma activity deve" +
                    " chamar o SingletonAudio inicialmente");
        }

        return instance;
    }

    private AudioUtil(Context context) {
        this.context = context;
        this.textToSpeak = new TextToSpeech(context, this, "com.google.android.tts");
    }

    public synchronized void playSound(int songId) {

        this.mediaPlayer = MediaPlayer.create(this.context, songId);
        this.duration = mediaPlayer.getDuration();

        this.mediaPlayer.setOnPreparedListener(mediaPlayer -> mediaPlayer.start());
        mediaPlayer.setOnCompletionListener(mediaPlayer -> stopSound());

    }

    public synchronized void speakWord(String world) {
        this.textToSpeak.speak(world, TextToSpeech.QUEUE_FLUSH, null);
    }

    public int getDuration() {
        return duration;
    }

    public void stopSound() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (IllegalStateException e) {
                Log.e("illegal state", "provavelmente o media player não foi iniciado");
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            this.textToSpeak.setLanguage(Locale.getDefault());
            Log.d("AUDIO", "Serviço de áudio disponível");
        } else {
            Log.d("AUDIO", "Serviço de áudio indisponível");
        }
    }

    public void stopTextToSpeak() {
        if (this.textToSpeak != null) {
            this.textToSpeak.stop();
            this.textToSpeak.shutdown();
        }
        this.instance = null;
    }
}
