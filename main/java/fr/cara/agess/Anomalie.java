package fr.cara.agess;

import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by cara on 10/07/2017.
 */
//Cette classe représente n'importe quel anomalie présent dans la Table ANOMALIES de notre base de donnée ppcsys
public class Anomalie{
    private String fam;
    private String name;
    private String type;
    public boolean checked;



    public Anomalie(String fam, String name, String type){
        this.name = fam;
        this.name = name;
        this.name = type;
        checked = false;
    }

    public Anomalie(){

    }

    public String getFam(){
        return fam;
    }
    public void setFam(String fam){
        this.fam = fam;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
}

