package com.luanmxz.config;

import org.springframework.stereotype.Component;

@Component
public class DownloadProgress {
    private volatile Float progress;

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }
}
