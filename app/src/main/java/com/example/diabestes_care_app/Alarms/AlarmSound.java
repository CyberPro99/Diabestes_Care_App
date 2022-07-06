package com.example.diabestes_care_app.Alarms;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

// Activity to Store the alarm sound
public class AlarmSound extends Activity {

   MediaPlayer mp;
   Context context;
   int track;

   public AlarmSound(Context ct) {
       //super();
       this.context = ct;
   }

   public void playTune(){
       mp = MediaPlayer.create(context, track);
       mp.start();
   }

   public void stopTune(){
       boolean playing = false;

       try{
           playing = mp.isPlaying();
       }catch(Exception e){
           //media player hasn't been initialized
           return;
       }
       mp.stop();
       mp.release();
   }

   public boolean isPlaying(){
       return mp.isPlaying();
   }

   public void chooseTrack(int id){
       track = id;
   }
}
