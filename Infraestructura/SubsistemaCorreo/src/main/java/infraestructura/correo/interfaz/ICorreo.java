package infraestructura.correo.interfaz;
public interface ICorreo {
    boolean enviarCorreoConfirmacion(String destinatario, String asunto, String mensaje);
    boolean notificarOrdenCreada(String correo, String nombreUsuario, int idOrden, double total);
}