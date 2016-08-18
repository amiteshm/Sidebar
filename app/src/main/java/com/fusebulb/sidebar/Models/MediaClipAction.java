package com.fusebulb.sidebar.Models;

import com.fusebulb.sidebar.Models.ClipAction;

import java.io.File;

/**
 * Created by amiteshmaheshwari on 22/07/16.
 */
public class MediaClipAction extends ClipAction {
    private static final String MEDIA_PHOTO = "photo";
    private static final String MEDIA_VIDEO = "video";
    private String mediaSource;
    private File mediaFile;

    public void setMediaSource(String filePath){
        this.mediaSource= filePath;
    }

    public String getMediaSource(){
        return this.mediaSource;
    }

    public void setMediaFile(File file){
        this.mediaFile = file;
    }

    public File getMediaFile(){
        return this.mediaFile;
    }
}
