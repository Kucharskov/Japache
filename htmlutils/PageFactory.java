package projekt.japache.htmlutils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;

//Klasa finalna wzorca projektowego "Fabryka"
public final class PageFactory {

    //Pole prywatne zawierające parę wartości "kod błędu" => "treść błędu"
    private static final Map<Integer, String> errors = new HashMap<>();

    //Statyczny blok inicializujący
    //Przy uruchomieniu wrzuca do mapy kody błędów z ich treścią
    static {
        errors.put(400, "Bad Request");
        errors.put(401, "Unauthorized");
        errors.put(402, "Payment");
        errors.put(403, "Forbidden");
        errors.put(404, "Not Found");
        errors.put(405, "Method Not Allowed");
        errors.put(406, "Not Acceptable");
        errors.put(407, "Proxy Authentication Required");
        errors.put(408, "Request Timeout");
        errors.put(409, "Conflict");
        errors.put(410, "Gone");
        errors.put(411, "Length required");
        errors.put(412, "Precondition Failed");
        errors.put(413, "Request Entity Too Large");
        errors.put(414, "Request-URI Too Long");
        errors.put(415, "Unsupported Media Type");
        errors.put(416, "Requested Range Not Satisfiable");
        errors.put(417, "Expectation Failed");
        errors.put(418, "I’m a teapot");
        errors.put(451, "Unavailable For Legal Reasons");
        errors.put(500, "Internal Server Error");
        errors.put(501, "Not Implemented");
        errors.put(502, "Bad Gateway");
        errors.put(503, "Service Unavailable");
        errors.put(504, "Gateway Timeout");
        errors.put(505, "HTTP Version Not Supported");
        errors.put(506, "Variant Also Negotiates");
        errors.put(507, "Insufficient Storage (WebDAV)");
        errors.put(508, "Loop Detected (WebDAV)");
        errors.put(509, "Bandwidth Limit Exceeded");
        errors.put(510, "Not Extended");
        errors.put(511, "Network Authentication Required");
    }

    //Statyczna metoda zwracająca nowy obiekt implementujący IPage obsługujący dany błąd lub null gdy dany błąd nie instnieje w mapie
    public static IPage createError(int code) {
        return (errors.get(code) == null) ? null : new Error(code, errors.get(code));
    }

    //Statyczna metoda zwracająca nowy obiekt implementujący IPage obsługujący eksplorację katalogu
    public static IPage createExplorer(Path root, Path dir) throws IOException {
        return new Explorer(root, dir);
    }

}
