/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrardatos3;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Ejemplo en el que ciframos una cadena mediante algoritmos de clave pública. Cifrado RSA directo
 * @author david.jimenez
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Generamos claves
            KeyPair claves = generarClaves();
            
            String texto = "Esto es una prueba";
            System.out.println("Texto: " + texto);
            byte[] texto_en_bytes = texto.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("Texto en bytes: " + texto_en_bytes);
            
            //Ciframos el texto
            byte[] texto_cifrado = null;
            texto_cifrado = cifrar(claves.getPublic(), texto_en_bytes);
            System.out.println("Texto cifrado: " + texto_cifrado);
            
            //Desciframos el texto
            byte[] texto_descifrado = null;
            texto_descifrado = descifrar(claves.getPrivate(), texto_cifrado);
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
        }catch (NoSuchProviderException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

       
    }

    public static KeyPair generarClaves() {
        KeyPair claves = null;
        try {         
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            claves = kpg.genKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return claves;
    }

    //Importante!!! Para cifrar hay que utilizar la clave pública
    public static byte[] cifrar(PublicKey clave, byte[] datos) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        byte[] datos_cifrados = null;
        //AES dispone de dos modos: ECB(más sencillo) y CBC
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding","SunJCE");
        c.init(Cipher.ENCRYPT_MODE, clave);
        datos_cifrados = c.doFinal(datos);
        return datos_cifrados;
    }

    //Importante!! Para descifrar hay que utilizar la clave privada
    public static byte[] descifrar(PrivateKey clave, byte[] datos_cifrados) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        byte[] datos = null;
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding","SunJCE");
        c.init(Cipher.DECRYPT_MODE, clave);
        datos = c.doFinal(datos_cifrados);
        return datos;
    }

}
