package dao;

import config.ConnectionFactory;
import domain.Ticket;

import java.sql.*;
import java.util.*;

public class TicketDaoJDBC implements TicketDao {

    @Override
    public void save(Ticket ticket) {
        String sql = "INSERT INTO tickets (title, description, reporter_id, assignee_id, category_id, state_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ticket.getTitle());
            ps.setString(2, ticket.getDescription());
            ps.setInt(3, ticket.getReporterId());

            if (ticket.getAssigneeId() != 0) {
                ps.setInt(4, ticket.getAssigneeId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.setInt(5, ticket.getCategoryId());
            ps.setInt(6, ticket.getStateId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving ticket", e);
        }
    }

    @Override
    public Optional<Ticket> findById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ticketId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setId(rs.getInt("id"));
                    ticket.setTitle(rs.getString("title"));
                    ticket.setDescription(rs.getString("description"));
                    ticket.setReporterId(rs.getInt("reporter_id"));
                    ticket.setCategoryId(rs.getInt("category_id"));
                    ticket.setStateId(rs.getInt("state_id"));
                    ticket.setAssigneeId((Integer) rs.getObject("assignee_id"));
                    ticket.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(ticket);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ticket por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Ticket ticket) {
        String sql = "UPDATE tickets SET title = ?, description = ?, assignee_id = ?, state_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ticket.getTitle());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setObject(3, ticket.getAssigneeId()); // setObject para manejar nulls
            pstmt.setInt(4, ticket.getStateId());
            pstmt.setInt(5, ticket.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar ticket: " + e.getMessage());
        }
    }

    @Override
    public void assign(int ticketId, int assigneeId) {
        String sql = "UPDATE tickets SET assignee_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, assigneeId);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al asignar ticket: " + e.getMessage());
        }
    }

    @Override
    public void changeState(int ticketId, int stateId) {
        String sql = "UPDATE tickets SET state_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stateId);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de ticket: " + e.getMessage());
        }
    }

    // === FIND ALL ===
    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = """
        SELECT t.id, t.title, t.description,
           r.id AS reporter_id,
           a.id AS assignee_id,
           c.id AS category_id,
           s.id AS state_id,
           r.username AS reporter,
           a.username AS assignee,
           c.name AS category,
           s.name AS state
        FROM tickets t
        JOIN users r ON t.reporter_id = r.id
        LEFT JOIN users a ON t.assignee_id = a.id
        JOIN categories c ON t.category_id = c.id
        JOIN states s ON t.state_id = s.id
        ORDER BY t.id ASC
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ticket t = new Ticket();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setReporterId(rs.getInt("reporter_id"));
                t.setAssigneeId(rs.getInt("assignee_id"));
                t.setCategoryId(rs.getInt("category_id"));
                t.setStateId(rs.getInt("state_id"));
                t.setCategoryName(rs.getString("category"));
                t.setStateName(rs.getString("state"));
                t.setReporterUsername(rs.getString("reporter"));
                t.setAssigneeUsername(rs.getString("assignee"));

                tickets.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing tickets", e);
        }
        return tickets;
    }


    // === LIST BY STATE + CATEGORY (JOIN) ===
    @Override
    public List<Ticket> listByStateAndCategory(String stateName, String categoryName) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = """
            SELECT t.id, t.title, t.description,
                   r.id AS reporter_id,
                   a.id AS assignee_id,
                   c.id AS category_id,
                   s.id AS state_id,
                   r.username AS reporter,
                   a.username AS assignee,
                   c.name AS category,
                   s.name AS state
            FROM tickets t
            JOIN users r ON t.reporter_id = r.id
            LEFT JOIN users a ON t.assignee_id = a.id
            JOIN categories c ON t.category_id = c.id
            JOIN states s ON t.state_id = s.id
            WHERE s.name = ? AND c.name = ?
            ORDER BY t.created_at DESC
            """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, stateName);
            ps.setString(2, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket t = new Ticket();
                    t.setId(rs.getInt("id"));
                    t.setTitle(rs.getString("title"));
                    t.setDescription(rs.getString("description"));

                    // IDs
                    t.setReporterId(rs.getInt("reporter_id"));
                    t.setAssigneeId(rs.getInt("assignee_id"));
                    t.setCategoryId(rs.getInt("category_id"));
                    t.setStateId(rs.getInt("state_id"));

                    // Nombres
                    t.setCategoryName(rs.getString("category"));
                    t.setStateName(rs.getString("state"));

                    tickets.add(t);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listByStateAndCategory", e);
        }
        return tickets;
    }


    // === LIST BY ASSIGNEE (buscar por username del assignee) ===
    @Override
    public List<Ticket> listByAssignee(String assigneeUsername) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = """
            SELECT t.id, t.title, t.description,
                   t.reporter_id, t.assignee_id, t.category_id, t.state_id,
                   r.username AS reporter,
                   a.username AS assignee,
                   c.name AS category,
                   s.name AS state
            FROM tickets t
            JOIN users r ON t.reporter_id = r.id
            LEFT JOIN users a ON t.assignee_id = a.id
            JOIN categories c ON t.category_id = c.id
            JOIN states s ON t.state_id = s.id
            WHERE a.username = ?
            ORDER BY t.created_at DESC
            """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, assigneeUsername);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket t = new Ticket();
                    t.setId(rs.getInt("id"));
                    t.setTitle(rs.getString("title"));
                    t.setDescription(rs.getString("description"));
                    // Ahora estas líneas funcionarán sin error
                    t.setReporterId(rs.getInt("reporter_id"));
                    t.setAssigneeId(rs.getInt("assignee_id"));
                    t.setCategoryId(rs.getInt("category_id"));
                    t.setStateId(rs.getInt("state_id"));
                    t.setReporterUsername(rs.getString("reporter"));
                    t.setAssigneeUsername(rs.getString("assignee"));
                    t.setCategoryName(rs.getString("category"));
                    t.setStateName(rs.getString("state"));

                    tickets.add(t);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listByAssignee", e);
        }
        return tickets;
    }

    // === TOP CATEGORIES (map ordered: category -> count) ===
    @Override
    public Map<String, Integer> topCategories(int limit) {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = """
                SELECT c.name AS category, COUNT(t.id) AS total
                FROM tickets t
                JOIN categories c ON t.category_id = c.id
                GROUP BY c.name
                ORDER BY total DESC
                LIMIT ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("category"), rs.getInt("total"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error topCategories", e);
        }
        return map;
    }

    // === RESOLVER ID POR NOMBRE ===
    @Override
    public int findCategoryIdByName(String name) {
        String sql = "SELECT id FROM categories WHERE name = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error resolving category ID", e);
        }
        throw new IllegalArgumentException("Category not found: " + name);
    }

    @Override
    public int findStateIdByName(String name) {
        String sql = "SELECT id FROM states WHERE name = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error resolving state ID", e);
        }
        throw new IllegalArgumentException("State not found: " + name);
    }

    @Override
    public int findUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE LOWER(username) = LOWER(?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error resolving user ID", e);
        }
        throw new IllegalArgumentException("User not found: " + username);
    }

    @Override
    public List<String> listAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY name ASC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing categories", e);
        }
        return categories;
    }

    @Override
    public List<String> listAllStates() {
        List<String> states = new ArrayList<>();
        String sql = "SELECT name FROM states ORDER BY name ASC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                states.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing states", e);
        }
        return states;
    }

    @Override
    public List<String> listAllUsernames() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT username FROM users ORDER BY username ASC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing usernames", e);
        }
        return users;
    }
}

