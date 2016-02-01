package com.jamesmorrisstudios.appbaselibrary.sound;

import android.content.res.TypedArray;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

import java.util.HashMap;

/**
 * Sound Effects and Music handler
 * <p/>
 * Created by James on 5/30/2015.
 */
public final class Sounds {
    private static Sounds instance = null;
    private SoundPool soundPool = null;
    private boolean soundEffectLoaded = false;
    private HashMap<Integer, Integer> soundIdMap;
    private boolean soundEffectEnabled = true, musicEnabled = true;
    private Handler musicFade = new Handler();
    private MusicFadeIn musicFadeIn = new MusicFadeIn();
    private MusicFadeOut musicFadeOut = new MusicFadeOut();
    private boolean musicPrimaryActive = true;
    private boolean[] musicLoaded = new boolean[2];
    private MediaPlayer[] music = new MediaPlayer[2];

    /**
     * Private constructor
     */
    private Sounds() {
    }

    /**
     * @return Sounds instance
     */
    @NonNull
    public static Sounds getInstance() {
        if (instance == null) {
            instance = new Sounds();
        }
        return instance;
    }

    /**
     * Call from Activity onStart
     */
    public final void onStart() {
        soundEffectEnabled = getPrefSoundEffect();
        musicEnabled = getPrefMusic();
        startSoundEffects();
        startMusic();
    }

    /**
     * Call from Activity onStop
     */
    public final void onStop() {
        stopSoundEffects();
        stopMusic();
    }

    /**
     * Reload settings if user options for music or sound effects settings change
     */
    public final void reloadSettings() {
        boolean soundEffect = getPrefSoundEffect();
        if (soundEffect != soundEffectEnabled) {
            soundEffectEnabled = soundEffect;
            if (soundEffect) {
                startSoundEffects();
            } else {
                stopSoundEffects();
            }
        }
        boolean music = getPrefMusic();
        if (music != musicEnabled) {
            musicEnabled = music;
            if (music) {
                startMusic();
            } else {
                stopMusic();
            }
        }
    }

