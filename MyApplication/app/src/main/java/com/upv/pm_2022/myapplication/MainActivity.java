package com.upv.pm_2022.myapplication;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText txv;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         *En esta parte se declaran los elementos que van a ser utilizados en el programa
         */
        txv = findViewById(R.id.edittext);
        txv.setText("");
        b1 = findViewById(R.id.button);
        /*
         * Se crea el metodo Onclick del boton
         */

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirArchivo(view); //Se llama a la funcion para abrir el explorador de archivos
            }
        });

    }

    /*
     * En esta parte se realizan las funciones principales para poder verificar que el archivo seleccionado cumpla con las dimensiones establecidas en el encabezado
     * */
    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            //Se abre el archivo utilizando uri
                            FileInputStream is = (FileInputStream) getApplicationContext().getContentResolver().openInputStream(uri);
                            BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
                            String paths = uri.getPath();
                            String[] Realpath = paths.split(":");
                            if (extencion(Realpath[1])){
                                //txv.setText(Realpath[1]);
                                String Row = "";
                                //Se crea un arraylist en el cual se van a guardar los datos que se necesitan para verificar que se cumplan con las dimensiones establecidas en el encabezado
                                ArrayList<String> elementosexaminar = new ArrayList<String>();
                                int aux1,aux3=0;
                                //ciclo en el cual se agregan al arraylist las lineas que no contengan "P" o que contenga "#"
                                while ((Row = isReader.readLine()) != null) {
                                    aux1=0;
                                    String text="";
                                    for (int r=0;r<Row.length();r++){
                                        switch (Row.charAt(r)){
                                            case '"':
                                                if(Row.charAt(r)=='"'&&Row.charAt(r+1)=='"' && Row.charAt(r+2)=='"' ) {
                                                    if (aux3 == 0) {
                                                        aux3 = 3;
                                                    } else {
                                                        aux3 = 0;
                                                    }
                                                    r += 2;
                                                }
                                                break;
                                            case '#':
                                                aux1=1;
                                                break;
                                            default:
                                                if(aux1==1 || aux3==3){
                                                    text=text+Row.charAt(r);
                                                }
                                        }

                                    }
                                    if(text!=""){
                                        elementosexaminar.add(text);
                                    }
                                }
                                for (int u=0;u<elementosexaminar.size();u++){
                                    txv.append(elementosexaminar.get(u)+"\n");
                                }
                                //txv.setText(elementosexaminar.toString());
                            } else{
                                txv.setText("");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
    );

    /*
     * En esta parte se abre el navegador de archivos del dispositivo movil para que el usuario pueda elegir el archivo que requiera analizar
     * @param view
     * */
    public void AbrirArchivo(View view){
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("*/*");
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);

    }

    /*
     * Aqui se verifica si el archivo que selecciono es un archivo pgm
     * Mandando un mensaje dependiendo de que si el archivo si es un archivo pgm o no es un archivo pgm
     * @param pathArchivo
     * */
    public boolean extencion(String pathArchivo){

        boolean acreditado = false;
        String extencion_obtenida = "";

        if (pathArchivo.contains(".")){
            extencion_obtenida = pathArchivo.substring(pathArchivo.lastIndexOf(".")+1);
        }
        if (extencion_obtenida.equalsIgnoreCase("py")){
            acreditado = true;
            Toast.makeText(getApplicationContext(),"El archivo elegido es valido",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"El archivo elegido no es valido",Toast.LENGTH_SHORT).show();
        }

        return  acreditado;
    }
}