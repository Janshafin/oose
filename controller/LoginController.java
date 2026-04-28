package controller;

import model.UserModel;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class LoginController implements HttpHandler {

    UserModel model = new UserModel();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            // Serve the login page
            serveHtmlFile(exchange, "view/login.html");
        } else if (method.equals("POST")) {
            // Read form data
            InputStream is = exchange.getRequestBody();
            String formData = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            // Parse username and password from form data: username=admin&password=1234
            String[] pairs = formData.split("&");
            String username = "";
            String password = "";
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    if (keyValue[0].equals("username")) username = keyValue[1];
                    if (keyValue[0].equals("password")) password = keyValue[1];
                }
            }

            // Validate using model
            if (model.validateUser(username, password)) {
                System.out.println("Login successful from web UI!");
                serveHtmlFile(exchange, "view/dashboard.html");
            } else {
                System.out.println("Invalid login attempt.");
                String response = "<h2>Invalid Username or Password</h2><a href='/'>Try Again</a>";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    private void serveHtmlFile(HttpExchange exchange, String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}