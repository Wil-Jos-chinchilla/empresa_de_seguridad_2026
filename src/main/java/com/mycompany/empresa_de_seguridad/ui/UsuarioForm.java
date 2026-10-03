/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.ui;

/**
 *
 * @author JOSUE
 */

import com.mycompany.empresa_de_seguridad.jpacontroller.AgenteSeguridadJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.RolJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.UsuarioJpaController;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import com.mycompany.empresa_de_seguridad.model.Rol;
import com.mycompany.empresa_de_seguridad.model.Usuario;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * CRUD de Usuarios. Tabla: Usuario(IdUsuario, NombreUsuario, Contrasena, IdRol,
 * IdCliente NULL, IdAgente NULL, Estado bit, FechaCreacion, UltimoAcceso NULL).
 * La contraseña se guarda en texto plano porque LoginForm la compara así.
 */
public class UsuarioForm extends JFrame {

    private static final String[] ESTADOS = {"ACTIVO", "INACTIVO"};

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final UsuarioJpaController usuarioCtrl = new UsuarioJpaController(emf);
    private final RolJpaController rolCtrl = new RolJpaController(emf);
    private final AgenteSeguridadJpaController agenteCtrl = new AgenteSeguridadJpaController(emf);

    private final SimpleDateFormat fmtFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final JTextField txtNombre = new JTextField(20);
    private final JPasswordField txtClave = new JPasswordField(20);
    private final JComboBox<Rol> cmbRol = new JComboBox<>();
    private final JComboBox<AgenteSeguridad> cmbAgente = new JComboBox<>();
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Usuario", "Rol", "Agente", "Estado", "Creación", "Último acceso"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private List<Usuario> usuarios = new ArrayList<>();
    private Usuario seleccionado = null;

    public UsuarioForm() {
        setTitle("Gestión de Usuarios y Roles");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        construirUI();
        cargarCombos();
        cargarTabla();
    }

    private void construirUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;

        agregarFila(form, c, 0, "Nombre de usuario:", txtNombre);
        agregarFila(form, c, 1, "Contraseña:", txtClave);
        agregarFila(form, c, 2, "Rol:", cmbRol);
        agregarFila(form, c, 3, "Agente (opcional):", cmbAgente);
        agregarFila(form, c, 4, "Estado:", cmbEstado);

