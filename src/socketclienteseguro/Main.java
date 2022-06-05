package socketclienteseguro;

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
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * El cliente también necesita una configuración previa. En este caso, lo que
 * debe especificarse es la lista de emisores de certificados digitales en los
 * que confía. De esta forma, sólo se aceptará la creación de una conexión hacia
 * un servicio que se considere fiable. Los certificados de emisores de
 * confianza deben de guardarse en un almacén de claves Java especial, llamado
 * truststore, o almacén de confianza. Para este cometido exportaremos el
 * certificado del servidor y lo importaremos al almacén trustore. Lo haremos
 * con los siguientes comandos:
 *
 * keytool -export -keystore AlmacenSSL.jks -alias srv_David -rfc -file
 * srv_David.crt
 *
 * keytool -import -file srv_David.crt -keystore ClientTrustSSL.jks -alias
 * srv_David
 *
 * Lo he importado con el mismo alias pero podría haberlo hecho con uno
 * diferente. Si quiero visualizar el contenido del almacen:
 *
 * keytool -list -v -keystore ClientTrustSSL.jks
 *
 * @author David Jiménez Riscardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.setProperty("javax.net.ssl.trustStore", "ClientTrustSSL.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        //Declaramos un objeto tipo Factory para crear sockets SSL
        SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();

        InputStream entrada = null;
        DataInputStream flujoEntrada = null;
        OutputStream salida = null;
        DataOutputStream flujoSalida = null;
        try {

            //Creamos un socket cliente seguro
            SSLSocket cliente = (SSLSocket) f.createSocket("localhost", 5000);

            //Creo un flujo de salida al servidor
            salida = cliente.getOutputStream();
            flujoSalida = new DataOutputStream(salida);

            //Creo un flujo de entrada del cliente           
            entrada = cliente.getInputStream();
            flujoEntrada = new DataInputStream(entrada);

            //Operamos....enviamos mensaje al servidor y mostramos mensaje del servidor
            flujoSalida.writeUTF("Hola servidorrrrrrrrr!!");
            System.out.println("Recibiendo del servidor: " + flujoEntrada.readUTF());

            //Cerramos flujos y sockets
            entrada.close();
            salida.close();
            cliente.close();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
