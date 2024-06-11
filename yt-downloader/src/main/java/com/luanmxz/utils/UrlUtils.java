package com.luanmxz.utils;

public class UrlUtils {

    public static String parsePlaylistFromSingleVideoURL(String url) {

        url = url.substring(0, url.indexOf("&list="));

        return url;
    }

    public static TipoUrlEnum getTypeUrl(String url) {

        TipoUrlEnum tipoUrl = TipoUrlEnum.SINGLE_VIDEO;

        if (url.contains("playlist:list")) {
            tipoUrl = TipoUrlEnum.PLAYLIST;
        } else if (url.contains("&list=") && url.contains("start_radio=")) {
            tipoUrl = TipoUrlEnum.PLAYLIST_FROM_SINGLE_VIDEO;
        } else {
            tipoUrl = TipoUrlEnum.SINGLE_VIDEO;
        }

        return tipoUrl;

    }

}
