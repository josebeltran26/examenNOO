package presentacion.paneles;

import dtos.OrdenDTO;
import presentacion.control.ControlPresentacion;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JPanel {
    private final ControlPresentacion control;
    private JPanel panelOrdenes;

    public AdminPanel(ControlPresentacion control) {
        this.control = control;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(1, 2, 20, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Panel de gestión de inventario
        add(crearPanelInventario());
        // Panel de órdenes pendientes
        add(crearPanelOrdenes());
    }

    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Gestión de Inventario"));
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelForm.add(new JLabel("Título del libro:"));
        JTextField txtTitulo = new JTextField();
        panelForm.add(txtTitulo);
        panelForm.add(new JLabel("Autor:"));
        JTextField txtAutor = new JTextField();
        panelForm.add(txtAutor);
        panelForm.add(new JLabel("Precio ($):"));
        JTextField txtPrecio = new JTextField();
        panelForm.add(txtPrecio);
        panelForm.add(new JLabel("Stock Inicial:"));
        JTextField txtStock = new JTextField();
        panelForm.add(txtStock);
        panelForm.add(new JLabel("URL de imagen:"));
        JTextField txtImagen = new JTextField("https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=200");
        panelForm.add(txtImagen);
        panel.add(panelForm, BorderLayout.CENTER);
        JButton btnGuardar = new JButton("Guardar Producto");
        btnGuardar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText();
                String autor = txtAutor.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = Integer.parseInt(txtStock.getText());
                String imagen = txtImagen.getText();
                if (control.agregarLibro(titulo, autor, precio, stock, imagen) != null) {
                    JOptionPane.showMessageDialog(this, "Libro agregado exitosamente");
                    txtTitulo.setText("");
                    txtAutor.setText("");
                    txtPrecio.setText("");
                    txtStock.setText("");
                    txtImagen.setText("https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=200");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar libro", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio y stock deben ser numeros validos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.add(btnGuardar);
        panel.add(panelBoton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelOrdenes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Órdenes Pendientes"));
        panelOrdenes = new JPanel();
        panelOrdenes.setLayout(new BoxLayout(panelOrdenes, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panelOrdenes);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll, BorderLayout.CENTER);
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarOrdenesPendientes());
        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.add(btnActualizar);
        panel.add(panelBoton, BorderLayout.SOUTH);
        cargarOrdenesPendientes();
        return panel;
    }

    public void cargarOrdenesPendientes() {
        panelOrdenes.removeAll();
        List<OrdenDTO> ordenes = control.obtenerOrdenesPendientes();
        if (ordenes == null || ordenes.isEmpty()) {
            JLabel lblSinOrdenes = new JLabel("No hay pedidos pendientes actualmente.");
            lblSinOrdenes.setHorizontalAlignment(SwingConstants.CENTER);
            lblSinOrdenes.setFont(new Font("Arial", Font.ITALIC, 12));
            lblSinOrdenes.setForeground(Color.GRAY);
            panelOrdenes.add(Box.createVerticalGlue());
            panelOrdenes.add(lblSinOrdenes);
            panelOrdenes.add(Box.createVerticalGlue());
        } else {
            for (OrdenDTO orden : ordenes) {
                panelOrdenes.add(crearPanelOrden(orden));
                panelOrdenes.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        panelOrdenes.revalidate();
        panelOrdenes.repaint();
    }

    private JPanel crearPanelOrden(OrdenDTO orden) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.setBackground(new Color(250, 250, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        // Info de la orden
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        JLabel lblOrden = new JLabel("ORDEN #" + orden.getIdOrden());
        lblOrden.setFont(new Font("Arial", Font.BOLD, 11));
        lblOrden.setForeground(Color.GRAY);
        JLabel lblUsuario = new JLabel(orden.getNombreUsuario());
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel lblTotal = new JLabel("Total: $" + String.format("%.2f", orden.getTotal()));
        lblTotal.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTotal.setForeground(new Color(0, 100, 200));
        panelInfo.add(lblOrden);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfo.add(lblUsuario);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfo.add(lblTotal);
        panel.add(panelInfo, BorderLayout.CENTER);
        // Botón aceptar
        JButton btnDetalle = new JButton("Ver Detalle");
        btnDetalle.setPreferredSize(new Dimension(100, 30));
        btnDetalle.addActionListener(e -> control.mostrarDetalleOrden(orden));
        panel.add(btnDetalle, BorderLayout.EAST);
        return panel;
    }
}