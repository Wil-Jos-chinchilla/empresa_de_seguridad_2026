
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// * @author josei

package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.PlanServicioJpaController;
import com.mycompany.empresa_de_seguridad.model.PlanServicio;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PlanServicioForm extends JFrame {

    private static final String UNIDAD_PERSISTENCIA = "empresa_seguridadPU";

    private static EntityManagerFactory emf;

    private final PlanServicioJpaController controller;

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JCheckBox chkActivo = new JCheckBox("Activo", true);

    private DefaultTableModel modelo;
    private JTable tabla;
    private List<PlanServicio> lista;
    private Integer idSeleccionado = null;

    public PlanServicioForm() {
        controller = new PlanServicioJpaController((jakarta.persistence.EntityManagerFactory) getEmf());
        initComponents();
        cargarTabla();
    }

    // Un solo EntityManagerFactory para toda la aplicación
    private static synchronized EntityManagerFactory getEmf() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);
        }
        return emf;
    }

    private void initComponents() {
        setTitle("Gestión de Planes de Servicio");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // no cierra toda la app
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del plan"));
        panelForm.add(new JLabel("Nombre del plan *"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Descripción"));
        panelForm.add(txtDescripcion);
        panelForm.add(new JLabel("Precio mensual *"));
        panelForm.add(txtPrecio);
        panelForm.add(new JLabel("Estado"));
        panelForm.add(chkActivo);
        add(panelForm, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre del plan", "Descripción", "Precio mensual", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFila();
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        lista = controller.findPlanServicioEntities();
        for (PlanServicio p : lista) {
            modelo.addRow(new Object[]{
                p.getIdPlan(),
                p.getNombrePlan(),
                p.getDescripcion(),
                p.getPrecioMensual() != null ? p.getPrecioMensual().setScale(2).toPlainString() : "",
                p.getEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        PlanServicio p = lista.get(fila);
        idSeleccionado = p.getIdPlan();
        txtNombre.setText(p.getNombrePlan());
        txtDescripcion.setText(p.getDescripcion() == null ? "" : p.getDescripcion());
        txtPrecio.setText(p.getPrecioMensual() == null ? "" : p.getPrecioMensual().toPlainString());
        chkActivo.setSelected(p.getEstado());
    }

    private void limpiar() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        chkActivo.setSelected(true);
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    // Si no hay plan seleccionado crea uno nuevo; si hay, lo actualiza
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();

        if (!validarCampos(nombre, precioTexto)) {
            return;
        }

        try {
            BigDecimal precio = new BigDecimal(precioTexto);
            if (idSeleccionado == null) {
                PlanServicio p = new PlanServicio();
                aplicarDatos(p, nombre, precio);
                controller.create(p);
                JOptionPane.showMessageDialog(this, "Plan registrado.");
            } else {
                PlanServicio p = controller.findPlanServicio(idSeleccionado);
                aplicarDatos(p, nombre, precio);
                controller.edit(p);
                JOptionPane.showMessageDialog(this, "Plan actualizado.");
            }
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos(String nombre, String precioTexto) {
        // Obligatorios
        if (nombre.isEmpty() || precioTexto.isEmpty()) {
            advertir("El nombre del plan y el precio mensual son obligatorios.", txtNombre);
            return false;
        }

        // Largos máximos 
        if (nombre.length() > 100) {
            advertir("El nombre del plan no puede pasar de 100 caracteres.", txtNombre);
            return false;
        }
        if (txtDescripcion.getText().trim().length() > 300) {
            advertir("La descripción no puede pasar de 300 caracteres.", txtDescripcion);
            return false;
        }

        // Nombre repetido (ignora al plan que se está editando)
        for (PlanServicio p : lista) {
            if (nombre.equalsIgnoreCase(p.getNombrePlan()) && !p.getIdPlan().equals(idSeleccionado)) {
                advertir("Ya existe un plan con ese nombre.", txtNombre);
                return false;
            }
        }

        // Precio: número positivo, hasta 8 enteros y 2 decimales (columna DECIMAL(10,2))
        if (!precioTexto.matches("\\d{1,8}(\\.\\d{1,2})?")) {
            advertir("El precio debe ser un número con hasta 2 decimales (ejemplo: 1500 o 1500.50).", txtPrecio);
            return false;
        }
        if (new BigDecimal(precioTexto).compareTo(BigDecimal.ZERO) <= 0) {
            advertir("El precio debe ser mayor que 0.", txtPrecio);
            return false;
        }

        return true;
    }

    private void advertir(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        campo.requestFocus();
    }

    private void aplicarDatos(PlanServicio p, String nombre, BigDecimal precio) {
        p.setNombrePlan(nombre);
        p.setDescripcion(vacioANull(txtDescripcion.getText()));
        p.setPrecioMensual(precio);
        p.setEstado(chkActivo.isSelected());
    }

    private String vacioANull(String texto) {
        String t = texto.trim();
        return t.isEmpty() ? null : t;
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un plan de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este plan?\n(Si ya no se ofrece, es mejor desmarcar Activo.)",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            controller.destroy(idSeleccionado);
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar (puede tener contratos asociados): " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}