package fr.cara.agess;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cara on 12/06/2017.
 */
//Cette classe représente simplement n'importe quel élément présent dans la Table ELEMENTS de notre base de donnée
//Element implémente la classe Parcelable afin que des Objets de type Collection<Element> puissent être passé en paramètre
public class Element implements Parcelable {
    private int num;
    private String code;
    private String fam;
    private String type;
    private String site;
    private String batiment;
    private String niveau;
    private String zone;
    private String lieu;
    private String infos;
    private String opautor;
    private String ncode;
    private String date;
    private String obsn;
    private String obsa;
    private String caract;
    private String toureff;
    private String opeff;
    private String numop;
    private String valid;
    private String opera;
    private String alert;
    private String nbre;
    private String touraf;
    private String opaf;
    private String entite;
    private String images;
    private String date_proverif;
    private String date_perem;
    private String date_procycle;
    private String date_prointerne;
    private String date_proctrl;
    private String document;
    private String puce;
    private String delai;

    public Element(){

    }

    public Element(int num, String code, String fam, String type, String site, String batiment, String niveau, String zone, String lieu, String infos, String opautor, String ncode, String date, String obsn, String obsa, String caract, String toureff, String opeff, String numop, String valid, String opera, String alert, String nbre, String touraf, String opaf, String entite, String images, String date_proverif, String date_perem, String date_procycle, String date_prointerne, String date_proctrl, String document, String puce, String delai) {
        this.num = num;
        this.code = code;
        this.fam = fam;
        this.type = type;
        this.site = site;
        this.batiment = batiment;
        this.niveau = niveau;
        this.zone = zone;
        this.lieu = lieu;
        this.infos = infos;
        this.opautor = opautor;
        this.ncode = ncode;
        this.date = date;
        this.obsn = obsn;
        this.obsa = obsa;
        this.caract = caract;
        this.toureff = toureff;
        this.opeff = opeff;
        this.numop = numop;
        this.valid = valid;
        this.opera = opera;
        this.alert = alert;
        this.nbre = nbre;
        this.touraf = touraf;
        this.opaf = opaf;
        this.entite = entite;
        this.images = images;
        this.date_proverif = date_proverif;
        this.date_perem = date_perem;
        this.date_procycle = date_procycle;
        this.date_prointerne = date_prointerne;
        this.date_proctrl = date_proctrl;
        this.document = document;
        this.puce = puce;
        this.delai = delai;
    }

    protected Element(Parcel in) {
        num = in.readInt();
        code = in.readString();
        fam = in.readString();
        type = in.readString();
        site = in.readString();
        batiment = in.readString();
        niveau = in.readString();
        zone = in.readString();
        lieu = in.readString();
        infos = in.readString();
        opautor = in.readString();
        ncode = in.readString();
        date = in.readString();
        obsn = in.readString();
        obsa = in.readString();
        caract = in.readString();
        toureff = in.readString();
        opeff = in.readString();
        numop = in.readString();
        valid = in.readString();
        opera = in.readString();
        alert = in.readString();
        nbre = in.readString();
        touraf = in.readString();
        opaf = in.readString();
        entite = in.readString();
        images = in.readString();
        date_proverif = in.readString();
        date_perem = in.readString();
        date_procycle = in.readString();
        date_prointerne = in.readString();
        date_proctrl = in.readString();
        document = in.readString();
        puce = in.readString();
        delai = in.readString();
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFam() {
        return fam;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getBatiment() {
        return batiment;
    }

    public void setBatiment(String batiment) {
        this.batiment = batiment;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public String getOpautor() {
        return opautor;
    }

    public void setOpautor(String opautor) {
        this.opautor = opautor;
    }

    public String getNcode() {
        return ncode;
    }

    public void setNcode(String ncode) {
        this.ncode = ncode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getObsn() {
        return obsn;
    }

    public void setObsn(String obsn) {
        this.obsn = obsn;
    }

    public String getObsa() {
        return obsa;
    }

    public void setObsa(String obsa) {
        this.obsa = obsa;
    }

    public String getCaract() {
        return caract;
    }

    public void setCaract(String caract) {
        this.caract = caract;
    }

    public String getToureff() {
        return toureff;
    }

    public void setToureff(String toureff) {
        this.toureff = toureff;
    }

    public String getOpeff() {
        return opeff;
    }

    public void setOpeff(String opeff) {
        this.opeff = opeff;
    }

    public String getNumop() {
        return numop;
    }

    public void setNumop(String numop) {
        this.numop = numop;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getOpera() {
        return opera;
    }

    public void setOpera(String opera) {
        this.opera = opera;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getNbre() {
        return nbre;
    }

    public void setNbre(String nbre) {
        this.nbre = nbre;
    }

    public String getTouraf() {
        return touraf;
    }

    public void setTouraf(String touraf) {
        this.touraf = touraf;
    }

    public String getOpaf() {
        return opaf;
    }

    public void setOpaf(String opaf) {
        this.opaf = opaf;
    }

    public String getEntite() {
        return entite;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDate_proverif() {
        return date_proverif;
    }

    public void setDate_proverif(String date_proverif) {
        this.date_proverif = date_proverif;
    }

    public String getDate_perem() {
        return date_perem;
    }

    public void setDate_perem(String date_perem) {
        this.date_perem = date_perem;
    }

    public String getDate_procycle() {
        return date_procycle;
    }

    public void setDate_procycle(String date_procycle) {
        this.date_procycle = date_procycle;
    }

    public String getDate_prointerne() {
        return date_prointerne;
    }

    public void setDate_prointerne(String date_prointerne) {
        this.date_prointerne = date_prointerne;
    }

    public String getDate_proctrl() {
        return date_proctrl;
    }

    public void setDate_proctrl(String date_proctrl) {
        this.date_proctrl = date_proctrl;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPuce() {
        return puce;
    }

    public void setPuce(String puce) {
        this.puce = puce;
    }

    public String getDelai() {
        return delai;
    }

    public void setDelai(String delai) {
        this.delai = delai;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num);
        dest.writeString(code);
        dest.writeString(fam);
        dest.writeString(type);
        dest.writeString(site);
        dest.writeString(batiment);
        dest.writeString(niveau);
        dest.writeString(zone);
        dest.writeString(lieu);
        dest.writeString(infos);
        dest.writeString(opautor);
        dest.writeString(ncode);
        dest.writeString(date);
        dest.writeString(obsn);
        dest.writeString(obsa);
        dest.writeString(caract);
        dest.writeString(toureff);
        dest.writeString(opeff);
        dest.writeString(numop);
        dest.writeString(valid);
        dest.writeString(opera);
        dest.writeString(alert);
        dest.writeString(nbre);
        dest.writeString(touraf);
        dest.writeString(opaf);
        dest.writeString(entite);
        dest.writeString(images);
        dest.writeString(date_proverif);
        dest.writeString(date_perem);
        dest.writeString(date_procycle);
        dest.writeString(date_prointerne);
        dest.writeString(date_proctrl);
        dest.writeString(document);
        dest.writeString(puce);
        dest.writeString(delai);
    }
}
