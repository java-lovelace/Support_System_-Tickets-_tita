package view;

import controller.UserController; // Updated import
import dao.UsuarioDao;
import dao.UserDaoJDBC;
import javax.swing.JOptionPane;

public class UserMenu {
    private final UsuarioDao usuarioDao = new UserDaoJDBC();
    // The controller is now UserController
    private final UserController userController = new UserController(usuarioDao);

    public void showMenu() {
        while (true) {
            String menuText = "--- MAIN MENU ---\n" +
                    "1. Create User\n" +
                    "2. List All Users\n" +
                    "3. Find User by ID\n" +
                    "4. Delete User\n" +
                    "5. Exit\n" +
                    "Please, enter the number of your option:";

            String option = JOptionPane.showInputDialog(null, menuText, "User Management", JOptionPane.PLAIN_MESSAGE);

            if (option == null) {
                break;
            }

            switch (option) {
                case "1":
                    userController.manageUserCreation();
                    break;
                case "2":
                    userController.manageUserListing();
                    break;
                case "3":
                    userController.manageFindUserById();
                    break;
                case "4":
                    userController.manageUserDeletion();
                    break;
                case "5":
                    JOptionPane.showMessageDialog(null, "Exiting the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option. Please enter a number from 1 to 5.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}