package presentacion.paneles;

import dtos.DetalleOrdenDTO;
import dtos.LibroDTO;
import presentacion.control.ControlPresentacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import presentacion.util.ImageLoader;

public class CatalogoPanel extends JPanel {
    private final ControlPresentacion control;
    private JPanel panelLibros;
    private Map<Integer, Integer> carrito;

    public CatalogoPanel(ControlPresentacion control) {
        this.control = control;
        this.carrito = new HashMap<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Título
        JLabel titulo = new JLabel("Lista de Libros Disponibles");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);
        // Panel de libros con scroll
        panelLibros = new JPanel();
        panelLibros.setLayout(new BoxLayout(panelLibros, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panelLibros);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.CENTER);
        // Botón de carrito (si hay sesión)
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVerCarrito = new JButton("Ver Carrito (" + carrito.size() + " items)");
        btnVerCarrito.setFont(new Font("Arial", Font.BOLD, 12));
        btnVerCarrito.addActionListener(e -> verCarrito());
        panelInferior.add(btnVerCarrito);
        add(panelInferior, BorderLayout.SOUTH);
        cargarCatalogo();
    }

    public void cargarCatalogo() {
        panelLibros.removeAll();
        List<LibroDTO> libros = control.obtenerCatalogo();
        if (libros.isEmpty()) {
            JLabel lblSinProductos = new JLabel("No hay libros disponibles en este momento");
            lblSinProductos.setHorizontalAlignment(SwingConstants.CENTER);
            lblSinProductos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSinProductos.setForeground(new Color(127, 140, 141));
            panelLibros.add(lblSinProductos);
        } else {
            for (LibroDTO libro : libros) {
                panelLibros.add(crearPanelLibro(libro));
                panelLibros.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        panelLibros.revalidate();
        panelLibros.repaint();
    }

    private JPanel crearPanelLibro(LibroDTO libro) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.setBackground(new Color(250, 250, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Imagen del libro
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(70, 100));
        if (libro.getImagenUrl() != null && !libro.getImagenUrl().isEmpty()) {
            lblImagen.setIcon(ImageLoader.cargarDesdeURL(libro.getImagenUrl(), 70, 100));
        } else {
            lblImagen.setIcon(ImageLoader.crearPlaceholder(70, 100));
        }
        panel.add(lblImagen, BorderLayout.WEST);

        // Info del libro
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        JLabel lblTitulo = new JLabel(libro.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel lblAutor = new JLabel("por " + libro.getAutor());
        lblAutor.setFont(new Font("Arial", Font.ITALIC, 11));
        lblAutor.setForeground(Color.GRAY);
        JLabel lblPrecio = new JLabel("$" + String.format("%.2f", libro.getPrecio()));
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrecio.setForeground(new Color(0, 100, 200));
        JLabel lblStock = new JLabel("Stock: " + libro.getStock());
        lblStock.setFont(new Font("Arial", Font.PLAIN, 10));
        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfo.add(lblAutor);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblPrecio);
        panelInfo.add(lblStock);
        panel.add(panelInfo, BorderLayout.CENTER);
        // Panel de acciones
        JPanel panelAcciones = new JPanel();
        panelAcciones.setLayout(new BoxLayout(panelAcciones, BoxLayout.Y_AXIS));
        panelAcciones.setOpaque(false);
        if (control.haySesionActiva()) {
            JSpinner spinnerCant = new JSpinner(new SpinnerNumberModel(1, 1, libro.getStock(), 1));
            spinnerCant.setMaximumSize(new Dimension(60, 25));
            JButton btnAgregar = new JButton("Añadir al Carrito");
            btnAgregar.setMaximumSize(new Dimension(150, 30));
            btnAgregar.addActionListener(e -> agregarAlCarrito(libro, (Integer) spinnerCant.getValue()));
            panelAcciones.add(spinnerCant);
            panelAcciones.add(Box.createRigidArea(new Dimension(0, 5)));
            panelAcciones.add(btnAgregar);
            panelAcciones.add(Box.createRigidArea(new Dimension(0, 5)));
            JButton btnDetalle = new JButton("Ver Detalles");
            btnDetalle.setMaximumSize(new Dimension(150, 30));
            btnDetalle.addActionListener(e -> control.mostrarDetalleLibro(libro));
            panelAcciones.add(btnDetalle);
            panelAcciones.add(Box.createRigidArea(new Dimension(0, 5)));

        } else {
            JButton btnAgregar = new JButton("Añadir al Carrito");
            btnAgregar.setMaximumSize(new Dimension(150, 30));
            btnAgregar.addActionListener(e -> {
                JOptionPane.showMessageDialog(this,
                        "Necesitas iniciar sesión para agregar libros al carrito",
                        "Inicia sesión",
                        JOptionPane.INFORMATION_MESSAGE);
                control.mostrarLogin();
            });
            panelAcciones.add(btnAgregar);
        }
        panel.add(panelAcciones, BorderLayout.EAST);
        return panel;
    }

    private void agregarAlCarrito(LibroDTO libro, int cantidad) {
        if (cantidad > libro.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "Lo sentimos, solo quedan " + libro.getStock() + " ejemplares disponibles",
                    "Stock limitado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        carrito.put(libro.getIdLibro(), carrito.getOrDefault(libro.getIdLibro(), 0) + cantidad);
        actualizarContadorCarrito();
        JOptionPane.showMessageDialog(this,
                "Libro agregado a tu carrito exitosamente",
                "Agregado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void verCarrito() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El carrito está vacío",
                    "Carrito",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        control.mostrarCarrito(carrito, this);
    }

    private void actualizarContadorCarrito() {
        // Actualizar el contador en el botón
        Component[] components = ((JPanel) getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JButton) {
            ((JButton) components[0]).setText("Ver Carrito (" + carrito.size() + " items)");
        }
    }

    public void limpiarCarrito() {
        carrito.clear();
        actualizarContadorCarrito();
    }
}