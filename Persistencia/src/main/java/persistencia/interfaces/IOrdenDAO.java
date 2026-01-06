package persistencia.interfaces;
import persistencia.entidades.Orden;
import java.util.List;
public interface IOrdenDAO {
    int guardar(Orden orden);
    Orden buscarPorId(int idOrden);
    List<Orden> obtenerTodos();
    List<Orden> obtenerPendientes();
    boolean actualizarEstado(int idOrden, String nuevoEstado);
    boolean tienePendiente(int idUsuario);
    boolean eliminar(int idOrden);
}