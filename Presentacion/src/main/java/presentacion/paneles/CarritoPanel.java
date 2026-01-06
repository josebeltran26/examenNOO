package presentacion.paneles;

import dtos.DetalleOrdenDTO;
import dtos.LibroDTO;
import presentacion.control.ControlPresentacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarritoPanel extends JPanel {
    private final ControlPresentacion control;
    private final Map<Integer, Integer> carrito;
    private final CatalogoPanel catalogoPanel;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;

    public CarritoPanel(ControlPresentacion control, Map<Integer, Integer> carrito, CatalogoPanel catalogoPanel) {
        this.control = control;
        this.carrito = carrito;
        this.catalogoPanel = catalogoPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Título
        JLabel titulo = new JLabel("Resumen de Compra");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);
        // Tabla de items
        String[] columnas = { "Libro", "Cant.", "Subtotal", "Acción" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la columna de acción
            }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        // Columna de botón eliminar
        tabla.getColumn("Acción").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("Eliminar");
            btn.setForeground(Color.RED);
            return btn;
        });
        tabla.getColumn("Acción").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private JButton button;

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                    int column) {
                button = new JButton("Eliminar");
                button.setForeground(Color.RED);
                button.addActionListener(e -> eliminarItem(row));
                return button;
            }
        });
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
        // Panel inferior con total y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        // Total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTotal.setBackground(new Color(240, 240, 240));
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelTotal.add(lblTotal);
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Catálogo");
        btnVolver.addActionListener(e -> volverAlCatalogo());
        JButton btnConfirmar = new JButton("Confirmar Orden");
        btnConfirmar.setBackground(new Color(0, 100, 200));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(e -> confirmarOrden());
        panelBotones.add(btnVolver);
        panelBotones.add(btnConfirmar);
        panelInferior.add(panelTotal, BorderLayout.WEST);
        panelInferior.add(panelBotones, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);
        cargarCarrito();
    }

    private void cargarCarrito() {
        modeloTabla.setRowCount(0);
        double total = 0.0;
        List<LibroDTO> libros = control.obtenerCatalogo();
        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            int idLibro = entry.getKey();
            int cantidad = entry.getValue();
            // Buscar libro
            LibroDTO libro = libros.stream()
                    .filter(l -> l.getIdLibro() == idLibro)
                    .findFirst()
                    .orElse(null);
            if (libro != null) {
                double subtotal = libro.getPrecio() * cantidad;
                total += subtotal;
                modeloTabla.addRow(new Object[] {
                        libro.getTitulo() + " - " + libro.getAutor(),
                        cantidad,
                        "$" + String.format("%.2f", subtotal),
                        "Eliminar"
                });
            }
        }
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void eliminarItem(int row) {
        // Obtener el libro de la fila
        List<Integer> keys = new ArrayList<>(carrito.keySet());
        if (row < keys.size()) {
            carrito.remove(keys.get(row));
            cargarCarrito();
        }
    }

    private void volverAlCatalogo() {
        control.mostrarCatalogo();
    }

    private void confirmarOrden() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tu carrito está vacío. Agrega algunos libros primero",
                    "Carrito vacío",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (control.tienePendiente()) {
            JOptionPane.showMessageDialog(this,
                    "Ya tienes un pedido en proceso. Espera hasta que sea aceptado para crear uno nuevo",
                    "Pedido pendiente",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Crear lista de detalles
        List<DetalleOrdenDTO> detalles = new ArrayList<>();
        List<LibroDTO> libros = control.obtenerCatalogo();
        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            int idLibro = entry.getKey();
            int cantidad = entry.getValue();
            LibroDTO libro = libros.stream()
                    .filter(l -> l.getIdLibro() == idLibro)
                    .findFirst()
                    .orElse(null);
            if (libro != null) {
                DetalleOrdenDTO detalle = new DetalleOrdenDTO();
                detalle.setIdDetalle(libro.getIdLibro());
                detalle.setTituloLibro(libro.getTitulo());
                detalle.setAutorLibro(libro.getAutor());
                detalle.setCantidad(cantidad);
                detalle.setSubtotal(libro.getPrecio() * cantidad);
                detalles.add(detalle);
            }
        }
        // Delegar confirmación al control
        control.mostrarConfirmacionOrden(detalles);
        // Agreguemos metodo limpiarCarrito a ControlPresentacion?
        // O mejor: ControlPresentacion sabe del CatalogoPanel (lo instanció en
        // FrmPrincipal).
        // Pero Control no tiene referencia a CatalogoPanel guardada.
        // Modifiquemos ControlPresentacion para tener referencia a CatalogoPanel.
    }
}