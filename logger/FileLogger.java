package projekt.japache.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.channels.FileChannel;

//Finalna klasa do zapisu logów do pliku
public final class FileLogger {
    
    //Pole przechowujące ścieżkę pliku
    private final Path file;
    
    public FileLogger(String filename, Boolean clearlog) throws IOException {
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
    
    //Metoda logująca dane
    public void log(String message) throws IOException {
        //Utwórz log jeżeli został usunięty
        createLog();
        //Zapisz dane kodowane w UTF-8
        Files.write(file, message.getBytes("UTF-8"), StandardOpenOption.APPEND);
    }
}
