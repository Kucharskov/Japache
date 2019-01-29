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

public class Japache {

    //Główna metoda projektu
    public static void main(String[] args) throws IOException {
        EventLogger.log(LogLevel.NORMAL, "Uruchomiono serwer Japache");

        //Pobranie konfiguracji z pliku config.properties klasą ConfigLoader
        //oraz przeparsowanie danych na port i hostname serwera
        ConfigLoader config = new ConfigLoader("config.properties");
        int port = Integer.parseInt(config.get("port"));
        String hostname = config.get("hostname");

        try {
            //Utworzenie instancji serwera pod danym hostname i portem
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(hostname, port), 0);
            //Ustawienie serwera na działanie dwuwątkowe
            server.setExecutor(Executors.newFixedThreadPool(2));
            //Podpięcie pod główny kontekst "/" naszego handlera
            server.createContext("/", new JapacheHandler(config));
            //Start serwera
            server.start();

            EventLogger.log(LogLevel.NORMAL, "Adres serwera: " + hostname + ":" + port);
        } catch (BindException ex) {
            EventLogger.log(LogLevel.ERROR, "Wystąpił błąd podczas bindowania pod adres " + hostname + ":" + port);
        }
    }

}
