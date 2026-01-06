package infraestructura.email;
import infraestructura.interfaces.IServicioEmail;
public class ServicioEmailSimulado implements IServicioEmail {
    @Override
    public boolean enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            System.out.println("\n========== CORREO ELECTRÓNICO ==========");
            System.out.println("Para: " + destinatario);
            System.out.println("Asunto: " + asunto);
            System.out.println("----------------------------------------");
            System.out.println(mensaje);
            System.out.println("========================================\n");
            return true;
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }
}