    /**
     * @return True if sound effects are enabled
     */
    private boolean getPrefSoundEffect() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String keySound = AppBase.getContext().getString(R.string.pref_sound_effect);
        return Prefs.getBoolean(pref, keySound, true);
    }

    /**
     * @return True if music is enabled
     */
    private boolean getPrefMusic() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String keyMusic = AppBase.getContext().getString(R.string.pref_music);
        return Prefs.getBoolean(pref, keyMusic, true);
    }

    /**
     * Checks if sound effects are loaded and ready
     *
     * @return True if sound effects are ready to be played
     */
    private boolean areSoundEffectsReady() {
        return soundPool != null && soundEffectLoaded;
    }

    /**
     * Play the given sound effect.
     * Make sure all sounds effects are loaded in the sound_effects array in xml first
     *
     * @param itemRes Sound resource id
     */
    public final void playSoundEffect(@RawRes final int itemRes) {
        if (areSoundEffectsReady()) {
            int soundId = getSoundId(itemRes);
            if (soundId != 0) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f);
            } else {
                Log.e("Sounds", "Failed to load sound effect");
            }
        } else {
            Log.w("Sounds", "Sound effects not ready");
        }
    }

    /**
     * Get the internal sound Id for the given sound resource Id
     *
     * @param itemRes Sound resource id
     * @return internal sound Id
     */
    private int getSoundId(@RawRes final int itemRes) {
        if (soundIdMap.containsKey(itemRes)) {
            return soundIdMap.get(itemRes);
        }
        return 0;
    }

    /**
     * Initializes the sound effects engine
     */
    @SuppressWarnings("deprecation")
    private void startSoundEffects() {
        if (soundEffectEnabled && soundPool == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes attr = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
                soundPool = new SoundPool.Builder()
                        .setAudioAttributes(attr)
                        .setMaxStreams(20)
                        .build();
            } else {
                soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
            }
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundEffectLoaded = true;
                }
            });
            soundIdMap = new HashMap<>();
            //Add the built in sound effects
            TypedArray sounds = AppBase.getContext().getResources().obtainTypedArray(R.array.sound_effects);
            for (int i = 0; i < sounds.length(); i++) {
                //Log.v("SOUNDS", "Have an ID");
                int id = sounds.getResourceId(i, 0);
                if (id > 0) {
                    //Log.v("SOUNDS", "Adding to sound pool");
                    soundIdMap.put(id, soundPool.load(AppBase.getContext(), id, 1));
                }
            }
            sounds.recycle();

        } else if (soundEffectEnabled) {
            soundPool.autoResume();
            soundEffectLoaded = true;
        }
    }

    /**
     * Pauses all currently playing sound effects
     */
    private void stopSoundEffects() {
        if (soundPool != null) {
            soundPool.autoPause();
            soundEffectLoaded = false;
        }
    }

    /**
     * Plays the primary music track
     * Ensure the music file is loaded via the music array in xml first.
     * Can have up to 2 tracks.
     *
     * @param crossfade True to crossfade with secondary if its playing
     * @param restart   True to restart file from the beginning. False to continue where it was before
     */
    public final void playMusicPrimary(final boolean crossfade, final boolean restart) {
        if (!musicEnabled) {
            return;
        }
        if (musicPrimaryActive && music[0].isPlaying()) {
            return;
        }
        musicPrimaryActive = true;
        cancelMusicFade();
        if (music[1] != null && musicLoaded[1] && music[1].isPlaying()) {
            if (crossfade) {
                fadeOutMusic(1, 0.5f);
            } else {
                music[1].pause();
            }
        }
        if (music[0] != null && musicLoaded[0]) {
            if (restart) {
                music[0].seekTo(0);
            }
            if (crossfade) {
                music[0].setVolume(0, 0);
                music[0].setLooping(true);
                music[0].start();
                fadeInMusic(0, 0.0f);
            } else {
                music[0].setLooping(true);
                music[0].start();
            }
        }
    }

    /**
     * Plays the secondary music track
     * Ensure the music file is loaded via the music array in xml first.
     * Can have up to 2 tracks.
     *
     * @param crossfade True to crossfade with primary if its playing
     * @param restart   True to restart file from the beginning. False to continue where it was before
     */
    public final void playMusicSecondary(final boolean crossfade, final boolean restart) {
        if (!musicEnabled) {
            return;
        }
        if (!musicPrimaryActive && music[1].isPlaying()) {
            return;
        }
        musicPrimaryActive = false;
        cancelMusicFade();
        if (music[0] != null && musicLoaded[0] && music[0].isPlaying()) {
            if (crossfade) {
                fadeOutMusic(0, 0.5f);
            } else {
                music[0].pause();
            }
        }
        if (music[1] != null && musicLoaded[1]) {
            if (restart) {
                music[1].seekTo(0);
            }
            if (crossfade) {
                music[1].setVolume(0, 0);
                music[1].setLooping(true);
                music[1].start();
                fadeInMusic(1, 0.0f);
            } else {
                music[1].setLooping(true);
                music[1].start();
            }
        }
    }

    /**
     * Starts music playback
     */
    private void startMusic() {
        if (musicEnabled && music[0] == null) {
            TypedArray sounds = AppBase.getContext().getResources().obtainTypedArray(R.array.music);
            int musicPrimaryId = 0;
            int musicSecondaryId = 0;
            if (sounds.length() == 1) {
                musicPrimaryId = sounds.getResourceId(0, 0);
            } else if (sounds.length() >= 2) {
                musicPrimaryId = sounds.getResourceId(0, 0);
                musicSecondaryId = sounds.getResourceId(1, 0);
            }
            sounds.recycle();
            if (musicPrimaryId != 0) {
                music[0] = MediaPlayer.create(AppBase.getContext(), musicPrimaryId);
                music[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mp == music[0]) {
                            musicLoaded[0] = true;
                            if (musicPrimaryActive) {
                                music[0].setLooping(true);
                                music[0].setVolume(0.0f, 0.0f);
                                music[0].start();
                                fadeInMusic(0, 0.0f);
                            }
                        }
                    }
                });
            }
            if (musicSecondaryId != 0) {
                music[1] = MediaPlayer.create(AppBase.getContext(), musicSecondaryId);
                music[1].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mp == music[1]) {
                            musicLoaded[1] = true;
                            if (!musicPrimaryActive) {
                                music[1].setLooping(true);
                                music[1].setVolume(0.0f, 0.0f);
                                music[1].start();
                                fadeInMusic(1, 0.0f);
                            }
                        }
                    }
                });
            }
        } else if (musicEnabled) {
            if (musicPrimaryActive) {
                music[0].start();
            } else {
                music[1].start();
            }
        }
    }

    /**
     * Stops music playback. Pauses at current location
     */
    private void stopMusic() {
        if (music[0] != null) {
            if (music[0].isPlaying()) {
                music[0].pause();
            }
        }
        if (music[1] != null) {
            if (music[1].isPlaying()) {
                music[1].pause();
            }
        }
    }

    /**
     * Cancel a music face callback
     */
    private void cancelMusicFade() {
        musicFade.removeCallbacks(musicFadeIn);
        musicFade.removeCallbacks(musicFadeOut);
    }

    /**
     * Fade in music track
     *
     * @param playerIndex Index of player to fade in
     * @param volume      Current volume
     */
    private void fadeInMusic(final int playerIndex, final float volume) {
        if (!music[playerIndex].isPlaying()) {
            return;
        }
        if (volume >= 0.5f) {
            return;
        }
        musicFadeIn.setValues(playerIndex, volume);
        musicFade.postDelayed(musicFadeIn, 50);
    }

    /**
     * Fade out music track
     *
     * @param playerIndex Index of player to fade out
     * @param volume      Current volume
     */
    private void fadeOutMusic(final int playerIndex, final float volume) {
        if (!music[playerIndex].isPlaying()) {
            return;
        }
        if (volume <= 0) {
            music[playerIndex].pause();
            return;
        }
        musicFadeOut.setValues(playerIndex, volume);
        musicFade.postDelayed(musicFadeOut, 50);
    }

    /**
     * Music fade in runnable
     */
    private final class MusicFadeIn implements Runnable {
        private int playerIndex;
        private float volume;

        /**
         * @param playerIndex Index of music player
         * @param volume      Current volume
         */
        public final void setValues(final int playerIndex, final float volume) {
            this.playerIndex = playerIndex;
            this.volume = volume;
        }

        /**
         * Run!
         */
        @Override
        public final void run() {
            if (musicLoaded[playerIndex] && music[playerIndex].isPlaying()) {
                music[playerIndex].setVolume(volume, volume);
                fadeInMusic(playerIndex, volume + 0.05f);
            }
        }
    }

    /**
     * Music fade out runnable
     */
    private final class MusicFadeOut implements Runnable {
        private int playerIndex;
        private float volume;

        /**
         * @param playerIndex Index of music player
         * @param volume      Current volume
         */
        public final void setValues(final int playerIndex, final float volume) {
            this.playerIndex = playerIndex;
            this.volume = volume;
        }

        /**
         * Run!
         */
        @Override
        public final void run() {
            if (musicLoaded[playerIndex] && music[playerIndex].isPlaying()) {
                music[playerIndex].setVolume(volume, volume);
                fadeOutMusic(playerIndex, volume - 0.05f);
            }
        }
    }

}
