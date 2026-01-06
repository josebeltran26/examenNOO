package infraestructura.correo.control;
import infraestructura.email.ServicioEmailSimulado;
// Control para envio de correos
public class ControlCorreo {
    private final ServicioEmailSimulado servicioEmail;
    public ControlCorreo() {
        this.servicioEmail = new ServicioEmailSimulado();
    }
    public boolean enviarCorreoConfirmacion(String destinatario, String asunto, String mensaje) {
        return servicioEmail.enviarCorreo(destinatario, asunto, mensaje);
    }
    public boolean notificarOrdenCreada(String correo, String nombreUsuario, int idOrden, double total) {
        // Crear mensaje de confirmacion
        String asunto = "Confirmacion de Orden #" + idOrden;
        String mensaje = String.format(
                "Hola %s,\n\n" +
                        "Tu orden #%d ha sido creada exitosamente.\n" +
                        "Total: $%.2f\n\n" +
                        "Gracias por tu compra!",
                nombreUsuario, idOrden, total);
        return servicioEmail.enviarCorreo(correo, asunto, mensaje);
    }
}