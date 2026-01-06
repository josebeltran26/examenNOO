package persistencia.interfaces;
import persistencia.entidades.Usuario;
import java.util.List;
public interface IUsuarioDAO {
    int guardar(Usuario usuario);
    Usuario buscarPorId(int idUsuario);
    Usuario buscarPorCorreo(String correo);
    List<Usuario> obtenerTodos();
    boolean actualizar(Usuario usuario);
    boolean eliminar(int idUsuario);
}