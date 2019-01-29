package projekt.japache.htmlutils;

//Klasa odpowiadająca za błąd implementująca interfejs IPage
public class Error implements IPage {

    //Prywatne pola przetrzymujące kod i treść błędu
    private final int code;
    private final String error;

    //Konstruktor przyjmujący kod i treśc błędu
    Error(int code, String error) {
        this.code = code;
        this.error = error;
    }

    //Metoda zwracająca cały HTML z błędem za pomocą StringBuildera
    @Override
    public String getHTML() {
        StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html><html lang='pl'><html><head>");
        builder.append("<meta charset='UTF-8'>");
        builder.append("<title>");
        builder.append(code);
        builder.append(": ");
        builder.append(error);
        builder.append("</title>");
        builder.append("<style><!--");
        builder.append("html,body{margin:0;padding:0;color:#000;background:#FFF;font-family:Tahoma,Verdana,Arial,sans-serif;font-size:16px}");
        builder.append("h1{color:#C00;margin:50px 0 15px;padding:0;text-align:center}");
        builder.append("p{margin:15px 0;text-align:center}");
        builder.append("--></style></head><body><h1>");
        builder.append(code);
        builder.append(": \n");
        builder.append(error);
        builder.append("</h1><p>Proszę nie panikować! Nawet komputery czasami robią coś źle...</p>\n");
        builder.append("</body></html>\n");

        return builder.toString();
    }
}
