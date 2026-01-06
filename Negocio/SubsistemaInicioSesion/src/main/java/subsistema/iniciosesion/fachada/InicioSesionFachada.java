package subsistema.iniciosesion.fachada;
import dtos.UsuarioDTO;
import subsistema.iniciosesion.control.ControlInicioSesion;
import subsistema.iniciosesion.interfaz.IInicioSesion;
// Fachada: Punto de entrada al subsistema de inicio de sesion
public class InicioSesionFachada implements IInicioSesion {
    private final ControlInicioSesion control;
    public InicioSesionFachada() {
        this.control = new ControlInicioSesion();
    }
    @Override
    public UsuarioDTO iniciarSesion(String correo, String password) {
        return control.iniciarSesion(correo, password);
    }
    @Override
    public UsuarioDTO registrarUsuario(String nombre, String correo, String password) {
        return control.registrarUsuario(nombre, correo, password);
    }
    @Override
    public boolean cerrarSesion(int idUsuario) {
        return control.cerrarSesion(idUsuario);
    }
}