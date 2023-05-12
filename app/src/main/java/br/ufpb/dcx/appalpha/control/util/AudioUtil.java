package br.ufpb.dcx.appalpha.control.util;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Class to manage sound player and TTS
 */
public class AudioUtil implements TextToSpeech.OnInitListener {
    private static AudioUtil instance;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerUrl;
    private int duration;
    private TextToSpeech textToSpeak;
    private Context context;

    /**
     * Get shared instance and alloc if not exist
     *
     * @param context
     * @return
     */
    public static AudioUtil getInstance(Context context) {
        if (instance == null) {
            instance = new AudioUtil(context);
        }

        return instance;
    }

    /**
     * Get current shared instance without allocate if not exist
     *
     * @return
     */
    public static AudioUtil getInstance() {
        if (instance == null) {
            throw new RuntimeException("Não há instância do serviço de áudio! Alguma activity deve" +
                    " chamar o SingletonAudio inicialmente");
        }

        return instance;
    }

    /**
     * Alloc instance and setup local variables
     *
     * @param context
     */
    private AudioUtil(Context context) {
        this.context = context;
        this.textToSpeak = new TextToSpeech(context, this, "com.google.android.tts");

    }

    /**
     * Play local music file locally
     *
     * @param songId
     */
    public synchronized void playSound(int songId) {
        this.mediaPlayer = MediaPlayer.create(this.context, songId);
        this.duration = mediaPlayer.getDuration();

        this.mediaPlayer.setOnPreparedListener(mediaPlayer -> mediaPlayer.start());
        mediaPlayer.setOnCompletionListener(mediaPlayer -> stopSound());
    }

    /**
     * Play remote music from url
     *
     * @param url
     */
    public synchronized void playSoundURL(String url) {

        this.mediaPlayerUrl = new MediaPlayer();
        mediaPlayerUrl.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayerUrl.setDataSource(url);
            mediaPlayerUrl.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.duration = mediaPlayerUrl.getDuration();
        Log.i("Audio URL", "Duracao do audio e: " + this.duration);

        this.mediaPlayerUrl.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayerUrl.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                } catch (IllegalStateException e) {
                    Log.e("illegal state", "provavelmente o media player não foi iniciado");
                }
            }
        });

    }

    /**
     * Start TTS speak from supplied word
     *
     * @param world
     */
    public synchronized void speakWord(String world) {
        this.textToSpeak.speak(world, TextToSpeech.QUEUE_FLUSH, null);
        this.duration = 0;
    }

    /**
     * Duration of current playing audio
     *
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Stop and Release specific player
     *
     * @param player
     */
    public void stopSoundPlayer(MediaPlayer player) {
        if (player != null) {
            try {
                player.stop();
                player.release();
                player = null;
            } catch (IllegalStateException e) {
                Log.e("illegal state", "provavelmente o media player não foi iniciado");
            }
        }
    }

    /**
     * Stop and Release all players
     */
    public void stopSound() {
        stopSoundPlayer(mediaPlayer);
        stopSoundPlayer(mediaPlayerUrl);
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

    /**
     * Stop speaking TTS
     */
    public void stopTextToSpeak() {
        if (this.textToSpeak != null) {
            this.textToSpeak.stop();
        }
    }

    /**
     * Check if TTS is currently playing
     *
     * @return
     */
    public boolean isTSS_Playing() {
        boolean ret = false;
        try {
            if (this.textToSpeak != null) {
                ret = this.textToSpeak.isSpeaking();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Wait in current thread until TTS finish
     */
    public void esperarTssParar() {
        while (AudioUtil.getInstance() != null && AudioUtil.getInstance().isTSS_Playing()) {
            try {
                sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stop TTS and All Playes
     */
    public void pararTSSePlayer() {
        stopSound();
        stopTextToSpeak();
    }
}
