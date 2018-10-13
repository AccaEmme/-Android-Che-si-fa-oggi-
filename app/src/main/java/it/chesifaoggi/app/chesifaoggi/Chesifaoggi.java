package it.chesifaoggi.app.chesifaoggi;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Chesifaoggi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int REQUEST_WRITE_STORAGE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chesifaoggi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Per suggerimenti o collaborazioni: chesifaoggi@live.it", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ImageButton logo = (ImageButton) findViewById(R.id.logobutton);
        logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Che si fa oggi? - Te lo dico subito!", Toast.LENGTH_LONG).show();
                Intent eventIntent = new Intent(Chesifaoggi.this, EventiActivity.class);
                Chesifaoggi.this.startActivity(eventIntent);
            }
        });
/*
        try {
            DBLayer db = new DBLayer(Chesifaoggi.this);
            db.open();

            db.close();
        } catch(Exception e){
            Log.e("Chesifaoggi-db create", e.toString());
        }
        */

        checkPermissionSD();
    }


    @TargetApi(Build.VERSION_CODES.M)// sembra non funzionare
    private void checkPermissionSD(){
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, "Build.VERSION.SDK_INT < 23", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Build.VERSION.SDK_INT > 23", Toast.LENGTH_LONG).show();

            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
        }
    }



    @TargetApi(Build.VERSION_CODES.M) // sembra non funzionare
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(this, getString(R.string.requestpermission_sd), Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.chesifaoggi_exittitle))
                    .setMessage(getString(R.string.chesifaoggi_exitmessage))
                    .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chesifaoggi, menu);
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
            Intent myIntent = new Intent(Chesifaoggi.this, SettingsActivity.class);
            Chesifaoggi.this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        TextView helloworld = (TextView) findViewById(R.id.helloworld);
        Intent intent;
        Fragment frammento;

        if (id == R.id.nav_home) { // Gestirla con i Fragment
            // Handle the camera action
            // helloworld.setText("Home");
            intent = new Intent(Chesifaoggi.this, EventiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lista) {
            helloworld.setText("Calendario eventi ***");
        } else if (id == R.id.nav_galleria) {
            helloworld.setText("galleria eventi ***");
        } else if (id == R.id.nav_aigioia){
            /*
            helloworld.setText("G.I.O.I.A. [ A.I ]\n\n\nG.I.O.I.A. Is Our Intelligence Artificial\nG.I.O.I.A. Is Our Interest Amousment in an Artificial Intelligence\nGIOIA Is Optimizing Interests and Amousment in an Artificial Intelligence\nGioia indicates and organizes ideas automatically");
            helloworld.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            */
            intent = new Intent(Chesifaoggi.this, AIActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_insertEvent){
            String hw;

            CountEventi ce = new CountEventi();
            //hw = ce.toString();
            ArrayList<CountEventi.CountEvento> alce = ce.getFromDB(this.getBaseContext());
            CountEventi cnti = new CountEventi(alce);
            hw = cnti.toString();
            hw += "\n\n";
            hw += "La tua provincia ha pochi eventi?\n" +
                    "Non renderla emarginata, inseriscili! :-)\n" +
                    "\n";

            helloworld.setText(hw + Html.fromHtml("http://www.chesifaoggi.it"));
            helloworld.setTextSize(12);
            Linkify.addLinks(helloworld, Linkify.ALL);
            helloworld.setLinksClickable(true);

            WebView webview = (WebView) findViewById(R.id.webView);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl("http://chesifaoggi.altervista.org/stadio7.3/?p=insertEvent");

        } else if (id == R.id.nav_advancedsearch) {
            helloworld.setText("Ricerca avanzata");
        } else if (id == R.id.nav_settings) {
            //helloworld.setText("Settings");
            intent = new Intent(Chesifaoggi.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            helloworld.setText("Condividi");
        } else if (id == R.id.nav_send) {
            helloworld.setText("Invia");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}