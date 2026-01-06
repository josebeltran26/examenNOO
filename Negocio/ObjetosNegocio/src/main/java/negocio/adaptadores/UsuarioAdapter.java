package negocio.adaptadores;
import dtos.UsuarioDTO;
import persistencia.entidades.Usuario;

public class UsuarioAdapter {
    

    public static UsuarioDTO toDTO(Usuario entidad) {
        if (entidad == null) {
            return null;
        }
        return new UsuarioDTO(
                entidad.getIdUsuario(),
                entidad.getNombre(),
                entidad.getCorreo(),
                entidad.getRol());
    }
    

    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario entidad = new Usuario();
        entidad.setIdUsuario(dto.getIdUsuario());
        entidad.setNombre(dto.getNombre());
        entidad.setCorreo(dto.getCorreo());
        entidad.setRol(dto.getRol());
        entidad.setActivo(true);
        return entidad;
    }
}