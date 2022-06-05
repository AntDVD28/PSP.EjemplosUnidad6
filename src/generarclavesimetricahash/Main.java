/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generarclavesimetricahash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Ejemplo de cómo obtener una clave simétrica AES a partir de un texto de una
 * contraseña, con la ayuda del algoritmo SHA-256
 *
 * @author David Jiménez Riscardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Introduzca una contraseña:");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String entrada = br.readLine();
        System.out.println("Mostramos la clave generada a partir de la contraseña");
        System.out.println(generarClave(entrada, 128));
    }

    public static SecretKey generarClave(String texto, int tamanio) {

        SecretKey sk = null;
        if ((tamanio == 128) || (tamanio == 192) || (tamanio == 256)) {

            try {
                byte[] datos = texto.getBytes("UTF-8");
                //Algoritmo Hash
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                //Aplicamos el algoritmo hash sobre los datos
                byte[] hash = md.digest(datos);
                //Extraemos tantos bytes como necesitamos para generar la clave
                byte[] clave = Arrays.copyOf(hash, 16);
                sk = new SecretKeySpec(clave, "AES");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sk;
    }

}
