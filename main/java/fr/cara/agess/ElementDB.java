package fr.cara.agess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cara on 12/06/2017.
 */

public class ElementDB {
    //Nom du dossier contenant les base de donnée
    private static final String DB_DIR = Environment.getExternalStorageDirectory() + "/BDD/";
    //non de la table présent dans les bases de données que l'on utilise
    private static final String TABLE_ELEMENT = "ELEMENTS";
    //Les différentes colonnes et leur index.
    private static final String COL_NUM = "NUM";
    private static final int NUM_COL_NUM = 0;
    private static final String COL_CODE = "CODE";
    private static final int NUM_COL_CODE = 1;
    private static final String COL_FAM = "FAM";
    private static final int NUM_COL_FAM = 2;
    private static final String COL_TYPE = "TYPE";
    private static final int NUM_COL_TYPE = 3;
    private static final String COL_SITE = "SITE";
    private static final int NUM_COL_SITE = 4;
    private static final String COL_BATIMENT = "BATIMENT";
    private static final int NUM_COL_BATIMENT = 5;
    private static final String COL_NIVEAU = "NIVEAU";
    private static final int NUM_COL_NIVEAU = 6;
    private static final String COL_ZONE = "ZONE";
    private static final int NUM_COL_ZONE = 7;
    private static final String COL_LIEU = "LIEU";
    private static final int NUM_COL_LIEU = 8;
    private static final String COL_INFOS = "INFOS";
    private static final int NUM_COL_INFOS = 9;
    private static final String COL_OPAUTOR = "OPAUTOR";
    private static final int NUM_COL_OPAUTOR = 10;
    private static final String COL_NCODE = "NCODE";
    private static final int NUM_COL_NCODE = 11;
    private static final String COL_DATE = "DATE";
    private static final int NUM_COL_DATE = 12;
    private static final String COL_OBSN = "OBSN";
    private static final int NUM_COL_OBSN = 13;
    private static final String COL_OBSA = "OBSA";
    private static final int NUM_COL_OBSA = 14;
    private static final String COL_CARACT = "CARACT";
    private static final int NUM_COL_CARACT = 15;
    private static final String COL_TOUREFF = "TOUREFF";
    private static final int NUM_COL_TOUREFF = 16;
    private static final String COL_OPEFF = "OPEFF";
    private static final int NUM_COL_OPEFF = 17;
    private static final String COL_NUMOP = "NUMOP";
    private static final int NUM_COL_NUMOP = 18;
    private static final String COL_VALID = "VALID";
    private static final int NUM_COL_VALID = 19;
    private static final String COL_OPERA = "OPERA";
    private static final int NUM_COL_OPERA = 20;
    private static final String COL_ALERT = "ALERT";
    private static final int NUM_COL_ALERT = 21;
    private static final String COL_NBRE = "NBRE";
    private static final int NUM_COL_NBRE = 22;
    private static final String COL_TOURAF = "TOURAF";
    private static final int NUM_COL_TOURAF = 23;
    private static final String COL_OPAF = "OPAF";
    private static final int NUM_COL_OPAF = 24;
    private static final String COL_ENTITE = "ENTITE";
    private static final int NUM_COL_ENTITE = 25;
    private static final String COL_IMAGES = "IMAGES";
    private static final int NUM_COL_IMAGES = 26;
    private static final String COL_DATE_PROVERIF = "DATE_PROVERIF";
    private static final int NUM_COL_DATE_PROVERIF = 27;
    private static final String COL_DATE_PEREM = "DATE_PEREM";
    private static final int NUM_COL_DATE_PEREM = 28;
    private static final String COL_DATE_PROCYCLE = "DATE_PROCYCLE";
    private static final int NUM_COL_DATE_PROCYCLE = 29;
    private static final String COL_DATE_PROINTERNE = "DATE_PROINTERNE";
    private static final int NUM_COL_DATE_PROINTERNE = 30;
    private static final String COL_DATE_PROCTRL = "DATE_PROCTRL";
    private static final int NUM_COL_DATE_PROCTRL = 31;
    private static final String COL_DOCUMENT = "DOCUMENT";
    private static final int NUM_COL_DOCUMENT = 32;
    private static final String COL_PUCE = "PUCE";
    private static final int NUM_COL_PUCE = 33;
    private static final String COL_DELAI = "DELAI";
    private static final int NUM_COL_DELAI = 34;
    //Variable qui représentera le non de la base de donnée
    private String nomBdd;
    private String[] colonnes = new String[]{COL_NUM, COL_CODE, COL_FAM ,COL_TYPE,
            COL_SITE, COL_BATIMENT, COL_NIVEAU,COL_ZONE,COL_LIEU,COL_INFOS,COL_OPAUTOR,COL_NCODE,
            COL_DATE,COL_OBSN,COL_OBSA,COL_CARACT,COL_TOUREFF,COL_OPEFF,COL_NUMOP,COL_VALID,
            COL_OPERA,COL_ALERT,COL_NBRE,COL_TOURAF,COL_OPAF,COL_ENTITE,COL_IMAGES,
            COL_DATE_PROVERIF,COL_DATE_PEREM,COL_DATE_PROCYCLE,COL_DATE_PROINTERNE,COL_DATE_PROCTRL,
            COL_DOCUMENT,COL_PUCE,COL_DELAI};

