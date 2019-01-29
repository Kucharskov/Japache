package projekt.japache.logger;

//Finalna klasa do zwracania informacji do konsoli
public final class EventLogger {

    //Metoda zwracająca sformatowaną wcześniej treść do konsoli
    public static void log(final LogLevel level, String message) {
        switch (level) {
            case NORMAL:
                System.out.println(message);
                break;
            case ERROR:
                System.out.println("[ERROR] " + message);
                break;
            case DEBUG:
                System.out.println("[DEBUG] " + message);
                break;
        }
    }

    //Statyczny enumerator odpowiadający z poziomy logów
    public static enum LogLevel {
        NORMAL, ERROR, DEBUG
    }
}
