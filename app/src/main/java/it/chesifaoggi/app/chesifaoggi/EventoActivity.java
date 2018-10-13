package it.chesifaoggi.app.chesifaoggi;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gaia on 08/03/16.
 */
public class EventoActivity extends Activity {
    SharedPreferences myPreference = null;

    public boolean isNull(String s){
        s= s.trim();
        if(s.equalsIgnoreCase("null") || s.equals("") || s.isEmpty())        return true;
        return false;
    }

    private ArrayList<String> StringToCategorie(String categorie){
        ArrayList<String> AL_categorie = new ArrayList<String>();
        if (categorie.equalsIgnoreCase("\"null\"") || categorie.isEmpty() || categorie.equals("")){
            AL_categorie.add( "-Nessuna categoria-" );
            return AL_categorie;
        }

        String[] ss = categorie.split(",");
        for(int i=0; i<ss.length; i++){
            AL_categorie.add( ss[i].substring(1,ss[i].length()-1) );
        }
        return AL_categorie;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        myPreference = PreferenceManager.getDefaultSharedPreferences(this);
// http://stackoverflow.com/questions/16809508/extract-code-country-from-phone-number-libphonenumber

        /*
        for (EventiActivity.Evento s : EventiActivity.stateList) {

        }
        ;
*/



        Intent intent = getIntent();
        final boolean preferito = intent.getBooleanExtra("preferito", false);
        final String id = intent.getStringExtra("id");
        String titolo = intent.getStringExtra("titolo");
        String descr                = intent.getStringExtra("descr");
        String data_i               = intent.getStringExtra("data_i");
        String data_f               = intent.getStringExtra("data_f");
        String zona                 = intent.getStringExtra("zona");
        String latitudine           = intent.getStringExtra("latitudine");
        String longitudine          = intent.getStringExtra("longitudine");
        String struttura            = intent.getStringExtra("struttura");
        String via                  = intent.getStringExtra("via");
        String immagine             = intent.getStringExtra("immagine");
        int gratuito                = intent.getIntExtra("gratuito", -1);
        double costo                = intent.getDoubleExtra("costo", -1);
        int su_prenotazione         = intent.getIntExtra("su_prenotazione", -1);
        int tipo_prenotazione       = intent.getIntExtra("tipo_prenotazione", -1);
        int da_eta                  = intent.getIntExtra("da_eta", -1);
        int a_eta                   = intent.getIntExtra("a_eta", -1);
        int tipo_eta                = intent.getIntExtra("tipo_eta", -1);
        String external_urls        = intent.getStringExtra("external_urls");
        String youtube_url          = intent.getStringExtra("youtube_url");
        String contatti             = intent.getStringExtra("contatti");
        String hashtag              = intent.getStringExtra("hashtag");
        String datainserimento      = intent.getStringExtra("datainserimento");
        String username             = intent.getStringExtra("username");
        String stato                = intent.getStringExtra("stato");
        String speciale             = intent.getStringExtra("speciale");
        String categorie            = intent.getStringExtra("categorie");

        // Creo l'oggetto, è più comodo da gestire nella chiamata all'AsyncTask in cui servono url e id di vari Evento.
        ArrayList<String> arr_categorie = StringToCategorie(categorie);
        //immagine = "http://www.chesifaoggi.it/ud/e-insert/1412378121.jpg";
        current_event = new Evento(preferito, id, titolo, descr, data_i, data_f, zona, latitudine, longitudine, struttura, via, immagine, gratuito, costo, su_prenotazione, tipo_prenotazione, da_eta, a_eta, tipo_eta, external_urls, youtube_url, contatti, hashtag, datainserimento, username, stato, speciale,arr_categorie);
        current_event.setImmagine(/*"http://www.chesifaoggi.it"+*/immagine);
        //current_event.setImmagine("http://www.chesifaoggi.it./ftp_temp/e-insert/1462309182.jpg");

        shareButton(id, titolo);
        addCalendarButton(titolo, Html.fromHtml(descr).toString(), longDate(data_i), longDate(data_f), via+" - "+struttura+" - "+zona);

        //GridLayout griglia      = (GridLayout)  findViewById( R.id.evento_griglia);
        TableLayout tablelayout = (TableLayout) findViewById(R.id.evento_tablelayout);
        TableRow row= new TableRow(this);
        TableRow.LayoutParams rowparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        rowparams.setMargins(20, 20, 20, 20);

        final CheckBox tb_preferito   = (CheckBox)    findViewById( R.id.evento_preferito );  tb_preferito.setChecked(preferito);
        tb_preferito.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String strQuery = "UPDATE eventi SET `preferito` = " + (tb_preferito.isChecked() ? 1 : 0) + " WHERE `count` = " + id;
                                                try {
                                                    DBLayer db = new DBLayer(EventoActivity.this);
                                                    db.open();
                                                    db.Execute(strQuery, DBLayer.TipoQuery.Comando);
                                                    db.close();
                                                    Log.i("DB_Update_Preferito", strQuery);
                                                } catch (Exception e) {
                                                    Log.i("DB_Update_Preferito", e.toString());
                                                }
                                            }
                                        }
        );
        //TextView tv_id          = (TextView)    findViewById( R.id.evento_id );         tv_id.setText(id);

        TextView tv_titolo      = (TextView)    findViewById( R.id.evento_titolo );     tv_titolo.setText(Html.fromHtml(titolo));
        LinearLayout ll = (LinearLayout) findViewById(R.id.imagebox);
        if(!isNull(immagine)) { // se locandina == null il bottone non deve proprio apparire
            Log.i("immagine: ", immagine);
            ib_locandina = new ImageButton(this);
            try {
                Log.i("download_pictures", myPreference.getBoolean("download_pictures", false) + "");
                ib_locandina.setImageResource(R.drawable.ic_menu_gallery);
                if (!PicDownloader.fileExists(id + ".png")) {
                    if (myPreference.getBoolean("download_pictures", false) == true) {
                        downloadImage();
                        Toast.makeText(getApplicationContext(), "Locandina scaricata", Toast.LENGTH_LONG).show();
                    }
                } else {
                    ib_locandina.setImageBitmap(
                            BitmapFactory.decodeFile(PicDownloader.getFile(id + ".png").toString())
                    );
                }

            } catch (Exception e) {
                Log.e("EventoActivity.java", e.toString());
            }

            ll.addView(ib_locandina);

            ib_locandina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!PicDownloader.fileExists(id + ".png")) {
                        downloadImage();
                    } else {
                        openInGallery();
                    }
                }
            });

            ib_locandina.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    registerForContextMenu(v);
                    return false;
                }
            });

        }
        TextView tv_descrizione = (TextView)    findViewById( R.id.evento_descrizione );tv_descrizione.setText(Html.fromHtml(descr));

        String[] ierioggidomani = {getString(R.string.eventoactivity_ieri), getString(R.string.eventoactivity_oggi), getString(R.string.eventoactivity_domani)};
        TextView tv_data_i      = (TextView)    findViewById( R.id.evento_data_i );     tv_data_i.setText( "Data inizio:\n" + parseDate(data_i,ierioggidomani) ); tv_data_i.setPadding(6, 2, 6, 2); tv_data_i.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        TextView tv_data_f      = (TextView)    findViewById( R.id.evento_data_f );     tv_data_f.setText("Data fine:\n" + parseDate(data_f, ierioggidomani) );   tv_data_f.setPadding(6, 2, 6, 2); tv_data_f.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        TextView tv_zona        = (TextView)    findViewById( R.id.evento_zona );       tv_zona.setText(zona);
        tv_zona.setMovementMethod(LinkMovementMethod.getInstance());



        if( !isNull(latitudine) ){
            row= new TableRow(this);
            //TextView tv_latitudine1 = new TextView(this); tv_latitudine1.setText("latitudine");  griglia.addView(tv_latitudine1);
            TextView tv_latitudine2 = new TextView(this);
            tv_latitudine2.setText("latitudine: " + latitudine); tv_latitudine2.setBackgroundResource(R.drawable.rounded_corner);
            //griglia.addView(tv_latitudine2);
            row.addView(tv_latitudine2);
            //tablelayout.addView(row);
        }
        if( !isNull(longitudine) ){
            //row= new TableRow(this);
            //TextView tv_longitudine1 = new TextView(this); tv_longitudine1.setText("longitudine");  griglia.addView(tv_longitudine1);
            TextView tv_longitudine2 = new TextView(this); tv_longitudine2.setText("longitudine: " + longitudine); tv_longitudine2.setBackgroundResource(R.drawable.rounded_corner);
            row.addView(tv_longitudine2);
            tablelayout.addView(row);
            TextView tv = new TextView(this);
            tv.setText(a_eta + "");
        }
        if( !isNull(struttura) ) {
            TextView tv = new TextView(this);
            tv.setText("Dove: "+struttura);
            makeSpecialTextView(tablelayout, tv, rowparams);
        }

        if( !isNull(via) ) {
            TextView tv = new TextView(this);
            tv.setText("Via: " + via);
            makeSpecialTextView(tablelayout, tv, rowparams);
        }

        /*
        if( !isNull(immagine) ){
            TextView tv_immagine1 = new TextView(this); tv_immagine1.setText("immagine");  griglia.addView(tv_immagine1);
            TextView tv_immagine2 = new TextView(this); tv_immagine2.setText( immagine );  griglia.addView( tv_immagine2 );
        }*/

        if( gratuito>-1 ){
            String risposta = null;
            switch (gratuito){
                case 1:
                    risposta="gratuito";
                    break;
                case 0:
                    risposta="non gratuito";
                    break;
            }
            TextView tv = new TextView(this);
            tv.setText(risposta + ((costo>0)?" €"+costo:""));
            makeSpecialTextView(tablelayout, tv, rowparams);
        }

        if( su_prenotazione != -1 && tipo_prenotazione != -1){//if(!(su_prenotazione+"").isEmpty()){
            String risposta = null;
            switch (tipo_prenotazione){
                case 0:
                    risposta="per alcune attività";
                    break;
                case 1:
                    risposta="per tutte le attività";
                    break;
                case 2:
                    risposta="per solo ingresso";
                    break;
                case 3:
                    risposta="consigliata";
                    break;
                case 4:
                    risposta="obbligatoria";
                    break;
            }
            TextView tv = new TextView(this);
            tv.setText("Prenotazione: "+risposta);
            makeSpecialTextView(tablelayout, tv, rowparams);
        }


        if( da_eta!=-1 && a_eta!=-1 ) {
            //if(!(da_eta+"").isEmpty() || a_eta!=0 )// {
            TextView tv = new TextView(this);
            String risposta = null;
            if( tipo_eta!=-1 ) {//if(!(tipo_eta+"").isEmpty() || tipo_eta!=0 ){
                switch (tipo_eta) {
                    case 0:
                        risposta = getString(R.string.suggerito);
                        break;
                    case 1:
                        risposta = getString(R.string.obbligatorio);
                        break;
                }
            }
            tv.setText(risposta + " da età: "+da_eta+" a età: "+a_eta);
            makeSpecialTextView(tablelayout, tv, rowparams);
        }


        if( !isNull(external_urls) ){
            TextView tv = new TextView(this);
            tv.setText(Html.fromHtml(external_urls));
            Linkify.addLinks(tv, Linkify.ALL);
            tv.setLinksClickable(true);
            makeSpecialTextView(tablelayout, tv, rowparams);
        }

        if( !isNull(youtube_url) ){
            TextView tv = new TextView(this);
            tv.setText(Html.fromHtml(youtube_url));
            makeSpecialTextView(tablelayout, tv, rowparams);
        }


        if( !isNull(contatti) ){
            TextView tv_contatti2 = new TextView(this);
            tv_contatti2.setText(Html.fromHtml(contatti));
            Linkify.addLinks(tv_contatti2, Linkify.ALL);
            tv_contatti2.setLinksClickable(true);
            makeSpecialTextView(tablelayout, tv_contatti2, rowparams);
        }

        if( !isNull(hashtag) ) {
            TextView tv_hashtag = new TextView(this);
            tv_hashtag.setText(hashtag);
            makeSpecialTextView(tablelayout, tv_hashtag, rowparams);
        }
        if( !isNull(datainserimento) ){
            TextView tv = new TextView(this);
            tv.setText("data inserimento: " + parseDate(datainserimento, ierioggidomani));
            makeSpecialTextView(tablelayout, tv, rowparams);
        }
        if( !isNull(username) ){
            TextView tv = new TextView(this);
            tv.setText("username: " + username);
            makeSpecialTextView(tablelayout, tv, rowparams);
        }
        /*
        if( !isNull(stato) ){
            TextView tv_stato1 = new TextView(this); tv_stato1.setText("stato:");  griglia.addView(tv_stato1);
            TextView tv_stato2 = new TextView(this); tv_stato2.setText( stato );  griglia.addView( tv_stato2 );
        }

        if( !isNull(speciale) ){
            TextView tv_speciale1 = new TextView(this); tv_speciale1.setText("speciale:");  griglia.addView(tv_speciale1);
            TextView tv_speciale2 = new TextView(this); tv_speciale2.setText( speciale );  griglia.addView( tv_speciale2 );
        }
*/

        TextView tv = new TextView(this);
        tv.setText("Categorie: \n"+categorie);
        makeSpecialTextView(tablelayout, tv, rowparams);


        // rialza l'evento per non tenerlo troppo sotto
        /*
        griglia.addView(new TextView(this));   griglia.addView(new TextView(this));
        griglia.addView(new TextView(this));   griglia.addView(new TextView(this));
        griglia.addView(new TextView(this));   griglia.addView(new TextView(this));
        */
        //FileManager fm = new FileManager();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Locandina");
        menu.add(0, v.getId(), 0, "Apri");
        menu.add(0, v.getId(), 0, "Scarica");
        menu.add(0, v.getId(), 0, "Elimina");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Apri") {
            openInGallery();
        } else if (item.getTitle() == "Scarica") {
            downloadImage();
        } else if (item.getTitle() == "Elimina") {
            Toast.makeText(this, "***Elimina NON FUNZIONA ANCORA. Qualcuno verrà punito per questo!", Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
        return true;
    }


    public void makeSpecialTextView(TableLayout tablelayout, TextView tv, TableRow.LayoutParams rowparams){
        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        tv.setBackgroundResource(R.drawable.rounded_corner);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(2,2,2,2);
        tv.setLayoutParams(rowparams);
        tv.setBackgroundResource(R.drawable.rounded_corner);
        row.addView(tv);
        tablelayout.addView(row);
    }

    public static long longDate(String stringdata){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date;
        long longDate = 0;
        try {
            date = sdf.parse(stringdata);
            longDate = date.getTime();
        } catch (ParseException e) {
            Log.e("Dataparsing: ", e.toString());
        }
        return longDate;
    }

    public static String parseDate(String d, String[] ierioggidomani){
        /*
         * - Gestisce il parsing della data, mostrandola nel formato a seconda della nazionalità dell'utente
         * - gestisce ieri, oggi, domani.
         */
        DateFormat targetFormat = new SimpleDateFormat("E dd MMM yyyy HH:mm", Locale.getDefault());
        DateFormat timeFormatString = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date;
        long longDate;
        String formattedDate = null;

        longDate = longDate(d);
            formattedDate = targetFormat.format(longDate);

            if( DateUtils.isToday(longDate) ) return ierioggidomani[1] + " " + timeFormatString.format(longDate);
            else{
                // isYesterday? public static boolean isYesterday(long longDate) {
                /* http://stackoverflow.com/questions/12818711/how-to-find-time-is-today-or-yesterday-in-android
                    return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                    && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                    && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
                 */

                Calendar now = Calendar.getInstance();
                Calendar cdate = Calendar.getInstance();
                cdate.setTimeInMillis(longDate);
                now.add(Calendar.DATE,-1);

                if(now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                        && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                        && now.get(Calendar.DATE) == cdate.get(Calendar.DATE)) return ierioggidomani[0] + " " + timeFormatString.format(longDate);

                // isTomorrow()
                now.add(Calendar.DATE, +2);
                if(now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                        && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                        && now.get(Calendar.DATE) == cdate.get(Calendar.DATE)) return ierioggidomani[2] + " " + timeFormatString.format(longDate);


                return formattedDate;
            }

    }

    //public void openInGallery(String imageId) {
    public void openInGallery() { // *** NON FUNZIONA
        //Toast.makeText(this,"NON FUNZIONA ***", Toast.LENGTH_LONG).show();
        File image = null;
        try {
            image = PicDownloader.getFile(current_event.getId() + ".png");

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
    }

    public void downloadImage(){
        // *** se non è un'immagine ma è un file? cosa accade?

        File f=null;
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(EventoActivity.this);
        mProgressDialog.setMessage("Download in corso");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(EventoActivity.this);
        downloadTask.execute(current_event); // execute this when the downloader must be fired

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    public void shareButton(final String id, final String titolo){
        ImageButton ib_sharebutton   = (ImageButton)    findViewById( R.id.shareeventbutton );
        ib_sharebutton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent sendIntent = new Intent();
                                                  sendIntent.setAction(Intent.ACTION_SEND);
                                                  sendIntent.putExtra(Intent.EXTRA_TEXT,
                                                          "Segui quest'evento: " + titolo +
                                                                  "\n\n" +
                                                                  "http://chesifaoggi.altervista.org/stadio7.3/?event=" + id +
                                                                  "\na cura dell'applicazione: " + getString(R.string.app_name)
                                                  );
                                                  sendIntent.setType("text/plain");
                                                  startActivity(sendIntent);
                                              }
                                          }
        );
    }

    public void addCalendarButton(final String titolo, final String descrizione, final long eventstartInMillis, final long eventEndInMillis, final String posizione){
        ImageButton ib_addCalendar   = (ImageButton)    findViewById(R.id.addcalendarbutton  );
        ib_addCalendar.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Toast.makeText(EventoActivity.this, "Aggiungo al calendario personale", Toast.LENGTH_SHORT).show();

                                                  Intent intent = new Intent(Intent.ACTION_EDIT);
                                                  intent.setType("vnd.android.cursor.item/event");
                                                  intent.putExtra(CalendarContract.Events.TITLE, getString(R.string.app_name) + ": \"" + titolo + "\"");
                                                  intent.putExtra(CalendarContract.Events.DESCRIPTION, descrizione);
                                                  intent.putExtra(CalendarContract.Events.EVENT_LOCATION, posizione);
                                                  intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventstartInMillis );
                                                  intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndInMillis);
                                                  intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                                                  //intent.putExtra("eventTimezone", TimeZone.getTimeZone("GMT"));
                                                  startActivity(intent);
                                              }
                                          }
        );
    }

    ProgressDialog mProgressDialog;

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

                File f = PicDownloader.getImage(url, id + ".png"); //Downloader.downloadFile(urls[i]);

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
            if (result != null)
                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                ib_locandina.setImageBitmap(
                        BitmapFactory.decodeFile(PicDownloader.getFile(current_event.getId() + ".png").toString())
                );
            }

        }
    }
    ImageButton ib_locandina;
    Evento current_event;
}