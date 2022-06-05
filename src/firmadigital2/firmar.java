/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firmadigital2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

/**
 *
 * @author AntDVD
 */
public class firmar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            System.out.println("Introduzca el nombre del fichero a firmar:");
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String fichero = br.readLine();
            
            //Creación de un generador de llaves
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            //Inicialización del generador de pares de llaves
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            //Generación de las llaves
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();
            //Firmamos los datos
            //Obtención de un objeto de tipo Signature
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            //Inicialización del objeto Signature
            dsa.initSign(priv);
            //Entrega al objeto Signature de los datos a ser firmados
            FileInputStream fis = new FileInputStream(fichero);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                dsa.update(buffer, 0, len);
            };
            bufin.close();
            //Generación de la firma
            byte[] realSig = dsa.sign();
            //Almacenamos la firma
            FileOutputStream sigfos = new FileOutputStream("sig");
            sigfos.write(realSig);
            sigfos.close();
            //Almacenamos la llave pública en un archivo
            byte[] key = pub.getEncoded();
            FileOutputStream keyfos = new FileOutputStream("pk");
            keyfos.write(key);
            keyfos.close();

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }

    }

}
