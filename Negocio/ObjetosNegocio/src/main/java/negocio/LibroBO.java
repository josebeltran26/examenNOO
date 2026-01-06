package negocio;
import dtos.LibroDTO;
import persistencia.entidades.Libro;
import negocio.interfaces.ILibroBO;
// Business Object: Logica de negocio para libros
public class LibroBO implements ILibroBO {
    private Libro libro;
    public LibroBO(Libro libro) {
        this.libro = libro;
    }
    @Override
    public boolean hayStock(int cantidad) {
        return libro.getStock() >= cantidad;
    }
    @Override
    public void descontarStock(int cantidad) {
        if (hayStock(cantidad)) {
            libro.setStock(libro.getStock() - cantidad);
        }
    }
    @Override
    public void agregarStock(int cantidad) {
        libro.setStock(libro.getStock() + cantidad);
    }
    @Override
    public LibroDTO toDTO() {
        return new LibroDTO(
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getPrecio(),
                libro.getStock(),
                libro.getImagenUrl());
    }
}