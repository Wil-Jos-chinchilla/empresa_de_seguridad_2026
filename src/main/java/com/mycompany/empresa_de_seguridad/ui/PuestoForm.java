
package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.ContratoJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.PuestoServicioJpaController;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import com.mycompany.empresa_de_seguridad.model.PuestoServicio;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PuestoForm extends JFrame {

    // Valores que acepta el CHECK de Estado en PuestoServicio (AJUSTAR si son otros)
    private static final String[] ESTADOS = {"ACTIVO", "INACTIVO"};

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final PuestoServicioJpaController puestoCtrl = new PuestoServicioJpaController(emf);
    private final ContratoJpaController contratoCtrl = new ContratoJpaController(emf);

    private final JComboBox<Contrato> cboContrato = new JComboBox<>();
    private final JTextField txtNombre = new JTextField(25);
    private final JTextField txtDescripcion = new JTextField(25);
    private final JTextField txtDireccion = new JTextField(25);
    private final JTextField txtLatitud = new JTextField(12);
    private final JTextField txtLongitud = new JTextField(12);
    private final JComboBox<String> cboEstado = new JComboBox<>(ESTADOS);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Contrato", "Nombre", "Descripción", "Dirección", "Latitud", "Longitud", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private PuestoServicio seleccionado = null;

    public PuestoForm() {
        setTitle("Gestión de Puestos de Servicio");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        armarUI();
        cargarContratos();
        cargarTabla();
    }

    private void armarUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del puesto"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        agregar(form, g, 0, "Contrato:", cboContrato);
        agregar(form, g, 1, "Nombre del puesto:", txtNombre);
        agregar(form, g, 2, "Descripción:", txtDescripcion);
        agregar(form, g, 3, "Dirección:", txtDireccion);
        agregar(form, g, 4, "Latitud:", txtLatitud);
        agregar(form, g, 5, "Longitud:", txtLongitud);
        agregar(form, g, 6, "Estado:", cboEstado);

        // Cómo se muestra cada contrato en el combo
        cboContrato.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof Contrato) {
                    Contrato c = (Contrato) v;
                    setText("Contrato #" + c.getIdContrato() + " (" + c.getEstado() + ")");
                }
                return this;
            }
        });

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

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarSeleccion((Integer) modelo.getValueAt(tabla.getSelectedRow(), 0));
            }
        });

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(form, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        setLayout(new BorderLayout(8, 8));
        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void agregar(JPanel p, GridBagConstraints g, int fila, String etiqueta, JComponent campo) {
        g.gridy = fila;
        g.gridx = 0;
        p.add(new JLabel(etiqueta), g);
        g.gridx = 1;
        p.add(campo, g);
    }

    private void cargarContratos() {
        cboContrato.removeAllItems();
        for (Contrato c : contratoCtrl.findContratoEntities()) {
            if ("ACTIVO".equals(c.getEstado())) { // solo contratos vigentes
                cboContrato.addItem(c);
            }
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (PuestoServicio p : puestoCtrl.findPuestoServicioEntities()) {
            modelo.addRow(new Object[]{
                p.getIdPuesto(),
                p.getIdContrato() != null ? p.getIdContrato().getIdContrato() : "",
                p.getNombrePuesto(), p.getDescripcion(), p.getDireccion(),
                p.getLatitud(), p.getLongitud(), p.getEstado()
            });
        }
    }

    private void cargarSeleccion(Integer id) {
        seleccionado = puestoCtrl.findPuestoServicio(id);
        if (seleccionado == null) {
            return;
        }
        // Si el contrato del puesto ya no está ACTIVO, lo agregamos al combo para poder mostrarlo
        Contrato c = seleccionado.getIdContrato();
        boolean esta = false;
        for (int i = 0; i < cboContrato.getItemCount(); i++) {
            if (cboContrato.getItemAt(i).getIdContrato().equals(c.getIdContrato())) {
                cboContrato.setSelectedIndex(i);
                esta = true;
                break;
            }
        }
        if (!esta) {
            cboContrato.addItem(c);
            cboContrato.setSelectedItem(c);
        }
        txtNombre.setText(seleccionado.getNombrePuesto());
        txtDescripcion.setText(seleccionado.getDescripcion() == null ? "" : seleccionado.getDescripcion());
        txtDireccion.setText(seleccionado.getDireccion() == null ? "" : seleccionado.getDireccion());
        txtLatitud.setText(seleccionado.getLatitud() == null ? "" : seleccionado.getLatitud().toPlainString());
        txtLongitud.setText(seleccionado.getLongitud() == null ? "" : seleccionado.getLongitud().toPlainString());
        cboEstado.setSelectedItem(seleccionado.getEstado());
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtDireccion.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        cboEstado.setSelectedIndex(0);
        cargarContratos();
    }

    private void guardar() {
        Contrato contrato = (Contrato) cboContrato.getSelectedItem();
        String nombre = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String dir = txtDireccion.getText().trim();
        String latTxt = txtLatitud.getText().trim();
        String lonTxt = txtLongitud.getText().trim();

        // Validaciones
        if (contrato == null) {
            aviso("Selecciona un contrato (debe estar ACTIVO).");
            return;
        }
        if (nombre.isEmpty() || nombre.length() > 100) {
            aviso("El nombre es obligatorio (máximo 100 caracteres).");
            return;
        }
        if (desc.length() > 250 || dir.length() > 250) {
            aviso("Descripción y dirección admiten máximo 250 caracteres.");
            return;
        }
        if (latTxt.isEmpty() != lonTxt.isEmpty()) {
            aviso("Llena latitud y longitud juntas, o deja ambas vacías.");
            return;
        }
        BigDecimal lat = null, lon = null;
        try {
            if (!latTxt.isEmpty()) {
                lat = new BigDecimal(latTxt);
                lon = new BigDecimal(lonTxt);
                if (lat.doubleValue() < -90 || lat.doubleValue() > 90
                        || lon.doubleValue() < -180 || lon.doubleValue() > 180) {
                    aviso("Latitud debe estar entre -90 y 90, y longitud entre -180 y 180.");
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            aviso("Latitud y longitud deben ser números (ej. 14.6349000).");
            return;
        }

        try {
            PuestoServicio p = (seleccionado != null) ? seleccionado : new PuestoServicio();
            p.setIdContrato(contrato);
            p.setNombrePuesto(nombre);
            p.setDescripcion(desc.isEmpty() ? null : desc);
            p.setDireccion(dir.isEmpty() ? null : dir);
            p.setLatitud(lat);
            p.setLongitud(lon);
            p.setEstado((String) cboEstado.getSelectedItem());

            if (seleccionado == null) {
                puestoCtrl.create(p);
            } else {
                puestoCtrl.edit(p);
            }
            JOptionPane.showMessageDialog(this, "Puesto guardado correctamente.");
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso("Selecciona un puesto de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar el puesto \"" + seleccionado.getNombrePuesto() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            puestoCtrl.destroy(seleccionado.getIdPuesto());
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar (puede tener rondas, asignaciones o turnos relacionados): " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }
}