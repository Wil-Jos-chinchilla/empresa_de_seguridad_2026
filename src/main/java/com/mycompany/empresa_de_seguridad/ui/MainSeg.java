package com.mycompany.empresa_de_seguridad.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Menú principal
 */
public class MainSeg extends JFrame {

    public MainSeg() {
        setTitle("Empresa de Seguridad - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitulo = new JLabel("Sistema de Gestión de Seguridad", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 10, 10));

        panelBotones.add(crearBoton("Agentes de Seguridad", e -> abrirModulo("Agentes")));
        panelBotones.add(crearBoton("Clientes", e -> abrirModulo("Clientes")));
        panelBotones.add(crearBoton("Contratos", e -> abrirModulo("Contratos")));
        panelBotones.add(crearBoton("Planes de Servicio", e -> abrirModulo("Planes de Servicio")));
        panelBotones.add(crearBoton("Puestos de Servicio", e -> abrirModulo("Puestos de Servicio")));
        panelBotones.add(crearBoton("Turnos y Asistencia", e -> abrirModulo("Turnos y Asistencia")));
        panelBotones.add(crearBoton("Rondas y Puntos de Control", e -> abrirModulo("Rondas")));
        panelBotones.add(crearBoton("Incidentes", e -> abrirModulo("Incidentes")));
        panelBotones.add(crearBoton("Facturación", e -> abrirModulo("Facturación")));
        panelBotones.add(crearBoton("Usuarios y Roles", e -> abrirModulo("Usuarios")));

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> System.exit(0));
        JPanel panelSalir = new JPanel();
        panelSalir.add(btnSalir);
        panelPrincipal.add(panelSalir, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        boton.addActionListener(accion);
        return boton;
    }

//  cambio para el agregado de case de los modulos de botones
    private void abrirModulo(String nombreModulo) {
        switch (nombreModulo) {
            case "Clientes":
                new ClienteForm().setVisible(true);
                break;

            case "Agentes":
                new AgenteForm().setVisible(true);
                break;

            case "Planes de Servicio":
                new PlanServicioForm().setVisible(true);
                break;
            case "Contratos":
                new ContratoForm().setVisible(true);
                break;

            // aquí^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            default:
                JOptionPane.showMessageDialog(this, "Módulo en construcción: " + nombreModulo);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainSeg menu = new MainSeg();
            menu.setVisible(true);
        });
    }
}
