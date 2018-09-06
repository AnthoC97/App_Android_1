package fr.cara.agess;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OperationActivity extends AppCompatActivity {
    public static final String STRING_OPERA = "fr.cara.agess.OperationActivity.STRING_OPERA";
    public static final String BUTTON_CODE = "fr.cara.agess.OperationActivity.BUTTON_CODE";

    private TextView nbre_operation;
    private ListView liste_operation;
    private Button valider;
    private Button annuler;

    private List<Operation> listO;
    private OperationListAdapter operationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nbre_operation = (TextView) findViewById(R.id.nbre_operation);
        liste_operation = (ListView)findViewById(R.id.liste_operation);
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

        OperationDB operationDB = new OperationDB();
        listO = new ArrayList<>();
        listO.addAll(operationDB.getOperation(element.getFam()));
        nbre_operation.setText("liste des opérations : "+ listO.size());
        if(element.getOpera() != null){
            for (int i = 0; i < element.getOpera().length(); i++){
                if(element.getOpera().charAt(i) == "1".charAt(0)){
                    listO.get(i).checked = true;
                }
            }
        }
        operationListAdapter =  new OperationListAdapter(this,listO);
        liste_operation.setAdapter(operationListAdapter);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOPERA = "";
                for(int i = 0; i < listO.size(); i++){
                    if(listO.get(i).checked){
                        strOPERA += "1";
                    }
                    else{
                        strOPERA += "0";
                    }
                }
                if(strOPERA.equals("")){
                    Toast.makeText(OperationActivity.this,"Vous n'avez coché aucune case. Si il n' " +
                            "y a aucune case à cocher, cliquez sur annuler",Toast.LENGTH_LONG).show();
                }
                else{
                    String[] controleDate = strOPERA.split("/");
                    strOPERA = "";
                    for(String s : controleDate){
                        strOPERA += s;
                    }
                    Intent intent = new Intent(OperationActivity.this,ElementActivity.class);
                    intent.putExtra(STRING_OPERA, strOPERA);
                    intent.putExtra(BUTTON_CODE,"valider");
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OperationActivity.this,ElementActivity.class);
                //On spécifie à ElementActivity que l'on a cliqué sur "anuler" ici
                intent.putExtra(BUTTON_CODE,"annuler");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
