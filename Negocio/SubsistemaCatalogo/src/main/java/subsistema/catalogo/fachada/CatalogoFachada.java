package subsistema.catalogo.fachada;
import dtos.LibroDTO;
import subsistema.catalogo.control.ControlCatalogo;
import subsistema.catalogo.interfaz.ICatalogo;
import java.util.List;
// Fachada: Punto de entrada al subsistema de catalogo
public class CatalogoFachada implements ICatalogo {
    private final ControlCatalogo control;
    public CatalogoFachada() {
        this.control = new ControlCatalogo();
    }
    @Override
    public LibroDTO agregarLibro(String titulo, String autor, double precio, int stock, String imagenUrl) {
        return control.agregarLibro(titulo, autor, precio, stock, imagenUrl);
    }
    @Override
    public List<LibroDTO> obtenerCatalogo() {
        return control.obtenerCatalogo();
    }
    @Override
    public List<LibroDTO> obtenerTodosLosLibros() {
        return control.obtenerTodosLosLibros();
    }
    @Override
    public LibroDTO buscarLibroPorId(int idLibro) {
        return control.buscarLibroPorId(idLibro);
    }
    @Override
    public boolean actualizarStock(int idLibro, int nuevoStock) {
        return control.actualizarStock(idLibro, nuevoStock);
    }
    @Override
    public boolean eliminarLibro(int idLibro) {
        return control.eliminarLibro(idLibro);
    }
}