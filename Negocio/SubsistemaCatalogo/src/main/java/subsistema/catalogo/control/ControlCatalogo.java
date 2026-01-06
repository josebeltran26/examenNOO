package subsistema.catalogo.control;
import dtos.LibroDTO;
import persistencia.factory.DAOFactory;
import persistencia.interfaces.ILibroDAO;
import subsistema.catalogo.interfaz.ICatalogo;
import java.util.ArrayList;
import java.util.List;
// Control del catalogo de libros
public class ControlCatalogo implements ICatalogo {
    private final ILibroDAO libroDAO;
    public ControlCatalogo() {
        this.libroDAO = DAOFactory.crearLibroDAO();
    }
    @Override
    public LibroDTO agregarLibro(String titulo, String autor, double precio, int stock, String imagenUrl) {
        try {
            // Crear nuevo libro
            persistencia.entidades.Libro libro = new persistencia.entidades.Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setPrecio(precio);
            libro.setStock(stock);
            libro.setImagenUrl(imagenUrl);
            libro.setDisponible(stock > 0);
            // Guardar en BD
            int idGenerado = libroDAO.guardar(libro);
            if (idGenerado > 0) {
                return new LibroDTO(idGenerado, titulo, autor, precio, stock, imagenUrl);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al agregar libro: " + e.getMessage());
            return null;
        }
    }
    @Override
    public List<LibroDTO> obtenerCatalogo() {
        List<LibroDTO> catalogo = new ArrayList<>();
        try {
            // Obtener solo libros disponibles
            List<persistencia.entidades.Libro> libros = libroDAO.obtenerDisponibles();
            // Convertir a DTO directamente
            for (persistencia.entidades.Libro libro : libros) {
                LibroDTO dto = new LibroDTO(
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getPrecio(),
                        libro.getStock(),
                        libro.getImagenUrl());
                catalogo.add(dto);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener catalogo: " + e.getMessage());
        }
        return catalogo;
    }
    @Override
    public List<LibroDTO> obtenerTodosLosLibros() {
        List<LibroDTO> libros = new ArrayList<>();
        try {
            List<persistencia.entidades.Libro> todosLibros = libroDAO.obtenerTodos();
            for (persistencia.entidades.Libro libro : todosLibros) {
                LibroDTO dto = new LibroDTO(
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getPrecio(),
                        libro.getStock(),
                        libro.getImagenUrl());
                libros.add(dto);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
        }
        return libros;
    }
    @Override
    public LibroDTO buscarLibroPorId(int idLibro) {
        try {
            persistencia.entidades.Libro libro = libroDAO.buscarPorId(idLibro);
            if (libro == null) {
                return null;
            }
            return new LibroDTO(
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getStock(),
                    libro.getImagenUrl());
        } catch (Exception e) {
            System.err.println("Error al buscar libro: " + e.getMessage());
            return null;
        }
    }
    @Override
    public boolean actualizarStock(int idLibro, int nuevoStock) {
        try {
            return libroDAO.actualizarStock(idLibro, nuevoStock);
        } catch (Exception e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminarLibro(int idLibro) {
        try {
            return libroDAO.eliminar(idLibro);
        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }
}