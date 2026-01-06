package persistencia.daos;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import persistencia.ConexionBD;
import persistencia.entidades.DetalleOrden;
import persistencia.entidades.Orden;
import persistencia.interfaces.IOrdenDAO;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.mongodb.client.model.Filters.*;
public class OrdenDAO implements IOrdenDAO {
    private final MongoCollection<Document> coleccion;
    private int lastId;
    public OrdenDAO() {
        this.coleccion = ConexionBD.getInstance().getDatabase().getCollection("ordenes");
        this.lastId = obtenerUltimoId();
    }
    private int obtenerUltimoId() {
        Document ultimo = coleccion.find().sort(new Document("idOrden", -1)).first();
        return (ultimo != null) ? ultimo.getInteger("idOrden", 0) : 0;
    }
    private synchronized int generarNuevoId() {
        return ++lastId;
    }
    private Document ordenToDocument(Orden orden) {
        Document doc = new Document("idOrden", orden.getIdOrden())
                .append("idUsuario", orden.getIdUsuario())
                .append("nombreUsuario", orden.getNombreUsuario())
                .append("fecha", Date.from(orden.getFecha().atZone(ZoneId.systemDefault()).toInstant()))
                .append("total", orden.getTotal())
                .append("estado", orden.getEstado());
        List<Document> detallesDocs = new ArrayList<>();
        for (DetalleOrden detalle : orden.getDetalles()) {
            detallesDocs.add(new Document("idLibro", detalle.getIdLibro())
                    .append("tituloLibro", detalle.getTituloLibro())
                    .append("autorLibro", detalle.getAutorLibro())
                    .append("cantidad", detalle.getCantidad())
                    .append("subtotal", detalle.getSubtotal()));
        }
        doc.append("detalles", detallesDocs);
        return doc;
    }
    @SuppressWarnings("unchecked")
    private Orden documentToOrden(Document doc) {
        if (doc == null)
            return null;
        Date fecha = doc.getDate("fecha");
        LocalDateTime localDateTime = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Orden orden = new Orden(
                doc.getInteger("idOrden"),
                doc.getInteger("idUsuario"),
                doc.getString("nombreUsuario"),
                localDateTime,
                doc.getDouble("total"),
                doc.getString("estado"));
        List<Document> detallesDocs = (List<Document>) doc.get("detalles");
        if (detallesDocs != null) {
            for (Document detalleDoc : detallesDocs) {
                DetalleOrden detalle = new DetalleOrden(
                        detalleDoc.getInteger("idLibro"),
                        detalleDoc.getString("tituloLibro"),
                        detalleDoc.getString("autorLibro"),
                        detalleDoc.getInteger("cantidad"),
                        detalleDoc.getDouble("subtotal"));
                orden.agregarDetalle(detalle);
            }
        }
        return orden;
    }
    @Override
    public int guardar(Orden orden) {
        try {
            int nuevoId = generarNuevoId();
            orden.setIdOrden(nuevoId);
            orden.setEstado("PENDIENTE");
            coleccion.insertOne(ordenToDocument(orden));
            return nuevoId;
        } catch (Exception e) {
            System.err.println("Error al guardar orden: " + e.getMessage());
            return -1;
        }
    }
    @Override
    public Orden buscarPorId(int idOrden) {
        try {
            Document doc = coleccion.find(eq("idOrden", idOrden)).first();
            return documentToOrden(doc);
        } catch (Exception e) {
            System.err.println("Error al buscar orden por ID: " + e.getMessage());
            return null;
        }
    }
    @Override
    public List<Orden> obtenerPendientes() {
        List<Orden> ordenes = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion.find(eq("estado", "PENDIENTE")).iterator()) {
            while (cursor.hasNext()) {
                ordenes.add(documentToOrden(cursor.next()));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener órdenes pendientes: " + e.getMessage());
        }
        return ordenes;
    }
    @Override
    public boolean tienePendiente(int idUsuario) {
        try {
            Document orden = coleccion.find(and(
                    eq("idUsuario", idUsuario),
                    eq("estado", "PENDIENTE"))).first();
            return orden != null;
        } catch (Exception e) {
            System.err.println("Error al verificar orden pendiente: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean actualizarEstado(int idOrden, String nuevoEstado) {
        try {
            Document update = new Document("$set", new Document("estado", nuevoEstado));
            return coleccion.updateOne(eq("idOrden", idOrden), update).getModifiedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar estado de orden: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Orden> obtenerTodos() {
        List<Orden> ordenes = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccion.find().iterator()) {
            while (cursor.hasNext()) {
                ordenes.add(documentToOrden(cursor.next()));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener todas las órdenes: " + e.getMessage());
        }
        return ordenes;
    }
    @Override
    public boolean eliminar(int idOrden) {
        try {
            return coleccion.deleteOne(eq("idOrden", idOrden)).getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar orden: " + e.getMessage());
            return false;
        }
    }
}