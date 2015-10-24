package com.example.nthings.busca;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by eduardo on 24/10/15.
 */
public class MainScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Implementación de dos lineas de código para quitar el encabezado y dar una mejor vista
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.primerapantalla);
        Thread timer = new Thread() {

            //El nuevo Thread exige el metodo run
            public void run() {
                try {
                    sleep(3*1000);
                } catch (InterruptedException e) {
                    //Si no puedo ejecutar el sleep muestro el error
                    e.printStackTrace();
                } finally {
                    Intent intencion = new Intent(MainScreen.this, MainActivity.class);
                    startActivity(intencion);
                }
            }
        };
        //ejecuto el thread
        timer.start();
    }//end onCreate
}//end class MainScreen