package hermessensorcollector.lbd.udc.es.hermessensorcollector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Clase con algunas utilidades genericas para ser usadas desde cualquier lugar
 * de la aplicacion
 *
 * @author Leticia Riestra Ainsua
 *
 */
public class Utils {

    /**
     * Metodo para mostrar un mensaje de tipo Toast
     * @param activity
     * @param mensaje
     */
    public static void crearMensajeToast(Activity activity, String mensaje){
        Toast toast1 = Toast.makeText(activity, mensaje,
                Toast.LENGTH_SHORT);

        toast1.show();
    }

    /**
     * Metodo para mostrar un mensaje de tipo Toast centrado en la pantalla
     * @param activity
     * @param mensaje
     */
    public static void crearMensajeToastCentrado(Activity activity, String mensaje){
        Toast toast1 = Toast.makeText(activity, mensaje, Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER,0,0);
        toast1.show();
    }

    /**
     * Metodo para ocultar el teclado
     * @param v Vista sobre la que se ejecuta
     */
    public static void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Metodo para formatear un numero con dos decimales
     * @param number Numero a formatear
     * @return String con el numero formateado
     */
    public static String formatDecimal(double number) {

        DecimalFormat nf = new DecimalFormat("#.##");
        String formatted = nf.format(number);

        return formatted;
    }

    public static void guardarNombreUsuario(Activity activity, String email){

        try {
            String nombreUsuario = computeHash(email);

            //Accedemos a nuestras preferencias para insertar el nombre de usuario
            SharedPreferences prefs = activity.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombreUsuario", nombreUsuario);
            editor.commit();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error creando el hash del email");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error creando el hash del email");
        }

    }

    public static String recuperarNombreUsuario(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String nombreUsuario = prefs.getString("nombreUsuario", null);

        return nombreUsuario;
    }

    public static String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


}
