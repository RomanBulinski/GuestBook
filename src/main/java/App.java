
import DAO.NoteDAO;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) throws Exception {
        // create a server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8010), 0);

        // set routes
//        server.createContext("/hello", new Hello());
        server.createContext("/gBook", new GuestBook());
        server.createContext("/static", new Static());

        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();

    }
}



