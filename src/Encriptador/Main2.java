/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encriptador;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Usuario
 */
public class Main2 {

    public static void main(String[] argv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        try {
            //Paso 1: Creamos la clave secreta usando AES (tb podríamos usar DES) y tamaño de clave 128 bits
            String pass = "1234";
            System.out.println("Paso clave 0: "+pass);
            byte[] passEncriptacion = pass.getBytes("UTF-8");
            System.out.println("Paso clave 1: "+passEncriptacion);
            
            
            //Opcional: Resumen de mensajes: https://www.uv.es/sto/cursos/seguridad.java/html/sjava-6.html#:~:text=Un%20algoritmo%20de%20resumen%20de,o%20one%20way%20hash%20algorithm.
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            passEncriptacion = sha.digest(passEncriptacion);
            System.out.println("Paso clave 2: "+passEncriptacion);
            
            passEncriptacion = Arrays.copyOf(passEncriptacion, 16);
            System.out.println("Paso clave 3: "+passEncriptacion);
        
            SecretKeySpec claveSecreta = new SecretKeySpec(passEncriptacion, "AES");
            System.out.println("Paso clave 4: "+claveSecreta.toString());
            
            //NOTA SOBRE EL PASO 1: ESTE PROCESO TAMBIÉN SE PUEDE HACER CON SECURERANDOM

            
            //Paso 2: Creamos un objeto Cipher con el algoritmo AES/ECB/PKCS5Padding y lo inicializamos a modo de 
            //encriptación con la clave creada anteriormente.
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE,claveSecreta);
            
            //Paso 3: Realizamos el cifrado de la información con el método doFinal()
            //Versión 1:
            String mensaje = "Hola mundo";
            System.out.println("Inicial: "+mensaje);
            byte textoPlano[]=mensaje.getBytes();
            byte textoCifrado[] = c.doFinal(textoPlano);
            System.out.println("Encriptado: "+ new String(textoCifrado));
            
            //Versión 2:
            /*String mensaje = "Hola mundo";
            System.out.println("Inicial: "+mensaje);
            byte textoPlano[]=mensaje.getBytes("UTF-8");
            byte textoCifrado[] = c.doFinal(textoPlano);
            String encriptado = Base64.getEncoder().encodeToString(textoCifrado);
            System.out.println("Encriptado: "+ encriptado);*/
            
            //Paso 4: Configuramos Cipher en modo desencriptación con la clave anterior y desncriptamos con el
            //método doFinal()
            c.init(Cipher.DECRYPT_MODE,claveSecreta);
            byte desencriptado[] = c.doFinal(textoCifrado);
            System.out.println("Desencriptado: "+new String(desencriptado));
            
            //OJO: byte[] textoCifrado = Base64.getDecoder().decode(encriptado);
            
        } catch (BadPaddingException ex) {
            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
