package com.luanmxz;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        System.out.println( "Iniciando conversão do arquivo!" );

        try{
            //Downloader.downloadAndConvertToAudio("https://www.youtube.com/watch?v=fnlJw9H0xAM");
            //Downloader.downloadAndConvertToAudio("https://www.youtube.com/playlist?list=PL2B9v9CpagYmZ_RkI6t8mtSvzmWnXK2cr");
            Downloader.downloadAndConvertToAudio(args[0]);
            
        System.out.println("Conversão realizada com sucesso!");
        }catch(IOException ioException){
            System.err.println(ioException.getMessage());
        }

    }
}
