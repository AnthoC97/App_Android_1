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

import java.util.List;

/**
 * Created by cara on 17/07/2017.
 */

public class OperationListAdapter extends BaseAdapter {
    private Context context;
    private List<Operation> list;
    private int compt;

    public OperationListAdapter(Context c, List<Operation> l){
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
            rowView = inflater.inflate(R.layout.affichage_operation, null);

            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
            //On met les informations de notre ViewHolder dans RowView
            rowView.setTag(viewHolder);
        }
        else{
            //on récuppère des informations dans rowView
            viewHolder = (OperationListAdapter.ViewHolder) rowView.getTag();
        }
        viewHolder.ref = position;
        //On coche ou laisse décoché la case d'un controle de maintenance
        viewHolder.checkBox.setChecked(list.get(position).checked);
        viewHolder.checkBox.setText(list.get(position).getOperation());

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
        int ref;
    }

}