package fr.cara.agess;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FiltreActivity extends AppCompatActivity {

    public static final String QUERY = "fr.cara.agess.FiltreActivity.QUERY";
    public static final String BUTTON_CODE = "fr.cara.agess.BUTTON_CODE";

    private CheckBox non_vu;
    private CheckBox deja_vu;
    private Spinner famille;
    private Spinner type;
    private Spinner site;
    private Spinner batiment;
    private Spinner niveau;
    private Spinner zone;
    private Spinner lieu;
    private Button desactiver;
    private Button vider;
    private Button valider;

    public static String VU = "";
    public static String FAMILLE = "";
    public static String TYPE = "";
    public static String SITE = "";
    public static String BATIMENT = "";
    public static String NIVEAU = "";
    public static String ZONE = "";
    public static String LIEU = "";


    private String queryFilter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final HashMap<String,Spinner> critereEtSpinner = new HashMap<>();
        final Intent intent = getIntent();
        //On réccupère le nom de la base de donnée à utiliser

        non_vu = (CheckBox)findViewById(R.id.non_vu);
        deja_vu = (CheckBox)findViewById(R.id.deja_vu);
        famille = (Spinner)findViewById(R.id.famille);
        critereEtSpinner.put("FAM",famille);
        type = (Spinner)findViewById(R.id.type);
        critereEtSpinner.put("TYPE",type);
        site = (Spinner)findViewById(R.id.site);
        critereEtSpinner.put("SITE",site);
        batiment = (Spinner)findViewById(R.id.batiment);
        critereEtSpinner.put("BATIMENT",batiment);
        niveau = (Spinner)findViewById(R.id.niveau);
        critereEtSpinner.put("NIVEAU",niveau);
        zone = (Spinner)findViewById(R.id.zone);
        critereEtSpinner.put("ZONE",zone);
        lieu = (Spinner)findViewById(R.id.lieu);
        critereEtSpinner.put("LIEU",lieu);
        desactiver = (Button)findViewById(R.id.desactiver);
        vider = (Button)findViewById(R.id.vider);
        valider = (Button)findViewById(R.id.valider);

        //Les listes des différentes familles, types, ...
        ElementDB edb = new ElementDB(ListeTourneeActivity.file,false);
        List<String> listFamille = new ArrayList<String>();
        listFamille.addAll(edb.getFamilles());

        List<String> listType = new ArrayList<>();
        listType.add("");
        listType.addAll(edb.getTypes());

        List<String> listSite = new ArrayList<>();
        listSite.add("");
        listSite.addAll(edb.getSites());

        List<String> listBatiment = new ArrayList<>();
        listBatiment.add("");
        listBatiment.addAll(edb.getBatiments());

        List<String> listNiveau = new ArrayList<>();
        listNiveau.add("");
        listNiveau.addAll(edb.getNiveaux());

        List<String> listZone = new ArrayList<>();
        listZone.add("");
        listZone.addAll(edb.getZones());

        List<String> listLieu = new ArrayList<>();
        listLieu.add("");
        listLieu.addAll(edb.getLieux());

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listFamille);
        famille.setAdapter(adapter);
        if(!FAMILLE.equals("")){
            for (int i = 0; i < listFamille.size() ; i ++){
                if(FAMILLE.equals(listFamille.get(i))){
                    famille.setSelection(i);
                }
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listType);
        type.setAdapter(adapter);
        if(!TYPE.equals("")){
            for (int i = 0; i < listType.size() ; i ++){
                if(TYPE.equals(listType.get(i))){
                    type.setSelection(i);
                }
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listSite);
        site.setAdapter(adapter);
        if(!SITE.equals("")){
            for (int i = 0; i < listSite.size() ; i ++){
                if(SITE.equals(listSite.get(i))){
                    site.setSelection(i);
                }
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listBatiment);
        batiment.setAdapter(adapter);
        if(!BATIMENT.equals("")){
            for (int i = 0; i < listBatiment.size() ; i ++){
                if(BATIMENT.equals(listBatiment.get(i))){
                    batiment.setSelection(i);
                }
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listNiveau);
        niveau.setAdapter(adapter);
        if(!NIVEAU.equals("")){
            for (int i = 0; i < listNiveau.size() ; i ++){
                if(NIVEAU.equals(listNiveau.get(i))){
                    niveau.setSelection(i);
                }
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listZone);
        zone.setAdapter(adapter);
        if(!ZONE.equals("")){
            for (int i = 0; i < listZone.size() ; i ++){
                if(ZONE.equals(listZone.get(i))){
                    zone.setSelection(i);
                }
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listLieu);
        lieu.setAdapter(adapter);
        if(!LIEU.equals("")){
            for (int i = 0; i < listLieu.size() ; i ++){
                if(LIEU.equals(listLieu.get(i))){
                    lieu.setSelection(i);
                }
            }
        }

        if(VU.equals("true")){
            deja_vu.setChecked(true);
            non_vu.setChecked(false);
        }
        else if(VU.equals("false")){
            deja_vu.setChecked(false);
            non_vu.setChecked(true);
        }

        non_vu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    deja_vu.setChecked(false);
                }
            }
        });

        deja_vu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    non_vu.setChecked(false);
                }
            }
        });

        desactiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VU = "";
                FAMILLE = "";
                TYPE = "";
                SITE = "";
                BATIMENT = "";
                NIVEAU = "";
                ZONE = "";
                LIEU = "";
                Intent intent = new Intent(FiltreActivity.this,TourneeActivity.class);
                TourneeActivity.query = "";
                startActivity(intent);
            }
        });

        vider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Spinner s : critereEtSpinner.values()){
                    s.setSelection(0);
                }
                non_vu.setChecked(false);
                deja_vu.setChecked(false);
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAMILLE = (String)famille.getSelectedItem();
                TYPE = (String)type.getSelectedItem();
                SITE = (String)site.getSelectedItem();
                BATIMENT = (String)batiment.getSelectedItem();
                NIVEAU = (String)niveau.getSelectedItem();
                ZONE = (String)zone.getSelectedItem();
                LIEU = (String)lieu.getSelectedItem();

                queryFilter = "SELECT * FROM ELEMENTS WHERE ";
                String info;
                boolean b = false;
                for (String critere : critereEtSpinner.keySet()){
                    Spinner s = (Spinner)critereEtSpinner.get(critere);
                    if(((String)s.getSelectedItem()).equals("")){
                        continue;
                    }
                    else{
                        if(!b){
                            info = critere+" = '"+(String)s.getSelectedItem()+"'";
                            queryFilter += info;
                            b = true;
                        }
                        else{
                            info = " AND "+critere+" = '"+(String)s.getSelectedItem()+"'";
                            queryFilter += info;
                        }

                    }
                }
                if(non_vu.isChecked()){
                    queryFilter += " AND VALID IS NULL";
                    VU = "false";
                }
                if(deja_vu.isChecked()){
                    queryFilter += " AND VALID IS NOT NULL"; //Soit IS NOT NULL soit = 'Y'
                    VU = "true";
                }
                Intent intent = new Intent(FiltreActivity.this,TourneeActivity.class);
                TourneeActivity.query = queryFilter;
                startActivity(intent);
                //Toast.makeText(FiltreActivity.this, queryFilter, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
