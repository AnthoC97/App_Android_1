package fr.cara.agess;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cara on 17/07/2017.
 */

//Cette classe représente un controle de mainteance avec la date et la clé attribuée (Ex : A, V, ... )
public class Operation implements Parcelable{
    private String fam;
    private String operation;
    private int numop;
    public boolean checked;

    public Operation(String fam, String operation, int numop){
        this.fam = fam;
        this.operation = operation;
        this.numop = numop;
        checked = false;
    }

    public Operation(){

    }

    public String getFam(){
        return fam;
    }
    public void setFam(String fam){
        this.fam = fam;
    }
    public String getOperation(){
        return operation;
    }
    public void setOperation(String operation){
        this.operation = operation;
    }
    public int getNumop(){return numop;}
    public void setNumop(int numop){
        this.numop = numop;
    }


    protected Operation(Parcel in){
        fam = in.readString();
        operation = in.readString();
        numop = in.readInt();
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
        dest.writeString(fam);
        dest.writeString(operation);
        dest.writeInt(numop);
    }
}
