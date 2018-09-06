package fr.cara.agess;

/**
 * Created by cara on 01/08/2017.
 */

public class Caracteristique {
    private String fam;
    private String caract;
    private String type;
    private String valeur;
    private int obligatoire;
    private int position;
    private String initiale;
    private String nom;
    private String newVal;

    public Caracteristique(String fam, String caract, String type, String valeur, int obligatoire, int position, String initiale, String nom, String newVal) {
        this.fam = fam;
        this.caract = caract;
        this.type = type;
        this.valeur = valeur;
        this.obligatoire = obligatoire;
        this.position = position;
        this.initiale = initiale;
        this.nom = nom;
        this.newVal = newVal;
    }

    public  Caracteristique(){

    }

    public String getFam() {
        return fam;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public String getCaract() {
        return caract;
    }

    public void setCaract(String caract) {
        this.caract = caract;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public int getObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(int obligatoire) {
        this.obligatoire = obligatoire;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getInitiale() {
        return initiale;
    }

    public void setInitiale(String initiale) {
        this.initiale = initiale;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNewVal() {
        return newVal;
    }

    public void setNewVal(String newVal){
        this.newVal = newVal;
    }
}
