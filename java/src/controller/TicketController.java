package controller;

import dao.CommentDao;
import dao.CommentDaoJDBC;
import dao.TicketDao;
import dao.TicketDaoJDBC;
import domain.Ticket;
import service.TicketService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TicketController {
    private final TicketService ticketService;

    public TicketController() {
        TicketDao ticketDao = new TicketDaoJDBC();
        CommentDao commentDao = new CommentDaoJDBC();
        this.ticketService = new TicketService(ticketDao, commentDao);
    }

    public void createTicket(Ticket ticket) {
        ticketService.createTicket(ticket);
    }

    public void createTicketByNames(String title, String description, String reporterUsername,
                                    String assigneeUsername, String categoryName, String stateName) {
        ticketService.createTicketByNames(title, description, reporterUsername, assigneeUsername, categoryName, stateName);
    }


    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    public List<Ticket> findByStateAndCategory(String stateName, String categoryName) {
        return ticketService.findByStateAndCategory(stateName, categoryName);
    }

    public List<Ticket> findByAssignee(String assigneeUsername) {
        return ticketService.findByAssignee(assigneeUsername);
    }

    public Map<String, Integer> getTopCategories() {
        return ticketService.getTopCategories(3);
    }

    public List<String> getAllCategories() {
        return ticketService.getAllCategories();
    }

    public List<String> getAllStates() {
        return ticketService.getAllStates();
    }

    public List<String> getAllUsernames() {
        return ticketService.getAllUsernames();
    }

    // Cambiar el estado del ticket usando el nombre del estado
    public void changeState(int ticketId, String newStateName) {
        Optional<Integer> newStateIdOpt = ticketService.getStateIdByName(newStateName);

        if (newStateIdOpt.isEmpty()){
            throw new IllegalStateException("Estado no encontrado" + newStateName);
        }

        boolean success = ticketService.changeState(ticketId, newStateIdOpt.get());
        if (!success) {
            throw new IllegalStateException("Cambio de estado no permitido o ticket no encontrado");
        }
    }

    // Reasignar un ticket a otro usuario usando el nombre
    public void assignTicket(int ticketId, String assigneeUsername) {
        Optional<Integer> assigneeIdOpt = ticketService.getStateIdByName(assigneeUsername);

        if (assigneeIdOpt.isEmpty()){
            throw new IllegalStateException("Usuario no encontrado: " + assigneeUsername);
        }
        boolean success = ticketService.assignTicket(ticketId, assigneeIdOpt.get());
        if (!success) throw new IllegalStateException("No se pudo reasignar el ticket. Verifique le rol del usuario");
    }

    // Agregar comentario usando el nombre del usuario
    public void addComment(int ticketId, String username, String text) {
        Optional<Integer> userIdOpt = ticketService.getUserIdByUsername(username);

        if(userIdOpt.isEmpty()){
            throw new IllegalStateException("Usuario no encontrado " + username);
        }
        ticketService.addComment(ticketId, userIdOpt.get(), text);
    }
}
