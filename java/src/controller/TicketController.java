package controller;

import domain.Ticket;
import service.TicketService;

import java.util.List;
import java.util.Map;

public class TicketController {
    private final TicketService ticketService = new TicketService();

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


}
