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
import com.mycompany.empresa_de_seguridad.jpacontroller.ContratoJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.IncidenteJpaController;
import com.mycompany.empresa_de_seguridad.model.AgenteSeguridad;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import com.mycompany.empresa_de_seguridad.model.Incidente;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
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
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * CRUD de Incidentes. Tabla: Incidente(IdIncidente, IdContrato, IdAgente,
 * FechaHora, TipoIncidente, Descripcion, NivelUrgencia, Latitud, Longitud,
 * Estado, FechaCierre NULL, ObservacionCierre NULL).
 */
public class IncidenteForm extends JFrame {

    // AJUSTAR a los valores de tus restricciones CHECK
    private static final String[] URGENCIAS = {"BAJA", "MEDIA", "ALTA", "CRITICA"};
    private static final String[] ESTADOS = {"ABIERTO", "EN_PROCESO", "CERRADO"};

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final IncidenteJpaController incCtrl = new IncidenteJpaController(emf);
    private final AgenteSeguridadJpaController agenteCtrl = new AgenteSeguridadJpaController(emf);
    private final ContratoJpaController contratoCtrl = new ContratoJpaController(emf);

    private final SimpleDateFormat fmtFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final JComboBox<Contrato> cmbContrato = new JComboBox<>();
    private final JComboBox<AgenteSeguridad> cmbAgente = new JComboBox<>();
    private final JTextField txtFechaHora = new JTextField(14);
    private final JTextField txtTipo = new JTextField(25);
    private final JTextField txtDescripcion = new JTextField(35);
    private final JComboBox<String> cmbUrgencia = new JComboBox<>(URGENCIAS);
    private final JTextField txtLatitud = new JTextField(12);
    private final JTextField txtLongitud = new JTextField(12);
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);
    private final JTextField txtFechaCierre = new JTextField(14);
    private final JTextField txtObsCierre = new JTextField(35);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Contrato", "Agente", "Fecha/Hora", "Tipo", "Urgencia", "Estado", "Cierre"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private List<Incidente> incidentes = new ArrayList<>();
    private Incidente seleccionado = null;

    public IncidenteForm() {
        setTitle("Gestión de Incidentes");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(980, 680);
        setLocationRelativeTo(null);
        construirUI();
        cargarCombos();
        cargarTabla();
        limpiar();
    }

    private void construirUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;

        agregarFila(form, c, 0, "Contrato:", cmbContrato);
        agregarFila(form, c, 1, "Agente:", cmbAgente);
        agregarFila(form, c, 2, "Fecha y hora (aaaa-MM-dd HH:mm):", txtFechaHora);
        agregarFila(form, c, 3, "Tipo de incidente:", txtTipo);
        agregarFila(form, c, 4, "Descripción:", txtDescripcion);
        agregarFila(form, c, 5, "Nivel de urgencia:", cmbUrgencia);
        agregarFila(form, c, 6, "Latitud:", txtLatitud);
        agregarFila(form, c, 7, "Longitud:", txtLongitud);
        agregarFila(form, c, 8, "Estado:", cmbEstado);
        agregarFila(form, c, 9, "Fecha cierre (aaaa-MM-dd HH:mm):", txtFechaCierre);
        agregarFila(form, c, 10, "Observación de cierre:", txtObsCierre);

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
        cmbContrato.setModel(new DefaultComboBoxModel<>(
                contratoCtrl.findContratoEntities().toArray(new Contrato[0])));
        cmbAgente.setModel(new DefaultComboBoxModel<>(
                agenteCtrl.findAgenteSeguridadEntities().toArray(new AgenteSeguridad[0])));
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        incidentes = incCtrl.findIncidenteEntities();
        for (Incidente i : incidentes) {
            AgenteSeguridad a = i.getIdAgente();
            modelo.addRow(new Object[]{
                i.getIdIncidente(),
                "#" + i.getIdContrato().getIdContrato(),
                a.getNombre() + " " + a.getApellido(),
                fmtFechaHora.format(i.getFechaHora()),
                i.getTipoIncidente(),
                i.getNivelUrgencia(),
                i.getEstado(),
                i.getFechaCierre() == null ? "" : fmtFechaHora.format(i.getFechaCierre())
            });
        }
    }

    private void cargarSeleccion(int fila) {
        seleccionado = incidentes.get(fila);
        for (int i = 0; i < cmbContrato.getItemCount(); i++) {
            if (cmbContrato.getItemAt(i).getIdContrato().equals(seleccionado.getIdContrato().getIdContrato())) {
                cmbContrato.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < cmbAgente.getItemCount(); i++) {
            if (cmbAgente.getItemAt(i).getIdAgente().equals(seleccionado.getIdAgente().getIdAgente())) {
                cmbAgente.setSelectedIndex(i);
                break;
            }
        }
        txtFechaHora.setText(fmtFechaHora.format(seleccionado.getFechaHora()));
        txtTipo.setText(seleccionado.getTipoIncidente());
        txtDescripcion.setText(seleccionado.getDescripcion());
        cmbUrgencia.setSelectedItem(seleccionado.getNivelUrgencia());
        txtLatitud.setText(seleccionado.getLatitud() == null ? "" : seleccionado.getLatitud().toPlainString());
        txtLongitud.setText(seleccionado.getLongitud() == null ? "" : seleccionado.getLongitud().toPlainString());
        cmbEstado.setSelectedItem(seleccionado.getEstado());
        txtFechaCierre.setText(seleccionado.getFechaCierre() == null ? ""
                : fmtFechaHora.format(seleccionado.getFechaCierre()));
        txtObsCierre.setText(seleccionado.getObservacionCierre() == null ? ""
                : seleccionado.getObservacionCierre());
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtFechaHora.setText(fmtFechaHora.format(new Date()));
        txtTipo.setText("");
        txtDescripcion.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtFechaCierre.setText("");
        txtObsCierre.setText("");
        cmbUrgencia.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        if (cmbContrato.getItemCount() > 0) {
            cmbContrato.setSelectedIndex(0);
        }
        if (cmbAgente.getItemCount() > 0) {
            cmbAgente.setSelectedIndex(0);
        }
    }

    private Date parsear(String texto) throws Exception {
        fmtFechaHora.setLenient(false);
        return fmtFechaHora.parse(texto.trim());
    }

    private void guardar() {
        Contrato contrato = (Contrato) cmbContrato.getSelectedItem();
        AgenteSeguridad agente = (AgenteSeguridad) cmbAgente.getSelectedItem();
        String tipo = txtTipo.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String latTxt = txtLatitud.getText().trim();
        String lonTxt = txtLongitud.getText().trim();
        String obs = txtObsCierre.getText().trim();
        String estado = (String) cmbEstado.getSelectedItem();

        if (contrato == null || agente == null) {
            aviso("Selecciona un contrato y un agente.");
            return;
        }
        if (tipo.isEmpty() || tipo.length() > 100) {
            aviso("El tipo de incidente es obligatorio (máximo 100 caracteres).");
            return;
        }
        if (desc.isEmpty() || desc.length() > 500) {
            aviso("La descripción es obligatoria (máximo 500 caracteres).");
            return;
        }
        if (obs.length() > 500) {
            aviso("La observación de cierre admite máximo 500 caracteres.");
            return;
        }

        Date fechaHora;
        try {
            fechaHora = parsear(txtFechaHora.getText());
        } catch (Exception ex) {
            aviso("La fecha y hora deben tener formato aaaa-MM-dd HH:mm.");
            return;
        }

        Date fechaCierre = null;
        try {
            if (!txtFechaCierre.getText().isBlank()) {
                fechaCierre = parsear(txtFechaCierre.getText());
            }
        } catch (Exception ex) {
            aviso("La fecha de cierre debe tener formato aaaa-MM-dd HH:mm (o déjala vacía).");
            return;
        }
        if (fechaCierre != null && fechaCierre.before(fechaHora)) {
            aviso("La fecha de cierre no puede ser anterior a la del incidente.");
            return;
        }
        if ("CERRADO".equals(estado) && fechaCierre == null) {
            aviso("Un incidente CERRADO necesita fecha de cierre.");
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
            Incidente i = (seleccionado == null) ? new Incidente() : seleccionado;
            i.setIdContrato(contrato);
            i.setIdAgente(agente);
            i.setFechaHora(fechaHora);
            i.setTipoIncidente(tipo);
            i.setDescripcion(desc);
            i.setNivelUrgencia((String) cmbUrgencia.getSelectedItem());
            i.setLatitud(lat);
            i.setLongitud(lon);
            i.setEstado(estado);
            i.setFechaCierre(fechaCierre);
            i.setObservacionCierre(obs.isEmpty() ? null : obs);

            if (seleccionado == null) {
                incCtrl.create(i);
            } else {
                incCtrl.edit(i);
            }
            cargarTabla();
            limpiar();
            JOptionPane.showMessageDialog(this, "Incidente guardado correctamente.");
        } catch (Exception ex) {
            aviso("No se pudo guardar el incidente: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            aviso("Selecciona un incidente de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar el incidente seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            incCtrl.destroy(seleccionado.getIdIncidente());
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