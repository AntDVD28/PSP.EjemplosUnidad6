/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketseguros;

import java.io.IOException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author profesor
 */
public class ServidorSsl {

    public static void main(String[] arg) throws IOException {
        int numeroPuerto = 6001; // puerto por donde escucharemos
        
        // indicamos la ubicacion del almacen de claves del servidor, para el handshake
        System.setProperty("javax.net.ssl.keyStore", "resources/AlmacenSrv");
        System.setProperty("javax.net.ssl.keyStorePassword", "1234567");

        // creamos una factoria de sslserversocket
        SSLServerSocketFactory factoria = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        //instanciamos un sslserversocket con esa factoria
        SSLServerSocket ServidorSsl = (SSLServerSocket) factoria.createServerSocket(numeroPuerto);
        System.out.println("Servidor SSL lanzado. Escuchando por el puerto " + numeroPuerto);
        while (true) {
            System.out.println("=>Esperando conexion de algun  cliente, con seguridad SSL...");

            // instanciamos un sslsocket por donde entraran las llamadas de los clientes
            SSLSocket ClienteSsl = (SSLSocket) ServidorSsl.accept();
            System.out.println("Recibo conexion cliente.  " + ClienteSsl.getInetAddress().getHostName());
            System.out.println("Lanzo un hilo para atenderlo y me voy a esperar nuevas conexiones. ");
            Thread hiloCliente = new Thread(new HiloServidorSsl(ClienteSsl));
            hiloCliente.start();
        }
    }
}
