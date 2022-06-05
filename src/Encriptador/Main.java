/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encriptador;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;

/**
 *
 * @author Usuario
 */
public class Main {

    public static void main(String[] argv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        try {
            //Paso 1: Creamos la clave secreta usando AES (tb podríamos usar DES) y tamaño de clave 128 bits
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            SecretKey claveSecreta = kg.generateKey();
            
            //Paso 2: Creamos un objeto Cipher con el algoritmo AES/ECB/PKCS5Padding y lo inicializamos a modo de 
            //encriptación con la clave creada anteriormente.
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE,claveSecreta);
            
            //Paso 3: Realizamos el cifrado de la información con el método doFinal()
            //Versión 1:
            /*String mensaje = "Hola mundo";
            System.out.println("Inicial: "+mensaje);
            byte textoPlano[]=mensaje.getBytes();
            byte textoCifrado[] = c.doFinal(textoPlano);
            System.out.println("Encriptado: "+ new String(textoCifrado));*/
            
            //Versión 2:
            String mensaje = "Hola mundo";
            System.out.println("Inicial: "+mensaje);
            byte textoPlano[]=mensaje.getBytes("UTF-8");
            byte textoCifrado[] = c.doFinal(textoPlano);
            String encriptado = Base64.getEncoder().encodeToString(textoCifrado);
            System.out.println("Encriptado: "+ encriptado);
            
            //Paso 4: Configuramos Cipher en modo desencriptación con la clave anterior y desncriptamos con el
            //método doFinal()
            
            //OJO: byte[] textoCifrado = Base64.getDecoder().decode(encriptado);
            
            c.init(Cipher.DECRYPT_MODE,claveSecreta);
            byte desencriptado[] = c.doFinal(textoCifrado);
            System.out.println("Desencriptado: "+new String(desencriptado));
            
           
            
        } catch (BadPaddingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
