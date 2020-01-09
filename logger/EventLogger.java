package projekt.japache.logger;

import java.io.IOException;

//Finalna klasa do zwracania informacji do konsoli
public final class EventLogger {

    //Pole zawierające instancję klasy logującej do pliku
    public static FileLogger filelog = null;
    
    //Metoda służąca ustaleniu opcji zapisu do pliku
    public static void setFileLogger(FileLogger filelog) {
        EventLogger.filelog = filelog;
    }
    
    //Metoda wyświetla sformatowaną wcześniej treść w konsoli oraz ewentualnie zapisuje log do pliku
    public static void log(final LogLevel level, String message) throws IOException {
        String output = "";
        switch (level) {
            case NORMAL:
                output = "[Japache] " + message;
                break;
            case ERROR:
                output = "[ERROR] " + message;
                break;
            case DEBUG:
                output = "[DEBUG] " + message;
                break;
        }
        System.out.println(output);
        
        //Zapis logu do pliku
        if(filelog != null) {
            filelog.log(output + "\n");
        }
    }

    //Statyczny enumerator odpowiadający z poziomy logów
    public static enum LogLevel {
        NORMAL, ERROR, DEBUG
    }
}
