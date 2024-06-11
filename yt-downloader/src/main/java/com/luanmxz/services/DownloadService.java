package com.luanmxz.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.luanmxz.utils.TipoUrlEnum;
import com.luanmxz.utils.UrlUtils;
import com.luanmxz.utils.ytDlCommands;

import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    private static final String PATH_YTDLP = "C:\\yt-dlp";

    public void downloadAndConvertToAudio(String url) {

        TipoUrlEnum urlEnum = UrlUtils.getTypeUrl(url);

        if (urlEnum.equals(TipoUrlEnum.PLAYLIST_FROM_SINGLE_VIDEO)) {
            url = UrlUtils.parsePlaylistFromSingleVideoURL(url);
        }

        try {
            String[] command = urlEnum.equals(TipoUrlEnum.PLAYLIST) ? ytDlCommands.buildPlaylistCommand(url)
                    : ytDlCommands.buildSingleVideoCommand(url);

            executeCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void executeCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(PATH_YTDLP));
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        process.waitFor();
    }

}
