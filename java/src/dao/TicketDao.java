package dao;

import domain.Ticket;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TicketDao {

    void save(Ticket ticket);

    Optional<Ticket> findById(int ticketId);

    void update(Ticket ticket);

    void assign(int ticketId, int assigneeId);

    void changeState(int ticketId, int stateId);

    List<Ticket> findAll();

    List<Ticket> listByStateAndCategory(String stateName, String categoryName);

    List<Ticket> listByAssignee(String assigneeUsername);

    Map<String, Integer> topCategories(int limit);

     int findCategoryIdByName(String name);

     int findStateIdByName(String name);

     int findUserIdByUsername(String username);

     List<String> listAllCategories();

     List<String> listAllStates();

     List<String> listAllUsernames();
}

