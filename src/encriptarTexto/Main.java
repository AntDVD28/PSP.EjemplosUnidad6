package encriptarTexto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Queremos encriptar un texto y, a continuación, escribir el resultado en un
 * documento con un procesador de textos para enviar por carta. El resultado de
 * la encriptación, por tanto, ha de contener caracteres imprimibles. Se pide:
 * Obtener el resultado de la encriptación, usando un algoritmo AES con longitud
 * clave 32 bytes,  y partiendo de la frase contraseña siguiente: "La lluvia en
 * Sevilla es una maravilla" Sugerencias: Para generar la clave de encriptación
 * puedes aplicar a la contraseña dada un resumen con algoritmo, por ejemplo,
 * SHA-256 o SHA-1. Para poder escribir en un fichero de texto el resultado de
 * la encriptación puedes codificar el resultado de la misma con Base64.
 */
/**
 *
 * @author David Jiménez Riscardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String frase = "La lluvia en Sevilla es una maravilla";
        byte[] texto_cifrado = null;
        byte[] texto_descifrado = null;

        try {
            //1. Generamos clave a partir de la frase. En el enunciado nos indica que la clave es de 32 bytes que son 256 bits
            SecretKey clave = generarClave(frase, 256);
            //System.out.println(clave);

            //2.Ciframos el texto
            System.out.print("Introduzca el mensaje a cifrar: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String texto = br.readLine();
            byte[] texto_en_bytes = texto.getBytes(java.nio.charset.StandardCharsets.UTF_8);

            texto_cifrado = cifrarTexto(clave, texto_en_bytes);

            //Mostramos el texto cifrado con caracteres imprimibles
            String texto_cifrado_imprimible = Base64.getEncoder().encodeToString(texto_cifrado);
            System.out.println("Texto cifrado: " + texto_cifrado_imprimible);

            //3.Escribimos el texto cifrado en un archivo
            escribeEnFichero("fichero.txt", texto_cifrado_imprimible);

            //4.Lo desencriptamos y mostramos por pantalla
            texto_descifrado = descifrarTexto(clave, texto_cifrado);
            System.out.println("Texto descifrado(en bytes): " + texto_descifrado);
            //Paso los bytes a String
            String s = new String(texto_descifrado, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("Texto descifrado(String): " + s);
            
            //OJO!!!!
            //Si nos pidieran desencriptar a partir de texto_cifrado_imprimible sería así:
            byte[] cifrado_imprimible = Base64.getDecoder().decode(texto_cifrado_imprimible);
            texto_descifrado = descifrarTexto(clave, cifrado_imprimible);
            //Paso los bytes a String
            String s2 = new String(texto_descifrado, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("Texto descifrado(String) desde un texto cifrado imprimible: " + s2);
            
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

    /**
     * Método para generar una clave
     *
     * @param texto Texto a partir del cual generamos la clave
     * @param tamanio Tamaño especificado en bits
     * @return Clave generada
     */
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
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sk;
    }

    /**
     * Método para cifrar un texto
     *
     * @param clave Clave para la encriptación
     * @param datos Texto a encriptar
     * @return Texto encriptado
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] cifrarTexto(SecretKey clave, byte[] datos) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] datos_cifrados = null;
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, clave);
        datos_cifrados = c.doFinal(datos);
        return datos_cifrados;
    }

    /**
     * Método para descifrar un texto
     *
     * @param clave Clave de encriptación
     * @param datos_cifrados Texto a desencriptar
     * @return Texto desencriptado
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] descifrarTexto(SecretKey clave, byte[] datos_cifrados) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] datos = null;
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, clave);
        datos = c.doFinal(datos_cifrados);
        return datos;
    }

    /**
     * Método mediante el cual escribimos el texto recibido en un fichero Si el
     * fichero no existe lo creamos
     *
     * @param nombreFichero Nombre del fichero
     * @param texto Texto que escribiremos en el fichero
     * @throws IOException
     */
    public static void escribeEnFichero(String nombreFichero, String texto) throws IOException {

        File f = new File(nombreFichero);
        //Si el archivo no existe lo creamos
        if (!f.exists()) {
            f.createNewFile();
        }
        //El parámetro true es necesario para agregar información al fichero
        //En caso contrario sobreescribiremos su contenido
        FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(texto);
        bw.newLine();

        if (bw != null) {
            bw.close();
        }
        if (fw != null) {
            fw.close();
        }
    }

}
