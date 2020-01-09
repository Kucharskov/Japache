package projekt.japache;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import projekt.japache.logger.EventLogger;
import projekt.japache.logger.EventLogger.LogLevel;
import projekt.japache.config.ConfigLoader;
import projekt.japache.handler.JapacheHandler;
import projekt.japache.logger.FileLogger;

public class Japache {
    
    public final static String JAPACHE_VERSION = "1.0a";

    public static void main(String[] args) throws IOException {
        EventLogger.log(LogLevel.NORMAL, "Uruchomiono serwer (wersja " + JAPACHE_VERSION + ")");

        //Pobranie konfiguracji z pliku config.properties
        //oraz przeparsowanie danych na port i hostname serwera
        ConfigLoader config = new ConfigLoader("config.properties");
        int port = Integer.parseInt(config.get("port"));
        String hostname = config.get("hostname");
        
        //Logowanie do pliku
        if (config.get("log_enable").equals("true")) {
            //Pobranie konfiguracji
            String logfile = config.get("log_file");
            Boolean clearlog = config.get("log_clear").equals("true");

            //Rozpoczęcie zapisu
            EventLogger.addLogger(new FileLogger(logfile, clearlog));
        } else {
            EventLogger.log(LogLevel.NORMAL, "Logowanie do pliku jest wyłączone");
        }

        try {
            //Utworzenie instancji serwera pod danym hostname i portem
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(hostname, port), 0);
            server.setExecutor(Executors.newFixedThreadPool(2));
            server.createContext("/", new JapacheHandler(config));
            server.start();

            EventLogger.log(LogLevel.NORMAL, "Adres serwera: " + hostname + ":" + port);
        } catch (BindException ex) {
            EventLogger.log(LogLevel.ERROR, "Wystąpił błąd podczas bindowania pod adres " + hostname + ":" + port);
        }
    }

}
