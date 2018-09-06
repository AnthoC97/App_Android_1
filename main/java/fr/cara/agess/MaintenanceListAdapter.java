package fr.cara.agess;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cara on 17/07/2017.
 */

public class MaintenanceListAdapter extends BaseAdapter {
    private Context context;
    private List<ControleMaintenance> list;
    private int compt;

    public MaintenanceListAdapter(Context c, List<ControleMaintenance> l){
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

    public boolean isChecked(int position) {
        return list.get(position).checked;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        final ViewHolder viewHolder;
        if(rowView == null){
            viewHolder = new ViewHolder();
            //Instanciation du Fichier  XML de context
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            //crée une vue à partir de affichage_anomalie
            rowView = inflater.inflate(R.layout.affichage_controle_maintenance, null);

            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
            viewHolder.editText_date = (EditText)rowView.findViewById(R.id.editText_date);
            //On met les informations de notre ViewHolder dans RowView
            rowView.setTag(viewHolder);
        }
        else{
            //on récuppère des informations dans rowView
            viewHolder = (MaintenanceListAdapter.ViewHolder) rowView.getTag();
        }
        viewHolder.ref = position;
        //On coche ou laisse décoché la case d'un controle de maintenance
        viewHolder.checkBox.setChecked(list.get(position).checked);
        viewHolder.checkBox.setText(list.get(position).getName());
        viewHolder.editText_date.setText(list.get(position).getDate());

        viewHolder.editText_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //On modifie la date du controle de mainteance avec celui que l'on vient de rentrer
                list.get(viewHolder.ref).setDate(s.toString());
                list.get(position).checked = true;
                viewHolder.checkBox.setChecked(list.get(position).checked);

            }
        });
        viewHolder.checkBox.setTag(position);
        //On change l'état de la checkbox au clic
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newState = !list.get(position).checked;
                list.get(position).checked = newState;
            }
        });


        //On coche ou déchoche la CheckBox
        viewHolder.checkBox.setChecked(isChecked(position));

        return rowView;
    }

    private class ViewHolder {
        CheckBox checkBox;//La case a cocher pour chaque controle de maintenance
        EditText editText_date;
        int ref;
    }

}