package fr.cara.agess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cara on 10/07/2017.
 */
//Cette classe poursuit le même raisonnement que ElementDB
public class AnomalieDB {
    private static final String DB_FILE = Environment.getExternalStorageDirectory() + "/BDD/ppcsys/ppcsysM.db";
    private static final String TABLE_ANOMALIE = "ANOMALIES";

    private static final String COL_FAM = "FAM";
    private static final int NUM_COL_FAM = 0;
    private static final String COL_ANOMALIE = "anomalie";
    private static final int NUM_COL_ANOMALIE = 1;
    private static final String COL_TYPE = "TYPE";
    private static final int NUM_COL_TYPE = 2;
    private SQLiteDatabase db;

    public AnomalieDB(){

    }

    public void close(){
        db.close();
    }

    public List<Anomalie> getAnomalies(){
        List<Anomalie> list = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_ANOMALIE;
        try{
            openDB();
            Cursor c = db.rawQuery(query,null);
            try{
                if(c.moveToFirst()){
                    do{
                        Anomalie anomalie = new Anomalie();
                        anomalie.setFam(c.getString(NUM_COL_FAM));
                        anomalie.setName(c.getString(NUM_COL_ANOMALIE));
                        anomalie.setType((c.getString(NUM_COL_TYPE)));
                        list.add(anomalie);
                    }while(c.moveToNext());
                }
            }
            finally {
                try{
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try{
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    public void openDB(){
        db = SQLiteDatabase.openOrCreateDatabase(DB_FILE,null);
    }

}
