///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this templa
// * @author josei

package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.PuestoServicioJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.PuntoControlJpaController;
import com.mycompany.empresa_de_seguridad.model.PuestoServicio;
import com.mycompany.empresa_de_seguridad.model.PuntoControl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PuntoControlForm extends JFrame {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final PuntoControlJpaController puntoCtrl = new PuntoControlJpaController(emf);
    private final PuestoServicioJpaController puestoCtrl = new PuestoServicioJpaController(emf);

    private final JComboBox<PuestoServicio> cboPuesto = new JComboBox<>();
    private final JTextField txtNombre = new JTextField(25);
    private final JTextField txtDescripcion = new JTextField(25);
    private final JTextField txtCodigoQR = new JTextField(25);
    private final JTextField txtLatitud = new JTextField(12);
    private final JTextField txtLongitud = new JTextField(12);
    private final JCheckBox chkActivo = new JCheckBox("Activo", true);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Puesto", "Nombre", "Descripción", "Código QR", "Latitud", "Longitud", "Activo"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private PuntoControl seleccionado = null;

    public PuntoControlForm() {
        setTitle("Gestión de Puntos de Control");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 560);
        setLocationRelativeTo(null);
        armarUI();
        cargarPuestos();
        cargarTabla();
    }

    private void armarUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del punto de control"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        agregar(form, g, 0, "Puesto:", cboPuesto);
        agregar(form, g, 1, "Nombre del punto:", txtNombre);
        agregar(form, g, 2, "Descripción:", txtDescripcion);
        agregar(form, g, 3, "Código QR:", txtCodigoQR);
        agregar(form, g, 4, "Latitud:", txtLatitud);
        agregar(form, g, 5, "Longitud:", txtLongitud);
        agregar(form, g, 6, "Estado:", chkActivo);

        // Cómo se muestra cada puesto en el combo
        cboPuesto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof PuestoServicio p) {
                    setText(p.getNombrePuesto());
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

    private void cargarPuestos() {
        cboPuesto.removeAllItems();
        for (PuestoServicio p : puestoCtrl.findPuestoServicioEntities()) {
            if ("ACTIVO".equals(p.getEstado())) { // solo puestos vigentes
                cboPuesto.addItem(p);
            }
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (PuntoControl pc : puntoCtrl.findPuntoControlEntities()) {
            modelo.addRow(new Object[]{
                pc.getIdPuntoControl(),
                pc.getIdPuesto() != null ? pc.getIdPuesto().getNombrePuesto() : "",
                pc.getNombrePunto(), pc.getDescripcion(), pc.getCodigoQR(),
                pc.getLatitud(), pc.getLongitud(),
                pc.getEstado() ? "Sí" : "No"
            });
        }
    }

    private void cargarSeleccion(Integer id) {
        seleccionado = puntoCtrl.findPuntoControl(id);
        if (seleccionado == null) {
            return;
        }
        // Si el puesto ya no está ACTIVO, lo agregamos al combo para poder mostrarlo
        PuestoServicio ps = seleccionado.getIdPuesto();
        boolean esta = false;
        for (int i = 0; i < cboPuesto.getItemCount(); i++) {
            if (cboPuesto.getItemAt(i).getIdPuesto().equals(ps.getIdPuesto())) {
                cboPuesto.setSelectedIndex(i);
                esta = true;
                break;
            }
        }
        if (!esta) {
            cboPuesto.addItem(ps);
            cboPuesto.setSelectedItem(ps);
        }
        txtNombre.setText(seleccionado.getNombrePunto());
        txtDescripcion.setText(seleccionado.getDescripcion() == null ? "" : seleccionado.getDescripcion());
        txtCodigoQR.setText(seleccionado.getCodigoQR() == null ? "" : seleccionado.getCodigoQR());
        txtLatitud.setText(seleccionado.getLatitud() == null ? "" : seleccionado.getLatitud().toPlainString());
        txtLongitud.setText(seleccionado.getLongitud() == null ? "" : seleccionado.getLongitud().toPlainString());
        chkActivo.setSelected(seleccionado.getEstado());
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCodigoQR.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        chkActivo.setSelected(true);
        cargarPuestos();
    }

    private void guardar() {
        PuestoServicio puesto = (PuestoServicio) cboPuesto.getSelectedItem();
        String nombre = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String qr = txtCodigoQR.getText().trim();
        String latTxt = txtLatitud.getText().trim();
        String lonTxt = txtLongitud.getText().trim();

        // Validaciones
        if (puesto == null) {
            aviso("Selecciona un puesto (debe estar ACTIVO).");
            return;
        }
        if (nombre.isEmpty() || nombre.length() > 100) {
            aviso("El nombre es obligatorio (máximo 100 caracteres).");
            return;
        }
        if (desc.length() > 250) {
            aviso("La descripción admite máximo 250 caracteres.");
            return;
        }
        // CodigoQR tiene restricción UNIQUE en la BD (SQL Server solo admite un NULL), por eso es obligatorio
        if (qr.isEmpty() || qr.length() > 150) {
            aviso("El código QR es obligatorio (máximo 150 caracteres).");
            return;
        }
        // Mensaje amigable antes de que la BD rechace el duplicado
        for (PuntoControl otro : puntoCtrl.findPuntoControlEntities()) {
            boolean mismoRegistro = seleccionado != null
                    && otro.getIdPuntoControl().equals(seleccionado.getIdPuntoControl());
            if (!mismoRegistro && qr.equalsIgnoreCase(otro.getCodigoQR())) {
                aviso("Ya existe un punto de control con ese código QR.");
                return;
            }
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
            PuntoControl pc = (seleccionado != null) ? seleccionado : new PuntoControl();
            pc.setIdPuesto(puesto);
            pc.setNombrePunto(nombre);
            pc.setDescripcion(desc.isEmpty() ? null : desc);
            pc.setCodigoQR(qr);
            pc.setLatitud(lat);
            pc.setLongitud(lon);
            pc.setEstado(chkActivo.isSelected());

            if (seleccionado == null) {
                puntoCtrl.create(pc);
            } else {
                puntoCtrl.edit(pc);
            }
            JOptionPane.showMessageDialog(this, "Punto de control guardado correctamente.");
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso("Selecciona un punto de control de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el punto \"" + seleccionado.getNombrePunto() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            puntoCtrl.destroy(seleccionado.getIdPuntoControl());
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar (puede tener registros de rondas relacionados): " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }
}