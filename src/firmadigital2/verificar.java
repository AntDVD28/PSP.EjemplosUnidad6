/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firmadigital2;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @author AntDVD
 */
public class verificar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        //Para poder verificar una firma digital incluida junto a algún archivo necesitamos: los datos, la forma y la llave pública
        
        //Ingreso y conversión de los bytes encodeados de la llave pública
        FileInputStream keyfis = new FileInputStream("pk");	
	byte[] encKey = new byte[keyfis.available()];  
	keyfis.read(encKey);
	keyfis.close();
        
        //Especificación de la llave, adumiento que fue encodeada de acuerdo a el estándar X.509
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        
        //Hacemos la conversión
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        
        //Ahora podemos generar la llave pública de la especificación
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        
        //Ingresamos los bytes de la firma
        FileInputStream sigfis = new FileInputStream("sig");	
	byte[] sigToVerify = new byte[sigfis.available()];
	sigfis.read(sigToVerify);
	sigfis.close();
        
        //Inicializamos el objeto Signature para la verificación
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
	sig.initVerify(pubKey);
        
        //Entregar los datos al objeto Signature para que sean verificados
        FileInputStream datafis = new FileInputStream("prueba.txt");
	BufferedInputStream bufin = new BufferedInputStream(datafis);
	byte[] buffer = new byte[1024];
	int len;	
	while (bufin.available() != 0) {
            len = bufin.read(buffer);
            sig.update(buffer, 0, len);
	};
        
        //Verificar la firma
        boolean verifies = sig.verify(sigToVerify);
	System.out.println("Firma verificada: " + verifies);
        
    }
    
}
