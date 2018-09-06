package fr.cara.agess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cara on 01/08/2017.
 */

public class CaracteristiqueDB {
    private static final String DB_FILE = Environment.getExternalStorageDirectory() + "/BDD/ppcsys/ppcsysM.db";
    private static final String TABLE_CARACTERISTIQUE = "CARACTERISTIQUES";

    private static final String COL_FAM = "FAM";
    private static final int NUM_COL_FAM = 0;
    private static final String COL_CARACT= "caract";
    private static final int NUM_COL_CARACT = 1;
    private static final String COL_TYPE= "TYPE";
    private static final int NUM_COL_TYPE= 2;
    private static final String COL_VALEUR= "VALEUR";
    private static final int NUM_COL_VALEUR= 3;
    private static final String COL_OBLIGATOIRE= "OBLIGATOIRE";
    private static final int NUM_COL_OBLIGATOIRE= 4;
    private static final String COL_POSITION= "POSITION";
    private static final int NUM_COL_POSITION= 5;
    private static final String COL_INITIALE= "INITIALE";
    private static final int NUM_COL_INITIALE= 6;
    private static final String COL_NOM= "NOM";
    private static final int NUM_COL_NOM= 7;
    private SQLiteDatabase db;

    public CaracteristiqueDB(){

    }

    public void close(){
        db.close();
    }

    public List<Caracteristique> getCaracteristique(String fam){
            List<Caracteristique> list = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_CARACTERISTIQUE+" WHERE "+COL_FAM+" = '"+fam+"' ORDER BY "+COL_POSITION+"";
        //On réccupère les différents contrôles
        try{
            openDB();
            Cursor c = db.rawQuery(query,null);
            try{
                if(c.moveToFirst()){
                    do{
                        Caracteristique caracteristique =  new Caracteristique();
                        caracteristique.setFam(c.getString(NUM_COL_FAM));
                        caracteristique.setCaract(c.getString(NUM_COL_CARACT));
                        caracteristique.setType(c.getString(NUM_COL_TYPE));
                        caracteristique.setObligatoire(c.getInt(NUM_COL_OBLIGATOIRE));
                        caracteristique.setPosition(c.getInt(NUM_COL_POSITION));
                        caracteristique.setInitiale(c.getString(NUM_COL_INITIALE));
                        caracteristique.setNom(c.getString(NUM_COL_NOM));
                        caracteristique.setValeur(c.getString(NUM_COL_VALEUR));
                        list.add(caracteristique);
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
