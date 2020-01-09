package projekt.japache.logger;

import java.io.IOException;

//Interfejs klasy logującej dane
public interface ILogger {

    //Metoda logująca dane
    void log(String message) throws IOException;
    
}
