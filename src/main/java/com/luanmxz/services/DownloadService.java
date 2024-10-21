package com.luanmxz.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.luanmxz.utils.FileUtils;
import com.luanmxz.utils.TipoUrlEnum;
import com.luanmxz.utils.UrlUtils;
import com.luanmxz.utils.ytDlCommands;

import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;

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
     * @throws IOException
     * @throws Exception
     */
    public String downloadAndConvertToAudio(String url) {

        String musicFolder = "";

        try {
            musicFolder = FileUtils.getMusicFolder().toString().concat("\\");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        logger.info("MSC FOLDER -> {}", musicFolder);

        logger.info("Iniciando conversao de video para audio (.mp3)");

        TipoUrlEnum urlEnum = UrlUtils.getTypeUrl(url);

        if (urlEnum.equals(TipoUrlEnum.PLAYLIST_FROM_SINGLE_VIDEO)) {
            url = UrlUtils.parsePlaylistFromSingleVideoURL(url);
        }

        String fileName = "";

        try {
            Map<String, Object> result = urlEnum.equals(TipoUrlEnum.PLAYLIST)
                    ? ytDlCommands.buildPlaylistCommand(url, musicFolder)
                    : ytDlCommands.buildSingleVideoCommand(url, musicFolder);

            String[] commands = (String[]) result.get("commands");

            fileName = (String) result.get("filename");
            executeCommand(commands, Boolean.FALSE);

            logger.info("Finalizado a conversao do arquivo.");
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }

        return fileName;

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

    public Resource loadFileAsResource(String fileName) throws IOException {
        Path filePath = getFilePath(fileName);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new FileNotFoundException("Arquivo não encontrado: " + fileName);
        }
    }

    private Path getFilePath(String fileName) {
        // Retorne o caminho do arquivo baseado no nome do arquivo
        // Isso dependerá de onde você está salvando os arquivos convertidos
        return Paths.get("caminho/para/arquivos/convertidos").resolve(fileName);
    }

}
