package service;

import dao.TicketDAO;
import domain.Ticket;
import dao.CommentDao;
import domain.Comentario;
import java.util.List;
import java.util.Map;

public class TicketService {
    private final TicketDAO ticketDAO = new TicketDAO();
    private final CommentDao commentDao;
    private static final int ESTADO_INICIAL = 1;
  
     public TicketService(TicketDao ticketDao, CommentDao commentDao) {
      this.ticketDao = ticketDao;
      this.commentDao = commentDao;
    }

    // Crear ticket
    public void createTicket(Ticket ticket) {
        if (ticket == null) throw new IllegalArgumentException("ticket is null");
        if (ticket.getTitle() == null || ticket.getTitle().isBlank())
            throw new IllegalArgumentException("title required");
        ticketDAO.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.findAll();
    }

    // Buscar por estado y categoria
    public List<Ticket> findByStateAndCategory(String stateName, String categoryName) {
        if (stateName == null || stateName.trim().isEmpty() || categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("required status and category");
        }
        return ticketDAO.listByStateAndCategory(stateName, categoryName);
    }

    // Buscar por asignacion
    public List<Ticket> findByAssignee(String assigneeUsername) {
        if (assigneeUsername == null || assigneeUsername.isBlank())
            throw new IllegalArgumentException("assignee required");
        return ticketDAO.listByAssignee(assigneeUsername);
    }

    // Traer el top 3 de categorias
    public Map<String, Integer> getTopCategories(int limit) {
        if (limit <= 0) limit = 3;
        return ticketDAO.topCategories(limit);
    }

    // Crear ticket por nombre
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

    // Traer todas las categorias
    public List<String> getAllCategories() {
        return ticketDAO.listAllCategories();
    }

    // Traer toos los estados
    public List<String> getAllStates() {
        return ticketDAO.listAllStates();
    }

    //Traer todos los usernames
    public List<String> getAllUsernames() {
        return ticketDAO.listAllUsernames();
    }
  
    //H5: Asigna un ticket, validando que el assignee exista y tenga el rol correcto.
    public boolean assignTicket(int ticketId, int assigneeId) {
        if (ticketDao.findById(ticketId).isEmpty()) {
            System.err.println("Error: El ticket " + ticketId + " no existe.");
            return false;
        }

        ticketDao.assign(ticketId, assigneeId);
        System.out.println("Ticket " + ticketId + " asignado a usuario " + assigneeId);
        return true;
    }


   //H5: Cambia el estado de un ticket, validando la secuencia de transición.
    public boolean changeState(int ticketId, int newStateId) {
        return ticketDao.findById(ticketId).map(ticket -> {
            int currentStateId = ticket.getStateId();
            boolean isValidTransition = false;
            // Lógica de transición: Abierto(1) -> En Proceso(2) -> Resuelto(3) / Cerrado(4)
            switch (currentStateId) {
                case 1: // Abierto
                    isValidTransition = (newStateId == 2);
                    break;
                case 2: // En Proceso
                    isValidTransition = (newStateId == 3 || newStateId == 4);
                    break;
                case 3: // Resuelto
                    isValidTransition = (newStateId == 4);
                    break;
            }

            if (isValidTransition) {
                ticketDao.changeState(ticketId, newStateId);
                System.out.println("Estado del ticket " + ticketId + " cambiado a " + newStateId);
                return true;
            } else {
                System.err.println("Transición de estado no permitida de " + currentStateId + " a " + newStateId);
                return false;
            }
        }).orElse(false);
    }


    //H6: Agrega un comentario a un ticket.
    public void addComment(int ticketId, int userId, String text) {
        if (ticketDao.findById(ticketId).isEmpty()) {
            System.err.println("No se puede agregar comentario: ticket " + ticketId + " no existe.");
            return;
        }

        Comentario comentario = new Comentario(ticketId, userId, text);
        commentDao.addComment(comentario);
        System.out.println("Comentario agregado al ticket " + ticketId);
    }


}
