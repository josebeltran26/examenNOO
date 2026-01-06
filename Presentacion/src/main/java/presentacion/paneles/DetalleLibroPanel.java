package presentacion.paneles;

import presentacion.control.ControlPresentacion;

import dtos.LibroDTO;
import javax.swing.*;
import java.awt.*;
import presentacion.util.ImageLoader;

public class DetalleLibroPanel extends JPanel {
    private ControlPresentacion control;
    private LibroDTO libro;
    private JLabel lblTitulo;
    private JLabel lblAutor;
    private JLabel lblPrecio;
    private JLabel lblStock;
    private JLabel lblImagen;
    private JSpinner spnCantidad;
    private JButton btnAgregarCarrito;
    private JButton btnVolver;

    public DetalleLibroPanel(ControlPresentacion control) {
        this.control = control;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Panel superior - Imagen
        JPanel panelImagen = new JPanel();
        lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(200, 300));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelImagen.add(lblImagen);
        // Panel central - Detalles
        JPanel panelDetalles = new JPanel(new GridLayout(4, 2, 10, 10));
        panelDetalles.add(new JLabel("Título:"));
        lblTitulo = new JLabel();
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelDetalles.add(lblTitulo);
        panelDetalles.add(new JLabel("Autor:"));
        lblAutor = new JLabel();
        panelDetalles.add(lblAutor);
        panelDetalles.add(new JLabel("Precio:"));
        lblPrecio = new JLabel();
        panelDetalles.add(lblPrecio);
        panelDetalles.add(new JLabel("Stock disponible:"));
        lblStock = new JLabel();
        panelDetalles.add(lblStock);
        // Panel inferior - Acciones
        JPanel panelAcciones = new JPanel(new FlowLayout());
        panelAcciones.add(new JLabel("Cantidad:"));
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        panelAcciones.add(spnCantidad);
        btnAgregarCarrito = new JButton("Agregar al Carrito");
        btnAgregarCarrito.addActionListener(e -> agregarAlCarrito());
        panelAcciones.add(btnAgregarCarrito);
        btnVolver = new JButton("Volver al Catálogo");
        btnVolver.addActionListener(e -> volver());
        panelAcciones.add(btnVolver);
        add(panelImagen, BorderLayout.NORTH);
        add(panelDetalles, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    public void mostrarDetalles(LibroDTO libro) {
        this.libro = libro;
        lblTitulo.setText(libro.getTitulo());
        lblAutor.setText(libro.getAutor());
        lblPrecio.setText(String.format("$%.2f", libro.getPrecio()));
        lblStock.setText(String.valueOf(libro.getStock()));
        // Actualizar imagen (si existe)
        if (libro.getImagenUrl() != null && !libro.getImagenUrl().isEmpty()) {
            lblImagen.setIcon(ImageLoader.cargarDesdeURL(libro.getImagenUrl(), 200, 300));
            lblImagen.setText("");
        } else {
            lblImagen.setIcon(ImageLoader.crearPlaceholder(200, 300));
            lblImagen.setText("");
        }
        // Habilitar/deshabilitar según stock
        boolean hayStock = libro.getStock() > 0;
        btnAgregarCarrito.setEnabled(hayStock);
        spnCantidad.setEnabled(hayStock);
        if (!hayStock) {
            lblStock.setText("AGOTADO");
            lblStock.setForeground(Color.RED);
        }
    }

    private void agregarAlCarrito() {
        if (libro == null)
            return;
        int cantidad = (Integer) spnCantidad.getValue();
        if (cantidad > libro.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "No hay suficiente stock disponible",
                    "Stock insuficiente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Agregar al carrito a través del control
        // control.agregarAlCarrito(libro, cantidad);
        JOptionPane.showMessageDialog(this,
                String.format("%d x %s agregado al carrito", cantidad, libro.getTitulo()),
                "Agregado al carrito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void volver() {
        control.mostrarCatalogo();
    }
}