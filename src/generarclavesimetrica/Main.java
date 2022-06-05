/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generarclavesimetrica;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Ejemplo Generador de clave simétrica con DES y AES
 * @author David Jiménez Riscardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Generamos una clave con el algoritmo DES y lo mostramos por pantalla");
        //System.out.println(generarClaveSimetricaDES(56));
        System.out.println("Clave: "+Base64.getEncoder().encodeToString(generarClaveSimetricaDES(56).getEncoded()));
        System.out.println("Generamos una clave con el algoritmo AES y lo mostramos por pantalla");
        //System.out.println("Clave:"+generarClaveSimetricaAES(128));
        System.out.println("Clave: "+Base64.getEncoder().encodeToString(generarClaveSimetricaAES(128).getEncoded()));
    }
    
    public static SecretKey generarClaveSimetricaAES(int tamanio){
        SecretKey sk = null;
        if((tamanio==128) || (tamanio==192) || (tamanio==256)){
            KeyGenerator kg = null;
            try {
                kg = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            kg.init(tamanio);
            sk = kg.generateKey();
        }
        return sk;
    }
    
    public static SecretKey generarClaveSimetricaDES(int tamanio){
        SecretKey sk = null;
        if((tamanio==56)){
            KeyGenerator kg = null;
            try {
                kg = KeyGenerator.getInstance("DES");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            kg.init(tamanio);
            sk = kg.generateKey();
        }
        return sk;
    }
    
}
