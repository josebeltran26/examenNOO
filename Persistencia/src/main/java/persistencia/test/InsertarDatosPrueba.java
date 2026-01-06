package persistencia.test;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import persistencia.ConexionBD;

/**
 * Script para insertar datos de prueba en MongoDB
 * Ejecutar una sola vez para crear usuario admin y libros de prueba
 */
public class InsertarDatosPrueba {

    public static void main(String[] args) {
        System.out.println("=== Insertando datos de prueba ===");

        insertarUsuarios();
        insertarLibros();

        System.out.println("=== Datos insertados correctamente ===");
    }

    private static void insertarUsuarios() {
        MongoCollection<Document> usuarios = ConexionBD.getInstance()
                .getDatabase().getCollection("usuarios");

        // Usuario Admin
        Document admin = new Document()
                .append("idUsuario", 1)
                .append("nombre", "Administrador")
                .append("correo", "admin@ebooks.com")
                .append("password", "admin123")
                .append("rol", "ADMIN")
                .append("activo", true);

        // Usuario Cliente de prueba
        Document cliente = new Document()
                .append("idUsuario", 2)
                .append("nombre", "Cliente Prueba")
                .append("correo", "cliente@test.com")
                .append("password", "cliente123")
                .append("rol", "CLIENTE")
                .append("activo", true);

        // Verificar si ya existen
        if (usuarios.countDocuments() == 0) {
            usuarios.insertOne(admin);
            usuarios.insertOne(cliente);
            System.out.println("Usuarios insertados:");
            System.out.println("  - Admin: admin@ebooks.com / admin123");
            System.out.println("  - Cliente: cliente@test.com / cliente123");
        } else {
            System.out.println("Ya existen usuarios en la BD");
        }
    }

    private static void insertarLibros() {
        MongoCollection<Document> libros = ConexionBD.getInstance()
                .getDatabase().getCollection("libros");

        if (libros.countDocuments() == 0) {
            libros.insertOne(crearLibro(1, "El Quijote", "Miguel de Cervantes", 15.99, 50));
            libros.insertOne(crearLibro(2, "Cien Años de Soledad", "Gabriel García Márquez", 18.50, 30));
            libros.insertOne(crearLibro(3, "1984", "George Orwell", 12.99, 45));
            libros.insertOne(crearLibro(4, "El Principito", "Antoine de Saint-Exupéry", 10.00, 100));
            libros.insertOne(crearLibro(5, "Don Juan Tenorio", "José Zorrilla", 8.99, 25));

            System.out.println("5 libros de prueba insertados");
        } else {
            System.out.println("Ya existen libros en la BD");
        }
    }

    private static Document crearLibro(int id, String titulo, String autor, double precio, int stock) {
        return new Document()
                .append("idLibro", id)
                .append("titulo", titulo)
                .append("autor", autor)
                .append("precio", precio)
                .append("stock", stock)
                .append("disponible", true)
                .append("imagenUrl", "");
    }
}
