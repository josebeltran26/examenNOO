package infraestructura.correo.fachada;
import infraestructura.correo.control.ControlCorreo;
import infraestructura.correo.interfaz.ICorreo;
// Fachada: Punto de entrada al subsistema de correo
public class CorreoFachada implements ICorreo {
    private final ControlCorreo control;
    public CorreoFachada() {
        this.control = new ControlCorreo();
    }
    @Override
    public boolean enviarCorreoConfirmacion(String destinatario, String asunto, String mensaje) {
        return control.enviarCorreoConfirmacion(destinatario, asunto, mensaje);
    }
    @Override
    public boolean notificarOrdenCreada(String correo, String nombreUsuario, int idOrden, double total) {
        return control.notificarOrdenCreada(correo, nombreUsuario, idOrden, total);
    }
}