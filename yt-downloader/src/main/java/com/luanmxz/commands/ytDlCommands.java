package com.luanmxz.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ytDlCommands {

    private static String ytDlpPath = "C:\\yt-dlp\\yt-dlp.exe";
    private static String pathToSaveFiles = "C:\\Users\\wise\\Desktop\\Java-estudo\\yt-downloader\\DownloadedVideos\\";

    public static String[] buildSingleVideoCommand(String videoUrl){

        String[] commands = {ytDlpPath, 
        "-i", 
        "-x", 
        "--audio-format", "mp3", 
        "--audio-quality", "320k", 
        "-o", pathToSaveFiles + "%(title)s.%(ext)s", 
        videoUrl};

return commands;
    }

    public static String[] buildPlaylistCommand(String playlistUrl) throws IOException, InterruptedException{

        String[] getTitleCommands = {  ytDlpPath,
            "--print", "playlist_title",
            playlistUrl};
        
        ProcessBuilder processBuilder = new ProcessBuilder(getTitleCommands);
        processBuilder.directory(new File("C:\\yt-dlp"));

        Process getTitleProcess = processBuilder.start();

        BufferedReader titleReader = new BufferedReader(new InputStreamReader(getTitleProcess.getInputStream()));
        String title = titleReader.readLine();
        getTitleProcess.waitFor();

        File file = new File(pathToSaveFiles + "Playlist" + "\\" + title + "\\");
        file.mkdirs();

        String[] commands = {ytDlpPath, 
            "-i",
            "-x",
            "--audio-format", "mp3", 
            "--audio-quality", "320k", 
            "-o", file.getAbsolutePath() + "\\" + "%(title)s.%(ext)s", 
            playlistUrl};

            return commands;
    }
}
