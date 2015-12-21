package com.jamesmorrisstudios.appbaselibrary.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

import java.io.IOException;

/**
 * Supports loading and playing one specific audio stream at a time.
 * Playback starts as soon as loading is complete. Cannot be stopped but cannot be paused.
 * <p/>
 * Created by James on 12/11/2015.
 */
public final class SoundInstant {
    private static SoundInstant instance = null;
    private MediaPlayer music;

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

}
