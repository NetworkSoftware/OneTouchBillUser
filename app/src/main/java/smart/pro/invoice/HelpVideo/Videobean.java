package smart.pro.invoice.HelpVideo;

import java.io.Serializable;

public class Videobean implements Serializable {
    String id;
    String title;
    String video;
    String thumbnail;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}