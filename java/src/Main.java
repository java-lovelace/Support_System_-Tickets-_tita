import java.sql.Connection;
import java.sql.SQLException;
import config.ConnectionFactory;
import view.UserMenu;

public class Main {
    public static void main(String[] args) {

        // Verificar conexión con Supabase antes de iniciar
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("✅ Connected to Supabase successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed:");
            e.printStackTrace();
            return;
        }

        // Iniciar menú principal
        // MenuTicket menuTicket = new MenuTicket();
        // menuTicket.start();

        UserMenu userMenu = new UserMenu();
        userMenu.showMenu();
    }
}
