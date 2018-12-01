package old;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;


public class Hello implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Hello World! \n" +
                "vvvvvvvvvvvvvvvvvvvvvvvvvvv" +
                "\n what is done?";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

}