    //Variable qui représentera la base de donnée
    private SQLiteDatabase db;
    boolean copyDB;

    public ElementDB(String nomBdd, Boolean b){
        this.nomBdd = nomBdd;
        if(b == null || b == false){
            copyDB = false;
        }
        else{
            copyDB = true;
        }
    }
    //Cette méthode ferme la base de donnée
    public void close(){
        db.close();
    }
    //Methode permettant de ce déplacer a travert les éléments sélectionées

    private Element cursorToExtincteur(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Element element = new Element();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        element.setNum(c.getInt(NUM_COL_NUM));
        element.setCode(c.getString(NUM_COL_CODE));
        element.setFam(c.getString(NUM_COL_FAM));
        element.setType(c.getString(NUM_COL_TYPE));
        element.setSite(c.getString(NUM_COL_SITE));
        element.setBatiment(c.getString(NUM_COL_BATIMENT));
        element.setNiveau(c.getString(NUM_COL_NIVEAU));
        element.setZone(c.getString(NUM_COL_ZONE));
        element.setLieu(c.getString(NUM_COL_LIEU));
        element.setInfos(c.getString(NUM_COL_INFOS));
        element.setOpautor(c.getString(NUM_COL_OPAUTOR));
        element.setNcode(c.getString(NUM_COL_NCODE));
        element.setDate(c.getString(NUM_COL_DATE));
        element.setObsn(c.getString(NUM_COL_OBSN));
        element.setObsa(c.getString(NUM_COL_OBSA));
        element.setCaract(c.getString(NUM_COL_CARACT));
        element.setToureff(c.getString(NUM_COL_TOUREFF));
        element.setOpeff(c.getString(NUM_COL_OPEFF));
        element.setNumop(c.getString(NUM_COL_NUMOP));
        element.setValid(c.getString(NUM_COL_VALID));
        element.setOpera(c.getString(NUM_COL_OPERA));
        element.setAlert(c.getString(NUM_COL_ALERT));
        element.setNbre(c.getString(NUM_COL_NBRE));
        element.setTouraf(c.getString(NUM_COL_TOURAF));
        element.setOpaf(c.getString(NUM_COL_OPAF));
        element.setEntite(c.getString(NUM_COL_ENTITE));
        element.setImages(c.getString(NUM_COL_IMAGES));
        element.setDate_proverif(c.getString(NUM_COL_DATE_PROVERIF));
        element.setDate_perem(c.getString(NUM_COL_DATE_PEREM));
        element.setDate_procycle(c.getString(NUM_COL_DATE_PROCYCLE));
        element.setDate_prointerne(c.getString(NUM_COL_DATE_PROINTERNE));
        element.setDate_proctrl(c.getString(NUM_COL_DATE_PROCTRL));
        element.setDocument(c.getString(NUM_COL_DOCUMENT));
        element.setPuce(c.getString(NUM_COL_PUCE));
        element.setDelai(c.getString(NUM_COL_DELAI));
        //On ferme le cursor
        c.close();
        close();

        //On retourne l'element
        System.out.println(element.getNum());
        return element;
    }

    public String getDbPath(){
        return DB_DIR+nomBdd;
    }

