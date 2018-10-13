package it.chesifaoggi.app.chesifaoggi;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by gaia on 03/06/16.
 */

public class Eventi {
    public Eventi() {
        eventi = new ArrayList<Evento>();
    }

    public Eventi(ArrayList<Evento> e) {
        this.eventi = e;
    }

    public Evento getEventoById(String id) {
        for (Evento e : eventi)
            if (id.equalsIgnoreCase(e.getId())) return e;
        return null;
    }

    public ArrayList<Evento> getEventiFromTest() { // ***
        ArrayList<Evento> eventi = new ArrayList<Evento>();

        Evento _states = null;

        String nullo = "a";
        int zero = 0;
        double zeros = 0;

        boolean selected;
        String id, titolo, descr, data_i, data_f, zona, latitudine, longitudine, struttura, via,
                immagine, external_urls, youtube_url, contatti, hashtag, datainserimento,
                username, stato, speciale;
        int gratuito, su_prenotazione, tipo_prenotazione, da_eta, a_eta, tipo_eta;
        double costo;

        selected = false;
        id = "1";        titolo = "Evento1";
        descr = "boh contattami allo +39082574925";
        data_i = "2016-03-01 18:30:00";        data_f = "2016-03-02 20:00:00";
        zona = "Avellino";
        latitudine = "";        longitudine = "";        struttura = "";        via = "";
        immagine = "";        gratuito = 0;        costo = 0;        su_prenotazione = 0;
        tipo_prenotazione = 0;        da_eta = 0;        a_eta = 0;        tipo_eta = 0;
        external_urls = "";
        youtube_url = "";
        contatti = "contattami allo +39082574925 oppure al 3774149554 oppure al +491231546";
        hashtag = "";
        datainserimento = "";
        username = "";
        stato = "";
        speciale = "";
        ArrayList<String> categorie = new ArrayList<String>();
        categorie.add("prova1");
        categorie.add("prova2");
        categorie.add("prova3");

        _states = new Evento(true, id, titolo, descr, data_i, data_f, zona, latitudine, longitudine, struttura, via, immagine, gratuito, costo, su_prenotazione, tipo_prenotazione, da_eta, a_eta, tipo_eta, external_urls, youtube_url, contatti, hashtag, datainserimento, username, stato, speciale,categorie);
        eventi.add(_states);

        _states = new Evento(false, "2", "Evento Benevento", nullo,
                nullo, nullo, nullo, nullo, nullo,
                nullo, nullo, nullo, zero, zeros,
                zero, zero, zero, zero,
                zero, nullo, nullo, nullo,
                nullo, nullo, nullo, nullo,
                nullo,categorie
        );
        eventi.add(_states);
        _states = new Evento(true, "3", "Festa", nullo,
                nullo, nullo, nullo, nullo, nullo,
                nullo, nullo, nullo, zero, zeros,
                zero, zero, zero, zero,
                zero, nullo, nullo, nullo,
                nullo, nullo, nullo, nullo,
                nullo,categorie
        );
        eventi.add(_states);
        _states = new Evento(false, "4", "Pareo", nullo,
                nullo, nullo, nullo, nullo, nullo,
                nullo, nullo, nullo, zero, zeros,
                zero, zero, zero, zero,
                zero, nullo, nullo, nullo,
                nullo, nullo, nullo, nullo,
                nullo,categorie
        );
        eventi.add(_states);
        _states = new Evento(false, "5", "spiaggia", nullo,
                nullo, nullo, nullo, nullo, nullo,
                nullo, nullo, nullo, zero, zeros,
                zero, zero, zero, zero,
                zero, nullo, nullo, nullo,
                nullo, nullo, nullo, nullo,
                nullo,categorie
        );
        eventi.add(_states);
        _states = new Evento(false, "6", "non lo so", nullo,
                nullo, nullo, nullo, nullo, nullo,
                nullo, nullo, nullo, zero, zeros,
                zero, zero, zero, zero,
                zero, nullo, nullo, nullo,
                nullo, nullo, nullo, nullo,
                nullo,categorie
        );
        eventi.add(_states);
        _states = new Evento(false, "7", "Pigiama party", nullo,
                nullo, nullo, nullo, nullo, nullo,
                nullo, nullo, nullo, zero, zeros,
                zero, zero, zero, zero,
                zero, nullo, nullo, nullo,
                nullo, nullo, nullo, nullo,
                nullo,categorie
        );
        eventi.add(_states);

        return eventi;
    }

    private int String2IntWithNull(String s) { // se stringa==null allora ritorna -1
        int retu = -1;
        if (s.equalsIgnoreCase("null") || s.equals("") || s.isEmpty()) {
            retu = -1;
        } else {
            try {
                retu = Integer.valueOf(s);

            } catch (Exception e) {
                Log.e("String2IntWithNull:", e.toString());
            }

        }
        return retu;
    }

