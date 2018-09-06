package fr.cara.agess;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by cara on 10/07/2017.
 */
//Cette classe est un Adapter concu pour les anomalies;
public class AnomaliesListAdapter extends BaseAdapter{
    private Context context;
    private List<Anomalie> list; //La liste des anomalies

    static class ViewHolder {
        CheckBox checkBox;//La case a cocher pour chaque anomalie
        TextView text;//Le nom de l'anomalie
    }

    public AnomaliesListAdapter(Context c, List<Anomalie> l){
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
    //Ici on obtient la Vue à la position donnée qui affiche les données.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        ViewHolder viewHolder = new ViewHolder();
        if(rowView == null){
            //Instanciation du Fichier  XML de context
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            //crée une vue à partir de affichage_anomalie
            rowView = inflater.inflate(R.layout.affichage_anomalie, null);

            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
            viewHolder.text = (TextView) rowView.findViewById(R.id.anomalie);
            //On met les informations de notre ViewHolder dans RowView
            rowView.setTag(viewHolder);
        }
        else{
            //on récuppère des informations dans rowView
            viewHolder = (ViewHolder) rowView.getTag();
        }
        //On coche ou laisse décoché la case d'une anomalie
        viewHolder.checkBox.setChecked(list.get(position).checked);
        //On affiche le nom de l'anomalie
        final String anomalieStr = list.get(position).getName();
        viewHolder.text.setText(anomalieStr);

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
}


