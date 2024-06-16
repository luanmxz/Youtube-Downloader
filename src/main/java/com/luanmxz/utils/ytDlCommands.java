package com.luanmxz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ytDlCommands {

        private static String ytDlpPath = "C:\\yt-dlp";
        private static String ytDlpExecutablePath = "C:\\yt-dlp\\yt-dlp.exe";

        public static String[] buildSingleVideoCommand(String videoUrl, String musicFolder) {

                String[] commands = { ytDlpExecutablePath,
                                "-i",
                                "-x",
                                "--audio-format", "mp3",
                                "--audio-quality", "320k",
                                "--download-archive", "downloaded_videos_history.txt",
                                "--progress", "%s",
                                "-o", musicFolder + "%(title)s.%(ext)s",
                                videoUrl };

                return commands;
        }

        public static String[] buildPlaylistCommand(String playlistUrl, String musicFolder)
                        throws IOException, InterruptedException {

                String[] getTitleCommands = { ytDlpExecutablePath,
                                "--print", "playlist_title",
                                playlistUrl };

                ProcessBuilder processBuilder = new ProcessBuilder(getTitleCommands);
                processBuilder.directory(new File(ytDlpPath));

                Process getTitleProcess = processBuilder.start();

                BufferedReader titleReader = new BufferedReader(
                                new InputStreamReader(getTitleProcess.getInputStream()));
                String title = titleReader.readLine();
                getTitleProcess.waitFor();

                File file = new File(musicFolder + "Playlist" + "\\" + title + "\\");
                file.mkdirs();

                String[] commands = { ytDlpExecutablePath,
                                "--download-archive", "downloaded_videos_history.txt",
                                "-i",
                                "-x",
                                "--audio-format", "mp3",
                                "--audio-quality", "320k",
                                "--progress", "%s",
                                "-o", file.getAbsolutePath() + "\\" + "%(title)s.%(ext)s",
                                playlistUrl };

                // if playlist_random_download is true, add this arg --playlist-random

                return commands;
        }
}
