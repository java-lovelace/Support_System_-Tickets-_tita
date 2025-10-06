import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

import config.ConnectionFactory;
import view.Menu;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.start();

        // Check connection to DB
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("Connected to Supabase successfully!");

        } catch (SQLException e) {
            System.err.println("Connection failed:");
            e.printStackTrace();

        }
    }

}