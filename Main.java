import controller.LoginController;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.awt.Desktop;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a web server on port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            
            // Use a single controller instance for all routes
            LoginController controller = new LoginController();
            
            // Map all URL paths to our controller
            server.createContext("/", controller);
            
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Web Server started on http://localhost:8000");
            System.out.println("Default login: admin / 1234");

            // Automatically open the browser to the web server URL
            Desktop.getDesktop().browse(new URI("http://localhost:8000/"));
            
        } catch (Exception e) {
            System.out.println("Server error: " + e);
            e.printStackTrace();
        }
    }
}