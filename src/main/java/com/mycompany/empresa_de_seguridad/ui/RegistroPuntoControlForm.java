
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// * @author josei

package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.PuntoControlJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.RegistroPuntoControlJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.RondaJpaController;
import com.mycompany.empresa_de_seguridad.model.PuntoControl;
import com.mycompany.empresa_de_seguridad.model.RegistroPuntoControl;
import com.mycompany.empresa_de_seguridad.model.Ronda;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RegistroPuntoControlForm extends JFrame {

    // Valores que acepta CK_Registro_Metodo en RegistroPuntoControl
    private static final String[] METODOS = {"QR", "GPS", "MANUAL"};

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final RegistroPuntoControlJpaController registroCtrl = new RegistroPuntoControlJpaController(emf);
    private final RondaJpaController rondaCtrl = new RondaJpaController(emf);
    private final PuntoControlJpaController puntoCtrl = new PuntoControlJpaController(emf);

    private final JComboBox<Ronda> cboRonda = new JComboBox<>();
    private final JComboBox<PuntoControl> cboPunto = new JComboBox<>();
    private final JTextField txtFechaHora = new JTextField(16);
    private final JComboBox<String> cboMetodo = new JComboBox<>(METODOS);
    private final JTextField txtLatitud = new JTextField(12);
    private final JTextField txtLongitud = new JTextField(12);
    private final JTextField txtObservacion = new JTextField(25);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Ronda", "Punto de control", "Fecha y hora", "Método", "Latitud", "Longitud", "Observación"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final SimpleDateFormat fmtFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private RegistroPuntoControl seleccionado = null;

    public RegistroPuntoControlForm() {
        setTitle("Registro de Puntos de Control en Rondas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 580);
        setLocationRelativeTo(null);
        fmtFechaHora.setLenient(false);
        armarUI();
        cargarRondas();
        txtFechaHora.setText(fmtFechaHora.format(new Date()));
        cargarTabla();
    }

    private void armarUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del registro"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        agregar(form, g, 0, "Ronda:", cboRonda);
        agregar(form, g, 1, "Punto de control:", cboPunto);
        agregar(form, g, 2, "Fecha y hora (aaaa-mm-dd HH:mm):", txtFechaHora);
        agregar(form, g, 3, "Método:", cboMetodo);
        agregar(form, g, 4, "Latitud:", txtLatitud);
        agregar(form, g, 5, "Longitud:", txtLongitud);
        agregar(form, g, 6, "Observación:", txtObservacion);

        cboRonda.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof Ronda r) {
                    setText(textoRonda(r));
                }
                return this;
            }
        });
        cboPunto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof PuntoControl p) {
                    setText(p.getNombrePunto() + " (" + p.getCodigoQR() + ")");
                }
                return this;
            }
        });

        // Al cambiar de ronda, el combo de puntos muestra solo los de ese puesto
        cboRonda.addActionListener(e -> cargarPuntos());

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

    private String textoRonda(Ronda r) {
        String puesto = r.getIdPuesto() != null ? r.getIdPuesto().getNombrePuesto() : "";
        String agente = r.getIdAgente() != null
                ? r.getIdAgente().getNombre() + " " + r.getIdAgente().getApellido() : "";
        return "Ronda #" + r.getIdRonda() + " | " + puesto + " | " + agente
                + " | " + new SimpleDateFormat("yyyy-MM-dd").format(r.getFecha()) + " | " + r.getEstado();
    }

    private void cargarRondas() {
        cboRonda.removeAllItems();
        for (Ronda r : rondaCtrl.findRondaEntities()) {
            if (!"PROGRAMADA".equals(r.getEstado())) { // una ronda programada aún no empieza
                cboRonda.addItem(r);
            }
        }
        cargarPuntos();
    }

    // Puntos ACTIVOS del mismo puesto de la ronda seleccionada
    private void cargarPuntos() {
        cboPunto.removeAllItems();
        Ronda ronda = (Ronda) cboRonda.getSelectedItem();
        if (ronda == null) {
            return;
        }
        Integer idPuesto = ronda.getIdPuesto().getIdPuesto();
        for (PuntoControl p : puntoCtrl.findPuntoControlEntities()) {
            if (p.getEstado() && p.getIdPuesto().getIdPuesto().equals(idPuesto)) {
                cboPunto.addItem(p);
            }
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (RegistroPuntoControl rp : registroCtrl.findRegistroPuntoControlEntities()) {
            modelo.addRow(new Object[]{
                rp.getIdRegistro(),
                rp.getIdRonda() != null ? "Ronda #" + rp.getIdRonda().getIdRonda() : "",
                rp.getIdPuntoControl() != null ? rp.getIdPuntoControl().getNombrePunto() : "",
                fmtFechaHora.format(rp.getFechaHora()),
                rp.getMetodoRegistro(), rp.getLatitud(), rp.getLongitud(), rp.getObservacion()
            });
        }
    }

    private void cargarSeleccion(Integer id) {
        seleccionado = registroCtrl.findRegistroPuntoControl(id);
        if (seleccionado == null) {
            return;
        }
        // Ronda: si no está en el combo, se agrega para poder mostrarla
        Ronda ronda = seleccionado.getIdRonda();
        boolean estaRonda = false;
        for (int i = 0; i < cboRonda.getItemCount(); i++) {
            if (cboRonda.getItemAt(i).getIdRonda().equals(ronda.getIdRonda())) {
                cboRonda.setSelectedIndex(i);
                estaRonda = true;
                break;
            }
        }
        if (!estaRonda) {
            cboRonda.addItem(ronda);
            cboRonda.setSelectedItem(ronda);
        }
        // Punto: si no está en el combo (p. ej. quedó inactivo), se agrega
        PuntoControl punto = seleccionado.getIdPuntoControl();
        boolean estaPunto = false;
        for (int i = 0; i < cboPunto.getItemCount(); i++) {
            if (cboPunto.getItemAt(i).getIdPuntoControl().equals(punto.getIdPuntoControl())) {
                cboPunto.setSelectedIndex(i);
                estaPunto = true;
                break;
            }
        }
        if (!estaPunto) {
            cboPunto.addItem(punto);
            cboPunto.setSelectedItem(punto);
        }
        txtFechaHora.setText(fmtFechaHora.format(seleccionado.getFechaHora()));
        cboMetodo.setSelectedItem(seleccionado.getMetodoRegistro());
        txtLatitud.setText(seleccionado.getLatitud() == null ? "" : seleccionado.getLatitud().toPlainString());
        txtLongitud.setText(seleccionado.getLongitud() == null ? "" : seleccionado.getLongitud().toPlainString());
        txtObservacion.setText(seleccionado.getObservacion() == null ? "" : seleccionado.getObservacion());
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        cargarRondas();
        txtFechaHora.setText(fmtFechaHora.format(new Date()));
        cboMetodo.setSelectedIndex(0);
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtObservacion.setText("");
    }

    private void guardar() {
        Ronda ronda = (Ronda) cboRonda.getSelectedItem();
        PuntoControl punto = (PuntoControl) cboPunto.getSelectedItem();
        String metodo = (String) cboMetodo.getSelectedItem();
        String latTxt = txtLatitud.getText().trim();
        String lonTxt = txtLongitud.getText().trim();
        String obs = txtObservacion.getText().trim();

        // Validaciones
        if (ronda == null) {
            aviso("Selecciona una ronda (debe estar EN_CURSO, COMPLETADA o INCUMPLIDA).");
            return;
        }
        if (punto == null) {
            aviso("Selecciona un punto de control. Solo aparecen los puntos activos del puesto de la ronda.");
            return;
        }
        // El punto debe pertenecer al mismo puesto de la ronda
        if (!punto.getIdPuesto().getIdPuesto().equals(ronda.getIdPuesto().getIdPuesto())) {
            aviso("El punto de control no pertenece al puesto de esta ronda.");
            return;
        }
        Date fechaHora;
        try {
            fechaHora = fmtFechaHora.parse(txtFechaHora.getText().trim());
        } catch (ParseException ex) {
            aviso("La fecha y hora son obligatorias, con el formato aaaa-mm-dd HH:mm (ej. 2026-10-02 22:15).");
            return;
        }
        // El registro debe caer dentro del horario de la ronda (si la ronda ya tiene horas)
        if (ronda.getHoraInicio() != null && fechaHora.before(ronda.getHoraInicio())) {
            aviso("La fecha y hora no pueden ser anteriores al inicio de la ronda ("
                    + fmtFechaHora.format(ronda.getHoraInicio()) + ").");
            return;
        }
        if (ronda.getHoraFin() != null && fechaHora.after(ronda.getHoraFin())) {
            aviso("La fecha y hora no pueden ser posteriores al fin de la ronda ("
                    + fmtFechaHora.format(ronda.getHoraFin()) + ").");
            return;
        }
        if (obs.length() > 250) {
            aviso("La observación admite máximo 250 caracteres.");
            return;
        }
        if (latTxt.isEmpty() != lonTxt.isEmpty()) {
            aviso("Llena latitud y longitud juntas, o deja ambas vacías.");
            return;
        }
        if ("GPS".equals(metodo) && latTxt.isEmpty()) {
            aviso("Un registro por GPS necesita latitud y longitud.");
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
            RegistroPuntoControl rp = (seleccionado != null) ? seleccionado : new RegistroPuntoControl();
            rp.setIdRonda(ronda);
            rp.setIdPuntoControl(punto);
            rp.setFechaHora(fechaHora);
            rp.setMetodoRegistro(metodo);
            rp.setLatitud(lat);
            rp.setLongitud(lon);
            rp.setObservacion(obs.isEmpty() ? null : obs);

            if (seleccionado == null) {
                registroCtrl.create(rp);
            } else {
                registroCtrl.edit(rp);
            }
            JOptionPane.showMessageDialog(this, "Registro guardado correctamente.");
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso("Selecciona un registro de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el registro #" + seleccionado.getIdRegistro() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            registroCtrl.destroy(seleccionado.getIdRegistro());
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }
}
