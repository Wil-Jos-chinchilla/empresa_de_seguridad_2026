package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.jpacontroller.AgenteSeguridadJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.ContratoJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.TurnoJpaController;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import com.mycompany.empresa_de_seguridad.model.Contrato;
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
 * CRUD de Turnos. Tabla: Turno(IdTurno, IdAgente, IdContrato, Fecha,
 * HoraEntrada, HoraSalida, Estado).
 */
public class TurnoForm extends JFrame {

    // AJUSTAR a los valores de tu restricción CHECK de Turno.Estado
    private static final String[] ESTADOS = {"PROGRAMADO", "COMPLETADO", "CANCELADO"};

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final TurnoJpaController turnoCtrl = new TurnoJpaController(emf);
    private final AgenteSeguridadJpaController agenteCtrl = new AgenteSeguridadJpaController(emf);
    private final ContratoJpaController contratoCtrl = new ContratoJpaController(emf);

    private final SimpleDateFormat fmtFecha = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm");

    private final JComboBox<AgenteSeguridad> cmbAgente = new JComboBox<>();
    private final JComboBox<Contrato> cmbContrato = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(10);
    private final JTextField txtEntrada = new JTextField(5);
    private final JTextField txtSalida = new JTextField(5);
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Agente", "Contrato", "Fecha", "Entrada", "Salida", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private List<Turno> turnos = new ArrayList<>();
    private Turno seleccionado = null;

    public TurnoForm() {
        setTitle("Gestión de Turnos");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(820, 520);
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

        agregarFila(form, c, 0, "Agente:", cmbAgente);
        agregarFila(form, c, 1, "Contrato:", cmbContrato);
        agregarFila(form, c, 2, "Fecha (aaaa-MM-dd):", txtFecha);
        agregarFila(form, c, 3, "Hora entrada (HH:mm):", txtEntrada);
        agregarFila(form, c, 4, "Hora salida (HH:mm):", txtSalida);
        agregarFila(form, c, 5, "Estado:", cmbEstado);

        cmbAgente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AgenteSeguridad) {
                    AgenteSeguridad a = (AgenteSeguridad) value;
                    setText(a.getIdAgente() + " - " + a.getNombre() + " " + a.getApellido());
                }
                return this;
            }
        });
        cmbContrato.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Contrato) {
                    Contrato ct = (Contrato) value;
                    setText("Contrato #" + ct.getIdContrato());
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

    private void cargarCombos() {
        cmbAgente.setModel(new DefaultComboBoxModel<>(
                agenteCtrl.findAgenteSeguridadEntities().toArray(new AgenteSeguridad[0])));
        cmbContrato.setModel(new DefaultComboBoxModel<>(
                contratoCtrl.findContratoEntities().toArray(new Contrato[0])));
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        turnos = turnoCtrl.findTurnoEntities();
        for (Turno t : turnos) {
            AgenteSeguridad a = t.getIdAgente();
            modelo.addRow(new Object[]{
                t.getIdTurno(),
                a.getNombre() + " " + a.getApellido(),
                "#" + t.getIdContrato().getIdContrato(),
                fmtFecha.format(t.getFecha()),
                fmtHora.format(t.getHoraEntrada()),
                fmtHora.format(t.getHoraSalida()),
                t.getEstado()
            });
        }
    }

    private void cargarSeleccion(int fila) {
        seleccionado = turnos.get(fila);
        cmbAgente.setSelectedItem(buscarAgente(seleccionado.getIdAgente().getIdAgente()));
        cmbContrato.setSelectedItem(buscarContrato(seleccionado.getIdContrato().getIdContrato()));
        txtFecha.setText(fmtFecha.format(seleccionado.getFecha()));
        txtEntrada.setText(fmtHora.format(seleccionado.getHoraEntrada()));
        txtSalida.setText(fmtHora.format(seleccionado.getHoraSalida()));
        cmbEstado.setSelectedItem(seleccionado.getEstado());
    }

    private AgenteSeguridad buscarAgente(Integer id) {
        for (int i = 0; i < cmbAgente.getItemCount(); i++) {
            if (cmbAgente.getItemAt(i).getIdAgente().equals(id)) {
                return cmbAgente.getItemAt(i);
            }
        }
        return null;
    }

    private Contrato buscarContrato(Integer id) {
        for (int i = 0; i < cmbContrato.getItemCount(); i++) {
            if (cmbContrato.getItemAt(i).getIdContrato().equals(id)) {
                return cmbContrato.getItemAt(i);
            }
        }
        return null;
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtFecha.setText("");
        txtEntrada.setText("");
        txtSalida.setText("");
        if (cmbAgente.getItemCount() > 0) {
            cmbAgente.setSelectedIndex(0);
        }
        if (cmbContrato.getItemCount() > 0) {
            cmbContrato.setSelectedIndex(0);
        }
        cmbEstado.setSelectedIndex(0);
    }

    private void guardar() {
        AgenteSeguridad agente = (AgenteSeguridad) cmbAgente.getSelectedItem();
        Contrato contrato = (Contrato) cmbContrato.getSelectedItem();
        if (agente == null || contrato == null) {
            aviso("Selecciona un agente y un contrato.");
            return;
        }

        Date fecha, entrada, salida;
        try {
            fmtFecha.setLenient(false);
            fmtHora.setLenient(false);
            fecha = fmtFecha.parse(txtFecha.getText().trim());
            entrada = fmtHora.parse(txtEntrada.getText().trim());
            salida = fmtHora.parse(txtSalida.getText().trim());
        } catch (Exception ex) {
            aviso("Revisa los formatos: fecha aaaa-MM-dd y horas HH:mm (24 horas).");
            return;
        }
        // Se permite salida < entrada (turno nocturno que cruza medianoche), pero no iguales.
        if (entrada.equals(salida)) {
            aviso("La hora de entrada y la de salida no pueden ser iguales.");
            return;
        }

        try {
            Turno t = (seleccionado == null) ? new Turno() : seleccionado;
            t.setIdAgente(agente);
            t.setIdContrato(contrato);
            t.setFecha(fecha);
            t.setHoraEntrada(entrada);
            t.setHoraSalida(salida);
            t.setEstado((String) cmbEstado.getSelectedItem());

            if (seleccionado == null) {
                turnoCtrl.create(t);
            } else {
                turnoCtrl.edit(t);
            }
            cargarTabla();
            limpiar();
            JOptionPane.showMessageDialog(this, "Turno guardado correctamente.");
        } catch (Exception ex) {
            aviso("No se pudo guardar el turno: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso("Selecciona un turno de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar el turno seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            turnoCtrl.destroy(seleccionado.getIdTurno());
            cargarTabla();
            limpiar();
        } catch (Exception ex) {
            aviso("No se pudo eliminar (puede tener asistencias relacionadas): " + ex.getMessage());
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}