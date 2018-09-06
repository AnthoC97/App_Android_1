package fr.cara.agess;

/**
 * Created by cara on 09/08/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

//Cette classe est un Adapter concu pour les anomalies;
public class TourneeAdapter extends BaseAdapter {
    private Context context;
    private List<Element> list; //La liste des anomalies

    static class ViewHolder {
        TextView text;//Le nom de l'anomalie
    }

    public TourneeAdapter(Context c, List<Element> l){
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

        ViewHolder viewHolder = new ViewHolder();
        if(rowView == null){
            //Instanciation du Fichier  XML de context
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(R.layout.affichageelems, null);

            viewHolder.text = (TextView) rowView.findViewById(R.id.famTypeCode);
            //On met les informations de notre ViewHolder dans RowView
            rowView.setTag(viewHolder);
        }
        else{
            //on récuppère des informations dans rowView
            viewHolder = (ViewHolder) rowView.getTag();
        }
        //On change l'état de la checkbox au clic
        //On coche ou déchoche la CheckBox
        viewHolder.text.setText(list.get(position).getFam()+"/"+list.get(position).getType()+" "+list.get(position).getCode());
       if(list.get(position).getValid() != null){
            viewHolder.text.setBackground(ContextCompat.getDrawable(context,R.drawable.valid));
        }

        return rowView;
    }
}



