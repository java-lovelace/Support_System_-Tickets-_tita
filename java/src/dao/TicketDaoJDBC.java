package dao;

import config.DbConfig;
import domain.Ticket;

import java.sql.*;
import java.util.Optional;

public class TicketDaoJDBC implements TicketDao {

    @Override
    public Ticket crear(Ticket ticket) {
        String sql = "INSERT INTO tickets (title, description, reporter_id, category_id, state_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, ticket.getTitle());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setInt(3, ticket.getReporterId());
            pstmt.setInt(4, ticket.getCategoryId());
            pstmt.setInt(5, ticket.getStateId());
            pstmt.setTimestamp(6, Timestamp.valueOf(ticket.getCreatedAt()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ticket.setId(rs.getInt(1));
                    }
                }
            }
            return ticket;
        } catch (SQLException e) {
            System.err.println("Error al crear ticket: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al crear ticket.", e);
        }
    }

    @Override
    public Optional<Ticket> findById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try (Connection conn = DbConfig.getConnection();
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
        try (Connection conn = DbConfig.getConnection();
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
        try (Connection conn = DbConfig.getConnection();
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
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stateId);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de ticket: " + e.getMessage());
        }
    }
}

