// * @author josei
package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.ClienteJpaController;
import com.mycompany.empresa_de_seguridad.model.Cliente;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ClienteForm extends JFrame {

    private static final String UNIDAD_PERSISTENCIA = "empresa_seguridadPU";

    private static EntityManagerFactory emf;

    private final ClienteJpaController controller;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtDpi = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtDireccion = new JTextField();

    private DefaultTableModel modelo;
    private JTable tabla;
    private List<Cliente> lista;
    private Integer idSeleccionado = null;

    public ClienteForm() {
        controller = new ClienteJpaController((jakarta.persistence.EntityManagerFactory) getEmf());
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
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // no cierra toda la app
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));
        panelForm.add(new JLabel("Nombre *"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Apellido *"));
        panelForm.add(txtApellido);
        panelForm.add(new JLabel("DPI *"));
        panelForm.add(txtDpi);
        panelForm.add(new JLabel("Teléfono"));
        panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Correo"));
        panelForm.add(txtCorreo);
        panelForm.add(new JLabel("Dirección"));
        panelForm.add(txtDireccion);
        add(panelForm, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Apellido", "DPI", "Teléfono", "Correo", "Dirección", "Fecha registro"}, 0) {
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
        lista = controller.findClienteEntities();
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{
                c.getIdCliente(),
                c.getNombre(),
                c.getApellido(),
                c.getDpi(),
                c.getTelefono(),
                c.getCorreo(),
                c.getDireccion(),
                c.getFechaRegistro() != null ? formatoFecha.format(c.getFechaRegistro()) : ""
            });
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Cliente c = lista.get(fila);
        idSeleccionado = c.getIdCliente();
        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtDpi.setText(c.getDpi());
        txtTelefono.setText(c.getTelefono() == null ? "" : c.getTelefono());
        txtCorreo.setText(c.getCorreo() == null ? "" : c.getCorreo());
        txtDireccion.setText(c.getDireccion() == null ? "" : c.getDireccion());
    }

    private void limpiar() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtApellido.setText("");
        txtDpi.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    // Si no hay cliente seleccionado crea uno nuevo; si hay, lo actualiza
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dpi = txtDpi.getText().trim();

        if (!validarCampos(nombre, apellido, dpi)) {
            return;
        }

        try {
            if (idSeleccionado == null) {
                Cliente c = new Cliente();
                aplicarDatos(c, nombre, apellido, dpi);
                c.setFechaRegistro(new Date());
                controller.create(c);
                JOptionPane.showMessageDialog(this, "Cliente registrado.");
            } else {
                Cliente c = controller.findCliente(idSeleccionado);
                aplicarDatos(c, nombre, apellido, dpi);
                controller.edit(c);
                JOptionPane.showMessageDialog(this, "Cliente actualizado.");
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

        // DPI: 5 dígitos
        if (!dpi.matches("\\d{5}")) {
            advertir("El DPI debe tener exactamente 5 dígitos numéricos.", txtDpi);
            return false;
        }

        // DPI repetido (ignora al cliente que se está editando)
        for (Cliente c : lista) {
            if (dpi.equals(c.getDpi()) && !c.getIdCliente().equals(idSeleccionado)) {
                advertir("Ya existe un cliente con ese DPI.", txtDpi);
                return false;
            }
        }

        // Teléfono 4 dígitos
        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !telefono.matches("\\d{4}")) {
            advertir("El teléfono debe tener 4 dígitos numéricos.", txtTelefono);
            return false;
        }
        // Correo (opcional): formato básico
        String correo = txtCorreo.getText().trim();
        if (!correo.isEmpty() && !correo.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            advertir("Ingresa un correo válido (ejemplo: nombre@dominio.com).", txtCorreo);
            return false;
        }

        return true;
    }

    private void advertir(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        campo.requestFocus();
    }

    private void aplicarDatos(Cliente c, String nombre, String apellido, String dpi) {
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setDpi(dpi);
        c.setTelefono(vacioANull(txtTelefono.getText()));
        c.setCorreo(vacioANull(txtCorreo.getText()));
        c.setDireccion(vacioANull(txtDireccion.getText()));
    }

    private String vacioANull(String texto) {
        String t = texto.trim();
        return t.isEmpty() ? null : t;
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar este cliente?",
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
                    "No se pudo eliminar (puede tener contratos o usuarios asociados): " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}