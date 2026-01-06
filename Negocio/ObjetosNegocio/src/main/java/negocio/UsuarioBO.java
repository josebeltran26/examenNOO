package negocio;
import dtos.UsuarioDTO;
import persistencia.entidades.Usuario;
import negocio.interfaces.IUsuarioBO;
public class UsuarioBO implements IUsuarioBO {
    private Usuario usuario;
    public UsuarioBO() {
    }
    public UsuarioBO(Usuario usuario) {
        this.usuario = usuario;
    }
    @Override
    public boolean validarCredenciales(String correo, String password) {
        if (usuario == null) {
            return false;
        }
        return usuario.getCorreo().equals(correo) &&
                usuario.getPassword().equals(password) &&
                usuario.isActivo();
    }
    @Override
    public boolean esAdministrador() {
        if (usuario == null) {
            return false;
        }
        return "ADMIN".equalsIgnoreCase(usuario.getRol());
    }
    @Override
    public UsuarioDTO toDTO() {
        if (usuario == null) {
            return null;
        }
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol());
    }
    public static UsuarioBO fromDTO(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);
        return new UsuarioBO(usuario);
    }
    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}