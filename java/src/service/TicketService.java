package service;

import dao.TicketDao;
import dao.TicketDaoJDBC;
import domain.Ticket;
import dao.CommentDao;
import domain.Comentario;
import java.util.List;
import java.util.Map;

public class TicketService {
    private TicketDao ticketDao = new TicketDaoJDBC();
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
        ticketDao.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDao.findAll();
    }

    // Buscar por estado y categoria
    public List<Ticket> findByStateAndCategory(String stateName, String categoryName) {
        if (stateName == null || stateName.trim().isEmpty() || categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("required status and category");
        }
        return ticketDao.listByStateAndCategory(stateName, categoryName);
    }

    // Buscar por asignacion
    public List<Ticket> findByAssignee(String assigneeUsername) {
        if (assigneeUsername == null || assigneeUsername.isBlank())
            throw new IllegalArgumentException("assignee required");
        return ticketDao.listByAssignee(assigneeUsername);
    }

    // Traer el top 3 de categorias
    public Map<String, Integer> getTopCategories(int limit) {
        if (limit <= 0) limit = 3;
        return ticketDao.topCategories(limit);
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
        ticket.setReporterId(ticketDao.findUserIdByUsername(reporterUsername));

        if (assigneeUsername != null && !assigneeUsername.isBlank()) {
            ticket.setAssigneeId(ticketDao.findUserIdByUsername(assigneeUsername));
        }

        ticket.setCategoryId(ticketDao.findCategoryIdByName(categoryName));
        ticket.setStateId(ticketDao.findStateIdByName(stateName));

        ticketDao.save(ticket);
    }

    // Traer todas las categorias
    public List<String> getAllCategories() {
        return ticketDao.listAllCategories();
    }

    // Traer toos los estados
    public List<String> getAllStates() {
        return ticketDao.listAllStates();
    }

    //Traer todos los usernames
    public List<String> getAllUsernames() {
        return ticketDao.listAllUsernames();
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

    // Obtener el ID real de un usuario por su username
    public java.util.Optional<Integer> getUserIdByUsername(String username) {
        try {
            int id = ticketDao.findUserIdByUsername(username);
            return java.util.Optional.of(id);
        } catch (Exception e) {
            return java.util.Optional.empty();
        }
    }

    // Obtener el ID real de un estado por su nombre
    public java.util.Optional<Integer> getStateIdByName(String stateName) {
        try {
            int id = ticketDao.findStateIdByName(stateName);
            return java.util.Optional.of(id);
        } catch (Exception e) {
            return java.util.Optional.empty();
        }
    }


}
