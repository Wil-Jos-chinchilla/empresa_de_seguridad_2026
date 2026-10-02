
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// * @author josei

package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.AgenteSeguridadJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.PuestoServicioJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.RondaJpaController;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import com.mycompany.empresa_de_seguridad.model.PuestoServicio;
import com.mycompany.empresa_de_seguridad.model.Ronda;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RondaForm extends JFrame {

    // Valores que acepta CK_Ronda_Estado en la tabla Ronda
    private static final String[] ESTADOS = {"PROGRAMADA", "EN_CURSO", "COMPLETADA", "INCUMPLIDA"};

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final RondaJpaController rondaCtrl = new RondaJpaController(emf);
    private final PuestoServicioJpaController puestoCtrl = new PuestoServicioJpaController(emf);
    private final AgenteSeguridadJpaController agenteCtrl = new AgenteSeguridadJpaController(emf);

    private final JComboBox<PuestoServicio> cboPuesto = new JComboBox<>();
    private final JComboBox<AgenteSeguridad> cboAgente = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(12);
    private final JTextField txtHoraInicio = new JTextField(8);
    private final JTextField txtHoraFin = new JTextField(8);
    private final JComboBox<String> cboEstado = new JComboBox<>(ESTADOS);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Puesto", "Agente", "Fecha", "Inicio", "Fin", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final SimpleDateFormat fmtFecha = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat fmtFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Ronda seleccionada = null;

    public RondaForm() {
        setTitle("Gestión de Rondas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 560);
        setLocationRelativeTo(null);
        fmtFecha.setLenient(false);
        fmtHora.setLenient(false);
        fmtFechaHora.setLenient(false);
        armarUI();
        cargarPuestos();
        cargarAgentes();
        cargarTabla();
    }

    private void armarUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos de la ronda"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        agregar(form, g, 0, "Puesto:", cboPuesto);
        agregar(form, g, 1, "Agente:", cboAgente);
        agregar(form, g, 2, "Fecha (aaaa-mm-dd):", txtFecha);
        agregar(form, g, 3, "Hora inicio (HH:mm):", txtHoraInicio);
        agregar(form, g, 4, "Hora fin (HH:mm):", txtHoraFin);
        agregar(form, g, 5, "Estado:", cboEstado);

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
        cboAgente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof AgenteSeguridad a) {
                    setText(a.getNombre() + " " + a.getApellido());
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

    private void cargarAgentes() {
        cboAgente.removeAllItems();
        for (AgenteSeguridad a : agenteCtrl.findAgenteSeguridadEntities()) {
            if (!"INACTIVO".equals(a.getEstado())) { // DISPONIBLE o ASIGNADO
                cboAgente.addItem(a);
            }
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Ronda r : rondaCtrl.findRondaEntities()) {
            modelo.addRow(new Object[]{
                r.getIdRonda(),
                r.getIdPuesto() != null ? r.getIdPuesto().getNombrePuesto() : "",
                r.getIdAgente() != null ? r.getIdAgente().getNombre() + " " + r.getIdAgente().getApellido() : "",
                fmtFecha.format(r.getFecha()),
                r.getHoraInicio() == null ? "" : fmtFechaHora.format(r.getHoraInicio()),
                r.getHoraFin() == null ? "" : fmtFechaHora.format(r.getHoraFin()),
                r.getEstado()
            });
        }
    }

    private void cargarSeleccion(Integer id) {
        seleccionada = rondaCtrl.findRonda(id);
        if (seleccionada == null) {
            return;
        }
        // Si el puesto ya no está ACTIVO, lo agregamos al combo para poder mostrarlo
        PuestoServicio ps = seleccionada.getIdPuesto();
        boolean estaPuesto = false;
        for (int i = 0; i < cboPuesto.getItemCount(); i++) {
            if (cboPuesto.getItemAt(i).getIdPuesto().equals(ps.getIdPuesto())) {
                cboPuesto.setSelectedIndex(i);
                estaPuesto = true;
                break;
            }
        }
        if (!estaPuesto) {
            cboPuesto.addItem(ps);
            cboPuesto.setSelectedItem(ps);
        }
        // Igual con el agente (por si quedó INACTIVO)
        AgenteSeguridad ag = seleccionada.getIdAgente();
        boolean estaAgente = false;
        for (int i = 0; i < cboAgente.getItemCount(); i++) {
            if (cboAgente.getItemAt(i).getIdAgente().equals(ag.getIdAgente())) {
                cboAgente.setSelectedIndex(i);
                estaAgente = true;
                break;
            }
        }
        if (!estaAgente) {
            cboAgente.addItem(ag);
            cboAgente.setSelectedItem(ag);
        }
        txtFecha.setText(fmtFecha.format(seleccionada.getFecha()));
        txtHoraInicio.setText(seleccionada.getHoraInicio() == null ? "" : fmtHora.format(seleccionada.getHoraInicio()));
        txtHoraFin.setText(seleccionada.getHoraFin() == null ? "" : fmtHora.format(seleccionada.getHoraFin()));
        cboEstado.setSelectedItem(seleccionada.getEstado());
    }

    private void limpiar() {
        seleccionada = null;
        tabla.clearSelection();
        txtFecha.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        cboEstado.setSelectedIndex(0);
        cargarPuestos();
        cargarAgentes();
    }

    private void guardar() {
        PuestoServicio puesto = (PuestoServicio) cboPuesto.getSelectedItem();
        AgenteSeguridad agente = (AgenteSeguridad) cboAgente.getSelectedItem();
        String fechaTxt = txtFecha.getText().trim();
        String iniTxt = txtHoraInicio.getText().trim();
        String finTxt = txtHoraFin.getText().trim();
        String estado = (String) cboEstado.getSelectedItem();

        // Validaciones básicas
        if (puesto == null) {
            aviso("Selecciona un puesto (debe estar ACTIVO).");
            return;
        }
        if (agente == null) {
            aviso("Selecciona un agente (no puede estar INACTIVO).");
            return;
        }
        Date fecha;
        try {
            fecha = fmtFecha.parse(fechaTxt);
        } catch (ParseException ex) {
            aviso("La fecha es obligatoria y debe tener el formato aaaa-mm-dd (ej. 2026-10-02).");
            return;
        }

        // Horas: se escriben como HH:mm y se combinan con la fecha de la ronda
        Date inicio = null, fin = null;
        try {
            if (!iniTxt.isEmpty()) {
                inicio = fmtFechaHora.parse(fechaTxt + " " + iniTxt);
            }
            if (!finTxt.isEmpty()) {
                fin = fmtFechaHora.parse(fechaTxt + " " + finTxt);
            }
        } catch (ParseException ex) {
            aviso("Las horas deben tener el formato HH:mm de 24 horas (ej. 22:30).");
            return;
        }
        if (fin != null && inicio == null) {
            aviso("No puede haber hora de fin sin hora de inicio.");
            return;
        }
        // Ronda nocturna: si la hora de fin es menor o igual al inicio, termina al día siguiente
        if (inicio != null && fin != null && !fin.after(inicio)) {
            Calendar c = Calendar.getInstance();
            c.setTime(fin);
            c.add(Calendar.DAY_OF_MONTH, 1);
            fin = c.getTime();
        }

        // Reglas según el estado
        switch (estado) {
            case "PROGRAMADA" -> {
                if (inicio != null || fin != null) {
                    aviso("Una ronda PROGRAMADA no lleva hora de inicio ni de fin.");
                    return;
                }
            }
            case "EN_CURSO" -> {
                if (inicio == null || fin != null) {
                    aviso("Una ronda EN_CURSO necesita hora de inicio y no puede tener hora de fin.");
                    return;
                }
            }
            case "COMPLETADA" -> {
                if (inicio == null || fin == null) {
                    aviso("Una ronda COMPLETADA necesita hora de inicio y hora de fin.");
                    return;
                }
            }
            default -> {
                // INCUMPLIDA: las horas son opcionales
            }
        }

        try {
            Ronda r = (seleccionada != null) ? seleccionada : new Ronda();
            r.setIdPuesto(puesto);
            r.setIdAgente(agente);
            r.setFecha(fecha);
            r.setHoraInicio(inicio);
            r.setHoraFin(fin);
            r.setEstado(estado);

            if (seleccionada == null) {
                rondaCtrl.create(r);
            } else {
                rondaCtrl.edit(r);
            }
            JOptionPane.showMessageDialog(this, "Ronda guardada correctamente.");
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (seleccionada == null) {
            aviso("Selecciona una ronda de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la ronda #" + seleccionada.getIdRonda() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            rondaCtrl.destroy(seleccionada.getIdRonda());
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar (puede tener registros de puntos de control relacionados): " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }
}