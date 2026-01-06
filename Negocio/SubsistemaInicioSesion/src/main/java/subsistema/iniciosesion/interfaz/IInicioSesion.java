package subsistema.iniciosesion.interfaz;
import dtos.UsuarioDTO;
// Interfaz del subsistema de inicio de sesion
public interface IInicioSesion {
    UsuarioDTO iniciarSesion(String correo, String password);
    UsuarioDTO registrarUsuario(String nombre, String correo, String password);
    boolean cerrarSesion(int idUsuario);
}