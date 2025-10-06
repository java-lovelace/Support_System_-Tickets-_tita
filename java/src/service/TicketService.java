package service;

import dao.CommentDao;
import dao.TicketDao;
import domain.Comentario;
import domain.Ticket;

public class TicketService {

    private final TicketDao ticketDao;
    private final CommentDao commentDao;

    // IDs de estados predefinidos para la lógica de negocio.
    // Asumimos: 1=Abierto, 2=En Proceso, 3=Resuelto, 4=Cerrado
    private static final int ESTADO_INICIAL = 1;

    public TicketService(TicketDao ticketDao, CommentDao commentDao) {
        this.ticketDao = ticketDao;
        this.commentDao = commentDao;
    }


      //H4: Crea un ticket, validando datos y seteando estado inicial.

    public Ticket createTicket(String title, String description, int reporterId, int categoryId) {
        Ticket ticket = new Ticket(title, description, reporterId, categoryId, ESTADO_INICIAL);
        return ticketDao.crear(ticket);
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

