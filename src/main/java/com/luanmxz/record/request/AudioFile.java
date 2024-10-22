package com.luanmxz.record.request;

public class AudioFile {

    private String url;
    private String audioFormat;
    private String audioQuality;

    public AudioFile(String url, String audioFormat, String audioQuality) {
        this.url = url;
        this.audioFormat = audioFormat;
        this.audioQuality = audioQuality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }

    public String getAudioQuality() {
        return audioQuality;
    }

    public void setAudioQuality(String audioQuality) {
        this.audioQuality = audioQuality;
    }

}
