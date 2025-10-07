package dao;

import config.ConnectionFactory;
import domain.Role;
import domain.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBC implements UsuarioDao {

    @Override
    public void crear(Usuario usuario) {
        String sql = "INSERT INTO users (username, role) VALUES (?, ?::user_role)";
        try(Connection conn = ConnectionFactory.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getRole().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating user", e);
        }
    }

    @Override
    public void delete(int id) {
        String sqlUnassignTickets = "UPDATE tickets SET assignee_id = NULL WHERE assignee_id = ?";
        String sqlDeleteComments = "DELETE FROM comments WHERE user_id = ?";
        String sqlDeleteReportedTickets = "DELETE FROM tickets WHERE reporter_id = ?";
        String sqlDeleteUser = "DELETE FROM users WHERE id = ?";
        Connection conn = null;
        try {
            conn= ConnectionFactory.getConnection();
            // Start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUnassignTickets)){
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteComments)){
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteReportedTickets)){
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteUser)){
                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0){
                    throw new SQLException("Error deleting user, ID not found");
                }
            }
            // If everything went well, commit the transaction
            conn.commit();
        } catch (SQLException e) {
            // If something fails, rollback all changes
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Error trying to rollback transaction", ex);
                }
            }
            throw new RuntimeException("Error in delete user transaction", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Error trying to close the connection", e);
                }
            }
        }
    }

    @Override
    public Usuario findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Usuario usuario = null;
        try(Connection conn = ConnectionFactory.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setRole(Role.valueOf(rs.getString("role")));
                    usuario.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by ID", e);
        }
        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT id, username, role, created_at FROM users";
        List<Usuario> usuarios = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
                usuario.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding all users", e);
        }
        return usuarios;
    }
}