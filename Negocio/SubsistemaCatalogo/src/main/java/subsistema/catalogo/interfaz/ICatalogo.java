package subsistema.catalogo.interfaz;
import dtos.LibroDTO;
import java.util.List;
public interface ICatalogo {
    LibroDTO agregarLibro(String titulo, String autor, double precio, int stock, String imagenUrl);
    List<LibroDTO> obtenerCatalogo();
    List<LibroDTO> obtenerTodosLosLibros();
    LibroDTO buscarLibroPorId(int idLibro);
    boolean actualizarStock(int idLibro, int nuevoStock);
    boolean eliminarLibro(int idLibro);
}