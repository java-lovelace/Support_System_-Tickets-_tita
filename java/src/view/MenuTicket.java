package view;

import controller.TicketController;
import domain.Ticket;
import util.InputValidator;
import javax.swing.*;
import java.util.List;

public class MenuTicket {
    private final TicketController ticketController = new TicketController();

    public void start() {
        int opt;
        do {
            String input = JOptionPane.showInputDialog(null,
                    """
                             === Ticket System ===
                             1. Create Ticket (using names)
                             2. List all tickets
                             3. Search tickets by state and category
                             4. List tickets by assignee (with reporter and category)
                             5. Top 3 categories with the most tickets
                             6. Show available categories, states and users
                             7. Change ticket state
                             8. Assign ticket
                             9. Add comment to ticket
                             0. Go out
                             """,
                    "Main Menu", JOptionPane.QUESTION_MESSAGE);


            if (input == null) {
                JOptionPane.showMessageDialog(null, "Leaving the system...");
                return;
            }

            try {
                opt = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
                continue;
            }

            switch (opt) {
                case 1 -> createTicketByNames();
                case 2 -> listTickets();
                case 3 -> searchByStateAndCategory();
                case 4 -> listByAssignee();
                case 5 -> topCategories();
                case 6 -> showAvailableNames();
                case 7 -> changeTicketState();
                case 8 -> assignTicket();
                case 9 -> addCommentToTicket();

                case 0 -> {
                    JOptionPane.showMessageDialog(null, "Going out...");
                    return;
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option");
            }

        } while (true);
    }

    // Crear ticket
    private void createTicketByNames() {
        String title = JOptionPane.showInputDialog("Ticket title:");
        if (!InputValidator.isValid(title)) {
            JOptionPane.showMessageDialog(null, "The title cannot be empty");
            return;
        }

        String desc = JOptionPane.showInputDialog("Ticket description:");
        if (!InputValidator.isValid(desc)) {
            JOptionPane.showMessageDialog(null, "The description cannot be empty");
            return;
        }

        String reporterUsername = JOptionPane.showInputDialog("Reporter username:");
        if (!InputValidator.isValid(reporterUsername)) {
            JOptionPane.showMessageDialog(null, "You must enter a valid reporter username");
            return;
        }

        String assigneeUsername = JOptionPane.showInputDialog("Assignee username (optional):");

        String categoryName = JOptionPane.showInputDialog("Category name:");
        if (!InputValidator.isValid(categoryName)) {
            JOptionPane.showMessageDialog(null, "You must enter a valid category name");
            return;
        }

        String stateName = JOptionPane.showInputDialog("State name:");
        if (!InputValidator.isValid(stateName)) {
            JOptionPane.showMessageDialog(null, "You must enter a valid state name");
            return;
        }

        try {
            ticketController.createTicketByNames(title, desc, reporterUsername, assigneeUsername, categoryName, stateName);
            JOptionPane.showMessageDialog(null, "✅ Ticket created successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error: " + e.getMessage());
        }
    }

    // Listar todos los tickets
    private void listTickets() {
        StringBuilder sb = new StringBuilder("=== Ticket List ===\n");
        for (Ticket t : ticketController.getAllTickets()) {
            sb.append(t).append("\n");
        }

        JOptionPane.showMessageDialog(null,
                sb.length() > 0 ? sb.toString() : "There are no registered tickets",
                "Ticket List", JOptionPane.INFORMATION_MESSAGE);
    }

    // Buscar por status y categoria
    private void searchByStateAndCategory() {
        String state = JOptionPane.showInputDialog("Enter the ticket status:");
        String category = JOptionPane.showInputDialog("Enter the category:");

        var tickets = ticketController.findByStateAndCategory(state, category);

        if (tickets.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tickets were found with those filters.");
        } else {
            StringBuilder sb = new StringBuilder("=== Tickets found ===\n");
            for (Ticket t : tickets) {
                sb.append(t).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    // Listar por asignacion
    private void listByAssignee() {
        String assignee = JOptionPane.showInputDialog("Enter the name of the assigned person:");
        var tickets = ticketController.findByAssignee(assignee);

        if (tickets.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no tickets assigned to that person.");
        } else {
            StringBuilder sb = new StringBuilder("=== Tickets per assigned ===\n");
            for (Ticket t : tickets) {
                sb.append(String.format("ID: %d | Title: %s | Reporter: %s | Category: %s | State: %s\n",
                        t.getId(), t.getTitle(), t.getReporterUsername(), t.getCategoryName(), t.getStateName()));

            }
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    // Mostrar por categorias mas llenas
    private void topCategories() {
        var report = ticketController.getTopCategories();

        if (report.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No data available.");
        } else {
            StringBuilder sb = new StringBuilder("=== Top 3 categories ===\n");
            for (var entry : report.entrySet()) {
                sb.append(String.format("Category: %s | Tickets: %d\n", entry.getKey(), entry.getValue()));
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    // Ver nombres de todo
    private void showAvailableNames() {
        StringBuilder sb = new StringBuilder("=== Available Options ===\n");

        List<String> categories = ticketController.getAllCategories();
        List<String> states = ticketController.getAllStates();
        List<String> users = ticketController.getAllUsernames();

        sb.append("Categories:\n");
        for (String c : categories) sb.append("- ").append(c).append("\n");

        sb.append("\nStates:\n");
        for (String s : states) sb.append("- ").append(s).append("\n");

        sb.append("\nUsers:\n");
        for (String u : users) sb.append("- ").append(u).append("\n");

        JOptionPane.showMessageDialog(null, sb.toString(), "Available Names", JOptionPane.INFORMATION_MESSAGE);
    }

    // Cambiar estado de un ticket
    private void changeTicketState() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ticket ID:"));
            String newState = JOptionPane.showInputDialog("Enter the new state name:");
            ticketController.changeState(id, newState);
            JOptionPane.showMessageDialog(null, "Ticket state updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // Asignar ticket a otro usuario
    private void assignTicket() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ticket ID:"));
            String newUser = JOptionPane.showInputDialog("Enter the new assignee username:");
            ticketController.assignTicket(id, newUser);
            JOptionPane.showMessageDialog(null, "Ticket reassigned successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // Agregar comentario a un ticket
    private void addCommentToTicket() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter ticket ID:");
            if (!InputValidator.isNumeric(idStr)) {
                JOptionPane.showMessageDialog(null, "ID de ticket no válido.");
                return;
            }
            int id = Integer.parseInt(idStr);

            String author = JOptionPane.showInputDialog("Enter your username:");
            // CORRECCIÓN: Usamos trim() para eliminar espacios al inicio y al final.
            if (!InputValidator.isValid(author)) {
                JOptionPane.showMessageDialog(null, "El nombre de autor no puede estar vacío.");
                return;
            }

            String content = JOptionPane.showInputDialog("Enter the comment:");
            if (!InputValidator.isValid(content)) {
                JOptionPane.showMessageDialog(null, "El comentario no puede estar vacío.");
                return;
            }

            // Enviamos el nombre de autor ya "limpio"
            ticketController.addComment(id, author.trim(), content);
            JOptionPane.showMessageDialog(null, "Comment added successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }



}
