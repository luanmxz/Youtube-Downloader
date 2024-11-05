package com.luanmxz.record.request;

public class AudioFile {

    private String url;
    private String audioQuality;

    public AudioFile(String url, String audioQuality) {
        this.url = url;
        this.audioQuality = audioQuality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAudioQuality() {
        return audioQuality;
    }

    public void setAudioQuality(String audioQuality) {
        this.audioQuality = audioQuality;
    }

}
