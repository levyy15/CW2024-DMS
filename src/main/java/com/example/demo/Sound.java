package com.example.demo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    private Clip clip;
    URL soundURL[] = new URL[30];

    public Sound(){
        soundURL[0] = getClass().getResource("/sound/game.wav");
        soundURL[1] = getClass().getResource("/sound/bullet.wav");
        soundURL[2] = getClass().getResource("/sound/fireUlt.wav");
        soundURL[3] = getClass().getResource("/sound/enemy.wav");

    }

    public void setFile(int i){
        try{

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch(Exception e) {
        }

    }

    public void play(){

        clip.start();

    }
    public void loop(){

        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }
    public void stop(){

        clip.stop();

    }
    public Clip getClip() {
        return clip;
    }

}
