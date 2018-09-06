package fr.cara.agess;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    public final static String ID = "fr.cara.agess.ID";
    private TextView date = null;
    public EditText id = null;
    private Button connexion = null;
    private Button accorder = null;
    public static String id_user;


    //cE TextWatcher surveillera la modifiecation d'un EditText
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        //Cette méthode s'exécute en pleine modification d'un EditText
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            verifEditText();
        }

        @Override
        public void afterTextChanged(Editable s) {
            id_user=s.toString();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //On défini ici le format d'une date
        SimpleDateFormat d = new SimpleDateFormat("EEEE d MMM yyyy");
        //on récupère le TextView qui servira à afficher la date.
        date = (TextView) findViewById(R.id.date);
        //on récupère l'EditText qui nous servira à réccupérer le nom de l'utilisateur.
        id = (EditText) findViewById(R.id.id);
        //On réccupère le bouton qui servira à ce connecter
        connexion = (Button) findViewById(R.id.connexion);
        id.addTextChangedListener(textWatcher);
        connexion.setEnabled(false);
        //On met à jour le TextView de la date avec la date actuelle
        date.setText(d.format(new Date()));

        id.requestFocus();
        id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //On créer un intent pour passer à l'activité main
                    Intent mainActivite = new Intent(getApplicationContext(),ListeTourneeActivity.class);
                    //On fourni à cet intent l'id que l'on récupéré.
                    mainActivite.putExtra(ID,id.getText().toString());
                    //Dès que l'on clique on passe à l'activité suivante
                    startActivity(mainActivite);
                    return true;
                }
                return false;
            }
        });
        if(id.isFocused()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        if(
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            explain();
        }else{
            /* Pour la localisation
            Criteria critere = new Criteria();

// Pour indiquer la précision voulue
// On peut mettre ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision
            critere.setAccuracy(Criteria.ACCURACY_FINE);

// Est-ce que le fournisseur doit être capable de donner une altitude ?
            critere.setAltitudeRequired(true);

// Est-ce que le fournisseur doit être capable de donner une direction ?
            critere.setBearingRequired(true);

// Est-ce que le fournisseur peut être payant ?
            critere.setCostAllowed(false);

// Pour indiquer la consommation d'énergie demandée
// Criteria.POWER_HIGH pour une haute consommation, Criteria.POWER_MEDIUM pour une consommation moyenne et Criteria.POWER_LOW pour une basse consommation
            critere.setPowerRequirement(Criteria.POWER_HIGH);

// Est-ce que le fournisseur doit être capable de donner une vitesse ?
            critere.setSpeedRequired(true);

            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            ArrayList<LocationProvider> providers = new ArrayList<>();
            ArrayList<String> names = (ArrayList<String>) locationManager.getProviders(critere,true);

            for(String name : names){
                providers.add(locationManager.getProvider(name));

            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Toast.makeText(LoginActivity.this,"Latitude : "+location.getLatitude()+", Longitude : "+location.getLongitude(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });*/
        }

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On créer un intent pour passer à l'activité main
                Intent mainActivite = new Intent(getApplicationContext(),ListeTourneeActivity.class);
                //On fourni à cet intent l'id que l'on récupéré.
                mainActivite.putExtra(ID,id.getText().toString());
                //Dès que l'on clique on passe à l'activité suivante
                startActivity(mainActivite);
            }
        });
    }
    //Cette méthode sert à vérifier si un l'EditText est remplie afin d'activer le bouton connexion ou non.
    public void verifEditText(){
        if (id.getText().toString().equals("")){
            connexion.setEnabled(false);
        }
        else{
            connexion.setEnabled(true);
        }

    }

    //Cette méthode permet à l'utilisateur de nous donner sa permission ou non pour l'acces a sont stockage externe (carte SD).
    private void explain() {
        FragmentManager fm = getFragmentManager();
        DialogFragmentPermission dialogFragment = new DialogFragmentPermission ();
        dialogFragment.show(fm, "Sample Fragment");
    }

    //Cette méthode demande à l'utilisateur la permission spécifique grace à une boîte de dialogue qui s'affiche
    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 2);
        }
    }


    //Cette méthode sert à gérer le cas ou l'utilisateur à refuser la permission et à cocher la case "ne plus demander"
    private void displayOptions() {
        final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        final Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent); //On redirige l'utilisateur vers les paramètres de l'application
    }

    //Listener sur les demandes de permission.
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Si l'on reçoit la demande de permission avec le code 2 (voir méthode askForPermission).
        if (requestCode == 2) {
            //L'utilisateur à autorisé
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                id.setEnabled(true);
            }
            //L'utilisateur a refusé et a coché "ne plus demander"
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) == false) {
                    displayOptions();
                }
                //L'utilisateur à refusé sans cocher la case "ne plus demander"
                else {
                    explain();
                }
            }
        }
    }


}
