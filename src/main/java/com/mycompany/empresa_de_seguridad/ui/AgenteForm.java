package com.mycompany.empresa_de_seguridad.ui;

///**
// * @author josei
// */
import com.mycompany.empresa_de_seguridad.jpacontroller.AgenteSeguridadJpaController;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class AgenteForm extends JFrame {

    private static final String UNIDAD_PERSISTENCIA = "empresa_seguridadPU";

    private static EntityManagerFactory emf;

    private final AgenteSeguridadJpaController controller;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtDpi = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JComboBox<String> cboEstado = new JComboBox<>(new String[]{"DISPONIBLE", "ASIGNADO", "INACTIVO"});
    private DefaultTableModel modelo;
    private JTable tabla;
    private List<AgenteSeguridad> lista;
    private Integer idSeleccionado = null;

    public AgenteForm() {
        controller = new AgenteSeguridadJpaController((jakarta.persistence.EntityManagerFactory) getEmf());
        initComponents();
        cargarTabla();
    }

    // EntityManagerFactory para toda la aplicación
    private static synchronized EntityManagerFactory getEmf() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);
        }
        return emf;
    }

    private void initComponents() {
        setTitle("Gestión de Agentes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // no cierra toda la app
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del agente"));
        panelForm.add(new JLabel("Nombre *"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Apellido *"));
        panelForm.add(txtApellido);
        panelForm.add(new JLabel("DPI *"));
        panelForm.add(txtDpi);
        panelForm.add(new JLabel("Teléfono"));
        panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Dirección"));
        panelForm.add(txtDireccion);
        panelForm.add(new JLabel("Estado *"));
        panelForm.add(cboEstado);
        add(panelForm, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Apellido", "DPI", "Teléfono", "Dirección", "Fecha ingreso", "Estado"}, 0) {
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
        lista = controller.findAgenteSeguridadEntities();
        for (AgenteSeguridad a : lista) {
            modelo.addRow(new Object[]{
                a.getIdAgente(),
                a.getNombre(),
                a.getApellido(),
                a.getDpi(),
                a.getTelefono(),
                a.getDireccion(),
                a.getFechaIngreso() != null ? formatoFecha.format(a.getFechaIngreso()) : "",
                a.getEstado()
            });
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        AgenteSeguridad a = lista.get(fila);
        idSeleccionado = a.getIdAgente();
        txtNombre.setText(a.getNombre());
        txtApellido.setText(a.getApellido());
        txtDpi.setText(a.getDpi());
        txtTelefono.setText(a.getTelefono() == null ? "" : a.getTelefono());
        txtDireccion.setText(a.getDireccion() == null ? "" : a.getDireccion());
        cboEstado.setSelectedItem(a.getEstado());
    }

    private void limpiar() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtApellido.setText("");
        txtDpi.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        cboEstado.setSelectedIndex(0);
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    // Si no hay agente seleccionado crea uno nuevo; si hay, lo actualiza
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dpi = txtDpi.getText().trim();

        if (!validarCampos(nombre, apellido, dpi)) {
            return;
        }

        try {
            if (idSeleccionado == null) {
                AgenteSeguridad a = new AgenteSeguridad();
                aplicarDatos(a, nombre, apellido, dpi);
                a.setFechaIngreso(new Date());
                controller.create(a);
                JOptionPane.showMessageDialog(this, "Agente registrado.");
            } else {
                AgenteSeguridad a = controller.findAgenteSeguridad(idSeleccionado);
                aplicarDatos(a, nombre, apellido, dpi);
                controller.edit(a);
                JOptionPane.showMessageDialog(this, "Agente actualizado.");
            }
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos(String nombre, String apellido, String dpi) {
        // Obligatorios
        if (nombre.isEmpty() || apellido.isEmpty() || dpi.isEmpty()) {
            advertir("Nombre, apellido y DPI son obligatorios.", txtNombre);
            return false;
        }

        // DPI 5 dígitos
        if (!dpi.matches("\\d{5}")) {
            advertir("El DPI debe tener exactamente 5 dígitos numéricos.", txtDpi);
            return false;
        }

        // DPI repetido (ignora al agente que se está editando)
        for (AgenteSeguridad a : lista) {
            if (dpi.equals(a.getDpi()) && !a.getIdAgente().equals(idSeleccionado)) {
                advertir("Ya existe un agente con ese DPI.", txtDpi);
                return false;
            }
        }

        // Teléfono 4 dígitos
        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !telefono.matches("\\d{4}")) {
            advertir("El teléfono debe tener 4 dígitos numéricos.", txtTelefono);
            return false;
        }

        return true;
    }

    private void advertir(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        campo.requestFocus();
    }

    private void aplicarDatos(AgenteSeguridad a, String nombre, String apellido, String dpi) {
        a.setNombre(nombre);
        a.setApellido(apellido);
        a.setDpi(dpi);
        a.setTelefono(vacioANull(txtTelefono.getText()));
        a.setDireccion(vacioANull(txtDireccion.getText()));
        a.setEstado((String) cboEstado.getSelectedItem());
    }

    private String vacioANull(String texto) {
        String t = texto.trim();
        return t.isEmpty() ? null : t;
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un agente de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este agente?\n(Si solo dejó de trabajar, es mejor cambiarle el estado a Inactivo.)",
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
                    "No se pudo eliminar (puede tener turnos, rondas, incidentes o usuarios asociados): " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
