package audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URL;
import java.util.Random;;

public class AudioPlayer {

    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;
    public static int LEVEL_3 = 3;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1.0f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer() {
        loadEffects();
        loadSongs();
        playSong(MENU_1);
    }

    private void loadSongs() {
        String[] names = { "menu", "level1", "level2", "level3" };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    private void loadEffects() {
        String[] effectnames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
        effects = new Clip[effectnames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectnames[i]);
        updateEffectsVolume();

    }
//D:\zoro\src\res\audio\lvlcompleted.wav
    private Clip getClip(String name) {
        URL url = getClass().getResource("/res/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateEffectsVolume();
        updateSongVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 3 == 0)
            playSong(LEVEL_1);
        else if (lvlIndex % 3 == 1)
            playSong(LEVEL_2);
        else
            playSong(LEVEL_3);
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playAttackSound() {
        int start = 4;// the index of attack1 is 4 inside effectnames array
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);// we want every sound to be played from start rather than previous
                                                  // continoution
        effects[effect].start();
    }

    public void playSong(int song) {
        stopSong();// stop the current song to play new song
        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(JUMP);

    }

    private void updateSongVolume() {
        // this is background music we are gonna here
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
