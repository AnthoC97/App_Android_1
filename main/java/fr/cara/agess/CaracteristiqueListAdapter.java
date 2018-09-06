package fr.cara.agess;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cara on 10/07/2017.
 */
//Cette classe est un Adapter concu pour les anomalies;
public class CaracteristiqueListAdapter extends BaseAdapter{
    private Context context;
    private List<Caracteristique> list; //La liste des anomalies

    static class ViewHolder {
        TextView text;
        EditText editText;
        Spinner spinner;
        int ressource;
        int ref;
    }

    public CaracteristiqueListAdapter(Context c, List<Caracteristique> l){
        context = c;
        list = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Ici on obtient la Vue à la position donnée qui affiche les données.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final ViewHolder viewHolder;

        if(rowView == null){
            viewHolder = new ViewHolder();
            //Instanciation du Fichier  XML de context
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            //On vérifie si il existe une ou plusieurs valeurs pour la caractéristique ("L" =  plusieurs valeurs)
            if (list.get(position).getType().trim().equals("L")){
                viewHolder.ressource = R.layout.affichage_caracteristique_spinner;
                rowView = inflater.inflate(viewHolder.ressource, null);
                viewHolder.spinner = (Spinner)rowView.findViewById(R.id.spinner);
            }
            else{
                viewHolder.ressource = R.layout.affichage_caracteristique;
                rowView = inflater.inflate(viewHolder.ressource, null);
                viewHolder.editText = (EditText)rowView.findViewById(R.id.editText);
            }
            viewHolder.text = (TextView) rowView.findViewById(R.id.text);
            //On met les informations de notre ViewHolder dans RowView
            rowView.setTag(viewHolder);
        }
        else{
            //on récuppère des informations dans rowView
            viewHolder = (ViewHolder) rowView.getTag();
        }
        viewHolder.ref = position;
        //On affiche le nom de l'anomalie
        final String caracteristiqueStr = list.get(position).getCaract();
        viewHolder.text.setText(caracteristiqueStr);
        if(!(viewHolder.ressource == R.layout.affichage_caracteristique_spinner)){
            viewHolder.editText.setText(list.get(position).getNewVal());
            if(list.get(position).getType().equals("F")){
                viewHolder.editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            else if(list.get(position).getType().equals("I")){
                viewHolder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else if(list.get(position).getType().equals("D")){
                viewHolder.editText.setInputType(InputType.TYPE_CLASS_DATETIME);
            }
            viewHolder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(position).setNewVal(s.toString());
                }
            });
        }

        else{
            String[] listV = list.get(position).getValeur().split(":");
            List<String> spinnerArray =  new ArrayList<String>();
            for(int i = 0; i < listV.length; i++){
                spinnerArray.add(listV[i]);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,spinnerArray);
            viewHolder.spinner.setAdapter(adapter);
            for (int i = 0 ; i < spinnerArray.size(); i++){
                if((list.get(position).getNewVal() != null) && (list.get(position).getNewVal().equals(spinnerArray.get(i)))){
                    viewHolder.spinner.setSelection(i);
                }
            }
            viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    list.get(viewHolder.ref).setNewVal((String)parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        return rowView;
    }
}


