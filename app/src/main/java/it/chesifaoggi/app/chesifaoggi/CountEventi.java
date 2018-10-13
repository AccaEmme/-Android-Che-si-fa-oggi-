package it.chesifaoggi.app.chesifaoggi;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by gaia on 17/09/16.
 */
public class CountEventi {
    public CountEventi(){
        this.counteventi = new ArrayList<CountEvento>();
    }

    public CountEventi(ArrayList<CountEvento> alce){
        this.counteventi = alce;
    }

    public int getCountByProv(String prov){

        for(CountEvento ce:counteventi){
         if(ce.getProv().equals(prov)) return ce.getCount();
        }
        Log.e("getCountByProv", "return -1 quindi la provincia cercata non esiste");
        return -1;
    }

    public void setByTest(){
        /*
        counteventi.add( new CountEvento("AV", 35) );
        counteventi.add( new CountEvento("BN", 4)  );
        counteventi.add( new CountEvento("CE", 7)  );
        counteventi.add( new CountEvento("NA", 55) );
        counteventi.add(new CountEvento("SA", 51));
        */

        String[] prov = {"AV", "BN", "CE", "NA", "SA", "AV"};
        int[] count = {35, 4, 7, 55, 51, 88};

        TreeMap<String, Integer> mmm = new TreeMap<String, Integer>();
        mmm.put("AV", 35);
        mmm.put("BN", 15);
        mmm.put("CE", 25);
        mmm.put("NA", 45);
        mmm.put("SA", 55);
        mmm.put("AV", 88);

        for(Map.Entry<String, Integer> e: mmm.entrySet()) {
            counteventi.add(
                    new CountEvento(
                            e.getKey(),
                            e.getValue()
                    )
            );
        }
    }

    public void setByJSON(){
        String jsonRawUrl = "http://chesifaoggi.altervista.org/stadio7.3/JSON/json_count_province.php";
        String json= null, JSONUrl = null;

        try {
            JsonParser jp = new JsonParser();
            json = jp.getStringbyJSONUrl(jsonRawUrl, 5000);

            if (json == null)             Log.e("CountEventi getJSONUrl: ", "mirror1 is null");
            else
                JSONUrl = jsonRawUrl;
        } catch (Throwable t) {
            Log.e("CountEventi JSON: ", "Could not parse malformed JSON: " + json + " - " + t.toString());
        }
        Log.i("CountEventi getJSONUrl: i will use ", JSONUrl);

        this.counteventi = getCountEventiFromJSON(JSONUrl);
    }

    public ArrayList<CountEvento> getCountEventiFromJSON(String s_url) { // "è un JSONObjectFromString esteso"
        String json = null;
        try {
            JsonParser jp = new JsonParser();
            json = jp.getStringbyJSONUrl(s_url, 5000);

            if (json == null) {
                Log.e("CountEventi getEventiFromJSON: ", "json is null");
                return counteventi;
            }
            json = getValidJSON(json);
            JSONObject obj = new JSONObject(json);
            Log.d("CountEventi JSON: ", obj.toString());
        } catch (Throwable t) {
            Log.e("CountEventi JSON: ", "Could not parse malformed JSON: " + json + " - " + t.toString());
        }


        ArrayList<CountEvento> json_counteventi = new ArrayList<CountEvento>();
        try {

            JSONObject jo = new JSONObject(json);
            JSONArray ja = jo.getJSONArray("results");

            for(int i=0; i<ja.length(); i++) {
                JSONObject oggetto_json = ja.getJSONObject(i);

                json_counteventi.add(
                        new CountEvento(
                                oggetto_json.getString("provincia"),
                                oggetto_json.getInt("count")
                        )
                );
            }
        }catch(JSONException e1){
            //e1.printStackTrace();
            Log.e("JSON: xx", e1.toString());
        }

        if (!(json_counteventi.isEmpty())) return json_counteventi;
        Log.e("CountEventi json: ", "nessun count eventi scaricato");
        return counteventi;
    }


    private String getValidJSON(String json){
        json = json.trim();
        Log.i("getValidJSON 1:", json);
        if(json.startsWith("[") && json.endsWith("]")) json = json.substring(1,json.length()-1);  // quando ci sono più valori, mette le [] esempio: [{},{}] e non lo interpreta bene.
        Log.i("getValidJSON 2:", json);
        return json;
    }

    public ArrayList<CountEvento> getCountEventi(){
        return this.counteventi;
    }

    public CountEvento getCEbyProv(String prov){
        for(CountEvento ce : counteventi){
            if ( ce.getProv().equals(prov) ) return ce;
        }
        return null;
    }


    public void storeinSharedPreference(SharedPreferences myPreference){
        Set<String> values = myPreference.getStringSet("province_interesse", new HashSet<String>());
        /*
        Non si possono modificare i valori statici di R! ;_;
        EventiActivity.this.getString(R.array.province);

        myPreference.edit().putStringSet("province_interesse", null);

        Set<String> selezione_province = myPreference.getStringSet("province_interesse", null);
        String[] province_selezionate = selezione_province.toArray(new String[]{});
        TextView ads = (TextView) findViewById(R.id.ads);
        String ads_text= "Comunicazioni e pubblicità:\nPer la pubblicità in questo spazio: chesifaoggi@live.it\nProvince d'interesse: ";
        for(String s:province_selezionate)            ads_text += s+" | ";
        */
    }

