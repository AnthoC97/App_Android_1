package fr.cara.agess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cara on 17/07/2017.
 */
//Cette classe poursuit le même raisonnenement
public class OperationDB {
    private static final String DB_FILE = Environment.getExternalStorageDirectory() + "/BDD/ppcsys/ppcsysM.db";
    private static final String TABLE_OPERATION = "OPERATIONS";

    private static final String COL_FAM = "FAM";
    private static final int NUM_COL_FAM = 0;
    private static final String COL_OPERATION= "OPERATION";
    private static final int NUM_COL_OPERATION = 1;
    private static final String COL_NUMOP= "NUMOP";
    private static final int NUM_COL_NUMOP= 2;
    private SQLiteDatabase db;

    public OperationDB(){

    }

    public void close(){
        db.close();
    }

    public List<Operation> getOperation(String fam){
        List<Operation> list = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_OPERATION+" WHERE "+COL_FAM+" = '"+fam+"' ORDER BY "+COL_NUMOP+"";
        //On réccupère les différents contrôles
        try{
            openDB();
            Cursor c = db.rawQuery(query,null);
            try{
                if(c.moveToFirst()){
                    do{
                        Operation operation = new Operation();
                        operation.setFam(c.getString(NUM_COL_FAM));
                        operation.setOperation(c.getString(NUM_COL_OPERATION));
                        operation.setNumop(c.getInt(NUM_COL_NUMOP));
                        list.add(operation);
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
        finally {
            try {
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
