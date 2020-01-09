package projekt.japache.logger;

import java.io.IOException;
import java.util.ArrayList;

//Finalna klasa obsługująca logi za pomocą dodanych loggerów
public final class EventLogger {

    //Pole zawierające listę logujących instancji
    private static final ArrayList<ILogger> loggers = new ArrayList<>();
    
    //Statyczny blok inicializujący
    //Przy uruchomieniu wrzuca do listy domyślne logger konsolowy
    static {
        loggers.add(new ConsoleLogger());
    }
    
    //Metoda służąca ustaleniu opcji zapisu do pliku
    public static void addLogger(ILogger logger) {
        loggers.add(logger);
    }
    
    //Metoda propagująca spreparowany log do podpiętych loggerów
    public static void log(final LogLevel level, String message) throws IOException {
        //Formatowanie wartości wyjściowej
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
        
        //Dalsza propagacja
        for(ILogger l : loggers) {
            l.log(output);
        }
    }

    //Statyczny enumerator odpowiadający z poziomy logów
    public static enum LogLevel {
        NORMAL, ERROR, DEBUG
    }
}
