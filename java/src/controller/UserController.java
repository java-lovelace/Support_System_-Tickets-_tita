package controller;

import dao.UsuarioDao;
import domain.Role;
import domain.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Controller that handles the business logic for the user menu options.
 * It orchestrates the interaction between the view (JOptionPane) and the data model (DAO).
 */
public class UserController {

    private final UsuarioDao usuarioDao;

    public UserController(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public void manageUserCreation() {
        String username = JOptionPane.showInputDialog(null, "Enter the new user's name:", "Create User", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Role[] roles = {Role.REPORTER, Role.ASSIGNEE};
        Role selectedRole = (Role) JOptionPane.showInputDialog(
                null, "Select a role:", "Create User",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]
        );

        if (selectedRole == null) {
            JOptionPane.showMessageDialog(null, "You must select a role.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario newUser = new Usuario();
        newUser.setUsername(username);
        newUser.setRole(selectedRole);

        try {
            usuarioDao.crear(newUser); // Assuming 'crear' is the method in the DAO interface
            JOptionPane.showMessageDialog(null, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error creating user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void manageUserDeletion() {
        String idStr = JOptionPane.showInputDialog(null,
                "Enter the ID of the user to DELETE:",
                "Delete User",
                JOptionPane.WARNING_MESSAGE);

        if (idStr == null) {
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete the user with ID " + id + "?\n" +
                            "This action will also unassign their tickets and delete those they have reported.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                usuarioDao.delete(id);
                JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Operation canceled.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid ID. You must enter an integer.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void manageFindUserById() {
        String idStr = JOptionPane.showInputDialog(null, "Enter the user ID to search for:", "Find User by ID", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null) {
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Usuario usuario = usuarioDao.findById(id);

            if (usuario != null) {
                String info = "--- User Found ---\n\n" +
                        "ID: " + usuario.getId() + "\n" +
                        "Username: " + usuario.getUsername() + "\n" +
                        "Role: " + usuario.getRole() + "\n" +
                        "Created At: " + usuario.getCreatedAt() + "\n";
                JOptionPane.showMessageDialog(null, info, "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No user found with ID: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid ID. You must enter an integer.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void manageUserListing() {
        List<Usuario> usuarios = usuarioDao.findAll();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no registered users.", "User List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder userListText = new StringBuilder("--- User List ---\n");
        for (Usuario user : usuarios) {
            userListText.append("ID: ").append(user.getId())
                    .append(", User: ").append(user.getUsername())
                    .append(", Role: ").append(user.getRole()).append("\n");
        }

        JTextArea textArea = new JTextArea(userListText.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        scrollPane.setPreferredSize(new java.awt.Dimension(300, 250));
        JOptionPane.showMessageDialog(null, scrollPane, "All Users", JOptionPane.INFORMATION_MESSAGE);
    }
}