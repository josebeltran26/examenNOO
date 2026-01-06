package persistencia.factory;
import persistencia.daos.*;
import persistencia.interfaces.*;
public class DAOFactory {
    public static IUsuarioDAO crearUsuarioDAO() {
        return new UsuarioDAO();
    }
    public static ILibroDAO crearLibroDAO() {
        return new LibroDAO();
    }
    public static IOrdenDAO crearOrdenDAO() {
        return new OrdenDAO();
    }
}