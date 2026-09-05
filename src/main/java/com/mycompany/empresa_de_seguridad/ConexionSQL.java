package com.mycompany.empresa_de_seguridad;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public class ConexionSQL {

    private static final String SERVIDOR = "localhost";
    private static final String PUERTO = "1433";
    private static final String BASE_DATOS = "EmpresaSeguridad";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "3719";

    private static final String URL = "jdbc:sqlserver://" + SERVIDOR + ":" + PUERTO
            + ";databaseName=" + BASE_DATOS
            + ";encrypt=true;trustServerCertificate=true;";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se encontró el driver JDBC: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar: " + e.getMessage());
        }
        return conexion;
    }
}
