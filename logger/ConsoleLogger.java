package projekt.japache.logger;

import java.io.IOException;


public class ConsoleLogger implements ILogger {

    @Override
    public void log(String message) throws IOException {
        System.out.println(message);
    }
    
}
