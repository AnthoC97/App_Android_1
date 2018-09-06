package fr.cara.agess;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cara on 17/07/2017.
 */

//Cette classe représente un controle de mainteance avec la date et la clé attribuée (Ex : A, V, ... )
public class ControleMaintenance implements Parcelable{
    private String name;
    private String date;
    private String key;
    public boolean checked;

    public ControleMaintenance(String name, String date){
        this.name = name;
        this.date = date;
        checked = false;
    }

    public ControleMaintenance(){

    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDate(){
        return date;
    }
    public String getKey(){return key;}
    public void setKey(String key){
        this.key = key;
    }
    public void setDate(String date){
        this.date = date;
    }

    protected ControleMaintenance(Parcel in){
        name = in.readString();
        date = in.readString();
        key = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(key);
    }
}
