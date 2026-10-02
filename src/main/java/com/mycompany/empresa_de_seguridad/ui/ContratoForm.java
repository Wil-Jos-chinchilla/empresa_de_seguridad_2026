
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

// * @author josei

package com.mycompany.empresa_de_seguridad.ui;

import com.mycompany.empresa_de_seguridad.model.Cliente;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import com.mycompany.empresa_de_seguridad.model.PlanServicio;
import com.mycompany.empresa_de_seguridad.jpacontroller.ClienteJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.ContratoJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.PlanServicioJpaController;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ContratoForm extends JFrame {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final ContratoJpaController ctrl = new ContratoJpaController(emf);
    private final ClienteJpaController clienteCtrl = new ClienteJpaController(emf);
    private final PlanServicioJpaController planCtrl = new PlanServicioJpaController(emf);

    private final JComboBox<Cliente> cmbCliente = new JComboBox<>();
    private final JComboBox<PlanServicio> cmbPlan = new JComboBox<>();
    private final JSpinner spInicio = crearSpinnerFecha();
    private final JSpinner spFin = crearSpinnerFecha();
    private final JTextField txtCosto = new JTextField(12);
    // Valores exactos de CK_Contrato_Estado
    private final JComboBox<String> cmbEstado =
            new JComboBox<>(new String[]{"ACTIVO", "SUSPENDIDO", "FINALIZADO"});

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Cliente", "Plan", "Inicio", "Fin", "Estado", "Costo mensual"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final List<Contrato> contratos = new ArrayList<>();
    private Contrato seleccionado = null;
    private boolean cargando = false;

    public ContratoForm() {
        super("Gestión de Contratos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        armarUI();
        cargarCombos();
        cargarTabla();
        pack();
        setLocationRelativeTo(null);
    }

    // ---------- Etiquetas de 
    private String etiquetaCliente(Cliente c) {
        return "#" + c.getIdCliente() + " - " + c.getNombre();
    }

    private String etiquetaPlan(PlanServicio p) {
        return p.getNombrePlan() + " (Q" + p.getPrecioMensual() + ")";
    }

    // ---------- UI ----------
    private static JSpinner crearSpinnerFecha() {
        JSpinner sp = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        sp.setEditor(new JSpinner.DateEditor(sp, "yyyy-MM-dd"));
        return sp;
    }

    private void armarUI() {
        cmbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                setText(v instanceof Cliente ? etiquetaCliente((Cliente) v) : "— Selecciona un cliente —");
                return this;
            }
        });
        cmbPlan.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(l, v, i, s, f);
                setText(v instanceof PlanServicio ? etiquetaPlan((PlanServicio) v) : "— Selecciona un plan —");
                return this;
            }
        });
        // Al elegir un plan en un contrato nuevo, sugiere su precio mensual
        cmbPlan.addActionListener(e -> {
            PlanServicio p = (PlanServicio) cmbPlan.getSelectedItem();
            if (!cargando && seleccionado == null && p != null) {
                txtCosto.setText(p.getPrecioMensual().toPlainString());
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del contrato"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        String[] etiquetas = {"Cliente *", "Plan *", "Fecha inicio *", "Fecha fin *", "Costo mensual *", "Estado"};
        JComponent[] campos = {cmbCliente, cmbPlan, spInicio, spFin, txtCosto, cmbEstado};
        for (int i = 0; i < etiquetas.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0;
            form.add(new JLabel(etiquetas[i]), g);
            g.gridx = 1; g.weightx = 1;
            form.add(campos[i], g);
        }

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");
        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCerrar.addActionListener(e -> dispose());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnCerrar);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(760, 220));

        setLayout(new BorderLayout());
        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    // ---------- Datos ----------
    private void cargarCombos() {
        cmbCliente.removeAllItems();
        for (Cliente c : clienteCtrl.findClienteEntities()) cmbCliente.addItem(c);
        cmbPlan.removeAllItems();
        for (PlanServicio p : planCtrl.findPlanServicioEntities()) {
            if (p.getEstado()) cmbPlan.addItem(p); // solo planes activos para contratos nuevos
        }
        cmbCliente.setSelectedItem(null);
        cmbPlan.setSelectedItem(null);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        contratos.clear();
        for (Contrato c : ctrl.findContratoEntities()) {
            contratos.add(c);
            modelo.addRow(new Object[]{
                c.getIdContrato(), etiquetaCliente(c.getIdCliente()), c.getIdPlan().getNombrePlan(),
                aLocal(c.getFechaInicio()), aLocal(c.getFechaFin()), c.getEstado(), c.getCostoMensual()});
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        cargando = true;
        seleccionado = contratos.get(fila);
        seleccionarPorId(cmbCliente, seleccionado.getIdCliente().getIdCliente());
        // Si el plan del contrato ya está inactivo, se agrega al combo para poder mostrarlo
        PlanServicio plan = seleccionado.getIdPlan();
        boolean estaEnCombo = false;
        for (int i = 0; i < cmbPlan.getItemCount(); i++) {
            if (cmbPlan.getItemAt(i).getIdPlan().equals(plan.getIdPlan())) {
                cmbPlan.setSelectedIndex(i);
                estaEnCombo = true;
                break;
            }
        }
        if (!estaEnCombo) {
            cmbPlan.addItem(plan);
            cmbPlan.setSelectedItem(plan);
        }
        spInicio.setValue(seleccionado.getFechaInicio());
        spFin.setValue(seleccionado.getFechaFin());
        txtCosto.setText(seleccionado.getCostoMensual().toPlainString());
        cmbEstado.setSelectedItem(seleccionado.getEstado());
        cargando = false;
    }

    private void seleccionarPorId(JComboBox<Cliente> combo, Integer id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdCliente().equals(id)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limpiar() {
        cargando = true;
        seleccionado = null;
        tabla.clearSelection();
        cargarCombos(); // vuelve a dejar solo planes activos
        spInicio.setValue(new Date());
        spFin.setValue(new Date());
        txtCosto.setText("");
        cmbEstado.setSelectedItem("ACTIVO");
        cargando = false;
    }

    // ---------- Fechas ----------
    private static LocalDate aLocal(Date d) {
        return new java.sql.Date(d.getTime()).toLocalDate(); 
    }

    private static Date aDate(LocalDate ld) {
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // ---------- Acciones ----------
    private boolean advertir(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private boolean llenarContrato(Contrato c) {
        Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
        PlanServicio plan = (PlanServicio) cmbPlan.getSelectedItem();
        if (cliente == null) return advertir("Selecciona un cliente.");
        if (plan == null) return advertir("Selecciona un plan.");

        LocalDate ini = aLocal((Date) spInicio.getValue());
        LocalDate fin = aLocal((Date) spFin.getValue());
        if (!fin.isAfter(ini)) return advertir("La fecha fin debe ser posterior a la fecha de inicio.");

        BigDecimal costo;
        try {
            costo = new BigDecimal(txtCosto.getText().trim());
        } catch (NumberFormatException ex) {
            return advertir("El costo mensual debe ser un número (ej. 4500.00).");
        }
        if (costo.compareTo(BigDecimal.ZERO) <= 0) return advertir("El costo mensual debe ser mayor que 0.");
        if (costo.scale() > 2) return advertir("El costo admite máximo 2 decimales.");
        if (costo.compareTo(new BigDecimal("99999999.99")) > 0) return advertir("El costo es demasiado grande.");

        c.setIdCliente(cliente);
        c.setIdPlan(plan);
        c.setFechaInicio(aDate(ini));
        c.setFechaFin(aDate(fin));
        c.setCostoMensual(costo);
        c.setEstado((String) cmbEstado.getSelectedItem());
        return true;
    }

    private void guardar() {
        boolean esNuevo = seleccionado == null;
        Contrato c = esNuevo ? new Contrato() : seleccionado;
        if (!llenarContrato(c)) return;
        try {
            if (esNuevo) ctrl.create(c);
            else ctrl.edit(c);
            JOptionPane.showMessageDialog(this, esNuevo ? "Contrato guardado." : "Contrato actualizado.");
            cargarTabla();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            advertir("Selecciona un contrato de la tabla para eliminar.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar el contrato seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        try {
            ctrl.destroy(seleccionado.getIdContrato());
            cargarTabla();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar. Si el contrato ya tiene puestos o facturas, cámbialo a FINALIZADO en lugar de borrarlo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void dispose() {
        if (emf.isOpen()) emf.close();
        super.dispose();
    }
}