package com.jamesmorrisstudios.appbaselibrary.sound;

import android.content.res.TypedArray;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RawRes;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.utilitieslibrary.app.AppUtil;
import com.jamesmorrisstudios.utilitieslibrary.preferences.Preferences;

import java.util.HashMap;

/**
 * Created by James on 5/30/2015.
 */
public class Sounds {

    private static Sounds instance = null;

    private Sounds() {}

    public static Sounds getInstance() {
        if(instance == null) {
            instance = new Sounds();
        }
        return instance;
    }

    private MediaPlayer musicPlayer = null;
    private SoundPool soundPool = null;
    private boolean soundEffectLoaded = false;
    private HashMap<Integer, Integer> soundIdMap;
    private boolean soundEffectEnabled = true, musicEnabled = true;

    public final void onStart() {
        soundEffectEnabled = getPrefSoundEffect();
        musicEnabled = getPrefMusic();
        startSoundEffects();
        startMusic();
    }

    public final void onStop() {
        stopSoundEffects();
        stopMusic();
    }

    public final void onDestroy() {
        destroyMusic();
    }

    public final void reloadSettings() {
        boolean soundEffect = getPrefSoundEffect();
        if(soundEffect != soundEffectEnabled) {
            if(soundEffect) {
                startSoundEffects();
            } else {
                stopSoundEffects();
            }
        }
        boolean music = getPrefMusic();
        if(music != musicEnabled) {
            if(music) {
                startMusic();
            } else {
                stopMusic();
            }
        }
    }

    private boolean getPrefSoundEffect() {
        String pref = AppUtil.getContext().getString(R.string.settings_pref);
        String keySound = AppUtil.getContext().getString(R.string.pref_sound_effect);
        return Preferences.getBoolean(pref, keySound, true);
    }

    private boolean getPrefMusic() {
        String pref = AppUtil.getContext().getString(R.string.settings_pref);
        String keyMusic = AppUtil.getContext().getString(R.string.pref_sound_effect);
        return Preferences.getBoolean(pref, keyMusic, true);
    }

    /**
     * Checks if sound effects are loaded and ready
     *
     * @return True if sound effects are ready to be played
     */
    private boolean areSoundEffectsReady() {
        return soundPool != null && soundEffectLoaded;
    }

    public final void playSoundEffect(@RawRes int itemRes) {
        if(areSoundEffectsReady()) {
            int soundId = getSoundId(itemRes);
            if(soundId != 0) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f);
            }
        }
    }

    private int getSoundId(@RawRes int itemRes) {
        if(soundIdMap.containsKey(itemRes)) {
            return soundIdMap.get(itemRes);
        }
        return 0;
    }

    /**
     * Initializes the sound effects engine
     */
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
            TypedArray sounds = AppUtil.getContext().getResources().obtainTypedArray(R.array.sound_effects);
            for (int i = 0; i < sounds.length(); i++) {
                Log.v("SOUNDS", "Have an ID");
                int id = sounds.getResourceId(i, 0);
                if (id > 0) {
                    Log.v("SOUNDS", "Adding to sound pool");
                    soundIdMap.put(id, soundPool.load(AppUtil.getContext(), id, 1));
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

    private void startMusic() {
        if (musicEnabled && musicPlayer == null) {
            TypedArray sounds = AppUtil.getContext().getResources().obtainTypedArray(R.array.music);
            int musicId = 0;
            if(sounds.length() >= 1) {
                musicId = sounds.getResourceId(0, 0);
            }
            sounds.recycle();
            if(musicId == 0) {
                return;
            }
            musicPlayer = MediaPlayer.create(AppUtil.getContext(), musicId);
            musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mp == musicPlayer) {
                        musicPlayer.setLooping(true);
                        musicPlayer.setVolume(0.5f, 0.5f);
                        musicPlayer.start();
                    }
                }
            });
        } else if (musicEnabled) {
            musicPlayer.start();
        }
    }

    private void stopMusic() {
        if (musicPlayer != null) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
            }
        }
    }

    /**
     * Stops and releases all holds on the playing music to free memory
     */
    private void destroyMusic() {
        if (musicPlayer != null) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.stop();
            }
            musicPlayer.reset();
            musicPlayer.release();
            musicPlayer = null;
        }
    }

}
