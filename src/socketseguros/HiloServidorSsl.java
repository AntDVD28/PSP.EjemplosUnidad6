/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketseguros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author profesor
 */
public class HiloServidorSsl implements Runnable {

    private SSLSocket socketCli;
    private DataInputStream flujoEntrada;
    private DataOutputStream flujoSalida;
    private boolean conectado;

    public HiloServidorSsl(SSLSocket socketCli) {
        this.socketCli = socketCli;
        try {
            // accedo al flujo de entrada del cliente
            InputStream entrada = socketCli.getInputStream();
            flujoEntrada = new DataInputStream(entrada);
            flujoSalida = new DataOutputStream(socketCli.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(HiloServidorSsl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        conectado = true;
        try {
            // leo el mensaje que me envia el cliente
            System.out.println("Hilo de atencion al cliente: Espero mensajes de cliente ..");
            String msg = flujoEntrada.readUTF();
            System.out.println("Hilo de atencion al cliente: Recibido mensaje del cliente: \n\t" + msg);
            //ENVÍO UN SALUDO AL CLIENTE
            String resp = "El Servidor SSL envia saludos aludos al cliente ";
            System.out.println("Hilo de atencion al cliente: Contesto a cliente: \n\t" + resp);
            flujoSalida.writeUTF(resp);
            
            // termino la conexion
            
            flujoEntrada = null;
            flujoSalida = null;
            socketCli = null;

        } catch (IOException ex) {
            Logger.getLogger(HiloServidorSsl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void salir() {
        flujoEntrada = null;
        flujoSalida = null;
        socketCli = null;
    }

}
