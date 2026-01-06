package dtos;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenDTO {
    private int idOrden;
    private LocalDateTime fecha;
    private double total;
    private String estado;
    private String nombreUsuario;
    private List<DetalleOrdenDTO> detalles;
    public OrdenDTO() {
        this.detalles = new ArrayList<>();
    }
    public OrdenDTO(int idOrden, LocalDateTime fecha, double total, String estado, String nombreUsuario) {
        this.idOrden = idOrden;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.nombreUsuario = nombreUsuario;
        this.detalles = new ArrayList<>();
    }
    // Getters y Setters
    public int getIdOrden() {
        return idOrden;
    }
    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public List<DetalleOrdenDTO> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetalleOrdenDTO> detalles) {
        this.detalles = detalles;
    }
    @Override
    public String toString() {
        return "OrdenDTO{" +
                "idOrden=" + idOrden +
                ", fecha=" + fecha +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                '}';
    }
}