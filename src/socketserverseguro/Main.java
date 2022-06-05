package socketserverseguro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Antes de poner en marcha un servicio que acepte comunicaciones mediante SSL
 * debemos de configurar previamente nuestro entorno. El servidor debe de contar
 * con una clave privada y un certificado digital, que es el que usará para
 * llevar a cabo la negociación SSL con cualquier cliente que se conecte. Estos
 * deben de estar guardados en un almacén Java. El almacén debe cumplir dos
 * condiciones: - sólo tiene que tener una única entrada, con un alias
 * cualquiera. Esta entrada contiene el par clave privada y certificado. - es
 * necesario que la contraseña del almacén y la de la entrada sean exactamente
 * la misma. Si no se cumplen estas dos condiciones, las clases que gestionan
 * SSL en Java no sabrán obtener la información necesaria para ejecutar el
 * protocolo seguro. Una vez disponemos del almacén debemos de indicar dentro
 * del programa su ruta dentro del sistema de archivos y su contraseña. Para la
 * creación del certificado autofirmado utilizaremos la herramienta contenida en
 * el JDK llamada "keytool". Utilizaremos el siguiente comando:
 *
 * keytool -genkey -alias srv_David -keyalg RSA -keystore AlmacenSSL.jks
 *
 * Con este comando creariamos un certificado autofirmado de clave pública de
 * nombre srv_David, con el algoritmo RSA, lo almacenaremos en el keystore
 * AlmacenSSL Después de introducir el comando nos pedirá diferente información
 * de nuestra organización, así como la contraseña del almacén. En mi caso he
 * utilizado "123456" Se creará el almacén llamado AlmacenSSL.jks en la ruta
 * C:\Usuarios\Usuario. Para visualizar el contenido del almacén escribiremos el
 * siguiente comando:
 *
 * keytool -list -keystore AlmacenSSL.jks
 *
 */
/**
 *
 * @author David Jiménez Riscardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Establecemos información relativa al almacén de llaves
        System.setProperty("javax.net.ssl.keyStore", "AlmacenSSL.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        //Declaramos un objeto de tipo Factory para crear un socket SSL
        SSLServerSocketFactory f = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        InputStream entrada = null;
        DataInputStream flujoEntrada = null;
        OutputStream salida = null;
        DataOutputStream flujoSalida = null;

        try {

            //Creamos un socket seguro
            SSLServerSocket server = (SSLServerSocket) f.createServerSocket(5000);

            System.out.println("Esperando al cliente...");
            //Aceptamos peticiones de cliente
            SSLSocket cliente = (SSLSocket) server.accept();

            //Creo flujo de entrada del cliente
            entrada = cliente.getInputStream();
            flujoEntrada = new DataInputStream(entrada);

            //Creo flujo de salida hacia el cliente
            salida = cliente.getOutputStream();
            flujoSalida = new DataOutputStream(salida);

            //Operamos... mostramos mensaje enviado por el cliente y enviamos mensaje al cliente
            System.out.println("Recibiendo del cliente: " + flujoEntrada.readUTF());
            flujoSalida.writeUTF("Bienvenido cliente!!");

            //Cierro flujos y sockets
            salida.close();
            entrada.close();
            cliente.close();
            server.close();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
