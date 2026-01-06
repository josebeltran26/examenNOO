package negocio.interfaces;
import dtos.LibroDTO;
public interface ILibroBO {
    boolean hayStock(int cantidad);
    void descontarStock(int cantidad);
    void agregarStock(int cantidad);
    LibroDTO toDTO();
}