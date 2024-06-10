package com.luanmxz;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.luanmxz.commands.ytDlCommands;

public class Downloader {

    public static void downloadAndConvertToAudio(String url) throws IOException, InterruptedException{

        String[] command = url.contains("playlist?list=") ? ytDlCommands.buildPlaylistCommand(url) : ytDlCommands.buildSingleVideoCommand(url);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File("C:\\yt-dlp"));
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        process.waitFor();
}
}
