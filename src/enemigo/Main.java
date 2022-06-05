package enemigo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Se ha logrado interceptar una comunicación digital que incluía un archivo
 * cifrado cuyo contenido, por el contexto de la comunicación, sabemos que debe
 * ser una imagen, aunque desconocemos qué tipo de formato (JPEG, PNG, GIF, BMP,
 * etc.). Se trata del archivo adjunto.cifrado. Por otro lado, tras la captura
 * de dron derribado, se han logrado incautar las últimas claves de encriptación
 * simétrica que se han estado utilizando (archivos clavexx.key). Sabemos que
 * cada uno de ellos es un archivo binario que contiene un objeto serializado de
 * tipo SecretKey. Es decir, que son claves de encriptación con las que
 * podríamos intentar desencriptar.
 *
 * Finalmente, el servicio de inteligencia dispone de un agente infiltrado que
 * ha afirmado que las personas a las que se les interceptó la comunicación aún
 * se siguen utilizando el algoritmo de encriptación DES, aunque esté ya
 * desfasado.
 *
 * Con toda esa información, debes intentar descifrar el archivo interceptado. A
 * ver qué consigues...
 *
 * Si lo consigues, además de ofrecer tu solución (programa que desencripte o
 * intente desencriptar), describe también qué contiene el archivo encriptado y
 * cuál de las claves conseguidas era la buena, si es que alguna de ellas era la
 * buena.
 *
 * En el archivo adjunto tienes todos archivos interceptados.
 */
/**
 *
 * @author David Jiménez Riscardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        File directorio = new File("archivos"); //directorio a listar                                             
        String[] lista = directorio.list();

        for (int i = 0; i < lista.length; i++) {

            if (lista[i].endsWith(".key")) {
                //System.out.println(lista[i]);

                System.out.println("Intentando con..." + lista[i]);

                //Desserializamos el fichero y obtenemos el objeto SecretKey
                FileInputStream fis = new FileInputStream("archivos/" + lista[i]);
                ObjectInputStream ois = new ObjectInputStream(fis);
                SecretKey sk = (SecretKey) ois.readObject();
                //System.out.println(sk);
                ois.close();
                fis.close();
                boolean realizado = desencriptarFichero("archivos/adjunto.cifrado", sk);
                
                if (realizado) {
                    
                    System.out.println("Archivo desencriptado correctamente con la clave " + lista[i]);

                    String mimeType = getMimeType("archivos/desencriptado");
                    
                    System.out.println("Se trata de un archivo: " + mimeType);
                    //Me quedo con los tres últimos caracteres
                    String[] parts = mimeType.split("/");
                    String extension = parts[1];
                    
                    File f1 = new File("archivos/desencriptado");
                    File f2 = new File("archivos/desencriptado."+extension);                
                    f1.renameTo(f2);
                    System.out.println("Archivo recuperado: "+f2.getName());
                   
                }
            }
        }

    }

    /**
     * Método que nos devuelve el tipo MIME del archivo
     *
     * @param namefile Nombre del archivo
     * @return Tipo MIME
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String getMimeType(String namefile) throws FileNotFoundException, IOException {
        File file = new File(namefile);
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        is.close();
        return mimeType;
    }

    /**
     * Método para desencriptar un fichero
     *
     * @param fichero Nombre del fichero
     * @param clave Clave que utilizaremos para su desencriptación
     * @return True si se realiza la desencriptación, false en caso contrario
     * @throws java.io.IOException
     */
    public static boolean desencriptarFichero(String fichero, SecretKey clave) throws IOException {

        Cipher c = null;
        try {
            c = Cipher.getInstance("DES");
            c.init(Cipher.DECRYPT_MODE, clave);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            System.out.println("Error en la configuración del algoritmo. ERROR: " + ex.getMessage());
        }
        if (c != null) {
            int bytesLeidos;
            byte[] buffer = new byte[1000]; //array de bytes
            byte[] bufferClaro;
            FileInputStream fe = null;
            FileOutputStream fs = null;
            try {
                fe = new FileInputStream(fichero); //objeto fichero de entrada
                fs = new FileOutputStream("archivos/desencriptado"); //fichero de salida
                //lee el fichero de 1k en 1k y pasa los fragmentos leidos al descifrador
                bytesLeidos = fe.read(buffer, 0, 1000);
                while (bytesLeidos != -1) {//mientras no se llegue al final del fichero
                    //pasa texto claro al descifrador y lo descifra, asignándolo a bufferClaro
                    bufferClaro = c.update(buffer, 0, bytesLeidos);
                    fs.write(bufferClaro); //Graba el texto descifrado en fichero
                    bytesLeidos = fe.read(buffer, 0, 1000);
                }
                bufferClaro = c.doFinal(); //Completa el descifrado
                fs.write(bufferClaro); //Graba el final del texto descifrado, si lo hay
                //Cierra ficheros
                fe.close();
                fs.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Archivo no encontrado. ERROR: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Error de E/S. ERROR: " + ex.getMessage());
            } catch (IllegalBlockSizeException ex) {
                System.out.print("El archivo indicado no está encriptado. ERROR: " + ex.getMessage());
                fe.close();
                //Debemos de cerrar el uso del fichero para poderlo eliminar
                fs.close();
                deleteFile("archivos/desencriptado");
            } catch (BadPaddingException ex) {
                System.out.println("La contraseña es diferente a la utilizada en la encriptación. ERROR: " + ex.getMessage());
                fe.close();
                //Debemos de cerrar el uso del fichero para poderlo eliminar
                fs.close();
                deleteFile("archivos/desencriptado");
            }
        }
        return existFile("archivos/desencriptado");

    }//Fin del método encriptar fichero

    /**
     * Método para comprobar si un fichero existe
     *
     * @param filename Nombre del fichero
     * @return Devuelve true si existe, false en caso contrario
     */
    public static boolean existFile(String filename) {
        boolean b = false;
        File file = new File(filename);
        if (file.exists()) {
            b = true;
        }
        return b;
    }

    /**
     * Método para eliminar un archivo
     *
     * @param filename Nombre del fichero
     */
    public static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

}
