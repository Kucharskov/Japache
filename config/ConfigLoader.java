package projekt.japache.config;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Properties;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import projekt.japache.logger.EventLogger;
import projekt.japache.logger.EventLogger.LogLevel;

//Klasa do obsługi konfigracji czytanej z pliku
public class ConfigLoader {

    //Pole prywatne zawierające pary "klucz" => "wartość"
    private final Properties config;

    public ConfigLoader(String file) throws IOException {
        this.config = new Properties();

        Path path = Paths.get(file);
        if (Files.exists(path)) {
            config.load(Files.newInputStream(path));

            EventLogger.log(LogLevel.NORMAL, "Wczytano ustawienia z pliku config.properties");
        } else {
            EventLogger.log(LogLevel.ERROR, "Błąd odczytu pliku config.properties");
        }
        //Uzupełnienie brakujących wartości konfiguracji
        setMissingDefaults();
    }

    //Metoda do zwracania wartości pod danym kluczem
    public String get(String key) {
        return config.getProperty(key);
    }

    //Metoda do zwracania listy wartości pod danym kluczem (wartości rozdzielone przecinkiem)
    public ArrayList<String> getList(String key) {
        return new ArrayList<>(Arrays.asList(config.getProperty(key).split(",")));
    }

    //Metoda do uzupełniania brakujących ustawień w przypadku usunięcia linijki z pliku
    private void setMissingDefaults() throws IOException {
        String list = "";
        if (!config.containsKey("root_dir")) {
            config.setProperty("root_dir", "data");
            list += "root_dir ";
        }
        if (!config.containsKey("hostname")) {
            config.setProperty("hostname", "localhost");
            list += "hostname ";
        }
        if (!config.containsKey("port")) {
            config.setProperty("port", "8080");
            list += "port ";
        }
        if (!config.containsKey("debug_mode")) {
            config.setProperty("debug_mode", "false");
            list += "debug_mode ";
        }
        if (!config.containsKey("log_enable")) {
            config.setProperty("log_enable", "false");
            list += "log_enable ";
        }
        if (!config.containsKey("log_clear")) {
            config.setProperty("log_clear", "false");
            list += "log_clear ";
        }
        if (!config.containsKey("log_file")) {
            config.setProperty("log_file", "logs.txt");
            list += "log_file ";
        }
        if (!config.containsKey("dir_listing")) {
            config.setProperty("dir_listing", "false");
            list += "dir_listing ";
        }
        if (!config.containsKey("index_files") || config.getProperty("index_files").equals("")) {
            config.setProperty("index_files", "index.html");
            list += "index_files ";
        }

        if (!list.equals("")) {
            EventLogger.log(LogLevel.NORMAL, "Ustawiono domyślne właściwości dla:\n " + list);
        }
    }
}
