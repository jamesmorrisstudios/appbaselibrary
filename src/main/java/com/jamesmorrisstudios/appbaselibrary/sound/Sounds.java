package com.jamesmorrisstudios.appbaselibrary.sound;

import android.content.res.TypedArray;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RawRes;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.utilitieslibrary.app.AppUtil;
import com.jamesmorrisstudios.utilitieslibrary.preferences.Prefs;

import java.util.HashMap;

/**
 * Created by James on 5/30/2015.
 */
public class Sounds {
    private static Sounds instance = null;
    private MediaPlayer musicPrimary = null, musicSecondary = null;
    private SoundPool soundPool = null;
    private boolean soundEffectLoaded = false, musicPrimaryActive = true, musicPrimaryLoaded = false, musicSecondaryLoaded = false;
    private HashMap<Integer, Integer> soundIdMap;
    private boolean soundEffectEnabled = true, musicEnabled = true;
    private Handler musicFade = new Handler();
    private MusicFadeIn musicFadeIn = new MusicFadeIn();
    private MusicFadeOut musicFadeOut = new MusicFadeOut();

    private Sounds() {}

    public static Sounds getInstance() {
        if (instance == null) {
            instance = new Sounds();
        }
        return instance;
    }

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
        if (soundEffect != soundEffectEnabled) {
            soundEffectEnabled = soundEffect;
            Log.v("Sounds", "Sound Effects... " + soundEffect);
            if (soundEffect) {
                startSoundEffects();
            } else {
                stopSoundEffects();
            }
        }
        boolean music = getPrefMusic();
        if (music != musicEnabled) {
            musicEnabled = music;
            Log.v("Sounds", "Music... " + music);
            if (music) {
                startMusic();
            } else {
                stopMusic();
            }
        }
    }

    private boolean getPrefSoundEffect() {
        String pref = AppUtil.getContext().getString(R.string.settings_pref);
        String keySound = AppUtil.getContext().getString(R.string.pref_sound_effect);
        return Prefs.getBoolean(pref, keySound, true);
    }

    private boolean getPrefMusic() {
        String pref = AppUtil.getContext().getString(R.string.settings_pref);
        String keyMusic = AppUtil.getContext().getString(R.string.pref_music);
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

    @SuppressWarnings("unused")
    public final void playSoundEffect(@RawRes int itemRes) {
        if (areSoundEffectsReady()) {
            int soundId = getSoundId(itemRes);
            if (soundId != 0) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f);
            }
        }
    }

    private int getSoundId(@RawRes int itemRes) {
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
            TypedArray sounds = AppUtil.getContext().getResources().obtainTypedArray(R.array.sound_effects);
            for (int i = 0; i < sounds.length(); i++) {
                //Log.v("SOUNDS", "Have an ID");
                int id = sounds.getResourceId(i, 0);
                if (id > 0) {
                    //Log.v("SOUNDS", "Adding to sound pool");
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

    public final void playMusicPrimary(boolean crossfade, boolean restart) {
        Log.v("Sounds", "Play music primary");
        musicPrimaryActive = true;
        cancelMusicFade();
        if(musicSecondary != null && musicSecondaryLoaded && musicSecondary.isPlaying()) {
            if(crossfade) {
                fadeOutMusic(musicSecondary, 0.5f);
            } else {
                musicSecondary.pause();
            }
        }
        if(musicPrimary != null && musicPrimaryLoaded) {
            if(restart) {
                musicPrimary.seekTo(0);
            }
            if(crossfade) {
                musicPrimary.setVolume(0, 0);
                musicPrimary.setLooping(true);
                musicPrimary.start();
                fadeInMusic(musicPrimary, 0.0f);
            } else {
                musicPrimary.setLooping(true);
                musicPrimary.start();
            }
        }
    }

    public final void playMusicSecondary(boolean crossfade, boolean restart) {
        Log.v("Sounds", "Play music secondary");
        musicPrimaryActive = false;
        cancelMusicFade();
        if(musicPrimary != null && musicPrimaryLoaded && musicPrimary.isPlaying()) {
            if(crossfade) {
                fadeOutMusic(musicPrimary, 0.5f);
            } else {
                musicPrimary.pause();
            }
        }
        if(musicSecondary != null && musicSecondaryLoaded) {
            if(restart) {
                musicSecondary.seekTo(0);
            }
            if(crossfade) {
                musicSecondary.setVolume(0, 0);
                musicSecondary.setLooping(true);
                musicSecondary.start();
                fadeInMusic(musicSecondary, 0.0f);
            } else {
                musicSecondary.setLooping(true);
                musicSecondary.start();
            }
        }
    }

    private void startMusic() {
        Log.v("Sounds", "Start Music");
        if (musicEnabled && musicPrimary == null) {
            TypedArray sounds = AppUtil.getContext().getResources().obtainTypedArray(R.array.music);
            int musicPrimaryId = 0;
            int musicSecondaryId = 0;
            if (sounds.length() == 1) {
                musicPrimaryId = sounds.getResourceId(0, 0);
            } else if(sounds.length() >= 2) {
                musicPrimaryId = sounds.getResourceId(0, 0);
                musicSecondaryId = sounds.getResourceId(1, 0);
            }
            sounds.recycle();
            if (musicPrimaryId != 0) {
                Log.v("Sounds", "Create Primary Music");
                musicPrimary = MediaPlayer.create(AppUtil.getContext(), musicPrimaryId);
                musicPrimary.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mp == musicPrimary) {
                            Log.v("Sounds", "Create Primary Music Complete");
                            musicPrimaryLoaded = true;
                            if(musicPrimaryActive) {
                                musicPrimary.setLooping(true);
                                musicPrimary.setVolume(0.0f, 0.0f);
                                musicPrimary.start();
                                fadeInMusic(musicPrimary, 0.0f);
                            }
                        }
                    }
                });
            }
            if (musicSecondaryId != 0) {
                Log.v("Sounds", "Create Secondary Music");
                musicSecondary = MediaPlayer.create(AppUtil.getContext(), musicSecondaryId);
                musicSecondary.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mp == musicSecondary) {
                            Log.v("Sounds", "Create Secondary Music Complete");
                            musicSecondaryLoaded = true;
                            if(!musicPrimaryActive) {
                                musicSecondary.setLooping(true);
                                musicSecondary.setVolume(0.0f, 0.0f);
                                musicSecondary.start();
                                fadeInMusic(musicSecondary, 0.0f);
                            }
                        }
                    }
                });
            }
        } else if (musicEnabled) {
            if(musicPrimaryActive) {
                musicPrimary.start();
            } else {
                musicSecondary.start();
            }
        }
    }

    private void stopMusic() {
        Log.v("Sounds", "Stop Music");
        if (musicPrimary != null) {
            if (musicPrimary.isPlaying()) {
                musicPrimary.pause();
            }
        }
        if (musicSecondary != null) {
            if (musicSecondary.isPlaying()) {
                musicSecondary.pause();
            }
        }
    }

    /**
     * Stops and releases all holds on the playing music to free memory
     */
    private void destroyMusic() {
        Log.v("Sounds", "Destroy Music");
        /*
        if (musicPrimary != null) {
            if (musicPrimary.isPlaying()) {
                musicPrimary.stop();
            }
            musicPrimary.reset();
            musicPrimary.release();
            musicPrimary = null;
        }
        if (musicSecondary != null) {
            if (musicSecondary.isPlaying()) {
                musicSecondary.stop();
            }
            musicSecondary.reset();
            musicSecondary.release();
            musicSecondary = null;
        }
        musicPrimaryLoaded = false;
        musicSecondaryLoaded = false;
        */
    }

    private void cancelMusicFade() {
        musicFade.removeCallbacks(musicFadeIn);
        musicFade.removeCallbacks(musicFadeOut);
    }

    private void fadeInMusic(final MediaPlayer player, final float volume) {
        if(!player.isPlaying()) {
            return;
        }
        if(volume >= 0.5f) {
            return;
        }
        musicFadeIn.setValues(player, volume);
        musicFade.postDelayed(musicFadeIn, 50);
    }

    private void fadeOutMusic(final MediaPlayer player, final float volume) {
        if(!player.isPlaying()) {
            return;
        }
        if(volume <= 0) {
            player.pause();
            return;
        }
        musicFadeOut.setValues(player, volume);
        musicFade.postDelayed(musicFadeOut, 50);
    }

    private class MusicFadeIn implements Runnable {
        private MediaPlayer player;
        private float volume;

        public final void setValues(MediaPlayer player, final float volume) {
            this.player = player;
            this.volume = volume;
        }

        @Override
        public void run() {
            if(musicPrimaryLoaded && musicSecondaryLoaded && player.isPlaying()) {
                player.setVolume(volume, volume);
                fadeInMusic(player, volume + 0.05f);
            }
        }
    }

    private class MusicFadeOut implements Runnable {
        private MediaPlayer player;
        private float volume;

        public final void setValues(MediaPlayer player, final float volume) {
            this.player = player;
            this.volume = volume;
        }

        @Override
        public void run() {
            if(musicPrimaryLoaded && musicSecondaryLoaded && player.isPlaying()) {
                player.setVolume(volume, volume);
                fadeOutMusic(player, volume - 0.05f);
            }
        }
    }

}
