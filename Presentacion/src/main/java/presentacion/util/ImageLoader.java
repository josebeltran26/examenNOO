package presentacion.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

/**
 * Utilidad para cargar imágenes desde recursos o URLs
 */
public class ImageLoader {

    /**
     * Carga una imagen desde los resources del proyecto
     */
    public static ImageIcon cargarDesdeResources(String nombreArchivo, int ancho, int alto) {
        try {
            InputStream is = ImageLoader.class.getResourceAsStream("/images/" + nombreArchivo);
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                Image scaled = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen desde resources: " + e.getMessage());
        }
        return crearPlaceholder(ancho, alto);
    }

    /**
     * Carga una imagen desde una URL
     */
    public static ImageIcon cargarDesdeURL(String urlString, int ancho, int alto) {
        try {
            URL url = new URL(urlString);
            BufferedImage img = ImageIO.read(url);
            if (img != null) {
                Image scaled = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen desde URL: " + e.getMessage());
        }
        return crearPlaceholder(ancho, alto);
    }

    /**
     * Crea un placeholder cuando no se puede cargar la imagen
     */
    public static ImageIcon crearPlaceholder(int ancho, int alto) {
        BufferedImage placeholder = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();

        // Fondo gris claro
        g2d.setColor(new Color(230, 230, 230));
        g2d.fillRect(0, 0, ancho, alto);

        // Borde
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, ancho - 1, alto - 1);

        // Icono de libro
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        String texto = "Libro";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (ancho - fm.stringWidth(texto)) / 2;
        int y = alto / 2;
        g2d.drawString(texto, x, y);

        g2d.dispose();
        return new ImageIcon(placeholder);
    }
}
