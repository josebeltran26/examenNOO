package subsistema.iniciosesion.control;
import dtos.UsuarioDTO;
import persistencia.factory.DAOFactory;
import persistencia.interfaces.IUsuarioDAO;
import subsistema.iniciosesion.interfaz.IInicioSesion;
// Control del subsistema de inicio de sesion
public class ControlInicioSesion implements IInicioSesion {
    private final IUsuarioDAO usuarioDAO;
    public ControlInicioSesion() {
        this.usuarioDAO = DAOFactory.crearUsuarioDAO();
    }
    @Override
    public UsuarioDTO iniciarSesion(String correo, String password) {
        try {
            // Validaciones basicas
            if (correo == null || correo.trim().isEmpty()) {
                System.err.println("Correo no valido");
                return null;
            }
            if (password == null || password.trim().isEmpty()) {
                System.err.println("Password no valido");
                return null;
            }
            // Buscar usuario por correo
            persistencia.entidades.Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
            if (usuario == null) {
                System.err.println("Usuario no encontrado");
                return null;
            }
            // Verificar password (en produccion deberia estar hasheado)
            if (!usuario.getPassword().equals(password)) {
                System.err.println("Password incorrecto");
                return null;
            }
            // Crear DTO para retornar
            return new UsuarioDTO(
                    usuario.getIdUsuario(),
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getRol());
        } catch (Exception e) {
            System.err.println("Error al iniciar sesion: " + e.getMessage());
            return null;
        }
    }
    @Override
    public UsuarioDTO registrarUsuario(String nombre, String correo, String password) {
        try {
            // Validaciones
            if (nombre == null || nombre.trim().isEmpty()) {
                System.err.println("Nombre no valido");
                return null;
            }
            if (correo == null || correo.trim().isEmpty()) {
                System.err.println("Correo no valido");
                return null;
            }
            if (password == null || password.length() < 4) {
                System.err.println("Password debe tener al menos 4 caracteres");
                return null;
            }
            // Verificar que el correo no exista
            if (usuarioDAO.buscarPorCorreo(correo) != null) {
                System.err.println("El correo ya esta registrado");
                return null;
            }
            // Crear nuevo usuario
            persistencia.entidades.Usuario usuario = new persistencia.entidades.Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setPassword(password); // En produccion deberia hashearse
            usuario.setRol("CLIENTE"); // Por defecto es cliente
            // Guardar en BD
            int idGenerado = usuarioDAO.guardar(usuario);
            if (idGenerado > 0) {
                return new UsuarioDTO(idGenerado, nombre, correo, "CLIENTE");
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return null;
        }
    }
    @Override
    public boolean cerrarSesion(int idUsuario) {
        // En esta implementacion simple no hay tracking de sesiones
        // Solo retorna true para indicar exito
        return true;
    }
}