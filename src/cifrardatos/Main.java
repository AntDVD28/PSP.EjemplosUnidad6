/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrardatos;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Ejemplo de CIFRADO EN BLOQUE
 * Cifrado AES en modo ECB (Electronic Code Book, Libro de Códigos Electrónico)
 * @author david.jimenez
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Generamos una clave privada
            SecretKey clave = generarClave();
            
            String texto = "Esto es una prueba";
            System.out.println("Texto: " + texto);
            byte[] texto_en_bytes = texto.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("Texto en bytes: " + texto_en_bytes);
            
            //Ciframos el texto
            byte[] texto_cifrado = null;
            texto_cifrado = cifrar(clave, texto_en_bytes);
            System.out.println("Texto cifrado: " + texto_cifrado);
            
            //Desciframos el texto
            byte[] texto_descifrado = null;
            texto_descifrado = descifrar(clave, texto_cifrado);
            System.out.println("Texto descifrado(en bytes): " + texto_descifrado);
            //Paso los bytes a String
            String s = new String(texto_descifrado, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("Texto descifrado(String):" +s);


        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SecretKey generarClave() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); //se indica el tamaño de la clave
        SecretKey clave = keyGen.generateKey(); //genera la clave privada
        return clave;
    }

    public static byte[] cifrar(SecretKey clave, byte[] datos) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] datos_cifrados = null;
        //AES dispone de dos modos: ECB(más sencillo) y CBC
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, clave);
        datos_cifrados = c.doFinal(datos);
        return datos_cifrados;
    }

    public static byte[] descifrar(SecretKey clave, byte[] datos_cifrados) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] datos = null;
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, clave);
        datos = c.doFinal(datos_cifrados);
        return datos;
    }

}
