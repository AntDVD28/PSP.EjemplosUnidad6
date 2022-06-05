/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrardatos4;

import static cifrardatos3.Main.generarClaves;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Cifrado RSA con llave envuelta
 * Los datos se cifran usando una clave simétrica desechable, generada al azar. Esta clave entonces
 * se cifra usando la clave pública del destinatario del mensaje. Por último, se envía al destinatario
 * el mensaje y la clave cifradas, conjuntamente.
 * @author david.jimenez
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Generamos claves
        KeyPair claves = generarClaves();
        
        String texto = "Esto es una prueba";
        System.out.println("Texto(String): " + texto);
        byte[] texto_en_bytes = texto.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("Texto cifrado(bytes): " + texto_en_bytes);
        
        //Ciframos texto y clave
        byte[][] cifrados = null;
        cifrados = encriptar(texto_en_bytes, claves.getPublic());
               
        //Desciframos texto y clave
        byte[] texto_descifrado = desencriptar(cifrados, claves.getPrivate());
        System.out.println("Texto descifrado(bytes): " + texto_descifrado);
        
        //Paso los bytes a String
        String s = new String(texto_descifrado, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("Texto descifrado(String):" +s);
        
    }
    
    public static KeyPair generarClaves() {
        KeyPair claves = null;
        try {         
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            claves = kpg.genKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(cifrardatos3.Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return claves;
    }
    
    public static byte[][] encriptar(byte[] datos, PublicKey pk){
        
        byte[][] cifrados = new byte[2][];
        try{
            //Generamos una clave simétrica
            KeyGenerator kg = KeyGenerator.getInstance("AES"); 
            kg.init(128); 
            SecretKey clave = kg.generateKey();
            //Ciframos los datos utilizando la clave simétrica
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);           
            byte[] datos_cifrados = cipher.doFinal(datos);
            //Ciframos la clave simétrica utilizando la clave pública
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.WRAP_MODE, pk);
            byte[] clave_cifrada = cipher.wrap(clave);
            cifrados[0] = datos_cifrados;
            cifrados[1] = clave_cifrada;            
        }catch(Exception e){
            System.err.println("Ha ocurrido un error cifrando: " + e);
        }
        return cifrados;
    }
    
    public static byte[] desencriptar(byte[][] cifrados, PrivateKey pk){
        
        byte[] datos_descifrados = null;
        try {
            //Desciframos la clave simétrica utilizando la clave privada
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
            cipher.init(Cipher.UNWRAP_MODE, pk);
            byte[] clave_cifrada = cifrados[1];
            Key clave_descifrada = cipher.unwrap(clave_cifrada,"AES",Cipher.SECRET_KEY);
            
            //Desciframos los datos utilizando la clave simétrica descifrada
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave_descifrada);
            datos_descifrados = cipher.doFinal(cifrados[0]);
            
        }catch(Exception e){
            System.err.println("Ha ocurrido un error cifrando: " + e);
        }
        return datos_descifrados;
    }
    
    
    
}
