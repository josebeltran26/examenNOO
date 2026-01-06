package presentacion.dialogos;

import javax.swing.*;
import java.awt.*;

public class MensajeDialog {
    public enum TipoMensaje {
        EXITO,
        ERROR,
        ADVERTENCIA,
        INFORMACION
    }

    public static void mostrarExito(Component parent, String mensaje) {
        mostrarMensaje(parent, mensaje, "Éxito", TipoMensaje.EXITO);
    }

    public static void mostrarError(Component parent, String mensaje) {
        mostrarMensaje(parent, mensaje, "Error", TipoMensaje.ERROR);
    }

    public static void mostrarAdvertencia(Component parent, String mensaje) {
        mostrarMensaje(parent, mensaje, "Advertencia", TipoMensaje.ADVERTENCIA);
    }

    public static void mostrarInformacion(Component parent, String mensaje) {
        mostrarMensaje(parent, mensaje, "Información", TipoMensaje.INFORMACION);
    }

    public static boolean confirmar(Component parent, String mensaje, String titulo) {
        int respuesta = JOptionPane.showConfirmDialog(
                parent,
                mensaje,
                titulo,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    public static void mostrarMensaje(Component parent, String mensaje, String titulo, TipoMensaje tipo) {
        int tipoJOption;
        Icon icono = null;
        switch (tipo) {
            case EXITO:
                tipoJOption = JOptionPane.INFORMATION_MESSAGE;
                // Icono personalizado de éxito (checkmark verde)
                icono = UIManager.getIcon("OptionPane.informationIcon");
                break;
            case ERROR:
                tipoJOption = JOptionPane.ERROR_MESSAGE;
                break;
            case ADVERTENCIA:
                tipoJOption = JOptionPane.WARNING_MESSAGE;
                break;
            case INFORMACION:
            default:
                tipoJOption = JOptionPane.INFORMATION_MESSAGE;
                break;
        }
        if (icono != null) {
            JOptionPane.showMessageDialog(parent, mensaje, titulo, tipoJOption, icono);
        } else {
            JOptionPane.showMessageDialog(parent, mensaje, titulo, tipoJOption);
        }
    }

    public static String solicitarTexto(Component parent, String mensaje, String titulo) {
        return JOptionPane.showInputDialog(parent, mensaje, titulo, JOptionPane.QUESTION_MESSAGE);
    }

    public static String mostrarOpciones(Component parent, String mensaje, String titulo, String[] opciones) {
        return (String) JOptionPane.showInputDialog(
                parent,
                mensaje,
                titulo,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }
}