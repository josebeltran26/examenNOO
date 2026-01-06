package negocio.interfaces;
import dtos.UsuarioDTO;
public interface IUsuarioBO {
    boolean validarCredenciales(String correo, String password);
    boolean esAdministrador();
    UsuarioDTO toDTO();
}