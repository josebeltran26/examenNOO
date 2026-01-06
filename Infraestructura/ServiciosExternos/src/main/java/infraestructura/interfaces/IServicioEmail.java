package infraestructura.interfaces;
public interface IServicioEmail {
    boolean enviarCorreo(String destinatario, String asunto, String mensaje);
}