        cmbRol.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Rol) {
                    Rol r = (Rol) value;
                    setText(r.getNombreRol());
                }
                return this;
            }
        });
        cmbAgente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AgenteSeguridad) {
                    AgenteSeguridad a = (AgenteSeguridad) value;
                    setText(a.getIdAgente() + " - " + a.getNombre() + " " + a.getApellido());
                } else {
                    setText("(ninguno)");
                }
                return this;
            }
        });

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRoles = new JButton("Gestionar roles");
        JButton btnCerrar = new JButton("Cerrar");
        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCerrar.addActionListener(e -> dispose());
        btnRoles.addActionListener(e -> {
            RolForm rf = new RolForm();
            rf.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent ev) {
                    cargarCombos();
                    cargarTabla();
                }
            });
            rf.setVisible(true);
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnRoles);
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

    private void cargarCombos() {
        cmbRol.setModel(new DefaultComboBoxModel<>(rolCtrl.findRolEntities().toArray(new Rol[0])));

        List<AgenteSeguridad> lista = agenteCtrl.findAgenteSeguridadEntities();
        AgenteSeguridad[] items = new AgenteSeguridad[lista.size() + 1];
        items[0] = null; // opción "(ninguno)"
        for (int i = 0; i < lista.size(); i++) {
            items[i + 1] = lista.get(i);
        }
        cmbAgente.setModel(new DefaultComboBoxModel<>(items));
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        usuarios = usuarioCtrl.findUsuarioEntities();
        for (Usuario u : usuarios) {
            AgenteSeguridad a = u.getIdAgente();
            modelo.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombreUsuario(),
                u.getIdRol().getNombreRol(),
                a == null ? "" : a.getNombre() + " " + a.getApellido(),
                u.getEstado() ? "ACTIVO" : "INACTIVO",
                fmtFechaHora.format(u.getFechaCreacion()),
                u.getUltimoAcceso() == null ? "" : fmtFechaHora.format(u.getUltimoAcceso())
            });
        }
    }

    private void cargarSeleccion(int fila) {
        seleccionado = usuarios.get(fila);
        txtNombre.setText(seleccionado.getNombreUsuario());
        txtClave.setText(""); // vacía = conservar la contraseña actual
        for (int i = 0; i < cmbRol.getItemCount(); i++) {
            if (cmbRol.getItemAt(i).getIdRol().equals(seleccionado.getIdRol().getIdRol())) {
                cmbRol.setSelectedIndex(i);
                break;
            }
        }
        cmbAgente.setSelectedIndex(0);
        if (seleccionado.getIdAgente() != null) {
            for (int i = 1; i < cmbAgente.getItemCount(); i++) {
                if (cmbAgente.getItemAt(i).getIdAgente().equals(seleccionado.getIdAgente().getIdAgente())) {
                    cmbAgente.setSelectedIndex(i);
                    break;
                }
            }
        }
        cmbEstado.setSelectedItem(seleccionado.getEstado() ? "ACTIVO" : "INACTIVO");
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtNombre.setText("");
        txtClave.setText("");
        cmbEstado.setSelectedIndex(0);
        if (cmbRol.getItemCount() > 0) {
            cmbRol.setSelectedIndex(0);
        }
        cmbAgente.setSelectedIndex(0);
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String clave = new String(txtClave.getPassword());
        Rol rol = (Rol) cmbRol.getSelectedItem();
        AgenteSeguridad agente = (AgenteSeguridad) cmbAgente.getSelectedItem();

        if (nombre.isEmpty() || nombre.length() > 50) {
            aviso("El nombre de usuario es obligatorio (máximo 50 caracteres).");
            return;
        }
        if (rol == null) {
            aviso("Selecciona un rol (crea uno con \"Gestionar roles\" si no hay).");
            return;
        }
        if (seleccionado == null && clave.isEmpty()) {
            aviso("La contraseña es obligatoria para un usuario nuevo.");
            return;
        }
        if (!clave.isEmpty() && (clave.length() < 4 || clave.length() > 100)) {
            aviso("La contraseña debe tener entre 4 y 100 caracteres.");
            return;
        }
        for (Usuario u : usuarios) {
            boolean mismo = seleccionado != null && u.getIdUsuario().equals(seleccionado.getIdUsuario());
            if (!mismo && u.getNombreUsuario().equalsIgnoreCase(nombre)) {
                aviso("Ya existe un usuario con ese nombre.");
                return;
            }
        }

        try {
            Usuario u = (seleccionado == null) ? new Usuario() : seleccionado;
            u.setNombreUsuario(nombre);
            if (!clave.isEmpty()) {
                u.setContrasena(clave);
            }
            u.setIdRol(rol);
            u.setIdAgente(agente);
            u.setEstado("ACTIVO".equals(cmbEstado.getSelectedItem()));

            if (seleccionado == null) {
                u.setFechaCreacion(new Date());
                usuarioCtrl.create(u);
            } else {
                usuarioCtrl.edit(u);
            }
            cargarTabla();
            limpiar();
            JOptionPane.showMessageDialog(this, "Usuario guardado correctamente.");
        } catch (Exception ex) {
            aviso("No se pudo guardar el usuario: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso("Selecciona un usuario de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al usuario \"" + seleccionado.getNombreUsuario() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            usuarioCtrl.destroy(seleccionado.getIdUsuario());
            cargarTabla();
            limpiar();
        } catch (Exception ex) {
            aviso("No se pudo eliminar: " + ex.getMessage());
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
