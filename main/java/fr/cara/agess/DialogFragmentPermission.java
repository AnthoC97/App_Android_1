package fr.cara.agess;

import android.Manifest;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by cara on 03/07/2017.
 */

public class DialogFragmentPermission extends DialogFragment{

    private Button accorder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //crée une vue à partir de dialog_permission_fragment
        View rootView = inflater.inflate(R.layout.dialog_permission_fragment, container, false);
        getDialog().setTitle("Simple Dialog");

        accorder = (Button) rootView.findViewById(R.id.accorder);
        accorder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                askForPermission();
            }
        });
        return rootView;
    }

    private void explain() {
        FragmentManager fm = getFragmentManager();
        DialogFragmentPermission dialogFragment = new DialogFragmentPermission ();
        dialogFragment.show(fm, "Sample Fragment");
    }

    //Cette méthode demande à l'utilisateur la permission spécifique grace à une boîte de dialogue qui s'affiche
    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA ,Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
    }


    //Cette méthode sert à gérer le cas ou l'utilisateur à refuser la permission et à cocher la case "ne plus demander"
    private void displayOptions() {
        final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        final Uri uri = Uri.fromParts("package", "fr.cara.agess", null);
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
                dismiss();
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
