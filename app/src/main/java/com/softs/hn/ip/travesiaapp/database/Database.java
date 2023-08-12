package com.softs.hn.ip.travesiaapp.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import java.util.Date;


@androidx.room.Database(version = 1, exportSchema = false, entities = {Nota.class, Contacto.class})
public abstract class Database extends RoomDatabase {
    public abstract NotasDao NotasDao();
    public abstract ContactosDao ContactosDao();

    private static volatile Database INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static Database getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (Database.class){
                if(INSTANCE == null){

                    Callback miCallback = new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            databaseWriteExecutor.execute(() -> {
                                NotasDao dao = INSTANCE.NotasDao();
                                dao.deleteAll();


                                Date currentDate = new Date();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                String formattedDateTime = dateFormat.format(currentDate);
                                Nota nota1= new Nota("Roatan",formattedDateTime,"Comentario de esta nota","Juan Perez","10.10101","-1.55444");
                                Nota nota2= new Nota("Castillo de omoa",formattedDateTime,"Comentario de esta nota, este se supone que es mas grande.","Juan Perez","10.10101","-1.55444");
                                Nota nota3= new Nota("Santa Rosa de copan",formattedDateTime,"Este es el comentario de este lugar","Juan Perez","10.10101","-1.55444");
                                Nota nota4= new Nota("Copan Ruinas",formattedDateTime,"Este es el comentario de este lugar","Juan Perez","10.10101","-1.55444");

                                dao.insert(nota1);
                                dao.insert(nota2);
                                dao.insert(nota3);
                                dao.insert(nota4);

                                dao.insert(nota1);
                                dao.insert(nota2);
                                dao.insert(nota3);
                                dao.insert(nota4);
                            });

                        }
                    };
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "clientes_db").addCallback(miCallback).build();
                }
            }
        }
        return INSTANCE;
    }
}
