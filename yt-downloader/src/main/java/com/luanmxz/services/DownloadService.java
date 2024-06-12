package com.luanmxz.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.luanmxz.utils.TipoUrlEnum;
import com.luanmxz.utils.UrlUtils;
import com.luanmxz.utils.ytDlCommands;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class DownloadService {

    private static final Logger logger = LoggerFactory.getLogger(DownloadService.class);
    private static final Dotenv dotEnv = Dotenv.load();
    private static final String PATH_YTDLP = dotEnv.get("PATH_YTDLP");

    /**
     * Realiza o download do video e faz a conversão para áudio, exclui o video
     * original quando terminar a conversão.
     * 
     * @param url url do video do youtube
     */
    public void downloadAndConvertToAudio(String url) {

        logger.info("Iniciando conversao de video para audio (.mp3)");

        TipoUrlEnum urlEnum = UrlUtils.getTypeUrl(url);

        if (urlEnum.equals(TipoUrlEnum.PLAYLIST_FROM_SINGLE_VIDEO)) {
            url = UrlUtils.parsePlaylistFromSingleVideoURL(url);
        }

        try {
            String[] command = urlEnum.equals(TipoUrlEnum.PLAYLIST) ? ytDlCommands.buildPlaylistCommand(url)
                    : ytDlCommands.buildSingleVideoCommand(url);

            executeCommand(command);

            logger.info("Finalizado a conversao do arquivo.");
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }

    }

    public void executeCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(PATH_YTDLP));
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            logger.info(line);
        }

        process.waitFor();
    }

}
