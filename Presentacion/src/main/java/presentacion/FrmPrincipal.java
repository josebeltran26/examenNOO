package presentacion;

import presentacion.control.ControlPresentacion;
import presentacion.paneles.AdminPanel;
import presentacion.paneles.CatalogoPanel;
import javax.swing.*;
import java.awt.*;

public class FrmPrincipal extends JFrame {
    private final ControlPresentacion control;
    private CatalogoPanel catalogoPanel;
    private AdminPanel adminPanel;
    private JPanel panelContenido;
    private JToolBar toolBar;

    public FrmPrincipal() {
        this.control = new ControlPresentacion();
        this.control.setFramePrincipal(this);
        initComponents();
    }

    private void initComponents() {
        setTitle("E-Books");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        // Barra de herramientas
        crearToolBar();
        add(toolBar, BorderLayout.NORTH);
        // Panel de contenido
        panelContenido = new JPanel(new CardLayout());
        catalogoPanel = new CatalogoPanel(control);
        adminPanel = new AdminPanel(control);
        panelContenido.add(catalogoPanel, "catalogo");
        panelContenido.add(adminPanel, "admin");
        add(panelContenido, BorderLayout.CENTER);
    }

    private void crearToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        toolBar.setBackground(new Color(41, 128, 185));
        JLabel lblTitulo = new JLabel("E-Books");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        toolBar.add(lblTitulo);
        toolBar.add(Box.createHorizontalStrut(20));
        // Boton Catalogo
        JButton btnCatalogo = new JButton("Catalogo");
        btnCatalogo.addActionListener(e -> mostrarCatalogo());
        toolBar.add(btnCatalogo);
        toolBar.add(Box.createHorizontalStrut(10));
        // Boton Ver Carrito
        JButton btnCarrito = new JButton("Ver Carrito");
        btnCarrito.addActionListener(e -> {
            if (control.haySesionActiva()) {
                catalogoPanel.verCarrito();
            } else {
                JOptionPane.showMessageDialog(this, "Debes iniciar sesion para usar el carrito");
            }
        });
        toolBar.add(btnCarrito);
        toolBar.add(Box.createHorizontalGlue());
        // Panel de sesion (se actualiza dinamicamente)
        actualizarBarraSesion();
    }

    public void actualizarBarraSesion() {
        // Remover componentes de sesion anteriores
        Component[] components = toolBar.getComponents();
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] instanceof JLabel &&
                    ((JLabel) components[i]).getText().contains("Usuario:")) {
                toolBar.remove(i);
            } else if (components[i] instanceof JButton &&
                    (((JButton) components[i]).getText().equals("Cerrar Sesion") ||
                            ((JButton) components[i]).getText().equals("Panel Admin") ||
                            ((JButton) components[i]).getText().equals("Ver Carrito") ||
                            ((JButton) components[i]).getText().equals("Iniciar Sesion") ||
                            ((JButton) components[i]).getText().equals("Registro"))) {
                toolBar.remove(i);
            }
        }
        if (control.haySesionActiva()) {
            // Usuario logueado
            JLabel lblUsuario = new JLabel("Usuario: " + control.getUsuarioEnSesion().getNombre());
            lblUsuario.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblUsuario.setForeground(Color.WHITE);
            toolBar.add(lblUsuario);
            toolBar.add(Box.createHorizontalStrut(10));
            // Boton Ver Carrito
            JButton btnCarrito = new JButton("Ver Carrito");
            btnCarrito.addActionListener(e -> catalogoPanel.verCarrito());
            toolBar.add(btnCarrito);
            toolBar.add(Box.createHorizontalStrut(10));
            // Boton Panel Admin (solo si es admin)
            if (control.esAdministrador()) {
                JButton btnAdmin = new JButton("Panel Admin");
                btnAdmin.addActionListener(e -> mostrarAdmin());
                toolBar.add(btnAdmin);
                toolBar.add(Box.createHorizontalStrut(10));
            }
            // Boton Cerrar Sesion
            JButton btnCerrarSesion = new JButton("Cerrar Sesion");
            btnCerrarSesion.addActionListener(e -> {
                control.cerrarSesion();
                catalogoPanel.limpiarCarrito();
                actualizarBarraSesion();
                mostrarCatalogo();
            });
            toolBar.add(btnCerrarSesion);
        } else {
            // Usuario NO logueado
            JButton btnLogin = new JButton("Iniciar Sesion");
            btnLogin.addActionListener(e -> control.mostrarLogin());
            toolBar.add(btnLogin);
            toolBar.add(Box.createHorizontalStrut(10));
            JButton btnRegistro = new JButton("Registro");
            btnRegistro.addActionListener(e -> control.mostrarRegistro());
            toolBar.add(btnRegistro);
        }
        toolBar.revalidate();
        toolBar.repaint();
    }

    public void actualizarSesion() {
        actualizarBarraSesion();
        // Si es admin y esta en el catalogo, puede ir a admin
        if (control.esAdministrador()) {
            // Ya se muestra el boton admin en la barra
        }
    }

    public void mostrarCatalogo() {
        CardLayout cl = (CardLayout) (panelContenido.getLayout());
        cl.show(panelContenido, "catalogo");
        catalogoPanel.cargarCatalogo();
    }

    public void mostrarAdmin() {
        if (!control.esAdministrador()) {
            JOptionPane.showMessageDialog(this, "Solo administradores pueden acceder");
            return;
        }
        CardLayout cl = (CardLayout) (panelContenido.getLayout());
        cl.show(panelContenido, "admin");
        adminPanel.cargarOrdenesPendientes();
    }

    public void cambiarPanel(JPanel panel) {
        panelContenido.add(panel, "temp");
        CardLayout cl = (CardLayout) (panelContenido.getLayout());
        cl.show(panelContenido, "temp");
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    public static void main(String[] args) {
        // Configurar Look and Feel nativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            FrmPrincipal frame = new FrmPrincipal();
            frame.setVisible(true);
        });
    }
}