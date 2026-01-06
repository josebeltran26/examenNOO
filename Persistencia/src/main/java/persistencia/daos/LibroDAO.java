package persistencia.daos;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import persistencia.ConexionBD;
import persistencia.entidades.Libro;
import persistencia.interfaces.ILibroDAO;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.*;
public class LibroDAO implements ILibroDAO {
    private final MongoCollection<Document> coleccion;
    private int lastId;
    public LibroDAO() {
        this.coleccion = ConexionBD.getInstance().getDatabase().getCollection("libros");
        this.lastId = obtenerUltimoId();
    }
    private int obtenerUltimoId() {
        Document ultimo = coleccion.find().sort(new Document("idLibro", -1)).first();
        return (ultimo != null) ? ultimo.getInteger("idLibro", 0) : 0;
    }
    private synchronized int generarNuevoId() {
        return ++lastId;
    }
    private Document libroToDocument(Libro libro) {
        return new Document("idLibro", libro.getIdLibro())
                .append("titulo", libro.getTitulo())
                .append("autor", libro.getAutor())
                .append("precio", libro.getPrecio())
                .append("stock", libro.getStock())
                .append("disponible", libro.isDisponible())
                .append("imagenUrl", libro.getImagenUrl());
    }
    private Libro documentToLibro(Document doc) {
        if (doc == null)
            return null;
        return new Libro(
                doc.getInteger("idLibro"),
                doc.getString("titulo"),
                doc.getString("autor"),
                doc.getDouble("precio"),
                doc.getInteger("stock"),
                doc.getBoolean("disponible", true),
                doc.getString("imagenUrl"));
    }
    @Override
    public int guardar(Libro libro) {
        try {
            int nuevoId = generarNuevoId();
            libro.setIdLibro(nuevoId);
            libro.setDisponible(true);
            coleccion.insertOne(libroToDocument(libro));
            return nuevoId;
        } catch (Exception e) {
            System.err.println("Error al guardar libro: " + e.getMessage());
            return -1;
        }
    }
    @Override
    public Libro buscarPorId(int idLibro) {
        try {
            Document doc = coleccion.find(eq("idLibro", idLibro)).first();
            return documentToLibro(doc);
        } catch (Exception e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
            return null;
        }
    }
    @Override
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion.find().iterator()) {
            while (cursor.hasNext()) {
                libros.add(documentToLibro(cursor.next()));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
        }
        return libros;
    }
    @Override
    public List<Libro> obtenerDisponibles() {
        List<Libro> libros = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion.find(and(
                eq("disponible", true),
                gt("stock", 0))).iterator()) {
            while (cursor.hasNext()) {
                libros.add(documentToLibro(cursor.next()));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener libros disponibles: " + e.getMessage());
        }
        return libros;
    }
    @Override
    public boolean actualizar(Libro libro) {
        try {
            Document doc = libroToDocument(libro);
            return coleccion.replaceOne(eq("idLibro", libro.getIdLibro()), doc).getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean actualizarStock(int idLibro, int nuevoStock) {
        try {
            Document update = new Document("$set", new Document("stock", nuevoStock)
                    .append("disponible", nuevoStock > 0));
            return coleccion.updateOne(eq("idLibro", idLibro), update).getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminar(int idLibro) {
        try {
            return coleccion.deleteOne(eq("idLibro", idLibro)).getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }
}