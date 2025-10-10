import java.sql.Connection;
import java.sql.SQLException;
import config.ConnectionFactory;
import view.UserMenu;
import view.MenuTicket;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Verificar conexión con Supabase antes de iniciar
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("Connected to Supabase successfully!");
        } catch (SQLException e) {
            System.err.println("Connection failed:");
            e.printStackTrace();
            return;
        }
        showMainMenu();
    }

    private static void showMainMenu(){
        UserMenu userMenu = new UserMenu();
        MenuTicket menuTicket = new MenuTicket();

        while (true){
            String input = JOptionPane.showInputDialog(null,
                    """
                            Menu principal
                            1. Gestionar Usuarios
                            2. Gestionar Tickets
                            0. Salir
                            """,
                    "Sistemas de Tickets", JOptionPane.QUESTION_MESSAGE);
            if(input == null) {
                break;
            }

            switch (input) {
                case "1" -> userMenu.showMenu();
                case "2" -> menuTicket.start();
                case "0" -> {
                    JOptionPane.showMessageDialog(null, "saliendo del sistema...");
                    return;
                }
                default -> JOptionPane.showMessageDialog(null, "Opción no valida");
            }
        }
    }
}