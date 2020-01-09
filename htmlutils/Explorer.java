package projekt.japache.htmlutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

//Klasa odpowiadająca za generowanie eksporatora katalogu implementująca interfejs IPage
public class Explorer implements IPage {

    //Prywatne pola zawierające pliki, katalogi i ścieżki
    private final Path root;
    private final Path dir;
    private final ArrayList<Path> files;
    private final ArrayList<Path> dirs;

    //Konstruktor przyjmujący katalog i katalog główny
    Explorer(Path root, Path dir) throws IOException {
        this.root = root;
        this.dir = dir;
        this.files = new ArrayList<>();
        this.dirs = new ArrayList<>();

        //Dla każdego elementu z katalogu przypisanie go do plików lub katalogów
        for (Path element : Files.newDirectoryStream(dir)) {
            if (Files.isDirectory(element)) {
                this.dirs.add(element);
            } else if (Files.isRegularFile(element)) {
                this.files.add(element);
            }
        }

    }

    //Metoda zwracająca cały generowany HTML za pomocą StringBuildera
    @Override
    public String getHTML() {
        StringBuilder builder = new StringBuilder();

        //Generowanie nazwy katalogu
        Path parent = getRelativePath(dir).getParent();
        String dirName = (parent == null) ? "katalog główny" : dir.getFileName().toString();

        builder.append("<!DOCTYPE html><html lang='pl'><html><head><meta charset='UTF-8'><title>Lista plików w katalogu: ");
        builder.append(dirName);
        builder.append("</title><style><!--");
        builder.append("html,body{margin:0;padding:0;color:#000;background:#FFF;font-family:Tahoma,Verdana,Arial,sans-serif;font-size:16px}");
        builder.append("h1{margin:50px 0 15px;padding:0;text-align:center}");
        builder.append("#dir{color:#C00}");
        builder.append("table{margin:0 auto}");
        builder.append("table th,table td{padding:5px}");
        builder.append("table th#name{min-width:250px}");
        builder.append("table th#size{min-width:100px;}");
        builder.append("table td#nofiles{font-weight:700;color:#C00;text-align:center}");
        builder.append("table td.size{text-align:right}");
        builder.append("--></style></head><body>");
        builder.append("<h1>Lista plików w <span id='dir'>");
        builder.append(dirName);
        builder.append("</span></h1><table border='1'><tr><th id='name'>Nazwa</th><th id='size'>Rozmiar</th></tr>");

        //Dodawanie przycisku powrotu folder wyżej
        if (parent != null) {
            builder.append(getRow(parent.toString(), "/..", "&nbsp;"));
        }
        //Informacja o pustym katalogu
        if (dirs.isEmpty() && files.isEmpty()) {
            builder.append("<tr><td colspan='2' id='nofiles'>Brak plikow w katalogu</td></tr>");
        } else {
            //Listowanie wszystkich katalogów
            dirs.forEach((d) -> {
                String path = getRelativePath(d).toString();
                String name = "/" + d.getFileName().toString() + "/";

                builder.append(getRow(path, name, "&nbsp;"));
            });
            //Listowanie wszystkich plików
            files.forEach((f) -> {
                try {
                    String path = getRelativePath(f).toString();
                    String name = f.getFileName().toString();
                    String size = getFormatedSize(f);

                    builder.append(getRow(path, name, size));
                } catch (IOException ex) {}
            });
        }
        builder.append("</table></body></html>");

        return builder.toString();
    }

    //Metoda generująca jeden wiersz tabeli z podaną ścieżką, nazwą i rozmiarem
    private String getRow(String path, String filename, String size) {
        StringBuilder builder = new StringBuilder();

        builder.append("<tr><td><a href='");
        builder.append(path);
        builder.append("'>");
        builder.append(filename);
        builder.append("</a></td><td class='size'>");
        builder.append(size);
        builder.append("</td></tr>");

        return builder.toString();
    }

    //Metoda generująca relatywną ścieżkę względem głównego katalogu
    private Path getRelativePath(Path path) {
        return Paths.get(path.toString().substring(root.toString().length()));
    }

    //Metoda generująca formatowany rozmiar z zachowaniem przeliczeń zgodnym z systemem Windows
    private String getFormatedSize(Path path) throws IOException {
        long size = Files.size(path);

        if (size > 1000000000) {
            return size / 1073741824 + "&nbsp;GB";
        } else if (size > 1000000) {
            return size / 1048574 + "&nbsp;MB";
        } else if (size > 1000) {
            return size / 1024 + "&nbsp;KB";
        } else {
            return size + "&nbsp;B";
        }
    }
}
