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

public class UsuarioDaoJDBC implements UsuarioDao {

    @Override
    public void crear(Usuario usuario) {
        String sql = "INSERT INTO users (username, role) VALUES (?, ?)";
        try(Connection conn = ConnectionFactory.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getRole().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return usuarios;
    }
}
