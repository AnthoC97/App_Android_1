package fr.cara.agess;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaintenanceActivity extends AppCompatActivity {
    public static final String STRING_OPEFF = "fr.cara.agess.MaintenanceActivity.STRING_OPEFF";
    public static final String BUTTON_CODE = "fr.cara.agess.MaintenanceActivity.BUTTON_CODE";

    private ListView liste_maintenance;
    private MaintenanceListAdapter maintenanceListAdapter;
    private Button valider;
    private Button annuler;

    public List<ControleMaintenance> listC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        liste_maintenance = (ListView)findViewById(R.id.list_maintenance);
        valider = (Button)findViewById(R.id.valider);
        annuler = (Button)findViewById(R.id.annuler);

        final Intent intent = getIntent();
        final Element infosElem =  intent.getParcelableExtra(ElementActivity.ELEM);

        Element element = new Element();
        ElementDB edb = new ElementDB(ListeTourneeActivity.file,false);
        edb.openDB();
        //On obtient l'élement sur lequel on travaille grace à son code
        element = edb.getElementWithCODE((String) infosElem.getCode());
        edb.close();

        ControleMaintenanceDB cmdb = new ControleMaintenanceDB();
        listC = new ArrayList<>();
        List<ControleMaintenance> l = new ArrayList<>();
        l.addAll(cmdb.getControlesMaintenance(element.getFam(),element.getType()));
        for(int i = 0 ; i < l.size(); i++){
            //On ajoute à listC uniquement les controles de maintenances disponnibles.
            if(!(l.get(i).getName().trim().equals("//") || l.get(i).getName().trim().equals(""))){
                listC.add(l.get(i));
            }
        }
        //On restaure les controles de maintenance déjà effectués
        if(element.getOpeff() != null) {
            int opeffLenght = element.getOpeff().length();
            //On réccupère la chaine de caractère présent dans le champ OPEFF
            String opeff = element.getOpeff();
            String jour;
            String mois;
            String annee;
            //La longeur de la chaine de caractère est un multiple de 7 (En effet chaque controle de
            //Maintenance est une chaine de longueur 7
            String key;
            for (int j = 0; j < opeffLenght / 7; j++) {
                //On récupère la lettre qui sert à identifier un contrôle de maintenance
                key = opeff.substring(j * 7, (j * 7) + 1);
                for (int k = 0; k < listC.size(); k++) {
                    //Si la lettre correspond à une clé d'un des contrôleurs
                    if (key.equals(listC.get(k).getKey())) {
                        //On récuppère le jour...
                        jour = opeff.substring((j * 7) + 1,(j * 7) + 3);
                        //le mois ...
                        mois = opeff.substring((j * 7) + 3,(j * 7) + 5);
                        //l'année
                        annee = opeff.substring((j * 7) + 5,(j * 7) + 7);
                        //On modifie le contrôle de maintenance
                        listC.get(k).setDate(jour+"/"+mois+"/"+annee);
                        //On coche
                        listC.get(k).checked = true;
                    }
                }
            }
        }
        maintenanceListAdapter =  new MaintenanceListAdapter(this,listC);
        liste_maintenance.setAdapter(maintenanceListAdapter);




        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String strOPEFF = "";
            for(int i = 0; i < listC.size(); i++){
                if(listC.get(i).checked){
                    strOPEFF += listC.get(i).getKey()+""+listC.get(i).getDate();
                }
            }
            if(strOPEFF.equals("")){
                Toast.makeText(MaintenanceActivity.this,"Vous n'avez coché aucune case. Si il n' " +
                        "y a aucune case à cocher, cliquez sur annuler",Toast.LENGTH_LONG).show();
            }
            else{
                String[] controleDate = strOPEFF.split("/");
                strOPEFF = "";
                for(String s : controleDate){
                    strOPEFF += s;
                }
                Intent intent = new Intent(MaintenanceActivity.this,ElementActivity.class);
                intent.putExtra(STRING_OPEFF, strOPEFF);
                intent.putExtra(BUTTON_CODE,"valider");
                setResult(RESULT_OK,intent);
                finish();
            }
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaintenanceActivity.this,ElementActivity.class);
                //On spécifie à ElementActivity que l'on a cliqué sur "anuler" ici
                intent.putExtra(BUTTON_CODE,"annuler");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

}
