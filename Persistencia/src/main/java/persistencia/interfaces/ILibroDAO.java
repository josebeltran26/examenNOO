package persistencia.interfaces;
import persistencia.entidades.Libro;
import java.util.List;
public interface ILibroDAO {
    int guardar(Libro libro);
    Libro buscarPorId(int idLibro);
    List<Libro> obtenerTodos();
    List<Libro> obtenerDisponibles();
    boolean actualizar(Libro libro);
    boolean actualizarStock(int idLibro, int nuevoStock);
    boolean eliminar(int idLibro);
}