package fr.cara.agess;

import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AnomaliesActivity extends AppCompatActivity {

    //Les clés qui nous serviront à envoyer des informations a ElementActivity
    public static final String BUTTON_CODE = "fr.cara.agess.BUTTON_CODE";
    public static final String ANOMALIE_LIST = "fr.cara.agess.ANOMALIE_LIST";
    public static final String IMAGE_PATH = "fr.cara.agess.IMAGE_PATH";
    //Les vues de notre activity
    private ListView listAnomalies;
    private TextView element;
    private Button annuler;
    private Button valider;
    private EditText editText_anomalie;
    private CheckBox anomalie_null;
    public Button ajouter;

    //variables globales
    private String filename;
    private String str;
    //Adapter concu pour les anomalies
    private AnomaliesListAdapter anomaliesListAdapter;
    //TextWatcher concu pour editText_anomalie
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            verifEditText();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_anomalies);

        //On réccupère l'intent envoyé par ElementActivity
        final Intent intent = getIntent();
        final Element infosElem =  intent.getParcelableExtra(ElementActivity.ELEM);
        //On réccupère toute les vues qui seront nécessaire à l'affichage des anomalies et a leur envoie
        element = (TextView) findViewById(R.id.element);
        listAnomalies = (ListView) findViewById(R.id.list_anomalies);
        annuler = (Button) findViewById(R.id.annuler);
        valider = (Button) findViewById(R.id.valider);
        editText_anomalie = (EditText) findViewById(R.id.editText_anomalies);
        //Le bouton "valider" est de base inaccessible
        ajouter = (Button) findViewById(R.id.ajouter);
        anomalie_null = (CheckBox)findViewById(R.id.anomalie_null);
        //Variables globales qui nous servirons plus bas
        filename = "";
        str = "";
        //Affichage des informations sur l'élément sur lequel on travaille
        element.setText(infosElem.getFam()+"/"+infosElem.getType()+" "+infosElem.getCode());

        Element element = new Element();
        ElementDB edb = new ElementDB(ListeTourneeActivity.file,false);
        edb.openDB();
        //On obtient l'élement sur lequel on travaille grace à son code
        element = edb.getElementWithCODE((String) infosElem.getCode());
        edb.close();
        //On vérifie le champs OBSA
        valider.setEnabled(false);
        if(!element.getObsa().trim().equals("")){
            //Si il est remplie par quelque chose, on l'affiche dans editText_anomalie
            editText_anomalie.setText(element.getObsa());
            Toast.makeText(this, editText_anomalie.getText().toString(), Toast.LENGTH_SHORT).show();
            valider.setEnabled(true);
        }

        AnomalieDB adb = new AnomalieDB();
        //La liste qui contiendra toute les anomalies
        final List<Anomalie> listA = new ArrayList<>();
        //On alimente listA avec toute les anomalies
        listA.addAll(adb.getAnomalies());
        //On créer une liste qui contiendra uniquement des anomalies qui concerne l'élément
        final ArrayList<Anomalie> listAForAdapter = new ArrayList<>();
        for(int i = 0; i < listA.size(); i++){
            //si l'anomalie et l'élément ont la même famille
            if(listA.get(i).getFam().equals(element.getFam())){
                //si l'anomalie et l'élément ont le même type
                if(listA.get(i).getType() != null && listA.get(i).getType().equals(element.getType())){
                    listAForAdapter.add(0,listA.get(i));
                }
                //si l'anomalie n'as pas de type
                else if(listA.get(i).getType().equals("")){
                    listAForAdapter.add(listA.get(i));
                }
            }
        }
        //L'adapter qui génère les données pour afficher notre liste d'anomalie
        anomaliesListAdapter = new AnomaliesListAdapter(this,listAForAdapter);
        listAnomalies.setAdapter(anomaliesListAdapter);
        //En cliquant sur ajouter, on vérifie les cases qui sont cochés pour chaque anomalie
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!str.equals("")) {
                    str = editText_anomalie.getText().toString();
                }
                for(int i = 0; i < listAForAdapter.size() ; i++){
                    //si l'anomalie a sa case coché
                    if(listAForAdapter.get(i).checked){
                        if(i == listAForAdapter.size()-1){
                            str += listAForAdapter.get(i).getName();
                        }
                        else{
                            str += listAForAdapter.get(i).getName()+"\n";
                            listAForAdapter.get(i).checked = false;
                        }
                        listAForAdapter.get(i).checked = false;
                        anomaliesListAdapter = new AnomaliesListAdapter(AnomaliesActivity.this,listAForAdapter);
                        listAnomalies.setAdapter(anomaliesListAdapter);
                    }
                }
                //on affiche les anomalies qui ont eut leur case coché dans editText_anomalie
                editText_anomalie.setText(str);
            }
        });
        //S'active lorque la case "aucune anomalie" est cochée
        anomalie_null.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //On efface ce qu'il y'a dans l'editText_anomalie
                    editText_anomalie.setText("");
                    //"valider devient accessible
                    valider.setEnabled(true);
                }
                //Si la case "aucune anomalie n'est pas coché et que editText_anomalie est vide
                else if(!isChecked && editText_anomalie.getText().toString().trim().equals("")){
                    //"valider deveint inaccessible
                    valider.setEnabled(false);
                }
            }
        });
        //On applique textWatcher à editText_anomalie
        editText_anomalie.addTextChangedListener(textWatcher);
        editText_anomalie.setScroller(new Scroller(this));
        editText_anomalie.setVerticalScrollBarEnabled(true);
        editText_anomalie.setMovementMethod(new ScrollingMovementMethod());
        editText_anomalie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    return true;
                }
                return false;
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnomaliesActivity.this,ElementActivity.class);
                //On spécifie à ElementActivity que l'on a cliqué sur "anuler" ici
                intent.putExtra(BUTTON_CODE,"annuler");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnomaliesActivity.this,ElementActivity.class);
                intent.putExtra(BUTTON_CODE,"valider");
                //On envie à ElementActivity la liste des anomalies
                intent.putExtra(ANOMALIE_LIST,editText_anomalie.getText().toString());
                //On envie à ElementActivity le nom de la photo prise
                intent.putExtra(IMAGE_PATH,filename);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
        public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_anomalie, menu);
        return true;
    }


        public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_prise_photo) {
            //On lance l'application de l'appareil photo et de la caméra
            Intent mediaChooser = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(mediaChooser, 1);
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //On reçoit ici l'intent venant de l'appareil photo.
        if(requestCode == 1 && resultCode == RESULT_OK){
            //On réccupère l'image priso en photo
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            //On crée une partie du nom de la future image
            String partFilename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            //On sauvegarde l'image
            sauvegarderPhoto(imageBitmap,partFilename);
        }
    }
    //Cette méthode sert à sauvegarder des images
    private void sauvegarderPhoto(Bitmap bitmap, String currentDate){
        //Le nom de l'image sera composé de "photo_"+ la date ou elle a été prise et elle aura pour format le jpeg
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        filename = outputFile.getPath();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            //On copresse(convertit) l'image en jpg ici;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Cette fonction analyse le text deeditText_anomalie en agis en fonction de ce texte.
    public void verifEditText(){
        if (!editText_anomalie.getText().toString().trim().equals("")){
            //Si editText_anomalie est remplie, on décoche la case "aucune anomalie"
            anomalie_null.setChecked(false);
            valider.setEnabled(true);
        }
        //Si la case "aucune anomalie n'est pas coché et que editText_anomalie est vide
        else if(editText_anomalie.getText().toString().trim().equals("") && !anomalie_null.isChecked()){
            valider.setEnabled(false);
        }
    }
}
