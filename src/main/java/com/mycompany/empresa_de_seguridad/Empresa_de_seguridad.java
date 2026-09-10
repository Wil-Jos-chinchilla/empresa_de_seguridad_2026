/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author JOSUE
 */
package com.mycompany.empresa_de_seguridad;

import gt.edu.umg.bd.ConexionSQL;
import java.sql.Connection;

public class Empresa_de_seguridad {

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE SEGURIDAD ===");
        
        Connection conexion = ConexionSQL.conectar();
        
        if (conexion != null) {
            System.out.println("✅ Aplicación lista para usarse");
        } else {
            System.out.println("❌ Error al conectar a la base de datos");
        }
    }
}
