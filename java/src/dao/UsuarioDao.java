package dao;

import domain.Usuario;

import java.util.List;

public interface UsuarioDao {
    void crear (Usuario usuario);
    Usuario findById(int id);
    List<Usuario> findAll();
}
