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
import java.net.URLDecoder;

public class LoginController implements HttpHandler {

    UserModel model = new UserModel();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/login") && method.equals("POST")) {
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
                    if (keyValue[0].equals("username")) username = URLDecoder.decode(keyValue[1], "UTF-8");
                    if (keyValue[0].equals("password")) password = URLDecoder.decode(keyValue[1], "UTF-8");
                }
            }

            // Validate using model
            if (model.validateUser(username, password)) {
                System.out.println("Login successful for user: " + username);
                // Redirect to dashboard
                exchange.getResponseHeaders().set("Location", "/dashboard");
                exchange.sendResponseHeaders(302, -1);
                exchange.close();
            } else {
                System.out.println("Invalid login attempt for user: " + username);
                String response = "<html><head><style>"
                    + "body { font-family: 'Inter', sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); height: 100vh; margin: 0; display: flex; align-items: center; justify-content: center; }"
                    + ".error-box { background: rgba(255,255,255,0.95); padding: 40px; border-radius: 12px; box-shadow: 0 8px 32px rgba(0,0,0,0.2); text-align: center; max-width: 400px; }"
                    + "h2 { color: #e74c3c; }"
                    + "a { color: #667eea; text-decoration: none; font-weight: bold; font-size: 16px; }"
                    + "a:hover { text-decoration: underline; }"
                    + "</style></head><body>"
                    + "<div class='error-box'>"
                    + "<h2>Invalid Username or Password</h2>"
                    + "<p style='color:#666;'>Please check your credentials and try again.</p>"
                    + "<a href='/'>&#8592; Back to Login</a>"
                    + "</div></body></html>";
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            }
        } else if (path.equals("/dashboard") && method.equals("GET")) {
            // Serve the dashboard page
            serveHtmlFile(exchange, "view/dashboard.html");
        } else {
            // Serve the login page for GET /
            serveHtmlFile(exchange, "view/login.html");
        }
    }

    private void serveHtmlFile(HttpExchange exchange, String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}