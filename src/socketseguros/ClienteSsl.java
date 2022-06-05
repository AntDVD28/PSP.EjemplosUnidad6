/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketseguros;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author profesor
 */
public class ClienteSsl {

    public static void main(String[] arg) throws IOException, InterruptedException {
        // indicamos la ubicacion del almacen de certificados de confianza del cliente
        // donde hemos importado el certificado que nos emitio el servidor
        System.setProperty("javax.net.ssl.trustStore", "resources/CliCertConfianza");
        System.setProperty("javax.net.ssl.trustStorePassword", "333444");

        String Host = "localhost";
        int numeroPuerto = 6001;

        System.out.println("PROGRAMA CLIENTE INICIADO...Cliente Ssl");
        // instanciamos una factoria de sockets 
        SSLSocketFactory factoria = (SSLSocketFactory) SSLSocketFactory.getDefault();
        // insanciamos un sslSocket cliente
        SSLSocket clienteSsl = (SSLSocket) factoria.createSocket(Host, numeroPuerto);
        System.out.println("Establecida conexion con servidor SSL...");

        Thread.sleep(5000); // esperamos 5 sg. por hacer una pausa solamente

        //CREO FLUJO DE SALIDA AL SERVIDOR
        DataOutputStream flujoSalida = new DataOutputStream(clienteSsl.getOutputStream());

        //ENVÍO UN SALUDO AL SERVIDOR
        String msg = "Saludos al servidor desde el cliente SSL";
        System.out.println("Envio mensaje al servidor SSL:\n\t" + msg);
        flujoSalida.writeUTF(msg);

        //CREO FLUJO DE ENTRADA DEL CLIENTE
        DataInputStream flujoEntrada = new DataInputStream(clienteSsl.getInputStream());

        //EL SERVIDOR ME ENVÍA UN MENSAJE
        System.out.println("Recibiendo del servidor: \n\t" + flujoEntrada.readUTF());

        //CERRAR STREAMS Y SOCKETS
        flujoEntrada.close();
        flujoSalida.close();
        clienteSsl.close();
    }
}