    public void storeInDB(Context chiamante){ // *** Pessima idea. da eliminare. il db richiede un UPGRADE e se non trova l'elemento richiede INSERT. Troppi controlli per fare cose che già vengono gestite con le SharedPreference
        String strQuery = "INSERT OR REPLACE INTO count_province ( provincia, count, lastupd ) VALUES ";
        DBLayer db = null;
            try {
                db = new DBLayer(chiamante);
                db.open();
                if (!counteventi.isEmpty()) {
                    for (CountEvento ce : counteventi) {
                        Log.i("CountEventi storeInDB:", ce.toString());
                        if (counteventi.indexOf(ce) != 0) strQuery += ",";
                        strQuery += "("
                                + "'" + ce.getProv().replace("'", "''") + "', "
                                + "" + ce.getCount() + ", "
                                + ce.getLastUpd()
                                + ")";
                    }
                    //strQuery = strQuery.substring(0, strQuery.length()-1); // visto che non posso fare indexOf
                    //strQuery += "ON DUPLICATE KEY UPDATE provincia=VALUES(provincia);"; Presente in MySQL
                    strQuery += ";";
                } else {
                    Log.i("CountEventi storeInDB:", "eventi vuoto");
                }
                Log.i("CountEventi storeInDB", strQuery);

                db.Execute(strQuery, DBLayer.TipoQuery.Comando);
                db.close();
            } catch (Exception e) {
                Log.i("DB_Insert", e.toString());
            }

        /*
        strQuery = strQueries[1]; // ridondanza voluta, codice da schifo: update
        try {
            db = new DBLayer(chiamante);
            db.open();
            if (!counteventi.isEmpty()) {
                for (CountEvento ce : counteventi) {
                    Log.i("CountEventi storeInDB:", ce.toString());
                    if (counteventi.indexOf(ce) != 0) strQuery += ",";
                    strQuery += "("
                            + "'" + ce.getCount() + "', "
                            + "'" + ce.getProv().replace("'", "''") + "', "
                            + null
                            + ")";
                }
                strQuery += ";";
            } else {
                Log.i("CountEventi storeInDB:", "eventi vuoto");
            }
            Log.i("CountEventi storeInDB", strQuery);

            db.Execute(strQuery, DBLayer.TipoQuery.Comando);
            db.close();
        } catch (Exception e) {
            Log.i("DB_Insert", e.toString());
        }
*/
    }

    public ArrayList<CountEvento> getFromDB(Context chiamante){
        counteventi = new ArrayList<CountEvento>();    //Array list of events

        DBLayer db = null;
        try{
            db = new DBLayer(chiamante); // ho specificato il Context del chiamante, altrimenti avrei dovuto eseguire implements Activity ma non avrebbe mostrato nell'altra activity i risultati.
            db.open();

            Cursor c = null;
            String strQuery = "SELECT * FROM count_province order by 1;";
            try {
                c = db.Execute(strQuery, DBLayer.TipoQuery.Selezione);
            }catch(Exception e){
                Log.e("CountEventi DB_SelectCursor", e.toString());
            }
            Log.i("CountEventi getEventiFromDB: ", strQuery);
            //db.close();

            if (c != null){
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                    counteventi.add(
                            new CountEvento(
                                    c.getString(c.getColumnIndexOrThrow("provincia")),
                                    String2IntWithNull(c.getString(c.getColumnIndexOrThrow("count"))),
                                    String2LongWithNull(c.getString(c.getColumnIndexOrThrow("lastupd")))
                            )
                    );
                }
                if(counteventi.isEmpty()) Log.e("CountEventi getFromDB", "counteventi non è stato riempito");
                c.close(); // invitano a chiudere sempre il cursore: http://stackoverflow.com/questions/10723770/whats-the-best-way-to-iterate-an-android-cursor
            } else Log.i("CountEventi DB_Select else", "return is null");
            db.close();
        } catch (Exception e) {
            Log.e("CountEventi DB_Select catch Eventi:", e.toString());
        }
        return counteventi;
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

    private long String2LongWithNull(String s){ // se stringa==null allora ritorna -1
        long retu = -1;
        if (s.equalsIgnoreCase("null") || s.equals("") || s.isEmpty()) {
            retu = -1;
        } else {
            try {
                retu = Long.valueOf(s);
            } catch (Exception e) {
                Log.e("String2LongWithNull:", e.toString());
            }

        }
        return retu;
    }

    public String toString(){
        String dateString = null;
        if(!counteventi.isEmpty())
            dateString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(counteventi.get(1).getLastUpd()))+" { ";

        String res = dateString;
        for(CountEvento ce:counteventi){
         res+=ce.toString();
         res+="; ";
        }
        return res+" }";
    }


    ArrayList<CountEvento> counteventi = new ArrayList<CountEvento>();


    public class CountEvento{
        Calendar c = Calendar.getInstance();
        long timestamp = c.getTimeInMillis();

        public CountEvento(String prov, int count){
            this.prov = prov;
            this.count = count;
            this.lastupd = timestamp;
        }

        public CountEvento(String prov, int count, long lastupd){
            this.prov = prov;
            this.count = count;
            this.lastupd = lastupd;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getProv() {
            return prov;
        }

        public void setProv(String prov) {
            this.prov = prov;
        }

        public String toString(){
            return this.prov+"->"+this.count;
        }

        public void setLastupd(long lastupd){ this.lastupd = lastupd; }

        public long getLastUpd(){ return this.lastupd; }

        String prov;
        int count;
        long lastupd;
    }
}
