package dao;

import domain.Usuario;

import java.util.List;

public interface UsuarioDao {
    void crear (Usuario usuario);
    void delete(int id);
    Usuario findById(int id);
    List<Usuario> findAll();
}
