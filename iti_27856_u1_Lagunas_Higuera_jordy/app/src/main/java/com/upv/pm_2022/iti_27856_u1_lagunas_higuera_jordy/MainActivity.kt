package com.upv.pm_2022.iti_27856_u1_lagunas_higuera_jordy


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val clean:Button=findViewById(R.id.clean)
        clean.setOnClickListener {
            Limpiar()
        }


    }

    private var requestCode = 1
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val txv:TextView=findViewById(R.id.archivos)
        var tempstring = ""
        if (requestCode == requestCode && resultCode == RESULT_OK) {
            if (data == null) return
            //Se detecta mas de un archivo seleccionado
            if (null != data.clipData) {
                for (i in 0 until data.clipData!!.itemCount) {
                    val uri = data.clipData!!.getItemAt(i).uri
                    try {
                        //print(readTextFromUri(uri))
                    }catch (ex:IOException){

                    }
                }
                txv.text = tempstring
                Toast.makeText(applicationContext,"Se ha creado la ruta RUTA:", Toast.LENGTH_SHORT).show()
                //solo se detecta un archivo seleccionado
            } else {
                val uri = data.data
                try {
                    print(readTextFromUri(uri))
                }catch (ex:IOException){
                    ex.printStackTrace()
                }catch (ex:Exception){
                    ex.printStackTrace()
                }

                //txv.text = uri!!.path
            }
            if(tempstring!= ""){
                Analizar(tempstring)
            }
        }
    }

    val contentResolver1 = applicationContext.contentResolver
    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri):String {
        val stringBuilder=StringBuilder()
        contentResolver1.openInputStream(uri)?.use{inputStream->
            BufferedReader(InputStreamReader(inputStream)).use {reader ->
                var line:String?=reader.readLine()
                while (line!=null){
                    stringBuilder.append(line)
                    line=reader.readLine()
                }
            }
        }

        return stringBuilder.toString()
    }

    private fun Analizar(tempstring: String) {
        var tempstring2 = tempstring.replace("\n", ",")
        val output: List<String> = tempstring2.split(",")
        var i:Int=0
        var aux:Int=0
        //Toast.makeText(applicationContext, "Se han cargado: "+output.size,Toast.LENGTH_SHORT).show()
        i=output.size
        i -= 1
        while(aux<i){
            leer(output[aux])
            aux+=1
        }
        //Toast.makeText(applicationContext, "Se ha creado la ruta RUTA:"+output[0],Toast.LENGTH_SHORT).show()
    }

    private fun leer(s: String) {
        var txv2:TextView=findViewById(R.id.com)
        //Toast.makeText(applicationContext,"Ruta del archivo:"+s,Toast.LENGTH_SHORT).show()
        try {
            // Ya existe el directorio
                val F2 = File(s)
            //Toast.makeText(applicationContext, "El archivo existe: "+F2.exists(),Toast.LENGTH_SHORT).show()
                if (F2.exists()) { // Checar si el archivo Existe
                    val fIn = FileInputStream(F2)
                    val myReader = BufferedReader(
                        InputStreamReader(fIn)
                    )
                    var aDataRow = ""
                    var aBuffer = ""
                    while (myReader.readLine().also { aDataRow = it } != null) {
                        aBuffer += """
                $aDataRow
                
                """.trimIndent()
                    }
                    txv2.setText(aBuffer);
                    myReader.close()
                    Toast.makeText(
                        baseContext,
                        "Se leyó el archivo",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    /*Toast.makeText(
                        applicationContext,
                        "No EXISTE tal ARCHIVO en el Directorio: $s", Toast.LENGTH_SHORT
                    ).show()
                    */
                }
        }catch (ex:Exception){
            Toast.makeText(applicationContext, "Error al leer el archivo",Toast.LENGTH_SHORT).show()
            ex.printStackTrace()
        }
    }


    fun openFilechooser(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, requestCode)
    }


    private fun Limpiar() {
        val archivos:TextView = findViewById(R.id.archivos)
        val comentarios:TextView = findViewById(R.id.comentarios)
        archivos.text = ""
        comentarios.text = ""
        //Toast.makeText(this,"Limpiando",Toast.LENGTH_LONG).show()
    }

}