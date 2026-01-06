package negocio.adaptadores;
import dtos.LibroDTO;
import persistencia.entidades.Libro;

public class LibroAdapter {
    

    public static LibroDTO toDTO(Libro entidad) {
        if (entidad == null) {
            return null;
        }
        return new LibroDTO(
                entidad.getIdLibro(),
                entidad.getTitulo(),
                entidad.getAutor(),
                entidad.getPrecio(),
                entidad.getStock(),
                entidad.getImagenUrl());
    }
    

    public static Libro toEntity(LibroDTO dto) {
        if (dto == null) {
            return null;
        }
        Libro entidad = new Libro();
        entidad.setIdLibro(dto.getIdLibro());
        entidad.setTitulo(dto.getTitulo());
        entidad.setAutor(dto.getAutor());
        entidad.setPrecio(dto.getPrecio());
        entidad.setStock(dto.getStock());
        entidad.setDisponible(dto.getStock() > 0);
        entidad.setImagenUrl(dto.getImagenUrl());
        return entidad;
    }
}