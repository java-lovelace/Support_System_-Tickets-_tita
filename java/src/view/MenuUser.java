package app;

import controller.MenuController;
import dao.UsuarioDao;
import dao.UsuarioDaoJDBC;
import javax.swing.JOptionPane;


public class MenuUser {
    private final UsuarioDao usuarioDao = new UsuarioDaoJDBC();
    private final MenuController menuController = new MenuController(usuarioDao);

    public void mostrarMenu() {
        while (true) {
            String menuTexto = "--- MENU PRINCIPAL ---\n" +
                    "1. Crear Usuario\n" +
                    "2. Listar Todos los Usuarios\n" +
                    "3. Buscar Usuario por ID\n" +
                    "4. Salir\n" +
                    "Por favor, introduce el número de tu opción:";

            String opcion = JOptionPane.showInputDialog(null, menuTexto, "Gestión de Usuarios", JOptionPane.PLAIN_MESSAGE);

            if (opcion == null) {
                break;
            }

            switch (opcion) {
                case "1":
                    menuController.CrearDeUsuario();
                    break;
                case "2":
                    menuController.ListarUsuarios();
                    break;
                case "3":
                    menuController.BuscarPorId();
                    break;
                case "4":
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema. ¡Hasta pronto!");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, introduce un número del 1 al 4.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}