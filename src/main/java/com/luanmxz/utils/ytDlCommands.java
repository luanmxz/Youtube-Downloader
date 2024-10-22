package com.luanmxz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.luanmxz.record.request.AudioFile;

public class ytDlCommands {

        private static String ytDlpPath = "C:\\yt-dlp";
        private static String ytDlpExecutablePath = "C:\\yt-dlp\\yt-dlp.exe";

        public static String[] buildGetThumbnailCommand(String url, String thumbnailPath) {

                String[] commands = { ytDlpExecutablePath,
                                "--write-thumbnail",
                                "--convert-thumbnails", "jpg",
                                "--skip-download",
                                "--output", thumbnailPath,
                                url };

                return commands;
        }

        public static String[] buildGetTitleCommand(String url) {
                String[] commands = { ytDlpExecutablePath,
                                "--get-title",
                                url };

                return commands;
        }

        public static String[] buildSingleVideoCommand(String url, String musicFolder,
                        AudioFile audioFileRequest)
                        throws IOException, InterruptedException {
                String filename = "%(title)s.%(ext)s";
                String fullPath = musicFolder + filename;

                String[] commands = { ytDlpExecutablePath,
                                "-i",
                                "-x",
                                "--audio-format", audioFileRequest.getAudioFormat(),
                                "--audio-quality", audioFileRequest.getAudioQuality(),
                                "-o", fullPath,
                                "--no-check-certificate",
                                "--prefer-ffmpeg",
                                "--concurrent-fragments", "5",

                                url };

                return commands;
        }

        public static String[] buildPlaylistCommand(String url, String musicFolder,
                        AudioFile audioFileRequest)
                        throws IOException, InterruptedException {

                String filename = "playlist_title";

                String[] getTitleCommands = { ytDlpExecutablePath,
                                "--print", filename,
                                url };

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
                                "--audio-format", audioFileRequest.getAudioFormat(),
                                "--audio-quality", audioFileRequest.getAudioQuality(),
                                "-o", file.getAbsolutePath() + "\\" + "%(title)s.%(ext)s",
                                url };

                return commands;
        }
}
