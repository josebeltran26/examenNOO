package negocio.interfaces;
import dtos.*;
import persistencia.entidades.Libro;
// Interface no necesaria - OrdenBO es un adaptador estatico
// Se mantiene para compatibilidad pero se recomienda usar metodos estaticos directamente
public interface IOrdenBO {
    static double calcularSubtotal(Libro libro, int cantidad) {
        return negocio.OrdenBO.calcularSubtotal(libro, cantidad);
    }
}