package com.example.basesdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etGrupo;
    EditText etDisco;
    TextView resultado;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos las variables
        etGrupo=findViewById(R.id.edGrupo);
        etDisco=findViewById(R.id.edDisco);
        resultado=findViewById(R.id.tvResultado);

        //Creamos la base de datos y la tabla SI NO EXISTEN!!!
        db=openOrCreateDatabase("MisDiscos", Context.MODE_PRIVATE,null);

        db.execSQL("CREATE TABLE IF NOT EXISTS misDiscos(Grupo VARCHAR,Disco VARCHAR);");

    }
    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void anadir(View view){
        //añadimos la informacion a la base de datos
        db.execSQL("INSERT INTO misDiscos VALUES('"+etGrupo.getText().toString()+"',+'"+etDisco.getText().toString()+"')");
        limpiar();
    }
    public void editar(View view){
        //editamos información de la base de datos
        db.execSQL("UPDATE misDiscos SET Disco='"+etDisco.getText().toString()+"' WHERE Grupo='"+etGrupo.getText().toString()+"';");
        limpiar();
    }
    public void borrar(View view){
        //borramos info de la base de datos
        db.execSQL("DELETE FROM misDiscos WHERE Grupo ='"+etGrupo.getText().toString()+"';");
        limpiar();
    }
    public void buscar(View view){
        //mostramos el resultado en el text view
        String result = "";
        Cursor c = null;

        try {
            if (etGrupo.getText().toString().isEmpty()){
                c=db.rawQuery("SELECT * FROM misDiscos",null);
                if(c.getCount()==0)
                    result="No hay datos en la BD";
                else
                    while(c.moveToNext())
                        result=result+"\n"+c.getString(0)+"-"+c.getString(1);
            }
            else{
                c=db.rawQuery("SELECT * FROM misDiscos WHERE Grupo='\"+etGrupo.getText().toString()+\"'",null);
                if(c.getCount()==0)
                    result="No existe ese grupo";
                else
                    while(c.moveToNext())
                        result=result+"\n"+c.getString(0)+"-"+c.getString(1);

            }
        }catch (Exception e){
            result=e.toString();
        }finally {
            c.close();
        }
        resultado.setText(result);
    }
    public void limpiar(){
        etGrupo.setText("");
        etDisco.setText("");
    }
}