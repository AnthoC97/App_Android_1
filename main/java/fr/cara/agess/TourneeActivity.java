package fr.cara.agess;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TourneeActivity extends AppCompatActivity {


    public final static String ELEM = "fr.cara.agess.ELEM";
    public final static String LIST_EXT = "fr.cara.agess.LIST_EXT";
    public final static String CODE = "fr.cara.agess.CODE";
    public final static String FILE_NAME = "fr.cara.agess.FILE_NAME";
    public final static String ID = "fr.cara.agess.ID";

    public static  boolean FILTRE = false;

    public static String query = "";

    private Button tournees;
    private Button filtre;
    private Button fin_de_travail;
    private ListView listElements;
    private String[] code;
    private List<Element> listE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournee);
        //La barre qui s'affiche en haut et qui contiendra nos menus
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //On récupère l'intent envoyé par l'activité ListeTourneeActivity
        Intent intent = getIntent(); //Récupération de listE'intent envoyé par login
        //On réccupère le nom de la base de donnée à utiliser
        tournees = (Button)findViewById(R.id.tournees);
        fin_de_travail = (Button)findViewById(R.id.fin_de_travail);
        filtre = (Button)findViewById(R.id.filtre);
        listElements = (ListView) findViewById(R.id.listElements);
        //On créer une instance de l'objet ElementDb

        ElementDB edb = new ElementDB(ListeTourneeActivity.file,false);
        //On crée une liste d'élément
        listE = new ArrayList<Element>();
        //On alimente cete liste grace à edb  --------------- Async
        if((query.trim().equals(""))){
            listE = edb.getElement();
        }
        else{
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            listE = edb.getElement(query);
            List<Element> l = edb.getElement();
            //On montre visuellement le fait que le filtre est efficace
            if(!(l.size() == listE.size())){
                FILTRE = true;
                filtre.setBackground(ContextCompat.getDrawable(this, R.drawable.button_filter));
            }
            if(listE.size() == 0){
                //on créer une boite de dialogue
                AlertDialog.Builder adb = new AlertDialog.Builder(TourneeActivity.this);
                //on attribut un titre à notre boite de dialogue
                adb.setTitle("Aucun element trouvé");
                //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
                adb.setMessage("Nous n'avons pas trouvé d'élement, veuillez changer les critères de sélection");
                //on indique que l'on veut le bouton ok à notre boite de dialogue
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TourneeActivity.this,FiltreActivity.class);
                        startActivity(intent);
                    }
                });
                adb.setCancelable(false);
                //on affiche la boite de dialogue
                adb.show();
            }
        }
        //Cette vriable contiendra les codes de chaque élément de la base de donnée
        code = new String[listE.size()];
        for(int i = 0; i < listE.size(); i++){
            code[i] = listE.get(i).getCode();
        }
        //Cette arrayList contiendra les informations sur chaque élément et servira a les afficher
        ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
        //On alimente listItem
        for(int i = 0; i < listE.size(); i++){
            getElement(listItem,i);
        }
        //L'adapter qui génère les données qui seront affichés dans listElements
        TourneeAdapter tourneeAdapter = new TourneeAdapter(this,listE);

        listElements.setAdapter(tourneeAdapter);
        setItemClickable();

        tournees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourneeActivity.this,ListeTourneeActivity.class);
                startActivity(intent);
            }
        });

        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourneeActivity.this,FiltreActivity.class);
                startActivity(intent);
            }
        });

        fin_de_travail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on créer une boite de dialogue
                AlertDialog.Builder adb = new AlertDialog.Builder(TourneeActivity.this);
                //on attribut un titre à notre boite de dialogue
                adb.setTitle("Fin de Travail");
                //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
                adb.setMessage("Cette action verrouille définitivement toutes vos modifications et prépare la base pour une prochaine synchronisation. Les éléments modifiées ne seront plus accessibles. Voulez-vous continuer ?");
                //on indique que l'on veut le bouton ok à notre boite de dialogue
                adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String elemModifies = "";
                        ElementDB edb = new ElementDB(ListeTourneeActivity.file,false);
                        ElementDB edb2 = new ElementDB(ListeTourneeActivity.file,true);
                        List<Element> list =  edb.getElementValid();
                        for (int i = 0; i < list.size(); i++){
                            Element e = list.get(i);
                            edb2.insert(e);
                            elemModifies += list.get(i).getFam()+" "+list.get(i).getType()+" code : "+list.get(i).getCode()+"\n";
                        }
                        edb.nonSelectQuery("DELETE FROM ELEMENTS WHERE VALID = 'Y'");
                        AlertDialog.Builder adb2 = new AlertDialog.Builder(TourneeActivity.this);
                        adb2.setTitle("Elements sauvegardés");
                        adb2.setMessage(elemModifies);
                        adb2.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(TourneeActivity.this,ListeTourneeActivity.class);
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

    public void verifEditText(String text){
        String[] elem = new String[]{};
        for (String s : code){
            if (text.trim().equals(s)){
                elem = new String[]{s};
            }
        }
        if(elem.length == 0){
            Toast.makeText(this,"Aucun ElementActivity ne correspond au code fourni",Toast.LENGTH_LONG).show();
        }
        else{
            ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();

            for(int i = 0; i < listE.size(); i++){
                if (text.trim().equals(listE.get(i).getCode())){
                    getElement(listItem,i);
                }
            }
            TourneeAdapter tourneeAdapter = new TourneeAdapter(this,listE);

            listElements.setAdapter(tourneeAdapter);
            //On défini des actions au touché d'une vue dans ListView
            setItemClickable();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Alimente la barre des menus et ajoute les items présents dans R.menu.menu_tournee
        getMenuInflater().inflate(R.menu.menu_tournee, menu);
        //On crée une instance de searchView afin de pouvoir chercher un élément
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        //Permet de donner l'acces au service de système de recherche
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        //Permet d'obtenir des informations sur l'activité
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        //Des que l'on valide la recherche que l'on veut effectuer ...
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //On lance la fonction qui affichera l'élément recherché s'il existe
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

    //Cette méthode défini les actions sur les autres items du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //rafraichissement de la liste
        if(item.getItemId() ==  R.id.action_reload_all) {
            refresh();
        }
        else if(item.getItemId() == R.id.action_scan){
            //scanner un code bar
            Intent intent = new Intent(TourneeActivity.this,ScannerActivity.class);
            startActivityForResult(intent,0);
        }
        else if(item.getItemId() == R.id.action_deconnexion){
            //on créer une boite de dialogue
            AlertDialog.Builder adb = new AlertDialog.Builder(TourneeActivity.this);
            //on attribut un titre à notre boite de dialogue
            adb.setTitle("Déconnexion");
            //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
            adb.setMessage("Voulez vous vous déconnecter ?");
            //on indique que l'on veut le bouton ok à notre boite de dialogue
            adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(TourneeActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
            adb.setNegativeButton("Non",null);
            //on affiche la boite de dialogue
            adb.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //Méthode permettant le rafraichissement de la liste
    public void refresh(){
        ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();

        for(int i = 0; i < listE.size(); i++){
            getElement(listItem,i);
        }

        TourneeAdapter tourneeAdapter = new TourneeAdapter(this,listE);

        listElements.setAdapter(tourneeAdapter);
        setItemClickable();
    }

    //Méthode définissant des actions lors d'un clic sur une vue
    public void setItemClickable(){
        listElements.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(TourneeActivity.this,String.valueOf(parent.getCount()),Toast.LENGTH_LONG).show();
                Element selectedItem = listE.get(position);
                //On crée un intent à destination de ElementActivity
                Intent intent = new Intent(getApplicationContext(), ElementActivity.class);
                //On isère dans l'intent les infos sur l'élément sélectionné...
                intent.putExtra(ELEM, selectedItem);
                //La liste des élements...
                intent.putParcelableArrayListExtra(LIST_EXT,(ArrayList<Element>)listE);
                intent.putExtra(CODE,code);
                //Le nom de fichier de la base de donnée utilisée.
                startActivity(intent);
            }
        });
    }

    public void showElementValid(){
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        for(int i = 0; i < listE.size(); i++){
            Toast.makeText(this, listE.get(i).getValid() , Toast.LENGTH_SHORT).show();
            if(listE.get(i).getValid().equals("Y")){
                listElements.getChildAt(i).setBackground(ContextCompat.getDrawable(this, R.drawable.border));
            }
        }
    }
    //Des que l'on reçoit un intent d'une activité lancée à partir de celle ci
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //L'intent de retour est celui du scanner
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                if(data != null){
                    //On récupère le Barcode envoyé par la scanner
                    Barcode barcode = data.getParcelableExtra("barcode");
                    //On lance la fonction qui affichera l'élément recherché grace au scanner
                    verifEditText(barcode.displayValue);
                }
                else{
                    Toast.makeText(this,"Erreur de scan, veuillez réessayer",Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                if(data != null){

                }
            }
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    //Méthode qui sert a alimenter une liste afin de générer une vue via une liste view et un adapter
    //personalisé
    public void getElement(ArrayList<HashMap<String,String>> l, int i){
        HashMap<String,String> hm = new HashMap<String,String>();
        hm.put("num",String.valueOf(listE.get(i).getNum()));
        hm.put("famTypeCode",listE.get(i).getFam()+"/"+listE.get(i).getType()+" "+listE.get(i).getCode());
        hm.put("code",listE.get(i).getCode());
        hm.put("site",listE.get(i).getSite());
        hm.put("batiment",listE.get(i).getBatiment());
        hm.put("niveau",listE.get(i).getNiveau());
        hm.put("zone",listE.get(i).getZone());
        hm.put("lieu",listE.get(i).getLieu());
        hm.put("info",listE.get(i).getInfos());
        hm.put("date_proverif",listE.get(i).getDate_proverif());
        hm.put("date_perem",listE.get(i).getDate_perem());
        hm.put("date_procycle",listE.get(i).getDate_procycle());
        hm.put("date_prointerne",listE.get(i).getDate_prointerne());
        hm.put("date_proctrl",listE.get(i).getDate_proctrl());
        l.add(hm);
    }

    public  void test(){
        Toast.makeText(this, String.valueOf(listE.size()), Toast.LENGTH_SHORT).show();
    }

}
