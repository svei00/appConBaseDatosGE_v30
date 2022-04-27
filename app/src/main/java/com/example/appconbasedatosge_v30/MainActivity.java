package com.example.appconbasedatosge_v30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
Antes que nada crear un archivo .java al nivel del MainActivity con el nombre AdminSQLiteOpenHelper

Ayuda en validar registro duplicado
https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists/20422217
 */

public class MainActivity extends AppCompatActivity {

    // Creamos y encapsulamos los objetos para poder pasar los parametros
    private EditText et1, et2, et3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creamos los Puentes de Comunicación Parte Lógica y Gráfica
        et1 = (EditText)findViewById(R.id.txtId);
        et2 = (EditText)findViewById(R.id.txtDesc);
        et3 = (EditText)findViewById(R.id.txtPrecio);
    }

    // Creamos los Métodos a utilizar
    // Método para Registrar los Productos
    public void brnRegister(View view){
        // Creamos un Objeto de la clase que hemos creado, es decir AdminSQLiteOpenHelper
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(this, "admon", null, 1); // Pide los 4 paramétros que le asignamos previamente, más el Context que es el this

        // Abrimos la base de datos en modo Lectura y Escritura mediante SQLDataBase
        SQLiteDatabase db1 = adm.getWritableDatabase(); // Método Lectura/Escritura

        // Pasamos los Datos introducidos por el Usuario
        String sId = et1.getText().toString();
        String sDesc = et2.getText().toString();
        String sPrecio = et3.getText().toString();

        // Validamos si el Usuario esta duplucado
        Cursor duplicate = db1.rawQuery("SELECT codigo FROM articulos WHERE codigo = " + sId, null);
        if (duplicate.getCount() >= 1) {
            Toast.makeText(this, "Lo Sentimos el ID ya ha sido Registrado",Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Intenta uno Diferente", Toast.LENGTH_SHORT).show();
        } else {
            // Validamos que los datos Ingresados por el Usuario son Correctos
            if (!sId.isEmpty() && !sDesc.isEmpty() && !sPrecio.isEmpty()) { // Recuerda que al anteponer el ! significa diferente de
                // Registramos los Valores dentro de la Base de Datos
                ContentValues save = new ContentValues();

                // Mediante put pasamos los valores a la Base de Datos
                save.put("codigo", sId);
                save.put("descripcion", sDesc);
                save.put("precio", sPrecio);

                // Los Insertamos Dentro de la Tabla mediante el comando SQL insert
                db1.insert("articulos", null, save); // Con la siguiente sintaxis: articulos, null, save

                // Cerramos la Base de Datos
                db1.close();

                // Limpiamos los campos donde el usuario ha escrito.
                et1.setText("");
                et2.setText("");
                et3.getText().clear(); // Cualquiera de estos tres métodos sirven para cerrar la Base de Datos

                // Indicamos al Usuario que el Registro ha Sido Exitoso
                Toast.makeText(this, "Se ha Registrado el Producto de Manera Exitosa", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Favor de Llenar Todos los Campos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para Consultar un Producto
    public void btnConsultar (View view) {
        // Nuevamente creamos el objeto de que hemos creado para acceder a la Base de Datos
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(this, "admon", null, 1);

        // Abrimos la Base de Datos en Modo Apertura y Escritura
        SQLiteDatabase db1 = adm.getWritableDatabase();

        // Recuperamos el Valor que Vamos a buscar
        String sId = et1.getText().toString();

        // Creamos una validación con el Fin de que el Usuario deje ese campo vacío
        if (!sId.isEmpty()) {
            // Seleccionamos el Producto a través del Código mediante Cursor
            Cursor row = db1.rawQuery
                    ("SELECT descripcion, precio FROM articulos WHERE codigo = " + sId, null); // Con rawQuery nos permitirá enviar la sentencia SELECT de SQL

            // Estructura condicional que nos permitirá llenar los datos en caso de que se encuentren registros en la tabla
            if (row.moveToFirst()) { // Con este metodo identificamos que la consulta si tenga valores, en otras palabras como su nombre lo dice lo mandamos la primer fila
                et2.setText(row.getString(0)); // Asignamos el valor con la variable que creamos en Cursos, es decir row y 0
                et3.setText(row.getString(1));

                // Cerramos la Base de Datos
                db1.close();

            } else {
                Toast.makeText(this,"El ID del Procto no Existe",Toast.LENGTH_SHORT).show();

                // Cerramos la Base de Datos
                db1.close();
            }

        } else {
            Toast.makeText(this, "Favor de Introducir el ID del Producto", Toast.LENGTH_SHORT).show();

        }
    }

    // Método Modificar (Update)
    public void btnUpdate (View view){
        // Creamos el Objeto para Administrar nuestra Base de Datos
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper(this, "admon", null, 1);

        // Abrimos la Base de Datos en modo Lectura y Escritura
        SQLiteDatabase db1 = adm.getWritableDatabase();

        // Guardamos en las Siguientes variables los datos introducidos por el usuario
        String sId = et1.getText().toString();
        String sDesc = et2.getText().toString();
        String sPrecio = et3.getText().toString();

        // Creamos los validadores para evitar que el usuario deje campos en blanco
        if (!sId.isEmpty() && !sDesc.isEmpty() && !sPrecio.isEmpty()) {
            // Creamos el Objeto de la Clase Content Values
            ContentValues update = new ContentValues();

            // Guardamos en el objeto los cambios hechos por el usuario
            update.put("codigo", sId);
            update.put("descripcion", sDesc);
            update.put("precio", sPrecio);

            // Para realizar el registro dentro de la base de datos lo guardamos en una variable de tipo entero
            // Esta línea de Código es la que nos permitirá guardar en la base de datos
            int iCant = db1.update("articulos", update, "codigo = " + sId, null); // En la variable iCant ponemos el nombre de labase de datos seguido del objeto update que hemos creado más los paraemteos

            // Cerramos la Base de Datos
            db1.close();

            // Indicamos al usuario si el artículo fue modificado o no existe
            if (iCant == 1) {
                Toast.makeText(this, "El Arículo ha sido Modificado con Exito", Toast.LENGTH_SHORT).show();

                // Limpiamos los formularaios.
                et1.setText(null);
                et2.getText().clear();
                et3.setText(""); // Cualquiera de las trres formas funciona

            } else {
                Toast.makeText(this, "El Artículo Ingresado no Existe", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Favor de Llenar todos los Campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método Eliminar (Del)
    public void btnDel (View view){
        // Creamos el Objeto que nos Permite Administrar Nuestra Base de Datos
        AdminSQLiteOpenHelper adm = new AdminSQLiteOpenHelper
                (this, "admon", null, 1); // Pasamos los 4 paramétros

        // Creamos el Objeto para permitir la Lectura y Escritura de la Base de Datos
        SQLiteDatabase db1 = adm.getWritableDatabase();

        // Recuperamos el Dato del Artículo que se desea Eliminar
        String sId = et1.getText().toString();

        // Validamos el Campo
        if(!sId.isEmpty()){
            // Eliminamos el Producto mediante una variable de caracter entero ya que el método delete es el resultado que regresa
            int iCant = db1.delete("articulos", "codigo = "+ sId, null ); // Método Delete que viene de SQL

            // Cerramos la Base de Datos
            db1.close();

            // Limpiamos los Campos
            et1.getText().clear(); // Cualquiera de las tres funciona
            et2.setText(null);
            et3.setText("");

            // Estructura Condicional en Caso de que se cumpla la Condición
            if (iCant == 1) {
                // Mostramos al Usuario que se el artículo ha sido eliminado
                Toast.makeText(this, "El Artículo ha sido Eliminado Exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                // Mostramos al Usuario que el artículo no existe
                Toast.makeText(this, "El Artículo Específicado no Existe", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Favor de Introducir el ID del Artículo", Toast.LENGTH_SHORT).show();
        }
    }
}