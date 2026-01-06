package dtos;

public class DetalleOrdenDTO {
    private int idDetalle;
    private String tituloLibro;
    private String autorLibro;
    private int cantidad;
    private double subtotal;
    public DetalleOrdenDTO() {
    }
    public DetalleOrdenDTO(int idDetalle, String tituloLibro, String autorLibro, int cantidad, double subtotal) {
        this.idDetalle = idDetalle;
        this.tituloLibro = tituloLibro;
        this.autorLibro = autorLibro;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }
    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }
    public String getTituloLibro() {
        return tituloLibro;
    }
    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }
    public String getAutorLibro() {
        return autorLibro;
    }
    public void setAutorLibro(String autorLibro) {
        this.autorLibro = autorLibro;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    @Override
    public String toString() {
        return "DetalleOrdenDTO{" +
                "tituloLibro='" + tituloLibro + '\'' +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}