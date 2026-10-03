/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.empresa_de_seguridad.ui;

/**
 *
 * @author JOSUE
 */

import com.mycompany.empresa_de_seguridad.jpacontroller.ContratoJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.DetalleFacturaJpaController;
import com.mycompany.empresa_de_seguridad.jpacontroller.FacturaJpaController;
import com.mycompany.empresa_de_seguridad.model.Contrato;
import com.mycompany.empresa_de_seguridad.model.DetalleFactura;
import com.mycompany.empresa_de_seguridad.model.Factura;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
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
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * CRUD de Facturación (encabezado + detalle).
 * Factura(IdFactura, IdContrato, FechaFactura, Subtotal, Total, Estado)
 * DetalleFactura(IdDetalle, IdFactura, Descripcion, Cantidad, PrecioUnitario, Subtotal)
 */
public class FacturaForm extends JFrame {

    // AJUSTAR a los valores de tu restricción CHECK de Factura.Estado
    private static final String[] ESTADOS = {"PENDIENTE", "PAGADA", "ANULADA"};

    // IVA de Guatemala 12%. Si tu Total NO lleva impuesto, ponlo en "0".
    private static final BigDecimal IVA = new BigDecimal("0.12");

    /** Línea de detalle en memoria (aún no guardada). */
    private static class Linea {
        String descripcion;
        int cantidad;
        BigDecimal precio;

