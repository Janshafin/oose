package controller;
import model.userModel;
import java.awt.Desktop;
import java.io.File;
import java.util.Scanner;

public class LoginController{
    UserModel model = new UserModel();

    public void login(){
        Scanner sc = mew Scanner(System.in);

        System.out.println("LOGIN PAGE ");

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();
        
            if(model.validateUser(username, password)){
                System.out.println("Login successful!");
                try {
                    File file = new file  ("view/dashboard.html");
                    Desktop.getDesktop().browse(file.toURI()); 
                } catch (Exception e) { 
                    System.out.println("Dashboard page error: " + e);
                }
                else{
                System.out.println("Invalid username or password. Please try again.");
                }
    }
}