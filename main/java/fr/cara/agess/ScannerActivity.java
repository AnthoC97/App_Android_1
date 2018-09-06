package fr.cara.agess;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    private SurfaceView camera_preview; //La vue qui gèrera l'appercu de la caméra

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        camera_preview = (SurfaceView) findViewById(R.id.camera_preview);
        createCameraSource();
    }

    public void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build(); //Ce qui nous servira à détecter les codes bares sous diiférents formats
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector) //Ce nous servira à manager la caméra avec en conjonction avec un détecteur sous-jacent (ici le détecteur de code barre)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();
        //On utilise un surfaceHolder obtenu depuis notre surfaceView et on appelle CallBack pour bien gérer le cycle de vie de SurfaceView
        camera_preview.getHolder().addCallback(new SurfaceHolder.Callback() {
            //A la création de la surface
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //A modifier avec le système de permission pour api supérieur ou égal à 23
                if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(ScannerActivity.this,"pas la permission",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    cameraSource.start(camera_preview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //En cas de changement de la surface
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            //A la destruction de la surface
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                //On cas de succès de scan
                if(barcodes.size()>0){
                    Intent intent = new Intent();
                    intent.putExtra("barcode",barcodes.valueAt(0)); //on prend le dernier code barre scanné
                    setResult(RESULT_OK,intent);
                    finish();
                }
                //En cas d'échec
            }
        });
    }
}
