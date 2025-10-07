package dao;

import config.ConnectionFactory;
import domain.Comentario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoJDBC implements CommentDao {

    @Override
    public void addComment(Comentario comentario) {
        String sql = "INSERT INTO comments (ticket_id, user_id, text, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, comentario.getTicketId());
            pstmt.setInt(2, comentario.getUserId());
            pstmt.setString(3, comentario.getText());
            pstmt.setTimestamp(4, Timestamp.valueOf(comentario.getCreatedAt()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        comentario.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar comentario: " + e.getMessage());
        }
    }

    @Override
    public List<Comentario> listByTicket(int ticketId) {
        List<Comentario> comentarios = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE ticket_id = ? ORDER BY created_at ASC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ticketId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comentario comentario = new Comentario(
                            rs.getInt("ticket_id"),
                            rs.getInt("user_id"),
                            rs.getString("text")
                    );
                    comentario.setId(rs.getInt("id"));
                    comentario.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    comentarios.add(comentario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar comentarios por ticket: " + e.getMessage());
        }
        return comentarios;
    }
}

