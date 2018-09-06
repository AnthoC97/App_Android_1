package fr.cara.agess;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class ListeTourneeActivity extends AppCompatActivity {

    //Phrase d'accueil
    private TextView accueil = null;
    //ListView qui contient la liste des tournées, relatives aux bases de donnees presentes dans le
    // dossier BDD (possibilité de changer de dossiers)
    private ListView listeFichier;
    //Les AtomicBoolean servent ici à jouer le rôle d'un boolean mais elle sont Thread-safe
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private AtomicBoolean isPausing = new AtomicBoolean(false);
    //Chaines de caractere qui représentent des "clefs" utilisees plus bas
    private static final String LIST_OF_FILES = "ListOfFiles";
    public final static String FILE_NAME = "fr.cara.agess.FILE_NAME";
    public static String file = "";

    //Le Handler a pour fonction la com entre la Thread d'Arrière plan (ci-bas, c'est la Thread chargementFichier)
    //et la Thread UI. On défini ce que fais handler ici.
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            //On récupere les nom fichiers de base de donnees (*.db) forni par la methode String[] DbFileLoader.
            String[] filesDb = msg.getData().getStringArray(LIST_OF_FILES);
            //L'adapter qui génère les données qui seront affichés dans listeFichier
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListeTourneeActivity.this,android.R.layout.simple_list_item_1,filesDb);
            //On associe listeFichier et adapter
            listeFichier.setAdapter(adapter);
            //On défini le comportement que chaque vues dans ListeFichier doit avoir.
            listeFichier.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //On réccupère la chaîne de caractère de l'item sélectionné, cette chaine correspond
                    //nom de la base de donnée correspondant a la tournee que l'on veut effectuer.
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    //On crée un intent permettant la communication entre cette activité et Tournée
                    Intent intent = new Intent(getApplicationContext(), TourneeActivity.class);
                    //On insère le nom du fichier avec la clé qui servira à la réccupérer.
                    file = selectedItem;
                    //On fait une copie de la base de donnée (tournée) sélectionée et on travaillera sur cette copie
                    copyDB(selectedItem);
                    copyDB(selectedItem);
                    //On démarre l'activité tournée
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_tournee);
        //Récupération de l'intent envoyé par login
        Intent i = getIntent();
        //On réccupère le login que l'utilisateur à rentré
        //On récupère le TextView d'accueil
        accueil = (TextView) findViewById(R.id.accueil);
        //On récupère la ListView avec l'id "ListeFichier".
        listeFichier = (ListView)findViewById(R.id.listeFichier);
        //Modification du TextView d'accueil avec l'id récupéré
        accueil.setText("Bonjour " + LoginActivity.id_user);
        TourneeActivity.query="";

    }

    @Override
    public void onStart(){
        super.onStart();
        //Thread qui s'occupera de charger les fichiers *.db de manière asynchrone
        Thread chargementFichier = new Thread(new Runnable(){
            //Le Bundle qui contiendra la liste des fichiers *.db
            Bundle messageBundle = new Bundle();
            //Le message échangé entre la Thread et le Handler
            Message myMessage;

            public void run(){
                try{
                    //On réccupère les fichiers .db
                    String[] fichiersDb = DbFileLoader();
                    //on récupère un objet de type message dans le pool de handler
                    myMessage = handler.obtainMessage();
                    //On insère dans notre bundle les noms des fichiers récupérées
                    messageBundle.putStringArray(LIST_OF_FILES,fichiersDb);
                    //On insère messageBundle dans myMessage
                    myMessage.setData(messageBundle);
                    //on envoie le message au Handler
                    handler.sendMessage(myMessage);
                }
                catch (Throwable t){
                    t.printStackTrace();
                }
            }
        });
        isRunning.set(true);
        isPausing.set(false);
        //On lance la thread
        chargementFichier.start();
    }

    public void onStop() {
        super.onStop();
        //Mise-à-jour du booléen pour détruire la Thread de background
        isRunning.set(false);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Mise-à-jour du booléen pour mettre en pause la Thread de background
        isPausing.set(true);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Mise-à-jour du booléen pour relancer la Thread de background
        isPausing.set(false);
    }


    //Cette méthode liste les fichiers .db du récupertoire concerné (Le répertoire BDD).
    private String[] DbFileLoader() {
            //On vérifie si le stockage externe peut être monté
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                //On récupère le chemin du dossier contenant les bases de données et on créer un objet
                //de type file avec ce chemin
                String dossierBDDChemin = Environment.getExternalStorageDirectory() + "/BDD";
                File dossierBDD = new File(dossierBDDChemin);
                //On réccupère les noms des fichiers présents dans notre dossier
                String[] files = dossierBDD.list();
                //Si il n'y a pas de fichier ...
                if (files == null) {
                    Toast.makeText(this, "Votre dossier semble vide, veuillez le vérifier à l'aide d'un gestionnaire de fichier", Toast.LENGTH_LONG).show();
                }
                //Sinon ...
                else {
                    int i = 0;
                    String indexes = "";
                    for(String s : files){
                        //On réccupère uniquement les fichiers .db (d'autres fichiers pourraient être
                        //présents dans notre dossier grace à leurs emplacement dans files
                        if (s.endsWith(".db")){
                            indexes += i+",";
                            i++;
                        }
                    }
                    String[] filesDb = new String[indexes.split(",").length];
                    for(int j = 0; j < indexes.split(",").length; j++){
                        //On récupère les fichiers .db
                        filesDb[j] =  files[Integer.parseInt(indexes.split(",")[j])];
                    }
                    return filesDb;
                }
            }
            //Sinon ...
            else {
                Toast.makeText(this, "Votre stockage externe n'es pas monté", Toast.LENGTH_LONG).show();
            }
        return new String[0];
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_liste_tournee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_deconnexion){
            //on créer une boite de dialogue
            AlertDialog.Builder adb = new AlertDialog.Builder(ListeTourneeActivity.this);
            //on attribut un titre à notre boite de dialogue
            adb.setTitle("Déconnexion");
            //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
            adb.setMessage("Voulez vous vous déconnecter ?");
            //on indique que l'on veut le bouton ok à notre boite de dialogue
            adb.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListeTourneeActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
            adb.setNegativeButton("Non",null);
            //on affiche la boite de dialogue
            adb.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //Cette méthode copie une base de donnée présente dans /BDD vers /BDD/BDD-Fin_de_travail
    public void copyDB(String dbFile) {
        File f = new File(Environment.getExternalStorageDirectory() + "/BDD/BDD-Fin_de_travail/" + dbFile);
        try {
            if(!f.exists()) {
                // Copie depuis le in vers le out
                FileChannel in = new FileInputStream(Environment.getExternalStorageDirectory() + "/BDD/" + dbFile).getChannel(); // canal d'entrée
                FileChannel out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/BDD/BDD-Fin_de_travail/" + dbFile).getChannel(); // canal de sortie
                in.transferTo(0, in.size(), out);
                ElementDB e = new ElementDB(dbFile, true);
                e.nonSelectQuery("DELETE FROM ELEMENTS");
            }
        } catch (IOException e) {


        }
    }
}