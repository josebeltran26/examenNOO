package presentacion.dialogos;

import presentacion.control.ControlPresentacion;
import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private final ControlPresentacion control;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;

    public LoginDialog(Frame parent, ControlPresentacion control) {
        super(parent, "Login", true);
        this.control = control;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(350, 200);
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelForm.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelForm.add(txtCorreo);
        panelForm.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword);
        add(panelForm, BorderLayout.CENTER);
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> ingresar());
        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.addActionListener(e -> {
            setVisible(false);
            control.mostrarRegistro();
        });
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistro);
        add(panelBotones, BorderLayout.SOUTH);
        // Enter para submit
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void ingresar() {
        String correo = txtCorreo.getText();
        String password = new String(txtPassword.getPassword());
        if (control.iniciarSesion(correo, password) != null) {
            setVisible(false);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales invalidas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}