    private double String2DoubleWithNull(String s){ // se stringa==null allora ritorna -1
        double retu = -1;
        if (s.equalsIgnoreCase("null") || s.equals("") || s.isEmpty()) {
            retu = -1;
        } else {
            try {
                retu = Double.valueOf(s);
            } catch (Exception e) {
                Log.e("String2DoubleWithNull:", e.toString());
            }

        }
        return retu;
    }

    private ArrayList<String> String2ALString(String ss){
        ss = ss.substring(1,ss.length()-1);
        ArrayList<String> a = new ArrayList<String>();
        String[] sss = ss.split(",");
        for(String s:sss){
            a.add(s);
        }
        return a;
    }

    private Evento parseJSONCaratteristiche(String caratteristiche, ArrayList<String> categorie){ // di un evento
        Evento e = null;
        try {
            JSONObject jo_caratteristiche = new JSONObject(caratteristiche);
            e = new Evento(
                    false, // preferito
                    jo_caratteristiche.getString("count"),
                    jo_caratteristiche.getString("titolo"),
                    jo_caratteristiche.getString("descrizione"),
                    jo_caratteristiche.getString("data_i"),
                     jo_caratteristiche.getString("data_f"),
                    jo_caratteristiche.getString("zona"),
                    jo_caratteristiche.getString("latitudine"),
                    jo_caratteristiche.getString("longitudine"),
                    jo_caratteristiche.getString("struttura"),
                    jo_caratteristiche.getString("via"),
                    jo_caratteristiche.getString("immagine"),
                    String2IntWithNull(jo_caratteristiche.getString("gratuito")),//int
                    String2DoubleWithNull(jo_caratteristiche.getString("costo")),//double
                    String2IntWithNull(jo_caratteristiche.getString("su_prenotazione")),//int
                    String2IntWithNull(jo_caratteristiche.getString("tipo_prenotazione")),//int.
                    String2IntWithNull(jo_caratteristiche.getString("da_eta")),//int
                    String2IntWithNull(jo_caratteristiche.getString("a_eta")),//int
                    String2IntWithNull(jo_caratteristiche.getString("tipo_eta")),//int
                    jo_caratteristiche.getString("external_urls"),
                    jo_caratteristiche.getString("youtube_url"),
                    jo_caratteristiche.getString("contatti"),
                    jo_caratteristiche.getString("hashtag"),
                    jo_caratteristiche.getString("datainserimento"),
                    jo_caratteristiche.getString("username"),
                    "", // *** attributo "stato" da eliminare!!!
                    jo_caratteristiche.getString("speciale"),
                    categorie
            );
        }catch(JSONException e1){
            Log.e("parseJSONCaratteristiche: ", e1.toString());
        }
        Log.i("caratteristicheX:", caratteristiche);
        return e;
    }


    private ArrayList<String> parseJSONCategorie(String categorie){
        ArrayList<String> AL_categorie = new ArrayList<String>();
        if (categorie.equalsIgnoreCase("\"null\"") || categorie.isEmpty() || categorie.equals("")){
            AL_categorie.add( "-Nessuna categoria-" );
            return AL_categorie;
        }

        /*
        try {
            JSONObject jo_categorie = new JSONObject(categorie);
            JSONArray ja_categorie = new JSONArray(jo_categorie);//jo_categorie.toJSONArray("categorie");
            for(int i=0; i<ja_categorie.length(); i++){
                AL_categorie.add(ja_categorie.getString(i));
            }
        } catch(JSONException e1){
            Log.e("parseJSONCategorie: ", e1.toString());
        }
        */
        String[] ss = categorie.split(",");

        for(int i=0; i<ss.length; i++){
            AL_categorie.add( ss[i].substring(1,ss[i].length()-1) );
        }

        Log.i("categorieX:", categorie) ;
        return AL_categorie;
    }

