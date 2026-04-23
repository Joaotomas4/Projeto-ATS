package org.spotifumtp37.delegate;

import org.spotifumtp37.model.SpotifUMData;
import org.spotifumtp37.exceptions.AlreadyExistsException;
import org.spotifumtp37.exceptions.DoesntExistException;
import org.spotifumtp37.model.subscription.FreePlan;
import org.spotifumtp37.model.user.User;

import java.util.ArrayList;
import java.util.Scanner;

public class SpotifUMUI {
    private final Scanner scanner;
    private SpotifUMData modelData;
    private User loggedUser;
    private AdminUI adminUI;
    private UserUI userUI;

    public SpotifUMUI() {
        this.scanner = new Scanner(System.in);
        this.modelData = new SpotifUMData();
        this.loggedUser = null;
        this.adminUI = new AdminUI(modelData, scanner);
        this.userUI = new UserUI(modelData, loggedUser, scanner);
    }

    public void run() {
        this.showMainMenu();
    }

    //menus do programa

    private void showMainMenu() {
        NewMenu mainMenu = new NewMenu(new String[]{
                "Login as User",
                "Sign up as a new User",
                "Log in as Admin"
        });

        mainMenu.setHandler(1, this::loginAsUser);
        mainMenu.setHandler(2, this::signUpAsUser);
        mainMenu.setHandler(3, this::loginAsAdmin);

        mainMenu.run();
    }

    //handlers

    public void loginAsUser() {
        System.out.print("Input your username:");
        String username = scanner.nextLine();
        System.out.print("Input your password:");
        String password = scanner.nextLine();

        boolean isAuthenticated = this.authenticateUser(username, password);
        if (isAuthenticated) {
            System.out.println("Login successful! Redirecting to User Menu...");
            loggedUser = this.modelData.getCurrentUserPointer(username);
            userUI.setLoggedUser(loggedUser);
            userUI.showUserMenu();
        } else {
            System.out.println("Invalid credentials for User!");
        }
    }

    public void signUpAsUser() {
        System.out.println("Sign Up as a New User!");
        System.out.print("Enter your desired username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your desired password: ");
        String password = scanner.nextLine();

        System.out.print("Confirm your password: ");
        String confirmPassword = scanner.nextLine();

        System.out.print("Enter your email address: ");
        String email = scanner.nextLine();

        System.out.print("Enter your address: ");
        String address = scanner.nextLine();

        // Verify that the user confirmed the password correctly
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match! Please retry signing up.");
            return;
        }

        boolean isSuccess = this.registerNewUser(username, password, email, address);

        // Check if account creation was successful
        if (isSuccess) {
            System.out.println("Account created successfully! You can now log in.");
        } else {
            System.out.println("Failed to create account. Username might already exist.");
        }
    }

    public boolean registerNewUser(String username, String password, String email, String address) {
        User user = new User(username, email, address, new FreePlan(), password, 0, new ArrayList<>());
        try {
            modelData.addUser(user);
            return true;
        } catch (AlreadyExistsException e) {
            return false;
        }
    }

    public void loginAsAdmin() {
        System.out.print("Input admin username: ");
        String username = scanner.nextLine();
        System.out.print("Input admin password: ");
        String password = scanner.nextLine();

        if (username.equals("admin123") && password.equals("pass123")) {
            System.out.println("Login successful! Redirecting to Admin Menu...");
            adminUI.showAdminMenu();
        } else {
            System.out.println("Invalid admin credentials!");
        }
    }

    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        try {
            User user = modelData.getUser(username);
            return password.equals(user.getPassword());
        } catch (DoesntExistException e) {
            return false;
        }
    }


}