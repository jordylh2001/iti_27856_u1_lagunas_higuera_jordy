package com.upv.pm_2022.iti_27856_u1_lagunas_higuera_jordy


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {
    var edt1: EditText? = null
    var edt2: EditText? = null
    var b1: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edt1 = findViewById(R.id.archivos)
        edt2 = findViewById(R.id.comentarios)
        val clean: Button = findViewById(R.id.clean)
        clean.setOnClickListener {
            Limpiar()
        }
        val b1: Button = findViewById(R.id.analize)
        b1.setOnClickListener { view ->
            AbrirArchivo(view) //Se llama a la funcion para abrir el explorador de archivos
        }


    }

    var sActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val uri = data!!.data
            try {
                //Se abre el archivo utilizando uri
                val `is` = applicationContext.contentResolver.openInputStream(
                    uri!!
                ) as FileInputStream?
                val isReader = BufferedReader(InputStreamReader(`is`))
                val paths = uri.path
                val Realpath = paths!!.split(":".toRegex()).toTypedArray()
                if (extencion(Realpath[1])) {
                    edt1?.append(Realpath[1]+"\n")
                    var Row:String = "";
                    //Se crea un arraylist en el cual se van a guardar los datos que se necesitan para verificar que se cumplan con las dimensiones establecidas en el encabezado
                    val elementosexaminar:ArrayList<String> = ArrayList<String>()
                    var aux1: Int
                    var aux3:Int = 0
                    //ciclo en el cual se agregan al arraylist las lineas que no contengan "P" o que contenga "#"
                    while ((Row=isReader.readLine()) != null) {/*
                        aux1 = 0
                        var text = ""
                        var r = 0
                        while (r < Row.length) {
                            when (Row[r]) {
                                '"' -> if (Row[r] == '"' && Row[r + 1] == '"' && Row[r + 2] == '"') {
                                    aux3 = if (aux3 == 0) {
                                        3
                                    } else {
                                        0
                                    }
                                    r += 2
                                }
                                '#' -> aux1 = 1
                                else -> if (aux1 == 1 || aux3 == 3) {
                                    text = text + Row[r]
                                }
                            }
                            r++
                        }
                        if (text !== "") {
                            elementosexaminar.add(text)
                        }
                    }
                    for (u in elementosexaminar.indices) {
                        edt2?.append(
                            """
                                                      ${elementosexaminar[u]}
                                                      
                                                      """.trimIndent()
                        )*/
                    }
                    //edt2.setText(elementosexaminar.toString());
                } else {
                    edt2?.setText("")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /*
    * En esta parte se abre el navegador de archivos del dispositivo movil para que el usuario pueda elegir el archivo que requiera analizar
    * @param view
    * */
    fun AbrirArchivo(view: View?) {
        var data = Intent(Intent.ACTION_OPEN_DOCUMENT)
        data.type = "*/*"
        data = Intent.createChooser(data, "Choose a file")
        sActivityResultLauncher.launch(data)
    }

    /*
     * Aqui se verifica si el archivo que selecciono es un archivo pgm
     * Mandando un mensaje dependiendo de que si el archivo si es un archivo pgm o no es un archivo pgm
     * @param pathArchivo
     * */
    fun extencion(pathArchivo: String): Boolean {
        var acreditado = false
        var extencion_obtenida = ""
        if (pathArchivo.contains(".")) {
            extencion_obtenida = pathArchivo.substring(pathArchivo.lastIndexOf(".") + 1)
        }
        if (extencion_obtenida.equals("py", ignoreCase = true)) {
            acreditado = true
            Toast.makeText(applicationContext, "El archivo elegido es valido", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                applicationContext,
                "El archivo elegido no es valido",
                Toast.LENGTH_SHORT
            ).show()
        }
        return acreditado
    }

    private fun Limpiar() {
        val archivos: TextView = findViewById(R.id.archivos)
        val comentarios: TextView = findViewById(R.id.comentarios)
        archivos.text = ""
        comentarios.text = ""
        //Toast.makeText(this,"Limpiando",Toast.LENGTH_LONG).show()
    }

}