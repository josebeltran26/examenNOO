package dtos;

public class LibroDTO {
    private int idLibro;
    private String titulo;
    private String autor;
    private double precio;
    private int stock;
    private String imagenUrl;
    public LibroDTO() {
    }
    public LibroDTO(int idLibro, String titulo, String autor, double precio, int stock, String imagenUrl) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
    }
    // Getters y Setters
    public int getIdLibro() {
        return idLibro;
    }
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getImagenUrl() {
        return imagenUrl;
    }
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    @Override
    public String toString() {
        return "LibroDTO{" +
                "idLibro=" + idLibro +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                '}';
    }
}