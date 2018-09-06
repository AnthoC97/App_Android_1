package fr.cara.agess;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ElementActivity extends AppCompatActivity {

    public static final String ELEM = "fr.cara.agess.ELEM";
    public final static String ID = "fr.cara.agess.ID";
    public static final int SCANNER_CODE = 0;
    public static final int ANOMALIE_CODE = 1;
    public static final int MAINTENANCE_CODE = 3;
    public static final int CARACTERISTIQUE_CODE = 4;
    public static final int OPERATION_CODE = 6;

    private Toolbar toolbar;
    private TextView tournees;
    private TextView mode_liste;
    private TextView fin_de_travail;
    private TextView code;
    private TextView titleInfo;
    private TextView site;
    private TextView batiment;
    private TextView niveau;
    private TextView zone;
    private TextView lieu;
    private TextView info;
    private TextView date_proverif;
    private TextView date_perem;
    private TextView date_procycle;
    private TextView date_prointerne;
    private TextView date_proctrl;
    private Button anomalies;
    private Button notes;
    private Button maintenance;
    private Button deplacer;
    private Button echanger;
    private Button operation;
    private Button caracteristique;
    private Button type;
    private Button entite;
    private Button scan_lieu;
    private Button supprimer;
    private Button stock;


    private List<Element> listElem;
    private String[] codeElem;
    private Element infosElem;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Récupération de l'intent envoyé par Tournée
        final Intent intent = getIntent();
        //On réccupère les informations de l'élément sélectioné
        infosElem = (Element) intent.getParcelableExtra(TourneeActivity.ELEM);
        //On récupère la liste des éléments
        listElem = (ArrayList<Element>) intent.getSerializableExtra(TourneeActivity.LIST_EXT);
        //On réccupère le nom de la base de donnée à utiliser
        id = intent.getStringExtra(ID);
        //On récupère la lsite des codes des éléments
        codeElem = intent.getStringArrayExtra(TourneeActivity.CODE);
        //On réccupère toute les vues qui seront nécessaire à l'affichage d'un élément
        tournees = (TextView)findViewById(R.id.tournees);
        mode_liste = (TextView)findViewById(R.id.mode_liste);
        fin_de_travail = (TextView)findViewById(R.id.fin_de_travail);
        code = (TextView)findViewById(R.id.code);
        titleInfo = (TextView)findViewById(R.id.titleInfo);
        site = (TextView)findViewById(R.id.site);
        batiment = (TextView)findViewById(R.id.batiment);
        niveau = (TextView)findViewById(R.id.niveau);
        zone = (TextView)findViewById(R.id.zone);
        lieu = (TextView)findViewById(R.id.lieu);
        info = (TextView)findViewById(R.id.info);
        date_proverif = (TextView)findViewById(R.id.date_proverif);
        date_perem = (TextView)findViewById(R.id.date_perem);
        date_procycle = (TextView)findViewById(R.id.date_procycle);
        date_prointerne = (TextView)findViewById(R.id.date_prointerne);
        date_proctrl = (TextView)findViewById(R.id.date_proctrl);

        //on modifie ces vues avec les infos de notre élément
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(infosElem.getFam()+"/"+infosElem.getType()+" "+infosElem.getCode());
        site.setText("Site : "+infosElem.getSite());
        batiment.setText("Batiment : "+infosElem.getBatiment());
        niveau.setText("Niveau : "+infosElem.getNiveau());
        zone.setText("Zone : "+infosElem.getZone());
        lieu.setText("Lieu : "+infosElem.getLieu());
        info.setText("Infos : "+infosElem.getInfos());
        date_proverif.setText("Date de vérification : "+infosElem.getDate_proverif());
        date_perem.setText("Date de péremption : "+infosElem.getDate_perem());
        date_procycle.setText("Date de (procycle): "+infosElem.getDate_procycle());
        date_prointerne.setText("Date de contrôle interne: "+infosElem.getDate_prointerne());
        date_proctrl.setText("Date de contrôle : "+infosElem.getDate_proctrl());

        //Ce bouton sert à accéder a l'activité permettant de gérer les anomalies sur un élément
        anomalies = (Button)findViewById(R.id.anomalies);
        //Ce bouton set a accéder à l'activité permettant d'inscrire les notes sur un élément
        notes = (Button)findViewById(R.id.notes);
        //Ce bouton sert à accéder a l'activité permettant de gérer les maintenances sur un élément
        maintenance = (Button)findViewById(R.id.maintenance);
        deplacer = (Button)findViewById(R.id.deplacer);
        echanger = (Button)findViewById(R.id.echanger);
        operation = (Button)findViewById(R.id.operation);
        caracteristique = (Button)findViewById(R.id.caracteristique);
        type = (Button)findViewById(R.id.type);
        entite = (Button)findViewById(R.id.entite);
        scan_lieu = (Button)findViewById(R.id.scan_lieu);
        supprimer = (Button)findViewById(R.id.supprimer);
        stock = (Button)findViewById(R.id.stock);
        //Les boutons sont désactivé de bases
        anomalies.setEnabled(false);
        notes.setEnabled(false);
        maintenance.setEnabled(false);
        deplacer.setEnabled(false);
        //echanger.setEnabled(false);
        operation.setEnabled(false);
        caracteristique.setEnabled(false);
        type.setEnabled(false);
        entite.setEnabled(false);
        scan_lieu.setEnabled(false);
        supprimer.setEnabled(false);
        stock.setEnabled(false);

        Element element = new Element();
        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,true);
        element = edb.getElementWithCODE((String)infosElem.getCode());
        //On regarde les sont les actions possible sur l'éléméent sur lequel travaille l'utilisateur
        String opautor = element.getOpautor();
        for(int i = 0; i < opautor.length(); i++){
            switch (opautor.substring(i,i+1)){
                case "N" :
                    notes.setEnabled(true);
                    break;
                case "A" :
                    anomalies.setEnabled(true);
                    break;
                case "E" :
                    echanger.setEnabled(true);
                    break;
                case "M" :
                    maintenance.setEnabled(true);
                    break;
                case "O" :
                    operation.setEnabled(true);
                    break;
                case "C" :
                    caracteristique.setEnabled(true);
            }
        }

        //En cliquant sur ce bouton on lance l'activité AnomaliesActivity
        anomalies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementActivity.this,AnomaliesActivity.class);
                intent.putExtra(ELEM,infosElem);
                startActivityForResult(intent,ANOMALIE_CODE);
            }
        });
        //En cliquant sur ce bouton on lance l'activité MaintenanceActivity
        maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementActivity.this,MaintenanceActivity.class);
                intent.putExtra(ELEM,infosElem);
                startActivityForResult(intent,MAINTENANCE_CODE);
            }
        });

        echanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementActivity.this,EchangerActivity.class);
                startActivity(intent);
            }
        });

        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementActivity.this,OperationActivity.class);
                intent.putExtra(ELEM,infosElem);
                startActivityForResult(intent,OPERATION_CODE);
            }
        });

        caracteristique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementActivity.this,CaracteristiqueActivity.class);
                intent.putExtra(ELEM,infosElem);
                startActivityForResult(intent,CARACTERISTIQUE_CODE);
            }
        });

        tournees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementActivity.this,ListeTourneeActivity.class);
                intent.putExtra(ID,id);
                startActivity(intent);
            }
        });

        mode_liste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElementActivity.this,TourneeActivity.class);
                setResult(RESULT_OK);
                finish();
            }
        });

        fin_de_travail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on créer une boite de dialogue
                AlertDialog.Builder adb = new AlertDialog.Builder(ElementActivity.this);
                //on attribut un titre à notre boite de dialogue
                adb.setTitle("Fin de Travail");
                //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
                adb.setMessage("Cette action verrouille définitivement toutes vos modifications et prépare la base pour une prochaine synchronisation. Les éléments modifiées ne seront plus accessibles. Voulez-vous continuer ?");
                //on indique que l'on veut le bouton ok à notre boite de dialogue
                adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String elemModifies = "";
                        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,false);
                        ElementDB edb2 = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,true);
                        List<Element> list =  edb.getElementValid();
                        for (int i = 0; i < list.size(); i++){
                            Element e = list.get(i);
                            edb2.insert(e);
                            elemModifies += list.get(i).getFam()+" "+list.get(i).getType()+" code : "+list.get(i).getCode()+"\n";
                        }
                        edb.nonSelectQuery("DELETE FROM ELEMENTS WHERE VALID = 'Y'");
                        AlertDialog.Builder adb2 = new AlertDialog.Builder(ElementActivity.this);
                        adb2.setTitle("Elements sauvegardés");
                        adb2.setMessage(elemModifies);
                        adb2.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ElementActivity.this,ListeTourneeActivity.class);
                                startActivity(intent);
                            }
                        });
                        adb2.show();
                    }
                });
                adb.setNegativeButton("Non",null);
                //on affiche la boite de dialogue
                adb.show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_element, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                verifEditText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_scan){
            Intent intent = new Intent(ElementActivity.this,ScannerActivity.class);
            startActivityForResult(intent,SCANNER_CODE);
        }
        else if(item.getItemId() == R.id.action_deconnexion){
            //on créer une boite de dialogue
            AlertDialog.Builder adb = new AlertDialog.Builder(ElementActivity.this);
            //on attribut un titre à notre boite de dialogue
            adb.setTitle("Déconnexion");
            //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
            adb.setMessage("Voulez vous vous déconnecter ?");
            //on indique que l'on veut le bouton ok à notre boite de dialogue
            adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ElementActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
            adb.setNegativeButton("Non",null);
            //on affiche la boite de dialogue
            adb.show();
        }
        return super.onOptionsItemSelected(item);
    }
    //cette méthode recherche l'élément et affiche les informations sur celle ci
    public void verifEditText(String text){
        String[] elem = new String[]{};
        for (String s : codeElem){
            if (text.trim().equals(s)){
                elem = new String[]{s};
            }
        }
        //Si aucun élément ne correspond à notre recherche
        if(elem.length == 0){
            Toast.makeText(this,"Aucun ElementActivity ne correspond au code fourni",Toast.LENGTH_LONG).show();
        }
        //Sinon ...
        else{
            ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
            HashMap<String,String> map;

            for(int i = 0; i < listElem.size(); i++){
                if (text.trim().equals(listElem.get(i).getCode())){
                    map = new HashMap<String,String>();
                    map.put("famTypeCode",listElem.get(i).getFam()+"/"+listElem.get(i).getType()+" "+listElem.get(i).getCode());
                    map.put("code",listElem.get(i).getCode());
                    map.put("site",listElem.get(i).getSite());
                    map.put("batiment",listElem.get(i).getBatiment());
                    map.put("niveau",listElem.get(i).getNiveau());
                    map.put("zone",listElem.get(i).getZone());
                    map.put("lieu",listElem.get(i).getLieu());
                    map.put("info",listElem.get(i).getInfos());
                    map.put("date_proverif",listElem.get(i).getDate_proverif());
                    map.put("date_perem",listElem.get(i).getDate_perem());
                    map.put("date_procycle",listElem.get(i).getDate_procycle());
                    map.put("date_prointerne",listElem.get(i).getDate_prointerne());
                    map.put("date_proctrl",listElem.get(i).getDate_proctrl());

                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(infosElem.getFam()+"/"+infosElem.getType()+" "+infosElem.getCode());
                    site.setText("Site : "+infosElem.getSite());
                    batiment.setText("Batiment : "+infosElem.getBatiment());
                    niveau.setText("Niveau : "+infosElem.getNiveau());
                    zone.setText("Zone : "+infosElem.getZone());
                    lieu.setText("Lieu : "+infosElem.getLieu());
                    info.setText("Infos : "+infosElem.getInfos());
                    date_proverif.setText("Date de vérification : "+infosElem.getDate_proverif());
                    date_perem.setText("Date de péremption : "+infosElem.getDate_perem());
                    date_procycle.setText("Date de (procycle): "+infosElem.getDate_procycle());
                    date_prointerne.setText("Date de contrôle interne: "+infosElem.getDate_prointerne());
                    date_proctrl.setText("Date de contrôle : "+infosElem.getDate_proctrl());
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case SCANNER_CODE :
                if(resultCode == RESULT_OK){
                    if(data != null){
                        Barcode barcode = data.getParcelableExtra("barcode");
                        verifEditText(barcode.displayValue);
                    }
                    else{
                        Toast.makeText(this,"Erreur de scan, veuillez réessayer",Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case ANOMALIE_CODE :
                if(resultCode == RESULT_OK){
                    if(data != null){
                        String button_code = data.getStringExtra(AnomaliesActivity.BUTTON_CODE);
                        //Si l'utilisateur à cliqué sur "annuler"
                        if(button_code.equals("annuler")){
                            Toast.makeText(ElementActivity.this,"Vous avez annuler la saisie d'anomalie",Toast.LENGTH_LONG).show();
                        }
                        //Sinon...
                        else{
                            //On réccupère la liste des anomalies
                            String lstAnomalie = data.getStringExtra(AnomaliesActivity.ANOMALIE_LIST);
                            //On récupère le nom de l'image
                            String cheminImage = data.getStringExtra(AnomaliesActivity.IMAGE_PATH);
                            //Si il n'y a eu aucune anomalie d'enregistré
                            if(lstAnomalie.equals("")){
                                updateAnomalieDb();
                            }
                            //Sinon...
                            else{
                                updateAnomalieDb(lstAnomalie,cheminImage);
                            }
                        }
                    }
                }
                break;

            case MAINTENANCE_CODE :
                if(resultCode == RESULT_OK){
                    if(data != null){
                        String button_code = data.getStringExtra(MaintenanceActivity.BUTTON_CODE);
                        //Si l'utilisateur à cliqué sur "annuler"
                        if(button_code.equals("annuler")){
                            Toast.makeText(ElementActivity.this,"Vous avez annuler le contrôle de maintenance",Toast.LENGTH_LONG).show();
                        }
                        //Sinon...
                        else {
                            String strOPEFF = data.getStringExtra(MaintenanceActivity.STRING_OPEFF);
                            updateMaintenanceDb(strOPEFF);
                        }
                    }
                }
                break;

            case OPERATION_CODE :
                if(resultCode == RESULT_OK){
                    if(data != null){
                        String button_code = data.getStringExtra(OperationActivity.BUTTON_CODE);
                        //Si l'utilisateur à cliqué sur "annuler"
                        if(button_code.equals("annuler")){
                            Toast.makeText(ElementActivity.this,"Vous avez annuler l'enregistrement des opérations",Toast.LENGTH_LONG).show();
                        }
                        //Sinon...
                        else {
                            String strOPERA = data.getStringExtra(OperationActivity.STRING_OPERA);
                            Toast.makeText(this, strOPERA, Toast.LENGTH_SHORT).show();
                            updateOperationDb(strOPERA);
                        }
                    }
                }
                break;

            case CARACTERISTIQUE_CODE :
                if(resultCode == RESULT_OK){
                    if(data != null){
                        String button_code = data.getStringExtra(CaracteristiqueActivity.BUTTON_CODE);
                        //Si l'utilisateur à cliqué sur "annuler"
                        if(button_code.equals("annuler")){
                            Toast.makeText(ElementActivity.this,"Vous avez annuler l'enregistrement des caractéristiques",Toast.LENGTH_LONG).show();
                        }
                        //Sinon...
                        else {
                            String strCaract = data.getStringExtra(CaracteristiqueActivity.STRING_CARACT);
                            updateCaracteristiqueDb(strCaract);
                        }
                    }
                }
                break;

            default:
                super.onActivityResult(requestCode,resultCode,data);
        }
    }
    //Cette méthode met à jour la colonne TOUREFF de l'élément si il n'a aucune anomalie
    public void updateAnomalieDb(){
        Element element = new Element();
        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,false);
        edb.nonSelectQuery("UPDATE ELEMENTS SET TOUREFF= 'X' WHERE NUM = "+infosElem.getNum());
        element = edb.getElementWithCODE((String)infosElem.getCode());
        personneDateUpdateDb();
    }

    //Cette méthode met à jour la colonne OBSA et IMAGES
    public void updateAnomalieDb(String anomalies, String cheminImage){
        Element element = new Element();
        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,false);
        edb.openDB();
        edb.nonSelectQuery("UPDATE ELEMENTS SET OBSA = '"+anomalies+"', IMAGES = '"+cheminImage+"' WHERE NUM = "+infosElem.getNum());
        element = edb.getElementWithCODE((String)infosElem.getCode());
        personneDateUpdateDb();
    }

    public void updateMaintenanceDb(String str){
        Element element = new Element();
        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,false);
        edb.nonSelectQuery("UPDATE ELEMENTS SET OPEFF = '"+str+"' WHERE NUM = "+infosElem.getNum());
        element = edb.getElementWithCODE((String)infosElem.getCode());
        Toast.makeText(ElementActivity.this,"OPEFF= "+element.getOpeff(),Toast.LENGTH_LONG).show();
        personneDateUpdateDb();
    }

    public void updateOperationDb(String str){
        Element element = new Element();
        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,false);
        edb.nonSelectQuery("UPDATE ELEMENTS SET OPERA = '"+str+"' WHERE NUM = "+infosElem.getNum());
        element = edb.getElementWithCODE((String)infosElem.getCode());
        //Toast.makeText(ElementActivity.this,"OPEFF= "+element.getOpeff(),Toast.LENGTH_LONG).show();
        personneDateUpdateDb();
    }

    public void updateCaracteristiqueDb(String str){
        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,false);
        edb.nonSelectQuery("UPDATE ELEMENTS SET CARACT = '"+str+"' WHERE NUM = "+infosElem.getNum());
        personneDateUpdateDb();
    }

    public void personneDateUpdateDb(){
        SimpleDateFormat d = new SimpleDateFormat("yy-MM-d HH:mm:ss");
        String date  = d.format(new Date());
        Element element = new Element();
        ElementDB edb = new ElementDB(fr.cara.agess.ListeTourneeActivity.file,false);
        edb.nonSelectQuery("UPDATE ELEMENTS SET DATE = '"+date+"' , NUMOP = '"+ LoginActivity.id_user+"', VALID = 'Y' WHERE NUM = "+infosElem.getNum());
        element = edb.getElementWithCODE((String)infosElem.getCode());
    }

    @Override
    public void onBackPressed() {
        return;
    }

}
