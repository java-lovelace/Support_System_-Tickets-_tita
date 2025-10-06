package service;

import dao.TicketDAO;
import domain.Ticket;

import java.util.List;
import java.util.Map;

public class TicketService {
    private final TicketDAO ticketDAO = new TicketDAO();

    public void createTicket(Ticket ticket) {
        if (ticket == null) throw new IllegalArgumentException("ticket is null");
        if (ticket.getTitle() == null || ticket.getTitle().isBlank())
            throw new IllegalArgumentException("title required");
        ticketDAO.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.findAll();
    }

    public List<Ticket> findByStateAndCategory(String stateName, String categoryName) {
        if (stateName == null || stateName.trim().isEmpty() || categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("required status and category");
        }
        return ticketDAO.listByStateAndCategory(stateName, categoryName);
    }

    public List<Ticket> findByAssignee(String assigneeUsername) {
        if (assigneeUsername == null || assigneeUsername.isBlank())
            throw new IllegalArgumentException("assignee required");
        return ticketDAO.listByAssignee(assigneeUsername);
    }

    public Map<String, Integer> getTopCategories(int limit) {
        if (limit <= 0) limit = 3;
        return ticketDAO.topCategories(limit);
    }

    public void createTicketByNames(String title, String description, String reporterUsername,
                                    String assigneeUsername, String categoryName, String stateName) {

        if (title == null || title.isBlank()) throw new IllegalArgumentException("title required");
        if (reporterUsername == null || reporterUsername.isBlank()) throw new IllegalArgumentException("reporter required");
        if (categoryName == null || categoryName.isBlank()) throw new IllegalArgumentException("category required");
        if (stateName == null || stateName.isBlank()) throw new IllegalArgumentException("state required");

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setReporterId(ticketDAO.findUserIdByUsername(reporterUsername));

        if (assigneeUsername != null && !assigneeUsername.isBlank()) {
            ticket.setAssigneeId(ticketDAO.findUserIdByUsername(assigneeUsername));
        }

        ticket.setCategoryId(ticketDAO.findCategoryIdByName(categoryName));
        ticket.setStateId(ticketDAO.findStateIdByName(stateName));

        ticketDAO.save(ticket);
    }

    public List<String> getAllCategories() {
        return ticketDAO.listAllCategories();
    }

    public List<String> getAllStates() {
        return ticketDAO.listAllStates();
    }

    public List<String> getAllUsernames() {
        return ticketDAO.listAllUsernames();
    }


}
