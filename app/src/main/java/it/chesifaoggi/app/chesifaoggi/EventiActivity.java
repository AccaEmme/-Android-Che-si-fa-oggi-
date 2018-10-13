package it.chesifaoggi.app.chesifaoggi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by gaia on 25/02/16.
 */
public class EventiActivity extends Activity{
    protected static ArrayList<Evento> stateList = new ArrayList<Evento>(), nuovaLista;
    EditText inputSearch;
    MyCustomAdapter dataAdapter = null;
    private String db_evento_attributi = "`count`,`titolo`, `descrizione`, `data_i`, `data_f`, `zona`, `latitudine`, `longitudine`, `struttura`, `via`, `immagine`, `gratuito`, `costo`, `su_prenotazione`, `tipo_prenotazione`, `da_eta`, `a_eta`, `tipo_eta`, `external_urls`, `youtube_url`, `contatti`, `hashtag`, `datainserimento`, `username`, `speciale`, `preferito`, `categorie`";
    SharedPreferences myPreference = null;

    /*
        da fare:
         V Rinominare Settings in SettingsActivity e accorpare il settings e layout
         - Cerca eventi: usa posizione
         - entro raggio: 0 Km
         - preferiti
         - listview eventi con cuoricino per preferito
         - tab eventi N giorni
         V categorie nel json
         -
         - Ricerca tra categorie e province selezionate

         - contatti e descrizione textview dovrebbero essere giustificate, vanno fuori schermo

//android:allowBackup="true"
WebDB:
 - a prescindere da dove si fa l'evento, province d'interesse? valutare bene se può essere fastidioso... noi possiamo far pagare 50cent in più per ogni provincia in cui viene pubblicato


DB:
 - aggiungi a preferiti sia listview sia evento singolo, devono aggiornare il valore anche nel db

         I nostri punti di forza:
         - non serve essere online per accedere agli eventi, viene mantenuto un database in locale
         - Poter ricercare eventi anche di più zone
         - e tanto, tanto, tanto altro ancora!
         per il web db: ***
             - numeri di cellulare con (+39) e senza spazi in mezzo trim(), sono ammessi solo numeri e "+"
             V i numeri non devono essere null!!! mettere valore di default!
         */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventi);



        PreferenceManager.setDefaultValues(this, R.xml.mypreference, false);
        myPreference = PreferenceManager.getDefaultSharedPreferences(this);
        /*
        if(myPreference.getString("update_just_with_wifi",null).isEmpty()) {
            PreferenceManager.setDefaultValues(this, R.xml.mypreference, false); // senza di questo i valori non sono inizializzati e l'app va in crash
            Log.i("EventiActivity", "settings sharedpreference non era configurato");
        }
        */
        //myPreference.edit().remove("province_interesse").commit();
        //myPreference.edit().putStringSet("province_interesse", );
        //myPreference.edit().clear().commit();
        //myPreference.edit().apply();


        /*
        myPreference.getBoolean("update_just_with_wifi", true);     messo
        myPreference.getBoolean("update_onstartup", true);          messo
        myPreference.getBoolean("use_db", true);                    messo
        myPreference.getBoolean("wipe_expired_onstartup", true);    messo
        myPreference.getBoolean("wipe_expired", true);              messo
        myPreference.getBoolean("wipe_db", true);                   messo
        ha senso mettere anche uno svuota locandine senza cancellare gli eventi?***
        myPreference.getBoolean("province_interesse", true);        messo
        myPreference.getBoolean("si_categorie", true);
        myPreference.getBoolean("no_categorie", true);
        myPreference.getBoolean("search_attributi", true);
        myPreference.getInt("search_m_raggio",0);
        scarica locandine? (sono scaricabili nel singolo evento)
        myPreference.getBoolean("download_pictures", false);        messo in EventoActivity. Da mettere anche in EventiActivity?***
        myPreference.getBoolean("show_ads", false);                 messo


         - Se l'evento non ha locandine?
          - Salvataggio locandine: default su SD o nella memoria interna
        */


        updateEventButton();


        if (myPreference.getBoolean("wipe_expired_onstartup", false)) { // elimina eventi scaduti on startup
            wipeExpiredFromDB();
        }

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Eventi e = new Eventi();
        if (myPreference.getBoolean("update_just_with_wifi", false) && !mWifi.isConnected()) { // se vuoi aggiornare solo con wifi e se la connessione non è WiFi
            // l'unico caso in cui non aggiorna è solo se update_just_with_wifi==true e mWifi.isConnected()==false, altrimenti in tutti gli altri casi deve aggiornare.
            // questo statement non fa nulla.
            Toast.makeText(this, getString(R.string.eventiactivity_noWIFI), Toast.LENGTH_LONG).show();
            stateList = e.getEventiFromDB(this);
            displayListViewEvent(stateList);
        } else {
            if (myPreference.getBoolean("update_onstartup", true)) { // se settings aggiorna on startup allora...
                Toast.makeText(this, getString(R.string.eventiactivity_aggiornoDB), Toast.LENGTH_LONG).show();
                // altrimenti getByJSON
                updateEvent();
            } else { // se non aggiorna onstartup, mostra ciò che ha: può essere vuoto il db o popolato
                Toast.makeText(this, getString(R.string.eventiactivity_readDB), Toast.LENGTH_LONG).show();
                stateList = e.getEventiFromDB(this);
                displayListViewEvent(stateList);
            }
        }

        show_ads();
    }

    protected void onResume() {
        super.onResume();
        displayListViewEvent(stateList); //per il momento non serve molto, avrebbe dovuto gestire il preferito selezionato all'interno dell'evento, ma quello lo prende da db.
        // onResume potrebbe prendere se l'evento è preferito dall'altra finestra e aggiornare il database. no vabbè stesso quella pagina aggiorna il db
    }

    protected void show_ads() { // Area comunicazioni e pubblicità

        Set<String> selezione_province = myPreference.getStringSet("province_interesse", null);
        String[] province_selezionate = selezione_province.toArray(new String[]{});
        String ads_text = getString(R.string.eventiactivity_adstext);
        for (String s : province_selezionate) ads_text += s + " | ";

        TextView ads = (TextView) findViewById(R.id.ads);

        HashMap<String, Object> arr_ads_disponibili = new HashMap<String, Object>();
        arr_ads_disponibili.put("AV", null);


        if (myPreference.getBoolean("show_ads", true)) { //  arr_prov_selezionate e arr_ads_disponibili se flag pubblicità attivo: se uno dei due è nullo mostra, se esiste, pubblicità nazionale
            if (province_selezionate.length < 0 || arr_ads_disponibili.isEmpty())
                ads.setText("Spot nazionale");
            //else
            //  arr_ads_disponibili.get()

            ads.setText(ads_text);
            // *** potrebbe anche ritornare proprio la View
        } else {
            // Se la pubblicità non deve apparire, nasconde la tabella della pubblicità e reimposta la dimensione della listview

            TableLayout ads_table = (TableLayout) findViewById(R.id.ads_table);
            ads_table.setVisibility(View.INVISIBLE);

            ListView listView_eventi = (ListView) findViewById(R.id.listView_eventi);
            LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            listView_eventi.setLayoutParams(mParam);

        }

    }

    public boolean isNull(String s) {
        s = s.trim();
        if (s.equalsIgnoreCase("null") || s.equals("") || s.isEmpty()) return true;
        return false;
    }


    private boolean wipeExpiredFromDB() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.eventiactivity_delexpired_title))
                .setMessage(getString(R.string.eventiactivity_delexpired_descr))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DBLayer db = new DBLayer(EventiActivity.this);
                        db.open();
                        db.WipeExpiredEvents();
                        //db.WipeTable("eventi");
                        db.close();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

        return true;
    }


    private void updateEvent() {
        Eventi e = new Eventi();
        if (isInternetAvailable()) {// se non c'è connessione a internet, deve dirlo
            stateList.clear();
            if (myPreference.getBoolean("use_db", true)) {

                CountEventi ce = new CountEventi();
                //ce.setByTest();
                ce.setByJSON();
                //Toast.makeText(this, ce.toString(), Toast.LENGTH_LONG).show();
                ce.storeInDB(getBaseContext());

                updateEventDB();                    // aggiorno il db con i dati JSON
                stateList = e.getEventiFromDB(this);      // leggo i dati dal db
                Toast.makeText(this, getString(R.string.eventiactivity_updatevent_warningupdDB), Toast.LENGTH_LONG).show();
            } else {
                stateList = e.getEventiFromJSON(this, getJSONUrl());
                Toast.makeText(this, getString(R.string.eventiactivity_updatevent_consultazioneonline), Toast.LENGTH_LONG).show();
            }
            displayListViewEvent(stateList);
        } else {
            Toast.makeText(this, getString(R.string.eventiactivity_updatevent_nointernet), Toast.LENGTH_LONG).show();
        }


        /* *** download elenco immagini
        ImageView img =
        for(Evento e:stateList){
        holder.img.setImageBitmap(

                BitmapFactory.decodeFile(
                        PicDownloader.getImage(state.getImmagine(), state.getId()).toString()
                )
        );
        */
    }

    private void updateEventDB() {
        Eventi e1 = new Eventi();
        ArrayList<Evento> eventi =  /*getEventiFromTest();*/e1.getEventiFromJSON(this, getJSONUrl());

        String strQuery = "";
        DBLayer db = null;
        try {
            db = new DBLayer(this);
            db.open();
            if (!eventi.isEmpty()) {
                strQuery = "INSERT OR REPLACE INTO eventi ( " + db_evento_attributi + " ) VALUES ";
                for (Evento e : eventi) {
                    Log.i("updateEventDB:", e.toString());
                    if (eventi.indexOf(e) != 0) strQuery += ",";
                    strQuery += "("
                            + "'" + e.getId().replace("'", "''") + "', "
                            + "'" + e.getTitolo().replace("'", "''") + "', "
                            + "'" + e.getDescr().replace("'", "''") + "', "
                            + "'" + e.getData_i().replace("'", "''") + "', "
                            + "'" + e.getData_f().replace("'", "''") + "', "
                            + "'" + e.getZona().replace("'", "''") + "', "
                            + "'" + e.getLatitudine() + "', "
                            + "'" + e.getLongitudine().replace("'", "''") + "', "
                            + "'" + e.getStruttura().replace("'", "''") + "', "
                            + "'" + e.getVia().replace("'", "''") + "', "
                            + "'" + e.getImmagine().replace("'", "''") + "', "
                            + "'" + e.getGratuito() + "', "
                            + "'" + e.getCosto() + "', "
                            + "'" + e.getSu_prenotazione() + "', "
                            + "'" + e.getTipo_prenotazione() + "', "
                            + "'" + e.getDa_eta() + "', "
                            + "'" + e.getA_eta() + "', "
                            + "'" + e.getTipo_eta() + "', "
                            + "'" + e.getExternal_urls().replace("'", "''") + "', "
                            + "'" + e.getYoutube_url().replace("'", "''") + "', "
                            + "'" + e.getContatti().replace("'", "''") + "', "
                            + "'" + e.getHashtag().replace("'", "''") + "', "
                            + "'" + e.getDatainserimento().replace("'", "''") + "', "
                            + "'" + e.getUsername().replace("'", "''") + "', "
                            //+ e.getStato()     + ", '"
                            + "'" + e.getSpeciale() + "', "
                            + "'" + (e.isSelected() ? 1 : 0) + "', "
                            + "'" + (e.getCategorie().toString().replace("'", "''")) // ' -> ''
                            + "')";
                }
                strQuery += ";";
            } else {
                Log.i("updateEventDB:", "eventi vuoto");
            }
            Log.i("DB_Insert", strQuery);

            db.Execute(strQuery, DBLayer.TipoQuery.Comando);
            db.close();
        } catch (Exception e) {
            Log.i("DB_Insert", e.toString());
        }
    }


    private void displayListViewEvent(ArrayList<Evento> listaEventi) {
        //stateList = getEventiFromDB();
        //stateList.clear();
        stateList = listaEventi;

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.elenco_eventi, stateList);
        final ListView listView = (ListView) findViewById(R.id.listView_eventi);

        listView.setAdapter(dataAdapter);   // Assign adapter to ListView

        inputSearch = (EditText) findViewById(R.id.inputSearchEvent);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                nuovaLista = new ArrayList<Evento>();
                nuovaLista.addAll(stateList);//for(Evento s:stateList) nuovaLista.add(s);
                for (Evento s : stateList) {
                    if (
                            !(
                                    s.getTitolo().toUpperCase().matches("(.*)" + cs.toString().toUpperCase() + "(.*)")
                                    // || s.getId().toUpperCase().matches("(.*)" + cs.toString().toUpperCase() + "(.*)")
                            )
                            ) {
                        nuovaLista.remove(s);
                    }
                }
                dataAdapter = new MyCustomAdapter(EventiActivity.this, R.layout.elenco_eventi, nuovaLista);
                listView.setAdapter(dataAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                Evento state = (Evento) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Apro evento: " + state.getTitolo(), Toast.LENGTH_LONG).show();

                Intent eventIntent = new Intent(EventiActivity.this, EventoActivity.class);
                eventIntent.putExtra("preferito", state.isSelected());
                eventIntent.putExtra("id", state.getId());
                eventIntent.putExtra("titolo", state.getTitolo());
                eventIntent.putExtra("descr", state.getDescr());
                eventIntent.putExtra("data_i", state.getData_i());
                eventIntent.putExtra("data_f", state.getData_f());
                eventIntent.putExtra("zona", state.getZona());
                eventIntent.putExtra("latitudine", state.getLatitudine());
                eventIntent.putExtra("longitudine", state.getLongitudine());
                eventIntent.putExtra("struttura", state.getStruttura());
                eventIntent.putExtra("via", state.getVia());
                eventIntent.putExtra("immagine", state.getImmagine());
                eventIntent.putExtra("gratuito", state.getGratuito());
                eventIntent.putExtra("costo", state.getCosto());
                eventIntent.putExtra("su_prenotazione", state.getSu_prenotazione());
                eventIntent.putExtra("tipo_prenotazione", state.getTipo_prenotazione());
                eventIntent.putExtra("da_eta", state.getDa_eta());
                eventIntent.putExtra("a_eta", state.getA_eta());
                eventIntent.putExtra("tipo_eta", state.getTipo_eta());
                eventIntent.putExtra("external_urls", state.getExternal_urls());
                eventIntent.putExtra("youtube_url", state.getYoutube_url());
                eventIntent.putExtra("contatti", state.getContatti());
                eventIntent.putExtra("hashtag", state.getHashtag());
                eventIntent.putExtra("datainserimento", state.getDatainserimento());
                eventIntent.putExtra("username", state.getUsername());
                eventIntent.putExtra("stato", state.getStato());
                eventIntent.putExtra("speciale", state.getSpeciale());
                eventIntent.putExtra("categorie", state.getCategorie().toString());

                /*
                Scelta implementativa:
                 decido di non passare un oggetto tra due intent, strutturato mediante la Parcelable, bensì di passare direttamente i dati che servono
                 Questo perché comunque un'eventuale modifica degli attributi dell'oggetto, richiederebbe la singola modifica del writeToParcel() a questo punto li invio direttamente.
                 http://www.simplesoft.it/android/android-e-l-interfaccia-parcelable.html

                Bundle bundle = new Bundle();
                bundle.putParcelable("evento", (Parcelable) state);
                eventIntent.putExtras(bundle);

                */
                EventiActivity.this.startActivity(eventIntent);
            }
        });

              /*
              stavo qua, era una ImageView
        ImageButton iv = (ImageButton) findViewById(R.id.evento_immagine);


        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!PicDownloader.fileExists(current_event.getId() + ".png")) {
                    //downloadImage();
                } else {
                    //openInGallery();

                }

            }
        });
*/

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                //onItemListViewLongClick();
                final Evento e = (Evento) parent.getAdapter().getItem(position);
                deleteItemInListView(e);
                //deleteItemInDB(e);
                return true;
            }
        });


        /*
        iv.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                registerForContextMenu(v);
                return false;
            }
        });
        */
    }

    private void deleteItemInListView(final Evento e) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(EventiActivity.this);
        deleteDialog.setTitle("Delete " + e.getTitolo() + "?");
        deleteDialog.setMessage(getString(R.string.eventiactivity_listview_deletedesc));
        deleteDialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stateList.remove(e);
                deleteItemInDB(e.getId());
                //orderListView()***
                displayListViewEvent(stateList);
            }
        });

        deleteDialog.setNegativeButton("No", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    /*
    private void onItemListViewLongClick() {
        Toast.makeText(getBaseContext(), "***Longpressed long click", Toast.LENGTH_LONG).show();
    }
    */

    private void deleteItemInDB(String count) {
        DBLayer db = new DBLayer(EventiActivity.this);
        db.open();
        db.removeEvent(count);
        db.close();
    }

    public String getJSONUrl() {
        String mirror1 = "http://www.chesifaoggi.it/stadio7.3/JSON/new_json.php"; // *** aggiungere anche mirror2 FNS
        String mirror2 = "http://chesifaoggi.freenamesystem.net/stadio7.3/JSON/new_json.php";
        String JSONUrl = "http://chesifaoggi.altervista.org/stadio7.3/JSON/new_json2.php";
        String auth = "1234";

        String param = "?auth=" + auth;
        String code = "&code=B";

        String messageToast = "";

        Set<String> selezione_province = myPreference.getStringSet("province_interesse", null);
        if (selezione_province.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.eventiactivity_noprov_title))
                    .setMessage(getString(R.string.eventiactivity_noprov_desc))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(getApplicationContext(), getString(R.string.eventiactivity_noprov_scelta), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(EventiActivity.this, SettingsActivity.class);
                            //myIntent.putExtra("key", value); //Optional parameters
                            EventiActivity.this.startActivity(myIntent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        } else {
            //if(.equals("Italy"))
            //if Italy+ = ALL
            String[] province_selezionate = selezione_province.toArray(new String[]{});
            String param_provinteresse = "&a=";
            for (String s : province_selezionate) {
                if(s.equals("Generici")) param_provinteresse += "generic_ita|";
                param_provinteresse += s.trim() + "|";
            }
            param += param_provinteresse;
            messageToast += getString(R.string.eventiactivity_provinteresse) + param_provinteresse;
        }

        String mypreference_downloadprice = myPreference.getString("mypreference_downloadprice", "null");
        if(
                !mypreference_downloadprice.equals("null") &&
                !mypreference_downloadprice.equals("no_gratis") &&
                !mypreference_downloadprice.equals("gratis")
                ) mypreference_downloadprice = "null";
        Log.i("mypreference_downloadprice", mypreference_downloadprice);

        if (
                !(mypreference_downloadprice.isEmpty()) &&
                !(mypreference_downloadprice.equals("null"))
           ) {
            if(mypreference_downloadprice.equals("gratis"))
                code += ".C";
            else
                code += "!C";
            messageToast += "\n\nHai scelto di vedere solo gli eventi "+mypreference_downloadprice;
        }


        String mypreference_periodointeresse = myPreference.getString("periodo_interesse", "null");
        Log.i("mypreference_periodointeresse", mypreference_periodointeresse);
        if (!mypreference_periodointeresse.isEmpty() && !mypreference_periodointeresse.equals("null")) {
            int num=0;
            switch(mypreference_periodointeresse){
                case "15gg":
                    code += "E";
                    num=15;
                    param += "&gg="+num;
                    break;
                case "this_month":
                    code += "E";
                    num=31;
                    param += "&gg="+num;
                    break;
                case "3month":
                    code += "E";
                    num=90;
                    param += "&gg="+num;
                    break;
                case "6month":
                    code += "E";
                    num=180;
                    param += "&gg="+num;
                    break;
                case "12month":
                    code += "E";
                    num=365;
                    param += "&gg="+num;
                    break;
                case "null":
                    // niente.
                    break;
            }

            if(num!=0) messageToast += "\ndei prossimi "+num+" giorni";
        }


        // inizio: età
        Set<String> selezione_eta = myPreference.getStringSet("eta_interesse", null);
        if (selezione_province.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.eventiactivity_noeta_title))
                    .setMessage(getString(R.string.eventiactivity_noeta_desc))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(getApplicationContext(), getString(R.string.eventiactivity_noeta_scelta), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(EventiActivity.this, SettingsActivity.class);
                            EventiActivity.this.startActivity(myIntent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        } else {
            String[] eta_selezionate = selezione_eta.toArray(new String[]{});
            String param_etainteresse = "&eta_array=";
            String subcode = "!H";
            for (String s : eta_selezionate) {
                if(s.equals("All")) { subcode = ""; param_etainteresse=""; break;}
                if(s.equals("Not specified values")) subcode = ".H"; // includo evento non specificati
                param_etainteresse += s.trim() + "|";
            }
            code += subcode;
            param += param_etainteresse;
            messageToast += getString(R.string.eventiactivity_etainteresse) + param_etainteresse;
        }
        // fine: età

        // inizio: escludi categorie
        Set<String> selezione_categorieescluse = myPreference.getStringSet("no_categorie", null);
        if (!selezione_categorieescluse.isEmpty()) {
            String[] categorie_selezionate = selezione_categorieescluse.toArray(new String[]{});
            String param_categorieescluse = "&categorie_exclude=";

            for (String s : categorie_selezionate) {
                param_categorieescluse += s.trim() + "|";
            }
            code += "!G"; // escludi categorie
            param += param_categorieescluse;
            messageToast += getString(R.string.eventiactivity_categorieescluse) + param_categorieescluse;
        }
        // fine: escludi categorie


        messageToast += "\n\nL'elenco potrebbe risultare limitato";
        Toast.makeText(getApplicationContext(), messageToast, Toast.LENGTH_LONG).show();
        Log.i("messageToast", messageToast);

            param += "";
            param += "";
            param += "";
            String json = null;

            try {
                JsonParser jp = new JsonParser();
                json = jp.getStringbyJSONUrl(JSONUrl, 5000);

                /*
                if (json == null) {
                    Log.e("getJSONUrl: ", "mirror1 is null");
                    JSONUrl = JSONUrl;
                } else
                    JSONUrl = mirror1;
                */

            } catch (Throwable t) {
                Log.e("JSON: ", "Could not parse malformed JSON: " + json + " - " + t.toString());
            }
            Log.i("getJSONUrl: i will use ", JSONUrl + "?" + code + param);
            return JSONUrl + "?" + code + param;
        }


    private class MyCustomAdapter extends ArrayAdapter<Evento> {
    private ArrayList<Evento> stateList;

    public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Evento> stateList) {
        super(context, textViewResourceId, stateList);
        this.stateList = new ArrayList<Evento>();
        this.stateList.addAll(stateList);
    }

    private class ViewHolder {
        CheckBox preferito;
        TextView titolo, data_i, data_f, zona, gratuito, prenotazione, eta;

        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); // *** Context.
            convertView = vi.inflate(R.layout.elenco_eventi, null);

            holder = new ViewHolder();
            holder.titolo = (TextView) convertView.findViewById(R.id.evento_titolo);
            holder.preferito = (CheckBox) convertView.findViewById(R.id.evento_preferito);
            holder.data_i = (TextView) convertView.findViewById(R.id.evento_data_i);
            holder.data_f = (TextView) convertView.findViewById(R.id.evento_data_f);
            holder.zona = (TextView) convertView.findViewById(R.id.evento_zona);
            holder.img = (ImageView) convertView.findViewById(R.id.evento_immagine);
            holder.eta = (TextView) convertView.findViewById(R.id.evento_eta);
            holder.gratuito = (TextView) convertView.findViewById(R.id.evento_gratuito);
            holder.prenotazione = (TextView) convertView.findViewById(R.id.evento_prenotazione);

            convertView.setTag(holder);

            holder.preferito.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Evento _state = (Evento) cb.getTag();

                    if (cb.isChecked()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.eventiactivity_addToPrefered), Toast.LENGTH_LONG).show();
                        cb.setSelected(true);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.eventiactivity_removeToPrefered), Toast.LENGTH_LONG).show();
                        cb.setSelected(false);
                    }

                    _state.setSelected(cb.isChecked());
                    String strQuery = "UPDATE eventi SET `preferito` = " + (_state.isSelected() ? 1 : 0) + " WHERE `count` = " + _state.getId();
                    try {
                        DBLayer db = new DBLayer(EventiActivity.this);
                        db.open();
                        db.Execute(strQuery, DBLayer.TipoQuery.Comando);
                        db.close();
                        Log.i("DB_Update_Preferito", strQuery);
                    } catch (Exception e) {
                        Log.i("DB_Update_Preferito", e.toString());
                    }
                }
            });

            holder.img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        File image = null;
                        Evento ev = (Evento) v.getTag();
                        try {
                            if (!PicDownloader.fileExists(ev.getId() + ".png")) {
                                downloadImage(ev);
                            }
                            image = PicDownloader.getFile(ev.getId()/*current_event.getId()*/ + ".png");

                            Uri uri =  Uri.fromFile(image);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                            String mime = "*/*";
                            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            if (mimeTypeMap.hasExtension(
                                    mimeTypeMap.getFileExtensionFromUrl(uri.toString())))
                                mime = mimeTypeMap.getMimeTypeFromExtension(
                                        mimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                            intent.setDataAndType(uri,mime);
                            startActivity(intent);
                        }catch(Exception e){
                            Log.e("openInGallery: ", e.toString());
                        }
                        //fine openInGallery();
                    //}
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        current_event = stateList.get(position); // <--- mettendo questo va in crash il download automatico!!
        Evento state = stateList.get(position);
        Log.i("controllodownloadautomatico", state.getId()+"");
        holder.titolo.setText("(" + state.getId() + ") " + Html.fromHtml(state.getTitolo()));
        holder.titolo.setBackgroundResource(R.drawable.rounded_corner);
        holder.preferito.setChecked(state.isSelected()); // isSelected = isPreferred
        holder.preferito.setTag(state);
        String[] ierioggidomani = {getString(R.string.eventoactivity_ieri), getString(R.string.eventoactivity_oggi), getString(R.string.eventoactivity_domani)};
        holder.data_i.setText(getString(R.string.eventiactivity_from) + " " + EventoActivity.parseDate(state.getData_i(),ierioggidomani) );
        holder.data_f.setText(getString(R.string.eventiactivity_to)  + " " + EventoActivity.parseDate(state.getData_f(), ierioggidomani) );
        String zona = null, eta = null;
        if(!state.getStruttura().isEmpty()) zona = state.getStruttura() + "\n" + state.getZona();  else zona = state.getZona();
        holder.zona.setText(zona);
        holder.img.setTag(state);   // mi serve sapere l'id dell'immagine per aprirla durante lo scorrimento

        if( state.getTipo_eta()!=-1 )  if(state.getTipo_eta() == 0)  eta = getString(R.string.suggerito); else eta = getString(R.string.obbligatorio); else eta = "";
        if(state.getDa_eta() !=-1 && state.getA_eta() !=-1) eta += " "+state.getDa_eta() + " - "+ state.getA_eta() +" "+ getString(R.string.anni);
        holder.eta.setText( eta );
        if( isNull(state.getImmagine()) ) { // Se non c'è l'immagine, mette il vuoto, altrimenti: se è stato settato il download automatico la scarica se non presente, altrimenti non la scarica
            Log.i("ImgInvisible:", "id:" + state.getId() + " - Img: " + state.getImmagine());
            holder.img.setImageResource(0);            //holder.img.setVisibility(View.INVISIBLE);
        } else {
            try {
                Log.i("download_pictures", myPreference.getBoolean("download_pictures", false) + "");
                holder.img.setImageResource(R.drawable.ic_menu_gallery);
                holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (!PicDownloader.fileExists(state.getId() + ".png")) {
                    if (myPreference.getBoolean("download_pictures", false) == true) {
                        downloadImage(state);
                        //Toast.makeText(getApplicationContext(), "Locandina scaricata", Toast.LENGTH_LONG).show();
                    }
                } else {

                    holder.img.setImageBitmap(
                            BitmapFactory.decodeFile(PicDownloader.getFile(state.getId() + ".png").toString())
                    );

                }
            } catch (Exception e) {
                Log.e("EventiActivity download_pictures", e.toString());
            }
        }
        if(state.getGratuito()==0){
            holder.gratuito.setText( getString(R.string.eventiactivity_nongratuito) + " " + state.getCosto() + " EUR");
            holder.gratuito.setTextColor(Color.RED);
        } else {
            holder.gratuito.setText( getString(R.string.eventiactivity_gratuito) );
            holder.gratuito.setTextColor(Color.GREEN);
        }

        if(state.getSu_prenotazione()==0){
            holder.prenotazione.setText( getString(R.string.eventiactivity_noprenotazione) );
            holder.prenotazione.setTextColor(Color.GREEN);
        } else {
            holder.prenotazione.setText( getString(R.string.eventiactivity_siprenotazione) );
            holder.prenotazione.setTextColor(Color.RED);
        }


        // test
/*
        holder.img.setImageBitmap(
                BitmapFactory.decodeFile(
                                new File("/storage/sdcard/it.chesifaoggi.app/Downloads/1254.png").toString()
                )
        );
        */
        // test

        return convertView;
    }
}

    private void updateEventButton(){
        Button myButton = (Button) findViewById(R.id.button_updateEvents);
        myButton.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            updateEvent();
                                        }
                                    }
        );

    }

    public boolean isInternetAvailable() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e("isInternetAvailable:", e.toString());
            return false;
        }
    }

    public void downloadImage(Evento evento){
        mProgressDialog = new ProgressDialog(EventiActivity.this);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(EventiActivity.this);
        Log.i("downloadImage con attr", "id: " + evento.getId());
        try{
            downloadTask.execute(evento); // execute this when the downloader must be fired
        } catch (Exception e){
            Log.e("downloadTask execute", e.toString());
        }

        Log.i("downloadImage", "post execute, current_Event id " + current_event.getId() + " titolo: " + current_event.getTitolo());

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    /*
    public void downloadImage(){ // originale, poi ho creato quello con attributo per quando clicco sull'immagine specifica
        // *** se non è un'immagine ma è un file? cosa accade?

        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(EventiActivity.this);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(EventiActivity.this);
        Log.i("downloadImage", "pre execute, current_Event id " + current_event.getId() + " titolo: "+current_event.getTitolo());
        try{
            downloadTask.execute(current_event); // execute this when the downloader must be fired
        } catch (Exception e){
            Log.e("downloadTask execute", e.toString());
        }

        Log.i("downloadImage", "post execute, current_Event id " + current_event.getId() + " titolo: " + current_event.getTitolo());

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }
    */

    private class DownloadTask extends AsyncTask<Evento, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;        // lascia che il dispositivo non vada in stand-by durante l'operazione. Modificato manifest.


        public DownloadTask(Context context) {
            this.context = context;
        }
        protected String doInBackground(Evento... e) { // altrimenti avrebbe adoperato le varargs String... urls
            int count = e.length;
            for (int i = 0; i < count; i++) {
                String url = e[i].getImmagine();
                String id =  e[i].getId();

                if(url != null) {
                    Log.i("DownloadTask: ", "url: " + e[i].getImmagine() + " - id: " + e[i].getId());
                    File f = PicDownloader.getImage(url, id + ".png"); //Downloader.downloadFile(urls[i]);
                } else Log.i("DownloadTask: ", "url null - id: " + e[i].getId());

                publishProgress(i);
                if (isCancelled()) break; // Escape early if cancel() is called
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            try {
                if (result != null)
                    Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                    ib_locandina.setImageBitmap(
                            BitmapFactory.decodeFile(PicDownloader.getFile(current_event.getId() + ".png").toString())
                    );
                }
            } catch(Exception e){
                Log.e("onPostExecute:", e.toString());
            }

        }
    }

    ProgressDialog mProgressDialog;
    ImageButton ib_locandina;
    Evento current_event;
}