package dao;

import domain.Ticket;
import java.util.Optional;

public interface TicketDao {

    Ticket crear(Ticket ticket);

    Optional<Ticket> findById(int ticketId);

    void update(Ticket ticket);

    void assign(int ticketId, int assigneeId);

    void changeState(int ticketId, int stateId);
}

