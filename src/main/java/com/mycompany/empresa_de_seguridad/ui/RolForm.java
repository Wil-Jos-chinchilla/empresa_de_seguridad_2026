/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.ui;

/**
 *
 * @author JOSUE
 */

import com.mycompany.empresa_de_seguridad.jpacontroller.RolJpaController;
import com.mycompany.empresa_de_seguridad.model.Rol;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * CRUD de Roles. Tabla: Rol(IdRol, NombreRol, Descripcion NULL).
 */
public class RolForm extends JFrame {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final RolJpaController rolCtrl = new RolJpaController(emf);

    private final JTextField txtNombre = new JTextField(25);
    private final JTextField txtDescripcion = new JTextField(35);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Descripci\u00f3n"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private List<Rol> roles = new ArrayList<>();
    private Rol seleccionado = null;

    public RolForm() {
        setTitle("Gesti\u00f3n de Roles");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        agregarFila(form, c, 0, "Nombre del rol:", txtNombre);
        agregarFila(form, c, 1, "Descripci\u00f3n:", txtDescripcion);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");
        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnCerrar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(form, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarSeleccion(tabla.getSelectedRow());
            }
        });

        setLayout(new BorderLayout());
        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void agregarFila(JPanel p, GridBagConstraints c, int fila, String texto, Component campo) {
        c.gridy = fila;
        c.gridx = 0;
        p.add(new JLabel(texto), c);
        c.gridx = 1;
        p.add(campo, c);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        roles = rolCtrl.findRolEntities();
        for (Rol r : roles) {
            modelo.addRow(new Object[]{
                r.getIdRol(),
                r.getNombreRol(),
                r.getDescripcion() == null ? "" : r.getDescripcion()
            });
        }
    }

    private void cargarSeleccion(int fila) {
        seleccionado = roles.get(fila);
        txtNombre.setText(seleccionado.getNombreRol());
        txtDescripcion.setText(seleccionado.getDescripcion() == null ? "" : seleccionado.getDescripcion());
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtNombre.setText("");
        txtDescripcion.setText("");
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();
        if (nombre.isEmpty() || nombre.length() > 50) {
            aviso("El nombre del rol es obligatorio (m\u00e1ximo 50 caracteres).");
            return;
        }
        if (desc.length() > 200) {
            aviso("La descripci\u00f3n admite m\u00e1ximo 200 caracteres.");
            return;
        }
        for (Rol r : roles) {
            boolean mismoRegistro = seleccionado != null && r.getIdRol().equals(seleccionado.getIdRol());
            if (!mismoRegistro && r.getNombreRol().equalsIgnoreCase(nombre)) {
                aviso("Ya existe un rol con ese nombre.");
                return;
            }
        }
        try {
            Rol r = (seleccionado == null) ? new Rol() : seleccionado;
            r.setNombreRol(nombre);
            r.setDescripcion(desc.isEmpty() ? null : desc);
            if (seleccionado == null) {
                rolCtrl.create(r);
            } else {
                rolCtrl.edit(r);
            }
            cargarTabla();
            limpiar();
            JOptionPane.showMessageDialog(this, "Rol guardado correctamente.");
        } catch (Exception ex) {
            aviso("No se pudo guardar el rol: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso("Selecciona un rol de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "\u00bfEliminar el rol \"" + seleccionado.getNombreRol() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            rolCtrl.destroy(seleccionado.getIdRol());
            cargarTabla();
            limpiar();
        } catch (Exception ex) {
            aviso("No se pudo eliminar (puede tener usuarios asignados): " + ex.getMessage());
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}