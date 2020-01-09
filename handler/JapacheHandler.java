package projekt.japache.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import projekt.japache.logger.EventLogger;
import projekt.japache.logger.EventLogger.LogLevel;
import projekt.japache.config.ConfigLoader;
import projekt.japache.htmlutils.PageFactory;

//Główna klasa obsługująca żądania stron HTTP
public class JapacheHandler implements HttpHandler {

    //Pole prywatne zawierające konfigurację
    private final ConfigLoader config;
    //Pole prywatne zawierające ścieżkę do głównego katalogu z danymi
    private final Path root;

    public JapacheHandler(ConfigLoader config) {
        this.config = config;
        this.root = Paths.get(config.get("root_dir"));

        if (config.get("debug_mode").equals("true")) {
            EventLogger.log(LogLevel.DEBUG, "Serwer Japache działa w trybie DEBUG");
        }
        //Sprawdzanie integralności
        if (!checkIntegrity()) {
            EventLogger.log(LogLevel.ERROR, "Wystąpił błąd integralności systemu");
            EventLogger.log(LogLevel.ERROR, "Zalecane wyłączenie serwera i sprawdzenie spójności plików");
        }
    }

    //Główna metoda obsługująca żądania
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EventLogger.log(LogLevel.NORMAL, "Żądanie adresu: " + exchange.getRequestURI().getPath());

        //Pobranie rodzaju zapytania (GET, POST, DELETE, PUSH)
        String method = exchange.getRequestMethod();
        //Sklejenie całej ścieżki
        Path path = Paths.get(root + exchange.getRequestURI().getPath());
        String response;

        //Zwrócenie błędu 500 przy błędzie niespójności plików serwera
        if (!checkIntegrity()) {
            response = PageFactory.createError(500).getHTML();
            response(exchange, 500, response.getBytes("UTF-8"), "text/html");
            return;
        }
        //Zwrócenie błędu 501 przy zapytaniu innym niż GET lub HEAD
        if (!method.equals("GET") && !method.equals("HEAD")) {
            response = PageFactory.createError(501).getHTML();
            response(exchange, 501, response.getBytes("UTF-8"), "text/html");
            return;
        }
        //Zwrócenie błedu 404 jeżeli plik nie instnieje lub próbuje się path-traversal
        if (!Files.exists(path) || !path.toRealPath().startsWith(root.toRealPath())) {
            response = PageFactory.createError(404).getHTML();
            response(exchange, 404, response.getBytes("UTF-8"), "text/html");
            return;
        }
        //Jeżeli podany adres jest katalogiem
        if (Files.isDirectory(path)) {
            //znalezienie głównego pliku w katalogu
            Path dirIndex = findDirIndex(path);
            //Jeżeli go nie znaleziono głównego pliku
            if (dirIndex == null) {
                //ale włączone jest listowanie katalogów - wyświetlenie zawartości katalogu
                if (config.get("dir_listing").equals("true")) {
                    response = PageFactory.createExplorer(root, path).getHTML();
                    response(exchange, 200, response.getBytes("UTF-8"), "text/html");
                    return;
                } 
                //w przeciwnym wypadku zwrócenie błędu 403
                else {
                    response = PageFactory.createError(403).getHTML();
                    response(exchange, 403, response.getBytes("UTF-8"), "text/html");
                    return;
                }
            }
            path = dirIndex;
        }
        //Ostatecznie poszukiwana ścieżka jest plikiem, który należy zwrócić
        response(exchange, 200, Files.readAllBytes(path), getMimeType(path));
    }

    //Metoda służąca do zwracania odpowiedzi do użytkownika
    //Przyjmuje:
    // Uchwyt do klasy opakowującej zapytanie
    // Kod odpowiedzi
    // Ciąg bajtów odpowiedzi
    // Ustalony MimeType
    private void response(HttpExchange exchange, int code, byte[] bytes, String mimetype) throws IOException {
        //Informacje debugowe dla zapytania
        if (config.get("debug_mode").equals("true")) {
            EventLogger.log(LogLevel.DEBUG, "Adres IP nadawcy: " + exchange.getRemoteAddress().getHostString());
            EventLogger.log(LogLevel.DEBUG, "Parametry zapytania: " + exchange.getRequestURI().getQuery());
            EventLogger.log(LogLevel.DEBUG, "Metoda zapytania: " + exchange.getRequestMethod());
            EventLogger.log(LogLevel.DEBUG, "Kod odpowiedzi: " + code);
            EventLogger.log(LogLevel.DEBUG, "Zwrócony MimeType: " + mimetype);
        }

        //Ustawienie odpowiedniego MimeType oraz wysłanie odpowiedzi
        exchange.getResponseHeaders().set("Content-Type", mimetype);
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    //Metoda sprawdzająca spójność plików:
    //Czy istnieje główny katalog i jest katalogiem
    private boolean checkIntegrity() {
        return Files.exists(root) && Files.isDirectory(root);
    }

    //Metoda poszukująca głównego pliku w katalogu
    private Path findDirIndex(Path dir) throws IOException {
        //Dla wszystkich plików w liście pobranej z konfiguracji
        for (String index : config.getList("index_files")) {
            //Sklej tymczasową ścieżkę
            Path path = Paths.get(dir + "/" + index);
            //Jeżeli istnieje
            if (Files.exists(path)) {
                //zwróć nią
                return path;
            }
        }

        return null;
    }

    //Pobranie MimeType dla danego pliku, lub zwrócenie "octet-stream"
    private String getMimeType(Path file) throws IOException {
        return (Files.probeContentType(file) == null) ? "application/octet-stream" : Files.probeContentType(file);
    }
}
