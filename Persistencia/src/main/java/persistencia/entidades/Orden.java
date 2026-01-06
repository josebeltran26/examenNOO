package persistencia.entidades;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class Orden {
    private int idOrden;
    private int idUsuario;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private double total;
    private String estado;
    private List<DetalleOrden> detalles;
    public Orden() {
        this.detalles = new ArrayList<>();
    }
    public Orden(int idOrden, int idUsuario, String nombreUsuario, LocalDateTime fecha, double total, String estado) {
        this.idOrden = idOrden;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }
    public int getIdOrden() {
        return idOrden;
    }
    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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
    public List<DetalleOrden> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetalleOrden> detalles) {
        this.detalles = detalles;
    }
    public void agregarDetalle(DetalleOrden detalle) {
        this.detalles.add(detalle);
    }
}