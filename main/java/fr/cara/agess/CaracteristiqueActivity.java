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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaracteristiqueActivity extends AppCompatActivity {
    public static final String STRING_CARACT = "fr.cara.agess.CaracteristiqueActivity.STRING_CARACT";
    public static final String BUTTON_CODE = "fr.cara.agess.CracteristiqueActivity.BUTTON_CODE";

    private ListView liste_operation;
    private Button valider;
    private Button annuler;

    private List<Caracteristique> listC;
    private CaracteristiqueListAdapter caracteristiqueListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caracteristique);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        liste_operation = (ListView)findViewById(R.id.liste_caracteristique);
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

        CaracteristiqueDB caracteristiqueDB = new CaracteristiqueDB();
        listC = new ArrayList<>();
        listC.addAll(caracteristiqueDB.getCaracteristique(element.getFam()));
        if(element.getCaract() != null){
            String[] caractValList = element.getCaract().split(";");
            for (String s : caractValList){
                String[] caractVal = s.split("=");
                if (caractVal.length == 2){
                    for(int i = 0; i < listC.size(); i++){
                        if(listC.get(i).getPosition() == Integer.parseInt(caractVal[0])){
                            listC.get(i).setNewVal(caractVal[1]);
                        }
                    }
                }
            }
        }
        caracteristiqueListAdapter =  new CaracteristiqueListAdapter(this,listC);
        liste_operation.setAdapter(caracteristiqueListAdapter);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCaract= "";
                for(int i = 0; i < listC.size(); i++){
               if(i != listC.size()-1){
                        strCaract += (i+1)+"="+(listC.get(i).getNewVal() == null ? ";" : (listC.get(i).getNewVal())+";");
                    }
                    else{
                        strCaract += (i+1)+"="+listC.get(i).getNewVal();
                    }
                }
                Intent intent = new Intent(CaracteristiqueActivity.this,ElementActivity.class);
                intent.putExtra(STRING_CARACT, strCaract);
                intent.putExtra(BUTTON_CODE,"valider");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaracteristiqueActivity.this,ElementActivity.class);
                //On spécifie à ElementActivity que l'on a cliqué sur "anuler" ici
                intent.putExtra(BUTTON_CODE,"annuler");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
