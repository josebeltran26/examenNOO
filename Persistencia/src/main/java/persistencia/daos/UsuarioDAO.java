package persistencia.daos;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import persistencia.ConexionBD;
import persistencia.entidades.Usuario;
import persistencia.interfaces.IUsuarioDAO;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
public class UsuarioDAO implements IUsuarioDAO {
    private final MongoCollection<Document> coleccion;
    private int lastId;
    public UsuarioDAO() {
        this.coleccion = ConexionBD.getInstance().getDatabase().getCollection("usuarios");
        this.lastId = obtenerUltimoId();
    }
    private int obtenerUltimoId() {
        Document ultimo = coleccion.find().sort(new Document("idUsuario", -1)).first();
        return (ultimo != null) ? ultimo.getInteger("idUsuario", 0) : 0;
    }
    private synchronized int generarNuevoId() {
        return ++lastId;
    }
    private Document usuarioToDocument(Usuario usuario) {
        return new Document("idUsuario", usuario.getIdUsuario())
                .append("nombre", usuario.getNombre())
                .append("correo", usuario.getCorreo())
                .append("password", usuario.getPassword())
                .append("rol", usuario.getRol())
                .append("activo", usuario.isActivo());
    }
    private Usuario documentToUsuario(Document doc) {
        if (doc == null)
            return null;
        return new Usuario(
                doc.getInteger("idUsuario"),
                doc.getString("nombre"),
                doc.getString("correo"),
                doc.getString("password"),
                doc.getString("rol"),
                doc.getBoolean("activo", true));
    }
    @Override
    public int guardar(Usuario usuario) {
        try {
            int nuevoId = generarNuevoId();
            usuario.setIdUsuario(nuevoId);
            usuario.setActivo(true);
            coleccion.insertOne(usuarioToDocument(usuario));
            return nuevoId;
        } catch (Exception e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return -1;
        }
    }
    @Override
    public Usuario buscarPorCorreo(String correo) {
        try {
            Document doc = coleccion.find(eq("correo", correo)).first();
            return documentToUsuario(doc);
        } catch (Exception e) {
            System.err.println("Error al buscar usuario por correo: " + e.getMessage());
            return null;
        }
    }
    @Override
    public Usuario buscarPorId(int idUsuario) {
        try {
            Document doc = coleccion.find(eq("idUsuario", idUsuario)).first();
            return documentToUsuario(doc);
        } catch (Exception e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
            return null;
        }
    }
    @Override
    public boolean actualizar(Usuario usuario) {
        try {
            Document doc = usuarioToDocument(usuario);
            return coleccion.replaceOne(eq("idUsuario", usuario.getIdUsuario()), doc).getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminar(int idUsuario) {
        try {
            Document update = new Document("$set", new Document("activo", false));
            return coleccion.updateOne(eq("idUsuario", idUsuario), update).getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion.find().iterator()) {
            while (cursor.hasNext()) {
                usuarios.add(documentToUsuario(cursor.next()));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }
}