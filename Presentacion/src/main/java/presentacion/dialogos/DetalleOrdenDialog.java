package presentacion.dialogos;

import presentacion.control.ControlPresentacion;

import dtos.OrdenDTO;
import dtos.DetalleOrdenDTO;
import javax.swing.*;
import java.awt.*;

public class DetalleOrdenDialog extends JDialog {
    private ControlPresentacion control;
    private OrdenDTO orden;
    private boolean ordenAceptada = false;
    private JTextArea txtDetalles;
    private JLabel lblEstado;
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JButton btnCerrar;

    public DetalleOrdenDialog(Frame parent, ControlPresentacion control) {
        super(parent, "Detalle de Orden", true);
        this.control = control;
        initComponents();
        setSize(600, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        // Panel superior - Info de orden
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 10, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInfo.add(new JLabel("ID Orden:"));
        JLabel lblIdOrden = new JLabel();
        lblIdOrden.setFont(new Font("Arial", Font.BOLD, 12));
        panelInfo.add(lblIdOrden);
        panelInfo.add(new JLabel("Fecha:"));
        JLabel lblFecha = new JLabel();
        panelInfo.add(lblFecha);
        panelInfo.add(new JLabel("Estado:"));
        lblEstado = new JLabel();
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        panelInfo.add(lblEstado);
        // Panel central - Detalles
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Detalles de la Orden"));
        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        txtDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtDetalles);
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        // Panel total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(new JLabel("Total:"));
        JLabel lblTotal = new JLabel();
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(0, 128, 0));
        panelTotal.add(lblTotal);
        panelCentral.add(panelTotal, BorderLayout.SOUTH);
        // Panel inferior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnAceptar = new JButton("Aceptar Orden");
        btnAceptar.addActionListener(e -> aceptarOrden());
        btnRechazar = new JButton("Rechazar Orden");
        btnRechazar.addActionListener(e -> rechazarOrden());
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnAceptar);
        panelBotones.add(btnRechazar);
        panelBotones.add(btnCerrar);
        add(panelInfo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void mostrarDetalles(OrdenDTO orden) {
        this.orden = orden;
        // Actualizar info de orden
        Component[] components = ((JPanel) getContentPane().getComponent(0)).getComponents();
        ((JLabel) components[1]).setText(String.valueOf(orden.getIdOrden()));
        ((JLabel) components[3]).setText(orden.getFecha().toString());
        lblEstado.setText(orden.getEstado());
        if ("PENDIENTE".equals(orden.getEstado())) {
            lblEstado.setForeground(Color.ORANGE);
        } else if ("ACEPTADA".equals(orden.getEstado())) {
            lblEstado.setForeground(new Color(0, 128, 0));
        } else {
            lblEstado.setForeground(Color.RED);
        }
        // Actualizar detalles
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-35s %10s %15s\n",
                "Libro", "Cantidad", "Subtotal"));
        sb.append("-".repeat(65)).append("\n");
        if (orden.getDetalles() != null) {
            for (DetalleOrdenDTO detalle : orden.getDetalles()) {
                String titulo = detalle.getTituloLibro();
                if (titulo.length() > 33) {
                    titulo = titulo.substring(0, 30) + "...";
                }
                sb.append(String.format("%-35s %10d $%14.2f\n",
                        titulo,
                        detalle.getCantidad(),
                        detalle.getSubtotal()));
            }
        }
        sb.append("-".repeat(65)).append("\n");
        sb.append(String.format("\n%47s $%14.2f", "TOTAL:", orden.getTotal()));
        txtDetalles.setText(sb.toString());
        // Actualizar total en el label
        JPanel panelCentral = (JPanel) getContentPane().getComponent(1);
        JPanel panelTotal = (JPanel) panelCentral.getComponent(1);
        JLabel lblTotal = (JLabel) panelTotal.getComponent(1);
        lblTotal.setText(String.format("$%.2f", orden.getTotal()));
        // Habilitar/deshabilitar botones según estado
        boolean esPendiente = "PENDIENTE".equals(orden.getEstado());
        btnAceptar.setEnabled(esPendiente);
        btnRechazar.setEnabled(esPendiente);
    }

    private void aceptarOrden() {
        boolean confirmado = MensajeDialog.confirmar(this,
                "¿Está seguro de aceptar esta orden?",
                "Confirmar Aceptación");
        if (confirmado) {
            try {
                boolean exito = control.aceptarOrden(orden.getIdOrden());
                if (exito) {
                    ordenAceptada = true;
                    MensajeDialog.mostrarExito(this, "Orden aceptada exitosamente");
                    dispose();
                } else {
                    MensajeDialog.mostrarError(this, "No se pudo aceptar la orden");
                }
            } catch (Exception e) {
                MensajeDialog.mostrarError(this, "Error al aceptar la orden: " + e.getMessage());
            }
        }
    }

    private void rechazarOrden() {
        boolean confirmado = MensajeDialog.confirmar(this,
                "¿Está seguro de rechazar esta orden?",
                "Confirmar Rechazo");
        if (confirmado) {
            try {
                boolean exito = control.rechazarOrden(orden.getIdOrden());
                if (exito) {
                    MensajeDialog.mostrarExito(this, "Orden rechazada exitosamente");
                    dispose();
                } else {
                    MensajeDialog.mostrarError(this, "No se pudo rechazar la orden");
                }
            } catch (Exception e) {
                MensajeDialog.mostrarError(this, "Error al rechazar la orden: " + e.getMessage());
            }
        }
    }

    public boolean isOrdenAceptada() {
        return ordenAceptada;
    }
}