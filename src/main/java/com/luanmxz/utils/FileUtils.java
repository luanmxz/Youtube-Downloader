package com.luanmxz.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static Path getMusicFolder() throws IOException {
        // Obtém o diretório do usuário
        String userHome = System.getProperty("user.home");

        // Cria o caminho para a pasta Músicas
        Path musicPath = Paths.get(userHome, "Músicas");

        // Verifica se a pasta Músicas existe e é um diretório
        if (Files.exists(musicPath) && Files.isDirectory(musicPath)) {
            return musicPath;
        } else {
            Files.createDirectory(musicPath);
            return musicPath;
        }
    }
}
