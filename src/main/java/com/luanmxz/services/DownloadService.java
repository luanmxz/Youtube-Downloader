package com.luanmxz.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.luanmxz.record.request.AudioFile;
import com.luanmxz.utils.FileUtils;
import com.luanmxz.utils.TipoUrlEnum;
import com.luanmxz.utils.UrlUtils;
import com.luanmxz.utils.ytDlCommands;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.MalformedURLException;

@Service
public class DownloadService {

    private static final Logger logger = LoggerFactory.getLogger(DownloadService.class);
    private static final Dotenv dotEnv = Dotenv.load();
    private static final String PATH_YTDLP = dotEnv.get("PATH_YTDLP");

    public String getVideoTitleFromUrl(String url) throws IOException, InterruptedException {
        String[] commands = ytDlCommands.buildGetTitleCommand(url);

        String videoTitle = executeCommand(commands, Boolean.TRUE);

        return videoTitle;
    }

    /**
     * Realiza o download do video e faz a conversão para áudio, exclui o video
     * original quando terminar a conversão.
     * 
     * @param url url do video do youtube
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    public String[] downloadAndConvertToAudio(AudioFile audioFileRequest) throws IOException, InterruptedException {

        String musicFolder = System.getProperty("java.io.tmpdir").concat("\\");

        logger.info("Iniciando conversao de video para audio (.mp3)");

        TipoUrlEnum urlEnum = UrlUtils.getTypeUrl(audioFileRequest.getUrl());

        String url = audioFileRequest.getUrl();

        if (urlEnum.equals(TipoUrlEnum.PLAYLIST_FROM_SINGLE_VIDEO)) {
            url = UrlUtils.parsePlaylistFromSingleVideoURL(url);
        }

        String fileName = "";
        String[] commands = new String[0];
        String videoTitle = "";
        String sanitizedVideoTitle = "";

        switch (urlEnum) {
            case SINGLE_VIDEO:
                commands = ytDlCommands.buildGetTitleCommand(url);
                videoTitle = executeCommand(commands, true);
                sanitizedVideoTitle = FileUtils.sanitizeFilename(videoTitle);
                commands = ytDlCommands.buildSingleVideoCommand(url, musicFolder, audioFileRequest,
                        sanitizedVideoTitle);
                break;
            case PLAYLIST:
                commands = ytDlCommands.buildPlaylistCommand(url, musicFolder, audioFileRequest);
                break;
            default:
                break;
        }
        executeCommand(commands, Boolean.FALSE);

        String fullPath = System.getProperty("java.io.tmpdir").trim() + sanitizedVideoTitle + "."
                + audioFileRequest.getAudioFormat();
        fileName = videoTitle + "." + audioFileRequest.getAudioFormat();

        logger.info("Finalizado a conversao do arquivo.");

        return new String[] { fullPath, fileName };

    }

    public String executeCommand(String[] command, boolean returnOutput) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(PATH_YTDLP));
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            logger.info(line);
            if (returnOutput) {
                output.append(line);
            }
        }

        process.waitFor();

        return returnOutput ? output.toString().trim() : null;
    }

    public Resource loadFileAsResource(String filePath) throws IOException {
        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists()) {
                File file = resource.getFile();
                return new FileSystemResource(file);
            } else {
                throw new RuntimeException("Arquivo não encontrado: " + filePath);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Arquivo não encontrado: " + filePath, ex);
        }
    }
}
