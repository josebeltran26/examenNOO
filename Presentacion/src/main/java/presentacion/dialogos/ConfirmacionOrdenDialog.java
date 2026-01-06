package presentacion.dialogos;

import presentacion.control.ControlPresentacion;
import dtos.OrdenDTO;
import dtos.DetalleOrdenDTO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConfirmacionOrdenDialog extends JDialog {
    private ControlPresentacion control;
    private List<DetalleOrdenDTO> detalles;
    private boolean confirmado = false;
    private JTextArea txtResumen;
    private JLabel lblTotal;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    public ConfirmacionOrdenDialog(Frame parent, ControlPresentacion control) {
        super(parent, "Confirmar Orden", true);
        this.control = control;
        initComponents();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        // Panel superior - Título
        JPanel panelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel("Confirmar Orden de Compra");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);
        // Panel central - Resumen
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Resumen de la Orden"));
        txtResumen = new JTextArea();
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtResumen);
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        // Panel total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(new JLabel("Total a pagar:"));
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(0, 128, 0));
        panelTotal.add(lblTotal);
        panelCentral.add(panelTotal, BorderLayout.SOUTH);
        // Panel inferior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnConfirmar = new JButton("Confirmar Orden");
        btnConfirmar.setBackground(new Color(76, 175, 80));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(e -> confirmar());
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cancelar());
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void mostrarResumen(List<DetalleOrdenDTO> detalles) {
        this.detalles = detalles;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s %10s %12s %15s\n",
                "Libro", "Cantidad", "Precio Unit.", "Subtotal"));
        sb.append("-".repeat(70)).append("\n");
        double total = 0.0;
        for (DetalleOrdenDTO detalle : detalles) {
            String titulo = detalle.getTituloLibro();
            if (titulo.length() > 28) {
                titulo = titulo.substring(0, 25) + "...";
            }
            sb.append(String.format("%-30s %10d $%11.2f $%14.2f\n",
                    titulo,
                    detalle.getCantidad(),
                    detalle.getSubtotal() / detalle.getCantidad(),
                    detalle.getSubtotal()));
            total += detalle.getSubtotal();
        }
        sb.append("-".repeat(70)).append("\n");
        sb.append(String.format("\n%52s $%14.2f", "TOTAL:", total));
        txtResumen.setText(sb.toString());
        lblTotal.setText(String.format("$%.2f", total));
    }

    private void confirmar() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de confirmar esta orden?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            confirmado = true;
            dispose();
        }
    }

    private void cancelar() {
        confirmado = false;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}