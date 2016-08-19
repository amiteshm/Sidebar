package com.fusebulb.sidebar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.fusebulb.sidebar.Models.Clip;
import com.fusebulb.sidebar.Models.ClipAction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 24/07/16.
 */

public class PlayTourService extends Service implements
         MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{

    private final IBinder clipBinder = new ClipPlayerBinder();
    private MediaPlayer clipPlayer;
    private ArrayList<Clip> clipList;
    private Clip currentClip;
    private String APP_FOLDER;


    private boolean isPaused = false;
    private ArrayList<ClipAction> currentActionList;
    private int currentClipIndex;

    private Context context;

    private static final int NOTIFY_ID = 1;

    private static final int PLAYER_SEEK_FORWARD_TIME = 15;
    private static final int PLAYER_SEEK_REWIND_TIME = 15;


    @Override
    public void onCreate() {
        super.onCreate(); //create the service
    }

    @Override
    public IBinder onBind(Intent intent) {
        return clipBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        clipPlayer.stop();
        clipPlayer.release();
        return false;
    }


    private void playNext() {
        if (currentClipIndex + 1 < clipList.size()) {

            setClipIndex(currentClipIndex + 1);
            playClip();
        }
    }


    public MediaPlayer getMediaPlayer(){
        return clipPlayer;
    }
//    public void playClip() {
//        if (!isPlayerPrepared) {
//            prepareClip();
//        }
//    }

//    public void stopClip() {
//        clipPlayer.stop();
//    }

    public void playClip() {
        clipPlayer.start();
    }

    public void stopClip(){
        clipPlayer.stop();
    }

    public void pauseClip() {
        clipPlayer.pause();
        isPaused = true;
    }

    public Clip prepareClip() {
        initClipPlayer();
        currentClip = clipList.get(currentClipIndex);

        try {
            File clipFile = new File(APP_FOLDER, currentClip.getClipSource());
            File actionFile = new File(APP_FOLDER, currentClip.getActionFileSource());

            Uri trackUri = Uri.fromFile(clipFile);
            clipPlayer.setDataSource(getApplicationContext(), trackUri);
            clipPlayer.prepare();

            setSubTitles(clipPlayer, actionFile);
            Intent setBackgroundImageIntent = new Intent(PlayTourActivity.ACTION_SET_IMAGE_IN_FOCUS);
            setBackgroundImageIntent.putExtra(PlayTourActivity.KEY_FILE_PATH_IMAGE_IN_FOCUS, currentClip.getPictureSource());
            sendBroadcast(setBackgroundImageIntent);


//            Intent setTimedTextListenerIntent = new Intent(PlayTourActivity.ACTION_SET_TIMED_LISTENER);
//            sendBroadcast(setTimedTextListenerIntent);

        } catch (Exception e) {
            Log.e("Music Service", "Error setting data source", e);
        }
        return currentClip;
    }

    private void setSubTitles(MediaPlayer player, File file){
        try {
            player.addTimedTextSource(file.getAbsolutePath(),
                    MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);

            //player.addTimedTextSource(file.getAbsolutePath(), MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
            int textTrackIndex = findTrackIndexFor(
                    MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, player.getTrackInfo());
            if (textTrackIndex >= 0) {
                player.selectTrack(textTrackIndex);
            } else {
                Log.w("PlayTourService", "Cannot find text track!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Clip setClipIndex(int clipIndex) {
        this.currentClipIndex = clipIndex;
        return prepareClip();
    }

    public void setClipList(ArrayList<Clip> list) {
        this.clipList = list;
    }

    public class ClipPlayerBinder extends Binder {
        PlayTourService getService() {
            return PlayTourService.this;
        }
    }

    public void initClipPlayer() {
        if(clipPlayer != null){
            clipPlayer.release();
        }
        clipPlayer = new MediaPlayer();
        clipPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        clipPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //clipPlayer.setOnPreparedListener(this);
        clipPlayer.setOnCompletionListener(this);
        clipPlayer.setOnErrorListener(this);
    }

    public Location getCurrentClipLocation(){
        return currentClip.getLocation();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //What to do when the clip is completed
        //TODO cummnicate with activity to download the next file
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    public void seekPlayerTo(int posn) {
        clipPlayer.seekTo(posn);
    }

    public boolean isPlayerPlaying() {
        return clipPlayer.isPlaying();
    }

    public int getPlayerCurrentPosition() {
        return clipPlayer.getCurrentPosition();
    }

    public int getClipDuration() {
        return clipPlayer.getDuration();
    }

    public void fastForwardPlayer() {
        // get current song position
        int currentPosition = getPlayerCurrentPosition();
        // check if seekForward time is lesser than song duration
        if (currentPosition + PLAYER_SEEK_FORWARD_TIME <= getClipDuration()) {
            // forward song
            seekPlayerTo(currentPosition + PLAYER_SEEK_FORWARD_TIME);
        } else {
            // forward to end position
            seekPlayerTo(getClipDuration());
        }
    }

    public void rewindPlayer() {
        int currentPosition = getPlayerCurrentPosition();
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - PLAYER_SEEK_REWIND_TIME >= 0) {
            // backward song
            seekPlayerTo(currentPosition - PLAYER_SEEK_REWIND_TIME);
        } else {
            // backward to starting position
            seekPlayerTo(0);
        }
    }

    public void setAppFolder(String appFolder) {
        this.APP_FOLDER = appFolder;
    }


//    public void setActionFile(File file){
//        try {
//            clipPlayer.addTimedTextSource(file.getAbsolutePath(), MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
//            int textTrackIndex = findTrackIndexFor(MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, clipPlayer.getTrackInfo());
//            if(textTrackIndex >= 0 ){
//                clipPlayer.selectTrack(textTrackIndex);
//            } else{
//                Log.e("PlayTourService", "Cannot find the track!");
//            }
//            clipPlayer.setOnTimedTextListener(this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfos){
        int index = -1;
        for(int i = 0; i < trackInfos.length; i++){
            if(trackInfos[i].getTrackType() == mediaTrackType){
                return i;
            }
        }
        return index;
    }

    public void setContext(Context mContext){
        this.context = mContext;
    }
}
