package presentacion.dialogos;
import presentacion.control.ControlPresentacion;
import javax.swing.*;
import java.awt.*;
public class RegistroDialog extends JDialog {
    private final ControlPresentacion control;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    public RegistroDialog(Frame parent, ControlPresentacion control) {
        super(parent, "Registro de Usuario", true);
        this.control = control;
        initComponents();
        setLocationRelativeTo(parent);
    }
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(350, 220);
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);
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
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrar());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            setVisible(false);
            control.mostrarLogin();
        });
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnRegistrar);
    }
    private void registrar() {
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String password = new String(txtPassword.getPassword());
        if (control.registrarUsuario(nombre, correo, password) != null) {
            JOptionPane.showMessageDialog(this, "Registro exitoso!", "Exito", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            control.mostrarLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}