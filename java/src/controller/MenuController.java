package controller;

import dao.UsuarioDao;
import domain.Role;
import domain.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class MenuController {

    private final UsuarioDao usuarioDao;

    public MenuController(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public void CrearDeUsuario() {
        String username = JOptionPane.showInputDialog(null, "Introduce el nombre del nuevo Usuario:", "Crear Usuario", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre del usuario no puede estar vacío.", "Operación Cancelada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Role[] roles = {Role.REPORTER, Role.ASSIGNEE};
        Role rolSeleccionado = (Role) JOptionPane.showInputDialog(
                null, "Selecciona un rol:", "Crear Usuario",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]
        );

        if (rolSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un rol.", "Operación Cancelada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setRole(rolSeleccionado);

        try {
            usuarioDao.crear(nuevoUsuario);
            JOptionPane.showMessageDialog(null, "¡Usuario creado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear el usuario: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void BuscarPorId() {
        String idStr = JOptionPane.showInputDialog(null, "Introduce el ID del usuario a buscar:", "Buscar Usuario", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null) {
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Usuario usuario = usuarioDao.findById(id);

            if (usuario != null) {
                String info = "--- Usuario Encontrado ---\n\n" +
                        "ID: " + usuario.getId() + "\n" +
                        "Username: " + usuario.getUsername() + "\n" +
                        "Rol: " + usuario.getRole() + "\n" +
                        "Fecha Creación: " + usuario.getCreatedAt() + "\n";
                JOptionPane.showMessageDialog(null, info, "Resultado de la Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con el ID: " + id, "No Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID no válido. Debes introducir un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ListarUsuarios() {
        List<Usuario> usuarios = usuarioDao.findAll();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios registrados.", "Lista de Usuarios", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder listaTexto = new StringBuilder("--- Lista de Usuarios ---\n\n");
        for (Usuario user : usuarios) {
            listaTexto.append("ID: ").append(user.getId())
                    .append(", Usuario: ").append(user.getUsername())
                    .append(", Rol: ").append(user.getRole()).append("\n");
        }

        JTextArea textArea = new JTextArea(listaTexto.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 250));
        JOptionPane.showMessageDialog(null, scrollPane, "Todos los Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }
}