    private int auth(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);                  int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);           int auth = (year * 3) + day - month - 1;

        return auth;
    }

    private String getValidJSON(String json) {
        json = json.trim();
        Log.i("getValidJSON 1:", json);
        if(json.startsWith("[") && json.endsWith("]")) json = json.substring(1,json.length()-1);  // quando ci sono più valori, mette le [] esempio: [{},{}] e non lo interpreta bene.
        Log.i("getValidJSON 2:", json);
        return json;
    }



    public ArrayList<Evento> getEventiFromJSON(Context contextChiamante, String s_url) { // "è un JSONObjectFromString esteso"
        int auth = auth();
        String json = null;
        try {
            JsonParser jp = new JsonParser();
            json = jp.getStringbyJSONUrl(s_url, 5000);

            if (json == null) {
                Log.e("getEventiFromJSON: ", "json is null");
                if(contextChiamante != null) Toast.makeText(contextChiamante.getApplicationContext(), "Nessun evento scaricato", Toast.LENGTH_LONG).show();
                return eventi;
            }
            json = getValidJSON(json);
            JSONObject obj = new JSONObject(json);
            Log.d("JSON: ", obj.toString());
        } catch (Throwable t) {
            Log.e("JSON: ", "Could not parse malformed JSON: " + json + " - " + t.toString());
        }


        ArrayList<Evento> json_eventi = new ArrayList<Evento>();
        try {

            JSONObject jo = new JSONObject(json);
            JSONArray ja = jo.getJSONArray("results");

            for(int i=0; i<ja.length(); i++) {
                JSONObject oggetto_json = ja.getJSONObject(i);

                json_eventi.add(
                        parseJSONCaratteristiche(
                                getValidJSON(oggetto_json.getString("caratteristiche")),
                                parseJSONCategorie(getValidJSON(oggetto_json.getString("categorie")))
                        )
                );
            }
        }catch(JSONException e1){
            //e1.printStackTrace();
            Log.e("JSON: xx", e1.toString());
        }

        if (!(json_eventi.isEmpty())) return json_eventi;
        if(contextChiamante != null) Toast.makeText(contextChiamante.getApplicationContext(), "Nessun evento scaricato", Toast.LENGTH_LONG).show();
        return eventi;
    }



    public ArrayList<Evento> getEventiFromDB(Context chiamante){
        eventi = new ArrayList<Evento>();    //Array list of events
        boolean preferito;

        ArrayList<String> categorie = new ArrayList<String>();
        DBLayer db = null;
        try{
            db = new DBLayer(chiamante); // ho specificato il Context del chiamante, altrimenti avrei dovuto eseguire implements Activity ma non avrebbe mostrato nell'altra activity i risultati.
            db.open();

            Cursor c = null;
            String strQuery = "SELECT "+ db.getAttributes("eventi") +" FROM eventi ";
            SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(chiamante);

            if(myPreference.getBoolean("show_expired_events", false)==false ) strQuery += " WHERE date(data_i) >= date() OR date(data_f) >= date() ";
            strQuery += " order by data_i, data_f;";
            try {
                c = db.Execute(strQuery, DBLayer.TipoQuery.Selezione);
            }catch(Exception e){
                Log.e("DB_SelectCursor", e.toString());
            }
            Log.i("DB_Select getEventiFromDB: ", strQuery);
            //db.close();

            if (c != null){
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                    preferito = c.getInt(c.getColumnIndexOrThrow("preferito"))>0;
                    eventi.add(
                            new Evento(
                                    preferito,
                                    c.getString(c.getColumnIndexOrThrow("count")),
                                    c.getString(c.getColumnIndexOrThrow("titolo")),
                                    c.getString(c.getColumnIndexOrThrow("descrizione")),
                                    c.getString(c.getColumnIndexOrThrow("data_i")),
                                    c.getString(c.getColumnIndexOrThrow("data_f")),
                                    c.getString(c.getColumnIndexOrThrow("zona")),
                                    c.getString(c.getColumnIndexOrThrow("latitudine")),
                                    c.getString(c.getColumnIndexOrThrow("longitudine")),
                                    c.getString(c.getColumnIndexOrThrow("struttura")),
                                    c.getString(c.getColumnIndexOrThrow("via")),
                                    c.getString(c.getColumnIndexOrThrow("immagine")),
                                    String2IntWithNull(c.getString(c.getColumnIndexOrThrow("gratuito"))),
                                    String2DoubleWithNull(c.getString(c.getColumnIndexOrThrow("costo"))),
                                    String2IntWithNull(c.getString(c.getColumnIndexOrThrow("su_prenotazione"))),
                                    String2IntWithNull( c.getString(c.getColumnIndexOrThrow("tipo_prenotazione")) ),
                                    String2IntWithNull(c.getString(c.getColumnIndexOrThrow("da_eta"))),
                                    String2IntWithNull(c.getString(c.getColumnIndexOrThrow("a_eta"))),
                                    String2IntWithNull( c.getString(c.getColumnIndexOrThrow("tipo_eta")) ),
                                    c.getString(c.getColumnIndexOrThrow("external_urls")),
                                    c.getString(c.getColumnIndexOrThrow("youtube_url")),
                                    c.getString(c.getColumnIndexOrThrow("contatti")),
                                    c.getString(c.getColumnIndexOrThrow("hashtag")),
                                    c.getString(c.getColumnIndexOrThrow("datainserimento")),
                                    c.getString(c.getColumnIndexOrThrow("username")),
                                    "", // *** attributo "stato" da eliminare!!!
                                    c.getString(c.getColumnIndexOrThrow("speciale")),
                                    String2ALString(c.getString(c.getColumnIndexOrThrow("categorie")))
                            )
                    );
                }
                if(eventi.isEmpty()) Log.e("getEventiFromDB", "stateList/eventi non è stato riempito");
                c.close(); // invitano a chiudere sempre il cursore: http://stackoverflow.com/questions/10723770/whats-the-best-way-to-iterate-an-android-cursor
            } else Log.i("DB_Select else", "return is null");
            db.close();
        } catch (Exception e) {
            Log.e("DB_Select catch Eventi:", e.toString());
        }
        return eventi;
    }
    private  ArrayList<Evento> eventi = new ArrayList<Evento>();
}