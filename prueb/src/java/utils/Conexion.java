package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3308/carpinteria";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection con;

    // Constructor para establecer la conexión
    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        }
    }

    // Método para obtener la conexión
    public Connection getConnection() {
        return con;
    }

    // Método para cerrar la conexión
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Prueba de conexión
        Conexion conexion = new Conexion();
        if (conexion.getConnection() != null) {
            System.out.println("Conexión establecida correctamente.");
        } else {
            System.out.println("Error al establecer la conexión.");
        }
        conexion.closeConnection();
    }
}
