package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionDb {

    private static final String HOST = "";
    private static final String PORT = "";
    private static final String DATABASE = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;


    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                System.out.println("Conectando a la base de datos...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("¡Conexión exitosa!");

            } catch (SQLException e) {
                System.err.println("Error al conectar a la base de datos: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
