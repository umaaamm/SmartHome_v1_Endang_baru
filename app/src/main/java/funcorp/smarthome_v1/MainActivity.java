package funcorp.smarthome_v1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //SeekBar sekbar
    Firebase bacadata, bacadata1, bacadata2, bacadata3;
    String tamana,ruangana, gasa,pintua,jendelaa,kondisia, gas, ruangatamu, sekbara,pengaturan;
    //double gas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        final ImageView ruangan = (ImageView) findViewById(R.id.ruangan);
        final ImageView taman = (ImageView) findViewById(R.id.taman);
        final ImageView blower = (ImageView) findViewById(R.id.bloweerr);
        Firebase.setAndroidContext(this);
        bacadata = new Firebase("https://kebocoran-gas.firebaseio.com/kendali");
        final TextView gass = (TextView) findViewById(R.id.kadargas);

        final Switch  btnSwitch = (Switch) findViewById(R.id.switchb);


        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //on
                    bacadata.child("pengaturan").setValue("otomatis");
                }else {
                    //off
                    bacadata.child("pengaturan").setValue("manual");
                }
            }
        });


        bacadata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ruangana = dataSnapshot.child("ruangan").getValue().toString();
                tamana = dataSnapshot.child("teras").getValue().toString();
                gas = dataSnapshot.child("gas").getValue().toString();
                gasa = dataSnapshot.child("blower").getValue().toString();
                pengaturan = dataSnapshot.child("pengaturan").getValue().toString();
                //kondisia=dataSnapshot.child("kondisi").getValue().toString();
                String t, t2, d, d2, kondisi, r2, p, p2;


                //teras
                t = "off";
                t2 = "on";
                d = "off";
                d2 = "on";
                p="otomatis";
                kondisi="bahaya";

                if (ruangana.equals(t)) {
                    ruangan.setImageResource(R.drawable.lamp_luar_off);
                }
                if (ruangana.equals(t2)) {
                    ruangan.setImageResource(R.drawable.lamp_luar_on);
                }
                if (tamana.equals(d)) {
                    taman.setImageResource(R.drawable.lamp_luar_off);
                }
                if (tamana.equals(d2)) {
                    taman.setImageResource(R.drawable.lamp_luar_on);
                }

                if (gasa.equals(d)) {
                    blower.setImageResource(R.drawable.kipas_off);
                }
                if (gasa.equals(d2)) {
                    blower.setImageResource(R.drawable.kipas_blue);
                }

                if (pengaturan.equals(p)) {
                    btnSwitch.setChecked(true);
                }else {
                    btnSwitch.setChecked(false);
                }

                gass.setText(dataSnapshot.child("gas").getValue().toString());


                double gasa =  Double.parseDouble(gas);
                if (gasa >= 300){
                    notifkondisi();
                }

//                if (pintua.equals(t2)){
//                    notippintu();
//                }
//                if (jendelaa.equals(d2)){
//                    notifjendela();
//                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        blower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String a = "off";
                    if (gasa.equals(a)) {
                        bacadata.child("blower").setValue("on");
                    } else {
                        bacadata.child("blower").setValue("off");
                    }
                } catch (Exception e) {

                }
            }
        });

        ruangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String a = "off";
                    if (ruangana.equals(a)) {
                        bacadata.child("ruangan").setValue("on");
                    } else {
                        bacadata.child("ruangan").setValue("off");
                    }
                } catch (Exception e) {

                }
            }
        });
        taman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String a = "off";
                    if (tamana.equals(a)) {
                        bacadata.child("teras").setValue("on");
                    } else {
                        bacadata.child("teras").setValue("off");
                    }
                } catch (Exception e) {

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void notifkondisi() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.setAction("alkohol");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.kipas_off)
                .setContentTitle("Peringatan")
                .setContentText("Ada Kebocoran GAS!!")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(3, notificationBuilder.build());
    }

}
