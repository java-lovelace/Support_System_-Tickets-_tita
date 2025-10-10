package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    // CAMBIO IMPORTANTE: Esta es una ruta directa relativa a la raíz de tu proyecto.
    private static final String PROPERTIES_FILE_PATH = "src/resources/db.properties";
    private static final Properties properties = new Properties();

    static {
        // En lugar de getResourceAsStream, usamos FileInputStream para leer el archivo directamente.
        try (InputStream input = new FileInputStream(PROPERTIES_FILE_PATH)) {

            // Cargar las propiedades desde el archivo
            properties.load(input);

        } catch (IOException e) {
            System.err.println("--- ERROR DE LECTURA DE ARCHIVO ---");
            System.err.println("No se pudo leer el archivo desde la ruta: " + PROPERTIES_FILE_PATH);
            System.err.println("Asegúrate de que la estructura de carpetas es correcta: src -> resources -> config -> db.properties");
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la configuración de la base de datos.", e);
        }
    }

    /**
     * Obtiene una conexión a la base de datos utilizando las propiedades cargadas.
     * @return Una conexión JDBC.
     * @throws SQLException si ocurre un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format(
                "jdbc:postgresql://%s:%s/%s?sslmode=%s",
                properties.getProperty("db.host"),
                properties.getProperty("db.port"),
                properties.getProperty("db.name"),
                properties.getProperty("db.sslmode")
        );

        return DriverManager.getConnection(
                jdbcUrl,
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}