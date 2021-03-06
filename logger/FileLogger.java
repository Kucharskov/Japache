package projekt.japache.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

import projekt.japache.logger.EventLogger.LogLevel;

//Finalna klasa do zapisu logów do pliku
public final class FileLogger implements ILogger {
    
    //Pole przechowujące ścieżkę pliku
    private final Path file;
    
    public FileLogger(String filename, Boolean clearlog) throws IOException {
        EventLogger.log(LogLevel.NORMAL, "Rozpoczęto logowanie do pliku: " + filename);
        this.file = Paths.get(filename);
        
        //Utwórz log jeżeli nie istnieje
        createLog();
        
        //Czyszczenie pliku z logami przy wartości true
        if(clearlog) {
            FileChannel.open(file, StandardOpenOption.WRITE).truncate(0).close();
        }
    }
    
    //Metoda tworząca plik na logi jeżeli owy nie istnieje
    private void createLog() throws IOException {
        if (!Files.exists(file)) {
             Files.createFile(file);
        }
    }
    
    @Override
    public void log(String message) throws IOException {
        //Utwórz log jeżeli został usunięty
        createLog();
        
        //Dodaj enter do treści
        message += "\n";
        
        //Zapisz dane kodowane w UTF-8
        Files.write(file, message.getBytes("UTF-8"), StandardOpenOption.APPEND);
    }
}