        BigDecimal subtotal() {
            return precio.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("empresa_seguridadPU");
    private final FacturaJpaController facturaCtrl = new FacturaJpaController(emf);
    private final DetalleFacturaJpaController detalleCtrl = new DetalleFacturaJpaController(emf);
    private final ContratoJpaController contratoCtrl = new ContratoJpaController(emf);

    private final SimpleDateFormat fmtFecha = new SimpleDateFormat("yyyy-MM-dd");

    private final JComboBox<Contrato> cmbContrato = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(10);
    private final JComboBox<String> cmbEstado = new JComboBox<>(ESTADOS);
    private final JLabel lblSubtotal = new JLabel("0.00");
    private final JLabel lblTotal = new JLabel("0.00");

    private final JTextField txtDescripcion = new JTextField(25);
    private final JTextField txtCantidad = new JTextField(5);
    private final JTextField txtPrecio = new JTextField(8);

    private final DefaultTableModel modeloLineas = new DefaultTableModel(
            new String[]{"Descripción", "Cantidad", "Precio unit.", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaLineas = new JTable(modeloLineas);

    private final DefaultTableModel modeloFacturas = new DefaultTableModel(
            new String[]{"ID", "Contrato", "Fecha", "Subtotal", "Total", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaFacturas = new JTable(modeloFacturas);

    private final List<Linea> lineas = new ArrayList<>();
    private List<Factura> facturas = new ArrayList<>();
    private Factura seleccionada = null;

    public FacturaForm() {
        setTitle("Gestión de Facturación");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(960, 720);
        setLocationRelativeTo(null);
        construirUI();
        cargarContratos();
        cargarTabla();
        limpiar();
    }

    private void construirUI() {
        // ----- Encabezado -----
        JPanel encabezado = new JPanel(new GridBagLayout());
        encabezado.setBorder(BorderFactory.createTitledBorder("Datos de la factura"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        agregarFila(encabezado, c, 0, "Contrato:", cmbContrato);
        agregarFila(encabezado, c, 1, "Fecha (aaaa-MM-dd):", txtFecha);
        agregarFila(encabezado, c, 2, "Estado:", cmbEstado);
        agregarFila(encabezado, c, 3, "Subtotal:", lblSubtotal);
        agregarFila(encabezado, c, 4, "Total (con IVA):", lblTotal);

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

        // ----- Líneas de detalle -----
        JPanel entradaLinea = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        entradaLinea.add(new JLabel("Descripción:"));
        entradaLinea.add(txtDescripcion);
        entradaLinea.add(new JLabel("Cantidad:"));
        entradaLinea.add(txtCantidad);
        entradaLinea.add(new JLabel("Precio:"));
        entradaLinea.add(txtPrecio);
        JButton btnAgregar = new JButton("Agregar línea");
        JButton btnQuitar = new JButton("Quitar línea");
        btnAgregar.addActionListener(e -> agregarLinea());
        btnQuitar.addActionListener(e -> quitarLinea());
        entradaLinea.add(btnAgregar);
        entradaLinea.add(btnQuitar);

        tablaLineas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel panelLineas = new JPanel(new BorderLayout());
        panelLineas.setBorder(BorderFactory.createTitledBorder("Detalle de la factura"));
        panelLineas.add(entradaLinea, BorderLayout.NORTH);
        panelLineas.add(new JScrollPane(tablaLineas), BorderLayout.CENTER);

        // ----- Botones -----
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

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(encabezado, BorderLayout.NORTH);
        superior.add(panelLineas, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        // ----- Lista de facturas -----
        tablaFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaFacturas.getSelectedRow() >= 0) {
                cargarSeleccion(tablaFacturas.getSelectedRow());
            }
        });
        JPanel inferior = new JPanel(new BorderLayout());
        inferior.setBorder(BorderFactory.createTitledBorder("Facturas registradas"));
        inferior.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, superior, inferior);
        split.setResizeWeight(0.65);

        setLayout(new BorderLayout());
        add(split, BorderLayout.CENTER);
    }

    private void agregarFila(JPanel p, GridBagConstraints c, int fila, String texto, Component campo) {
        c.gridy = fila;
        c.gridx = 0;
        p.add(new JLabel(texto), c);
        c.gridx = 1;
        p.add(campo, c);
    }

    private void cargarContratos() {
        cmbContrato.setModel(new DefaultComboBoxModel<>(
                contratoCtrl.findContratoEntities().toArray(new Contrato[0])));
    }

    private void cargarTabla() {
        modeloFacturas.setRowCount(0);
        facturas = facturaCtrl.findFacturaEntities();
        for (Factura f : facturas) {
            modeloFacturas.addRow(new Object[]{
                f.getIdFactura(),
                "#" + f.getIdContrato().getIdContrato(),
                fmtFecha.format(f.getFechaFactura()),
                f.getSubtotal(),
                f.getTotal(),
                f.getEstado()
            });
        }
    }

    private void cargarSeleccion(int fila) {
        seleccionada = facturas.get(fila);
        for (int i = 0; i < cmbContrato.getItemCount(); i++) {
            if (cmbContrato.getItemAt(i).getIdContrato().equals(seleccionada.getIdContrato().getIdContrato())) {
                cmbContrato.setSelectedIndex(i);
                break;
            }
        }
        txtFecha.setText(fmtFecha.format(seleccionada.getFechaFactura()));
        cmbEstado.setSelectedItem(seleccionada.getEstado());

        lineas.clear();
        for (DetalleFactura d : detallesDe(seleccionada.getIdFactura())) {
            Linea l = new Linea();
            l.descripcion = d.getDescripcion();
            l.cantidad = d.getCantidad();
            l.precio = d.getPrecioUnitario();
            lineas.add(l);
        }
        refrescarLineas();
    }

    /** Detalles que pertenecen a una factura. */
    private List<DetalleFactura> detallesDe(Integer idFactura) {
        List<DetalleFactura> resultado = new ArrayList<>();
        for (DetalleFactura d : detalleCtrl.findDetalleFacturaEntities()) {
            if (d.getIdFactura() != null && d.getIdFactura().getIdFactura().equals(idFactura)) {
                resultado.add(d);
            }
        }
        return resultado;
    }

    private void limpiar() {
        seleccionada = null;
        tablaFacturas.clearSelection();
        txtFecha.setText(fmtFecha.format(new Date()));
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        cmbEstado.setSelectedIndex(0);
        if (cmbContrato.getItemCount() > 0) {
            cmbContrato.setSelectedIndex(0);
        }
        lineas.clear();
        refrescarLineas();
    }

    // ---------- Líneas ----------

    private void agregarLinea() {
        String desc = txtDescripcion.getText().trim();
        if (desc.isEmpty() || desc.length() > 250) {
            aviso("La descripción es obligatoria (máximo 250 caracteres).");
            return;
        }
        int cant;
        BigDecimal precio;
        try {
            cant = Integer.parseInt(txtCantidad.getText().trim());
            precio = new BigDecimal(txtPrecio.getText().trim());
        } catch (NumberFormatException ex) {
            aviso("Cantidad debe ser un entero y precio un número (ej. 150.50).");
            return;
        }
        if (cant <= 0) {
            aviso("La cantidad debe ser mayor que 0.");
            return;
        }
        if (precio.compareTo(BigDecimal.ZERO) < 0 || precio.compareTo(new BigDecimal("99999999.99")) > 0) {
            aviso("El precio debe estar entre 0 y 99,999,999.99.");
            return;
        }
        Linea l = new Linea();
        l.descripcion = desc;
        l.cantidad = cant;
        l.precio = precio.setScale(2, RoundingMode.HALF_UP);
        lineas.add(l);
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        refrescarLineas();
    }

    private void quitarLinea() {
        int fila = tablaLineas.getSelectedRow();
        if (fila < 0) {
            aviso("Selecciona una línea del detalle.");
            return;
        }
        lineas.remove(fila);
        refrescarLineas();
    }

    private BigDecimal subtotalActual() {
        BigDecimal suma = BigDecimal.ZERO;
        for (Linea l : lineas) {
            suma = suma.add(l.subtotal());
        }
        return suma.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal totalDe(BigDecimal subtotal) {
        return subtotal.add(subtotal.multiply(IVA)).setScale(2, RoundingMode.HALF_UP);
    }

    private void refrescarLineas() {
        modeloLineas.setRowCount(0);
        for (Linea l : lineas) {
            modeloLineas.addRow(new Object[]{l.descripcion, l.cantidad, l.precio, l.subtotal()});
        }
        BigDecimal sub = subtotalActual();
        lblSubtotal.setText(sub.toPlainString());
        lblTotal.setText(totalDe(sub).toPlainString());
    }

    // ---------- Guardar / Eliminar ----------

    private void guardar() {
        Contrato contrato = (Contrato) cmbContrato.getSelectedItem();
        if (contrato == null) {
            aviso("Selecciona un contrato.");
            return;
        }
        Date fecha;
        try {
            fmtFecha.setLenient(false);
            fecha = fmtFecha.parse(txtFecha.getText().trim());
        } catch (Exception ex) {
            aviso("La fecha debe tener formato aaaa-MM-dd.");
            return;
        }
        if (lineas.isEmpty()) {
            aviso("Agrega al menos una línea al detalle.");
            return;
        }
        BigDecimal subtotal = subtotalActual();
        BigDecimal total = totalDe(subtotal);

        try {
            Factura f;
            if (seleccionada == null) {
                f = new Factura();
                f.setIdContrato(contrato);
                f.setFechaFactura(fecha);
                f.setSubtotal(subtotal);
                f.setTotal(total);
                f.setEstado((String) cmbEstado.getSelectedItem());
                facturaCtrl.create(f);
            } else {
                // Se borran los detalles anteriores y se vuelven a crear con lo que hay en pantalla
                for (DetalleFactura d : detallesDe(seleccionada.getIdFactura())) {
                    detalleCtrl.destroy(d.getIdDetalle());
                }
                f = facturaCtrl.findFactura(seleccionada.getIdFactura());
                f.setIdContrato(contrato);
                f.setFechaFactura(fecha);
                f.setSubtotal(subtotal);
                f.setTotal(total);
                f.setEstado((String) cmbEstado.getSelectedItem());
                facturaCtrl.edit(f);
            }

            for (Linea l : lineas) {
                DetalleFactura d = new DetalleFactura();
                d.setIdFactura(f);
                d.setDescripcion(l.descripcion);
                d.setCantidad(l.cantidad);
                d.setPrecioUnitario(l.precio);
                d.setSubtotal(l.subtotal());
                detalleCtrl.create(d);
            }

            cargarTabla();
            limpiar();
            JOptionPane.showMessageDialog(this, "Factura guardada correctamente.");
        } catch (Exception ex) {
            aviso("No se pudo guardar la factura: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionada == null) {
            aviso("Selecciona una factura de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la factura #" + seleccionada.getIdFactura() + " y todo su detalle?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            for (DetalleFactura d : detallesDe(seleccionada.getIdFactura())) {
                detalleCtrl.destroy(d.getIdDetalle());
            }
            facturaCtrl.destroy(seleccionada.getIdFactura());
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
