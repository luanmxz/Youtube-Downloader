package com.luanmxz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ytDlCommands {

    private static String ytDlpPath = "C:\\yt-dlp";
    private static String ytDlpExecutablePath = "C:\\yt-dlp\\yt-dlp.exe";
    private static String pathToSaveFiles = "C:\\Users\\luanm\\OneDrive\\Desktop\\Estudos\\Youtube-Downloader\\yt-downloader\\DownloadedVideos\\";

    public static String[] buildSingleVideoCommand(String videoUrl) {

        String[] commands = { ytDlpExecutablePath,
                "-i",
                "-x",
                "--audio-format", "mp3",
                "--audio-quality", "320k",
                "-o", pathToSaveFiles + "%(title)s.%(ext)s",
                videoUrl };

        return commands;
    }

    public static String[] buildPlaylistCommand(String playlistUrl) throws IOException, InterruptedException {

        String[] getTitleCommands = { ytDlpExecutablePath,
                "--print", "playlist_title",
                playlistUrl };

        ProcessBuilder processBuilder = new ProcessBuilder(getTitleCommands);
        processBuilder.directory(new File(ytDlpPath));

        Process getTitleProcess = processBuilder.start();

        BufferedReader titleReader = new BufferedReader(new InputStreamReader(getTitleProcess.getInputStream()));
        String title = titleReader.readLine();
        getTitleProcess.waitFor();

        File file = new File(pathToSaveFiles + "Playlist" + "\\" + title + "\\");
        file.mkdirs();

        String[] commands = { ytDlpExecutablePath,
                "-i",
                "-x",
                "--audio-format", "mp3",
                "--audio-quality", "320k",
                "-o", file.getAbsolutePath() + "\\" + "%(title)s.%(ext)s",
                playlistUrl };

        // if playlist_random_download is true, add this arg --playlist-random

        return commands;
    }
}
