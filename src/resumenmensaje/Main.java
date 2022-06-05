
package resumenmensaje;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ejemplo de como aplicar una función de resumen sobre un texto
 * @author David Jiménez Riscardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA1");

            String texto = "Texto para el mensaje ejemplo SHA1";
            sha1.update(texto.getBytes()); //obtiene el resumen
            byte[] resumen = sha1.digest(); //completa la generación del resumen
            for (int k = 0; k < resumen.length; k++) { //muestra el resumen
                System.out.println("(" + resumen[k] + ")");
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
