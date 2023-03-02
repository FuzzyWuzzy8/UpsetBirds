package pl.pue.air.upsetbirds;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;

//import android.app.ActionBar;
//import androidx.appcompat.app.ActionBar;

public class MainActivity extends AppCompatActivity {

    private boolean CheckIfMuted;
    // private boolean SupportingHardware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //locale language
        loadLocale();
        /*
        //
        ActionBar actionBar = getSupportActionBar(); //change actionbar title
        actionBar.setTitle(getResources().getString(R.string.app_name));
        */

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,          //make if full screen
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                            //if we click, we start activity
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        //language
        TextView changeLang = findViewById(R.id.language);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
        //

        /*
        // First check if device is supporting
        SupportingHardware = getPackageManager().hasSystemFeature(PackageManager.); //add service here
        if (!SupportingHardware) {
            // show message device doesn't support flash
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(getResources().getString(R.string.error));
            alert.setMessage(getResources().getString(R.string.message));
        }
        //

         */

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        // saved in shared preferences

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText(getString(R.string.topscore) + prefs.getInt("topscore", 0));   //shows top score /edit string to change text
        //highScoreTxt.setText("Highest Score: " + prefs.getInt("topscore", 0));   //shows top score /edit to change text


        CheckIfMuted = prefs.getBoolean("CheckIfMuted", false);

        final ImageView VolumeC = findViewById(R.id.volumeButton);

        if (CheckIfMuted)                                                                          //mute or unmute game
            VolumeC.setImageResource(R.drawable.ic_volume_off_black_24dp);
        else
            VolumeC.setImageResource(R.drawable.ic_volume_up_black_24dp);

        VolumeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckIfMuted = !CheckIfMuted;
                if (CheckIfMuted)
                    VolumeC.setImageResource(R.drawable.ic_volume_off_black_24dp);
                else
                    VolumeC.setImageResource(R.drawable.ic_volume_up_black_24dp);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("CheckIfMuted", CheckIfMuted);
                editor.apply();

            }
        });

    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English", "Polish", "French", "German"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                if (i == 0){
                    //English
                    setLocale("en");
                    recreate();
                }
                else if (i == 1){
                    //Polish
                    setLocale("pl");
                    recreate();
                }
                else if (i == 2){
                    //French
                    setLocale("fr");
                    recreate();
                }
                else if (i == 3){
                    //German
                    setLocale("de");
                    recreate();
                }
                dialogInterface.dismiss();   //dismiss alert dialog when language is selected

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();                     //show alert dialog

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //load language from shared preference
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }


}
