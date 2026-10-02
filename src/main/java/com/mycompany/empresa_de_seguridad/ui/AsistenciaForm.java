package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.AsistenciaJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.TurnoJpaController;
import com.mycompany.empresa_de_seguridad.model.Asistencia;
import com.mycompany.empresa_de_seguridad.model.Turno;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * CRUD de Asistencia. Tabla: Asistencia(IdAsistencia, IdTurno, Fecha,
 * HoraEntrada datetime NULL, HoraSalida datetime NULL, Estado, Observacion NULL).
 */
public class AsistenciaForm extends JFrame {

    // AJUSTAR a los valores de tu restricción CHECK de Asistencia.Estado
    private static final String[] ESTADOS = {"PRESENTE", "TARDE", "AUSENTE"};

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final AsistenciaJpaController asistCtrl = new AsistenciaJpaController(emf);
    private final TurnoJpaController turnoCtrl = new TurnoJpaController(emf);

    private final SimpleDateFormat fmtFecha = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat fmtFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final JComboBox<Turno> cmbTurno = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(10);
    private final JTextField txtEntrada = new JTextField(14);
    private final JTextField txtSalida = new JTextField(14);
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);
    private final JTextField txtObservacion = new JTextField(30);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Turno", "Fecha", "Entrada", "Salida", "Estado", "Observación"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private List<Asistencia> asistencias = new ArrayList<>();
    private Asistencia seleccionada = null;

    public AsistenciaForm() {
        setTitle("Gestión de Asistencia");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(900, 540);
        setLocationRelativeTo(null);
        construirUI();
        cargarCombo();
        cargarTabla();
    }

    private void construirUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;

        agregarFila(form, c, 0, "Turno:", cmbTurno);
        agregarFila(form, c, 1, "Fecha (aaaa-MM-dd):", txtFecha);
        agregarFila(form, c, 2, "Entrada real (aaaa-MM-dd HH:mm):", txtEntrada);
        agregarFila(form, c, 3, "Salida real (aaaa-MM-dd HH:mm):", txtSalida);
        agregarFila(form, c, 4, "Estado:", cmbEstado);
        agregarFila(form, c, 5, "Observación:", txtObservacion);

        cmbTurno.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Turno) {
                    Turno t = (Turno) value;
                    setText(textoTurno(t));
                }
                return this;
            }
        });

        // Al elegir un turno sin registro seleccionado, sugiere la fecha del turno
        cmbTurno.addActionListener(e -> {
            Turno t = (Turno) cmbTurno.getSelectedItem();
            if (t != null && seleccionada == null && txtFecha.getText().isBlank()) {
                txtFecha.setText(fmtFecha.format(t.getFecha()));
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

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnCerrar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(form, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

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

    private String textoTurno(Turno t) {
        return "Turno #" + t.getIdTurno() + " - " + t.getIdAgente().getNombre() + " "
                + t.getIdAgente().getApellido() + " - " + fmtFecha.format(t.getFecha());
    }

    private void cargarCombo() {
        cmbTurno.setModel(new DefaultComboBoxModel<>(
                turnoCtrl.findTurnoEntities().toArray(new Turno[0])));
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        asistencias = asistCtrl.findAsistenciaEntities();
        for (Asistencia a : asistencias) {
            modelo.addRow(new Object[]{
                a.getIdAsistencia(),
                "#" + a.getIdTurno().getIdTurno(),
                fmtFecha.format(a.getFecha()),
                a.getHoraEntrada() == null ? "" : fmtFechaHora.format(a.getHoraEntrada()),
                a.getHoraSalida() == null ? "" : fmtFechaHora.format(a.getHoraSalida()),
                a.getEstado(),
                a.getObservacion() == null ? "" : a.getObservacion()
            });
        }
    }

    private void cargarSeleccion(int fila) {
        seleccionada = asistencias.get(fila);
        for (int i = 0; i < cmbTurno.getItemCount(); i++) {
            if (cmbTurno.getItemAt(i).getIdTurno().equals(seleccionada.getIdTurno().getIdTurno())) {
                cmbTurno.setSelectedIndex(i);
                break;
            }
        }
        txtFecha.setText(fmtFecha.format(seleccionada.getFecha()));
        txtEntrada.setText(seleccionada.getHoraEntrada() == null ? ""
                : fmtFechaHora.format(seleccionada.getHoraEntrada()));
        txtSalida.setText(seleccionada.getHoraSalida() == null ? ""
                : fmtFechaHora.format(seleccionada.getHoraSalida()));
        cmbEstado.setSelectedItem(seleccionada.getEstado());
        txtObservacion.setText(seleccionada.getObservacion() == null ? "" : seleccionada.getObservacion());
    }

    private void limpiar() {
        seleccionada = null;
        tabla.clearSelection();
        txtFecha.setText("");
        txtEntrada.setText("");
        txtSalida.setText("");
        txtObservacion.setText("");
        cmbEstado.setSelectedIndex(0);
        if (cmbTurno.getItemCount() > 0) {
            cmbTurno.setSelectedIndex(0);
        }
    }

    private Date parsear(SimpleDateFormat f, String texto) throws Exception {
        f.setLenient(false);
        return f.parse(texto.trim());
    }

    private void guardar() {
        Turno turno = (Turno) cmbTurno.getSelectedItem();
        if (turno == null) {
            aviso("Selecciona un turno.");
            return;
        }

        Date fecha;
        try {
            fecha = parsear(fmtFecha, txtFecha.getText());
        } catch (Exception ex) {
            aviso("La fecha debe tener formato aaaa-MM-dd.");
            return;
        }

        // Entrada y salida son opcionales (columnas NULL)
        Date entrada = null, salida = null;
        try {
            if (!txtEntrada.getText().isBlank()) {
                entrada = parsear(fmtFechaHora, txtEntrada.getText());
            }
            if (!txtSalida.getText().isBlank()) {
                salida = parsear(fmtFechaHora, txtSalida.getText());
            }
        } catch (Exception ex) {
            aviso("Entrada y salida deben tener formato aaaa-MM-dd HH:mm (o déjalas vacías).");
            return;
        }
        if (entrada != null && salida != null && !salida.after(entrada)) {
            aviso("La salida real debe ser posterior a la entrada real.");
            return;
        }
        if (txtObservacion.getText().length() > 250) {
            aviso("La observación no puede pasar de 250 caracteres.");
            return;
        }

        try {
            Asistencia a = (seleccionada == null) ? new Asistencia() : seleccionada;
            a.setIdTurno(turno);
            a.setFecha(fecha);
            a.setHoraEntrada(entrada);
            a.setHoraSalida(salida);
            a.setEstado((String) cmbEstado.getSelectedItem());
            String obs = txtObservacion.getText().trim();
            a.setObservacion(obs.isEmpty() ? null : obs);

            if (seleccionada == null) {
                asistCtrl.create(a);
            } else {
                asistCtrl.edit(a);
            }
            cargarTabla();
            limpiar();
            JOptionPane.showMessageDialog(this, "Asistencia guardada correctamente.");
        } catch (Exception ex) {
            aviso("No se pudo guardar la asistencia: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionada == null) {
            aviso("Selecciona un registro de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar el registro seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            asistCtrl.destroy(seleccionada.getIdAsistencia());
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