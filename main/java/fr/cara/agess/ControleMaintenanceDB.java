package fr.cara.agess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cara on 17/07/2017.
 */
//Cette classe poursuit le même raisonnenement
public class ControleMaintenanceDB {
    private static final String DB_FILE = Environment.getExternalStorageDirectory() + "/BDD/ppcsys/ppcsysM.db";
    private static final String TABLE_TYPE = "TYPES";

    private static final String COL_FAM = "FAM";
    private static final int NUM_COL_FAM = 0;
    private static final String COL_TYPE = "type";
    private static final int NUM_COL_TYPE = 1;
    private static final String COL_OPV= "OpV";
    private static final int NUM_COL_OPV = 2;
    private static final String COL_OPA= "OpA";
    private static final int NUM_COL_OPA = 3;
    private static final String COL_OPP = "OpP";
    private static final int NUM_COL_OPP = 4;
    private static final String COL_OPQ = "OpQ";
    private static final int NUM_COL_OPQ = 5;
    private static final String COL_OPR = "OpR";
    private static final int NUM_COL_OPR = 6;
    private static final String COL_MULTI = "MULTI";
    private static final int NUM_COL_MULTI = 7;
    private static final String COL_CARACTS = "CARACTS";
    private static final int NUM_COL_CARACTS = 8;
    private SQLiteDatabase db;

    public ControleMaintenanceDB(){

    }

    public void close(){
        db.close();
    }
    //On obtient les 5 contrôles de maintenance pour un élément donné grace à sa famille et son type.
    public List<ControleMaintenance> getControlesMaintenance(String fam, String type){
        List<ControleMaintenance> list = new ArrayList<>();
        String query;
        //Si l'élément a un type
        if (!type.equals("")){
            List<String> listFamType = new ArrayList<>();
            listFamType.addAll(getAllFamxType());
            String famType = fam+" "+type;
            //On vérifie si le type renseigné est connu
            boolean allInfo = false;
            for (int i = 0; i < listFamType.size(); i++){
                if(famType.equals(listFamType.get(i))){
                    allInfo = true;
                    break;
                }
            }
            if(allInfo){
                query = "SELECT * FROM "+TABLE_TYPE+" WHERE "+COL_FAM+" = '"+fam+"' AND "+COL_TYPE+" = '"+type+"'";
            }
            else{
                query = "SELECT * FROM "+TABLE_TYPE+" WHERE "+COL_FAM+" = '"+fam+"'";
            }
        }
        else {
            query = "SELECT * FROM "+TABLE_TYPE+" WHERE "+COL_FAM+" = '"+fam+"'";
        }
        //On réccupère les différents contrôles
        try{
            openDB();
            Cursor c = db.rawQuery(query,null);
            try{
                if(c.getCount() == 0){
                    return null;
                }
                c.moveToFirst();
                SimpleDateFormat d = new SimpleDateFormat("d/MM/yy");
                ControleMaintenance controleMaintenance = new ControleMaintenance();
                controleMaintenance.setName(c.getString(NUM_COL_OPV));
                controleMaintenance.setDate(d.format(new Date()));
                controleMaintenance.setKey("V");
                list.add(controleMaintenance);


                controleMaintenance = new ControleMaintenance();
                controleMaintenance.setName(c.getString(NUM_COL_OPA));
                controleMaintenance.setDate(d.format(new Date()));
                controleMaintenance.setKey("A");
                list.add(controleMaintenance);


                controleMaintenance = new ControleMaintenance();
                controleMaintenance.setName(c.getString(NUM_COL_OPP));
                controleMaintenance.setDate(d.format(new Date()));
                controleMaintenance.setKey("P");
                list.add(controleMaintenance);


                controleMaintenance = new ControleMaintenance();
                controleMaintenance.setName(c.getString(NUM_COL_OPQ));
                controleMaintenance.setDate(d.format(new Date()));
                controleMaintenance.setKey("Q");
                list.add(controleMaintenance);


                controleMaintenance = new ControleMaintenance();
                controleMaintenance.setName(c.getString(NUM_COL_OPR));
                controleMaintenance.setDate(d.format(new Date()));
                controleMaintenance.setKey("R");
                list.add(controleMaintenance);
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
    //Retourne tout les couples FAM-type de la base de donnée saus la forme "FAM type"
    public List<String> getAllFamxType(){
        openDB();
        String query;
        query = "SELECT * FROM "+TABLE_TYPE;
        List<String> listFamXType = new ArrayList<String>();
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                String fam = c.getString(NUM_COL_FAM);
                String type = c.getString(NUM_COL_TYPE);
                listFamXType.add(fam+" "+type);
            }while (c.moveToNext());
        }
        close();
        return listFamXType;
    }

    public void openDB(){
        db = SQLiteDatabase.openOrCreateDatabase(DB_FILE,null);
    }
}