    //Cette méthode retourne une liste d'element
    public List<Element> getElement(){
        List<Element> list = new ArrayList<Element>();
        String requete = "SELECT * FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(requete,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        Element element = new Element();
                        element.setNum(c.getInt(NUM_COL_NUM));
                        element.setCode(c.getString(NUM_COL_CODE));
                        element.setFam(c.getString(NUM_COL_FAM));
                        element.setType(c.getString(NUM_COL_TYPE));
                        element.setSite(c.getString(NUM_COL_SITE));
                        element.setBatiment(c.getString(NUM_COL_BATIMENT));
                        element.setNiveau(c.getString(NUM_COL_NIVEAU));
                        element.setZone(c.getString(NUM_COL_ZONE));
                        element.setLieu(c.getString(NUM_COL_LIEU));
                        element.setInfos(c.getString(NUM_COL_INFOS));
                        element.setOpautor(c.getString(NUM_COL_OPAUTOR));
                        element.setNcode(c.getString(NUM_COL_NCODE));
                        element.setDate(c.getString(NUM_COL_DATE));
                        element.setObsn(c.getString(NUM_COL_OBSN));
                        element.setObsa(c.getString(NUM_COL_OBSA));
                        element.setCaract(c.getString(NUM_COL_CARACT));
                        element.setToureff(c.getString(NUM_COL_TOUREFF));
                        element.setOpeff(c.getString(NUM_COL_OPEFF));
                        element.setNumop(c.getString(NUM_COL_NUMOP));
                        element.setValid(c.getString(NUM_COL_VALID));
                        element.setOpera(c.getString(NUM_COL_OPERA));
                        element.setAlert(c.getString(NUM_COL_ALERT));
                        element.setNbre(c.getString(NUM_COL_NBRE));
                        element.setTouraf(c.getString(NUM_COL_TOURAF));
                        element.setOpaf(c.getString(NUM_COL_OPAF));
                        element.setEntite(c.getString(NUM_COL_ENTITE));
                        element.setImages(c.getString(NUM_COL_IMAGES));
                        element.setDate_proverif(c.getString(NUM_COL_DATE_PROVERIF));
                        element.setDate_perem(c.getString(NUM_COL_DATE_PEREM));
                        element.setDate_procycle(c.getString(NUM_COL_DATE_PROCYCLE));
                        element.setDate_prointerne(c.getString(NUM_COL_DATE_PROINTERNE));
                        element.setDate_proctrl(c.getString(NUM_COL_DATE_PROCTRL));
                        element.setDocument(c.getString(NUM_COL_DOCUMENT));
                        element.setPuce(c.getString(NUM_COL_PUCE));
                        element.setDelai(c.getString(NUM_COL_DELAI));

                        list.add(element);
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //Cette méthode retourne une liste d'element
    public List<Element> getElementValid(){
        List<Element> list = new ArrayList<Element>();
        String requete = "SELECT * FROM "+TABLE_ELEMENT+" WHERE "+COL_VALID+" = 'Y'";
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(requete,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        Element element = new Element();
                        element.setNum(c.getInt(NUM_COL_NUM));
                        element.setCode(c.getString(NUM_COL_CODE));
                        element.setFam(c.getString(NUM_COL_FAM));
                        element.setType(c.getString(NUM_COL_TYPE));
                        element.setSite(c.getString(NUM_COL_SITE));
                        element.setBatiment(c.getString(NUM_COL_BATIMENT));
                        element.setNiveau(c.getString(NUM_COL_NIVEAU));
                        element.setZone(c.getString(NUM_COL_ZONE));
                        element.setLieu(c.getString(NUM_COL_LIEU));
                        element.setInfos(c.getString(NUM_COL_INFOS));
                        element.setOpautor(c.getString(NUM_COL_OPAUTOR));
                        element.setNcode(c.getString(NUM_COL_NCODE));
                        element.setDate(c.getString(NUM_COL_DATE));
                        element.setObsn(c.getString(NUM_COL_OBSN));
                        element.setObsa(c.getString(NUM_COL_OBSA));
                        element.setCaract(c.getString(NUM_COL_CARACT));
                        element.setToureff(c.getString(NUM_COL_TOUREFF));
                        element.setOpeff(c.getString(NUM_COL_OPEFF));
                        element.setNumop(c.getString(NUM_COL_NUMOP));
                        element.setValid(c.getString(NUM_COL_VALID));
                        element.setOpera(c.getString(NUM_COL_OPERA));
                        element.setAlert(c.getString(NUM_COL_ALERT));
                        element.setNbre(c.getString(NUM_COL_NBRE));
                        element.setTouraf(c.getString(NUM_COL_TOURAF));
                        element.setOpaf(c.getString(NUM_COL_OPAF));
                        element.setEntite(c.getString(NUM_COL_ENTITE));
                        element.setImages(c.getString(NUM_COL_IMAGES));
                        element.setDate_proverif(c.getString(NUM_COL_DATE_PROVERIF));
                        element.setDate_perem(c.getString(NUM_COL_DATE_PEREM));
                        element.setDate_procycle(c.getString(NUM_COL_DATE_PROCYCLE));
                        element.setDate_prointerne(c.getString(NUM_COL_DATE_PROINTERNE));
                        element.setDate_proctrl(c.getString(NUM_COL_DATE_PROCTRL));
                        element.setDocument(c.getString(NUM_COL_DOCUMENT));
                        element.setPuce(c.getString(NUM_COL_PUCE));
                        element.setDelai(c.getString(NUM_COL_DELAI));

                        list.add(element);
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //Cette méthode retourne un element dont le code est renseigné
    public Element getElementWithCODE(String code){
        openDB();
        Cursor c = db.query(TABLE_ELEMENT, colonnes, COL_CODE + " LIKE \"" + code +"\"", null, null, null, null);
        return cursorToExtincteur(c);
    }

    //Cette méthode retourne un element dont l'id est renseigné
    public Element getElementWithID(int id){
        openDB();
        Cursor c = db.query(TABLE_ELEMENT, colonnes, COL_NUM + " LIKE \"" + id +"\"", null, null, null, null);
        return cursorToExtincteur(c);
    }
    //Cette méthode ouvre une base de donnée
    public void openDB(){
        db = SQLiteDatabase.openOrCreateDatabase(DB_DIR+nomBdd,null);
    }

    public void openDBC(){
        db = SQLiteDatabase.openOrCreateDatabase(DB_DIR+"BDD-Fin_de_travail/"+nomBdd,null);
    }

    public void nonSelectQuery(String query){
        if(copyDB){
            openDBC();
            db.execSQL(query);
            close();
        }
        else{
            openDB();
            db.execSQL(query);
            close();
        }
    }
    //cette méthode insert des éléments dans une base de donnée
    public void insert(Element e){
        if(copyDB){
            openDBC();
        }
        else{
            openDB();
        }
        ContentValues value = new ContentValues();
        value.put(COL_NUM,e.getNum());
        value.put(COL_CODE,e.getCode());
        value.put(COL_FAM,e.getFam());
        value.put(COL_TYPE,e.getType());
        value.put(COL_SITE,e.getSite());
        value.put(COL_BATIMENT,e.getBatiment());
        value.put(COL_NIVEAU,e.getNiveau());
        value.put(COL_ZONE,e.getZone());
        value.put(COL_LIEU,e.getLieu());
        value.put(COL_INFOS,e.getInfos());
        value.put(COL_OPAUTOR,e.getOpautor());
        value.put(COL_NCODE,e.getNcode());
        value.put(COL_DATE,e.getDate());
        value.put(COL_OBSN,e.getObsn());
        value.put(COL_OBSA,e.getObsa());
        value.put(COL_CARACT,e.getCaract());
        value.put(COL_TOUREFF,e.getToureff());
        value.put(COL_OPEFF,e.getOpeff());
        value.put(COL_NUMOP,e.getNumop());
        value.put(COL_VALID,e.getValid());
        value.put(COL_OPERA,e.getOpera());
        value.put(COL_ALERT,e.getAlert());
        value.put(COL_NBRE,e.getNbre());
        value.put(COL_TOURAF,e.getTouraf());
        value.put(COL_OPAF,e.getOpaf());
        value.put(COL_ENTITE,e.getEntite());
        value.put(COL_IMAGES,e.getImages());
        value.put(COL_DATE_PROVERIF,e.getDate_proverif());
        value.put(COL_DATE_PEREM,e.getDate_perem());
        value.put(COL_DATE_PROCYCLE,e.getDate_procycle());
        value.put(COL_DATE_PROINTERNE,e.getDate_prointerne());
        value.put(COL_DATE_PROCTRL,e.getDate_proctrl());
        value.put(COL_DOCUMENT,e.getDocument());
        value.put(COL_PUCE,e.getPuce());
        value.put(COL_DELAI,e.getDelai());
        long t = db.insert(TABLE_ELEMENT,null,value);
        Log.d("Insert", String.valueOf(t));
        close();
    }

    //fontion retournants toute les familles distinctes de nos éléments
    public List<String> getFamilles(){
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT "+COL_FAM+" FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        list.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //fontion retournants toute les familles distinctes de nos éléments
    public List<String> getTypes(){
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT "+COL_TYPE+" FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        list.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //fontion retournants toute les familles distinctes de nos éléments
    public List<String> getSites(){
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT "+COL_SITE+" FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        list.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //fontion retournants toute les familles distinctes de nos éléments
    public List<String> getBatiments(){
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT "+COL_BATIMENT+" FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        list.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //fontion retournants toute les familles distinctes de nos éléments
    public List<String> getNiveaux(){
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT "+COL_NIVEAU+" FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        list.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //fontion retournants toute les familles distinctes de nos éléments
    public List<String> getZones(){
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT "+COL_ZONE+" FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        list.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    //fontion retournants toute les familles distinctes de nos éléments
    public List<String> getLieux(){
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT "+COL_LIEU+" FROM "+TABLE_ELEMENT;
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        list.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }

    public List<Element> getElement(String query){
        List<Element> list = new ArrayList<Element>();
        try{
            //Ouverture de la base
            openDB();
            //Exécution de la requête
            Cursor  c = db.rawQuery(query,null);
            try {
                if (c.moveToFirst()) {
                    //On alimente la liste avec tout les éléments
                    do {
                        Element element = new Element();
                        element.setNum(c.getInt(NUM_COL_NUM));
                        element.setCode(c.getString(NUM_COL_CODE));
                        element.setFam(c.getString(NUM_COL_FAM));
                        element.setType(c.getString(NUM_COL_TYPE));
                        element.setSite(c.getString(NUM_COL_SITE));
                        element.setBatiment(c.getString(NUM_COL_BATIMENT));
                        element.setNiveau(c.getString(NUM_COL_NIVEAU));
                        element.setZone(c.getString(NUM_COL_ZONE));
                        element.setLieu(c.getString(NUM_COL_LIEU));
                        element.setInfos(c.getString(NUM_COL_INFOS));
                        element.setOpautor(c.getString(NUM_COL_OPAUTOR));
                        element.setNcode(c.getString(NUM_COL_NCODE));
                        element.setDate(c.getString(NUM_COL_DATE));
                        element.setObsn(c.getString(NUM_COL_OBSN));
                        element.setObsa(c.getString(NUM_COL_OBSA));
                        element.setCaract(c.getString(NUM_COL_CARACT));
                        element.setToureff(c.getString(NUM_COL_TOUREFF));
                        element.setOpeff(c.getString(NUM_COL_OPEFF));
                        element.setNumop(c.getString(NUM_COL_NUMOP));
                        element.setValid(c.getString(NUM_COL_VALID));
                        element.setOpera(c.getString(NUM_COL_OPERA));
                        element.setAlert(c.getString(NUM_COL_ALERT));
                        element.setNbre(c.getString(NUM_COL_NBRE));
                        element.setTouraf(c.getString(NUM_COL_TOURAF));
                        element.setOpaf(c.getString(NUM_COL_OPAF));
                        element.setEntite(c.getString(NUM_COL_ENTITE));
                        element.setImages(c.getString(NUM_COL_IMAGES));
                        element.setDate_proverif(c.getString(NUM_COL_DATE_PROVERIF));
                        element.setDate_perem(c.getString(NUM_COL_DATE_PEREM));
                        element.setDate_procycle(c.getString(NUM_COL_DATE_PROCYCLE));
                        element.setDate_prointerne(c.getString(NUM_COL_DATE_PROINTERNE));
                        element.setDate_proctrl(c.getString(NUM_COL_DATE_PROCTRL));
                        element.setDocument(c.getString(NUM_COL_DOCUMENT));
                        element.setPuce(c.getString(NUM_COL_PUCE));
                        element.setDelai(c.getString(NUM_COL_DELAI));

                        list.add(element);
                    } while (c.moveToNext());
                }
            }
            finally{
                try {
                    c.close();
                }
                catch (Exception e){

                }
            }
        }
        finally{
            try {
                close();
            }
            catch (Exception e){

            }
        }
        return list;
    }
}
