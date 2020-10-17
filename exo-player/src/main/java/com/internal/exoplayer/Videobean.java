package com.internal.exoplayer;

import java.io.Serializable;

public class Videobean implements Serializable {
  public String id;
  public String title;
  public String video;
  public String thumbnail;
  public String duration;

    public Videobean() {
    }

    public Videobean(String title, String video, String thumbnail) {
        this.title = title;
        this.video = video;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}