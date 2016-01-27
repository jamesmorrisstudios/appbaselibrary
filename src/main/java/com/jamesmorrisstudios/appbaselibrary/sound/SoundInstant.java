package com.jamesmorrisstudios.appbaselibrary.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.UtilsLocale;
import com.jamesmorrisstudios.appbaselibrary.UtilsLocation;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Supports loading and playing one specific audio stream at a time.
 * Playback starts as soon as loading is complete. Cannot be stopped but cannot be paused.
 * <p/>
 * Created by James on 12/11/2015.
 */
public final class SoundInstant {
    private static SoundInstant instance = null;
    private MediaPlayer music;
    private TextToSpeech tts;

    /**
     * Private constructor
     */
    private SoundInstant() {
    }

    /**
     * @return SoundInstant instance
     */
    @NonNull
    public static SoundInstant getInstance() {
        if (instance == null) {
            instance = new SoundInstant();
        }
        return instance;
    }

    /**
     * Loads and plays sound in notification stream
     *
     * @param uri Uri of file
     */
    public final void loadAndPlayNotification(@NonNull final Uri uri) {
        loadAndPlay(uri, AudioManager.STREAM_NOTIFICATION);
    }

    /**
     * Loads and plays sound in alarm stream
     *
     * @param uri Uri of file
     */
    public final void loadAndPlayAlarm(@NonNull final Uri uri) {
        loadAndPlay(uri, AudioManager.STREAM_ALARM);
    }

    /**
     * Loads and plays sound in ring stream
     *
     * @param uri Uri of file
     */
    public final void loadAndPlayRing(@NonNull final Uri uri) {
        loadAndPlay(uri, AudioManager.STREAM_RING);
    }

    /**
     * Loads and plays sound in music stream
     *
     * @param uri Uri of file
     */
    public final void loadAndPlayMusic(@NonNull final Uri uri) {
        loadAndPlay(uri, AudioManager.STREAM_MUSIC);
    }

    /**
     * Loads and plays sound in the given music stream type
     *
     * @param uri        Uri of file
     * @param streamType Stream to play on
     */
    public final void loadAndPlay(@NonNull final Uri uri, final int streamType) {
        music = new MediaPlayer();
        music.setAudioStreamType(streamType);
        try {
            music.setDataSource(AppBase.getContext(), uri);
            music.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            music.stop();
                            music.release();
                        }
                    });
                    music.setLooping(false);
                    music.setVolume(1.0f, 1.0f);
                    music.start();
                }
            });
            music.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops any currently playing sounds.
     */
    public final void stopPlayback() {
        try {
            if (music != null) {
                if (music.isPlaying()) {
                    music.stop();
                }
                music.release();
            }
        } catch (Exception ex) {
            //Do nothing as the media player is stupid.
        }
    }

    /**
     * Speaks text in notification stream
     *
     * @param text Text to speak
     */
    public final void speakTextNotification(@NonNull final String text) {
        speakText(text, AudioManager.STREAM_NOTIFICATION);
    }

    /**
     * Speaks text in alarm stream
     *
     * @param text Text to speak
     */
    public final void speakTextAlarm(@NonNull final String text) {
        speakText(text, AudioManager.STREAM_ALARM);
    }

    /**
     * Speaks text in ring stream
     *
     * @param text Text to speak
     */
    public final void speakTextRing(@NonNull final String text) {
        speakText(text, AudioManager.STREAM_RING);
    }

    /**
     * Speaks text in music stream
     *
     * @param text Text to speak
     */
    public final void speakTextMusic(@NonNull final String text) {
        speakText(text, AudioManager.STREAM_MUSIC);
    }

    /**
     * Speaks text in given stream. If the current language is not support it falls back to english.
     * If english is not supported or the TTS engine is not setup it says nothing.
     *
     * @param text Text to speak
     */
    public final void speakText(@NonNull final String text, final int streamType) {
        tts = new TextToSpeech(AppBase.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int result=tts.setLanguage(UtilsLocale.getLocale());
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("SoundInstant", "This Language is not supported");
                        result=tts.setLanguage(Locale.US);
                    }
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("SoundInstant", "English not supported");
                        return;
                    }
                    HashMap<String, String> myHashAlarm = new HashMap<>();
                    myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(streamType));
                    tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                        @Override
                        public void onUtteranceCompleted(String utteranceId) {
                            tts.shutdown();
                        }
                    });
                    tts.speak(text, TextToSpeech.QUEUE_ADD, myHashAlarm);
                }
            }
        });
    }

}
