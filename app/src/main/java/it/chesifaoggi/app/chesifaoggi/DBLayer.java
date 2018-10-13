package it.chesifaoggi.app.chesifaoggi;

/**
 * Created by gaia on 20/03/16.
 */
import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class DBLayer {
    //private static String query_tabella1 = "CREATE TABLE eventi ( id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT);";

    // *** si potrebbero ricevere solo gli eventi con stato confermato, eliminando l'attributo e dati inutili
    private static String query_tabella1 = "CREATE TABLE IF NOT EXISTS eventi (" +
            "`count` int(11) PRIMARY KEY NOT NULL," +  // primary key è già not null, ridondanza inutile. Lasciarlo primary key evita che un download/refresh eventi li duplichi. Questo però inserisce anche eventi eliminati. Vanno presi nuovi, da un indice in poi
            "`titolo` varchar(150) NOT NULL,"+
            "`descrizione` text,"+
            "`data_i` datetime NOT NULL,"+
            "`data_f` datetime NOT NULL,"+
            "`zona` varchar(100) NOT NULL,"+
            "`latitudine` varchar(50) DEFAULT NULL,"+
            "`longitudine` varchar(50) DEFAULT NULL,"+
            "`struttura` varchar(100) DEFAULT NULL,"+
            "`via` varchar(200) DEFAULT NULL,"+
            "`immagine` varchar(300) DEFAULT NULL,"+ // *** deve diventare BLOB
            "`gratuito` tinyint(1) NOT NULL,"+
            "`costo` double DEFAULT NULL,"+
            "`su_prenotazione` tinyint(1) NOT NULL,"+
            "`tipo_prenotazione` DEFAULT NULL,"+
            "`da_eta` smallint(3) DEFAULT NULL,"+
            "`a_eta` smallint(3) DEFAULT NULL,"+
            "`tipo_eta` smallint(2) DEFAULT NULL,"+ // COMMENT 'null, 0 = suggerito, 1=obbligatorio'
            "`external_urls` text,"+
            "`youtube_url` varchar(50) DEFAULT NULL,"+
            "`contatti` text,"+
            "`hashtag` varchar(25) DEFAULT NULL,"+
            "`datainserimento` datetime NOT NULL,"+
            "`username` varchar(50) NOT NULL,"+
            //"`stato` enum('Cancellato','Rimandato','Confermato') DEFAULT NULL,"+
            "`speciale` smallint(6) NOT NULL DEFAULT '0',"+ // ' COMMENT '{0=niente,1=rossa,2=dorata,}

            "`preferito` tinyint(1),"+
            "`categorie` TEXT"+
            ");";

    private static String query_tabella2 = "CREATE TABLE IF NOT EXISTS count_province(`provincia` varchar(3) PRIMARY KEY UNIQUE NOT NULL, `count` int(11), `lastupd` timestamp );";

    public static enum TipoQuery {
        Selezione,
        Comando
    }

    private static final String DATABASE_NAME = "chesifaoggiDB";
    private static final int DATABASE_VERSION = 2;
    public static final String[] DATABASE_TABLES = {"eventi", "count_province"};
    public static final String[][] DATABASE_QUERIES = new String[][]{
            {"eventi", query_tabella1},
            {"count_province", "CREATE TABLE IF NOT EXISTS count_province(`provincia` varchar(3) PRIMARY KEY NOT NULL, `count` int(11), `lastupd` timestamp );"}
    };

    private DbHelper ourHelper;
    private  static Context ourContext;
    private SQLiteDatabase ourDatabase;


    private static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            String query_tabella1 = "CREATE TABLE IF NOT EXISTS eventi (" +
                    "`count` int(11) PRIMARY KEY NOT NULL," +  // primary key è già not null, ridondanza inutile. Lasciarlo primary key evita che un download/refresh eventi li duplichi. Questo però inserisce anche eventi eliminati. Vanno presi nuovi, da un indice in poi
                    "`titolo` varchar(150) NOT NULL,"+
                    "`descrizione` text,"+
                    "`data_i` datetime NOT NULL,"+
                    "`data_f` datetime NOT NULL,"+
                    "`zona` varchar(100) NOT NULL,"+
                    "`latitudine` varchar(50) DEFAULT NULL,"+
                    "`longitudine` varchar(50) DEFAULT NULL,"+
                    "`struttura` varchar(100) DEFAULT NULL,"+
                    "`via` varchar(200) DEFAULT NULL,"+
                    "`immagine` varchar(300) DEFAULT NULL,"+ // *** deve diventare BLOB
                    "`gratuito` tinyint(1) NOT NULL,"+
                    "`costo` double DEFAULT NULL,"+
                    "`su_prenotazione` tinyint(1) NOT NULL,"+
                    "`tipo_prenotazione` DEFAULT NULL,"+
                    "`da_eta` smallint(3) DEFAULT NULL,"+
                    "`a_eta` smallint(3) DEFAULT NULL,"+
                    "`tipo_eta` smallint(2) DEFAULT NULL,"+ // COMMENT 'null, 0 = suggerito, 1=obbligatorio'
                    "`external_urls` text,"+
                    "`youtube_url` varchar(50) DEFAULT NULL,"+
                    "`contatti` text,"+
                    "`hashtag` varchar(25) DEFAULT NULL,"+
                    "`datainserimento` datetime NOT NULL,"+
                    "`username` varchar(50) NOT NULL,"+
                    //"`stato` enum('Cancellato','Rimandato','Confermato') DEFAULT NULL,"+
                    "`speciale` smallint(6) NOT NULL DEFAULT '0',"+ // ' COMMENT '{0=niente,1=rossa,2=dorata,}

                    "`preferito` tinyint(1),"+
                    "`categorie` TEXT"+
                    ");";

            String query_tabella2 = "CREATE TABLE IF NOT EXISTS count_province(`provincia` varchar(3) PRIMARY KEY NOT NULL, `count` int(11), `lastupd` timestamp );";

            //try{
                db.execSQL(query_tabella1);
                db.execSQL(query_tabella2);
                db.setVersion(DATABASE_VERSION);
            /*
            } catch(Exception e){
                Log.e("DB_CreateTable", e.toString());
            }
            */

        }

        public void init(SQLiteDatabase db){
            db.execSQL(query_tabella1);
            db.execSQL(query_tabella2);
            db.setVersion(DATABASE_VERSION);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){ // Senza di questo non permette di fare il downgrade del database!
            for(String tabella:DATABASE_TABLES)
                db.execSQL("DROP TABLE IF EXISTS " + tabella);
            onCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //String DATABASE_TABLECN = "tabella1";
            //db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLECN);
            //onCreate(db);n

            /*

            Log.w(MySQLiteHelper.class.getName(),  "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + NEWTABLE);
            onCreate(db);
             */
            if(newVersion > oldVersion){
                for(String tabella:DATABASE_TABLES)
                    db.execSQL("DROP TABLE IF EXISTS " + tabella);
                onCreate(db);
            }
        }
    }


    public DBLayer(Context c){
        this.ourContext = c;
    }

    public DBLayer open() throws SQLException {
        this.ourHelper = new DbHelper(ourContext);
        this.ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.ourHelper.close();
    }

    protected void removeEvent(String count) {
        String query = "delete from eventi where `count` like \"" + count + "\";";

        try {
            ourDatabase.execSQL(query);
        } catch (Exception e) {
            Log.i("DBLayer - removeEvent:", query + " - " + " - " + e.toString());
        }
    }

    protected ArrayList<String> expiredEvents(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String query = "select `count` from eventi where `data_f` < \""+dateFormat.format(date)+"\";";
        ArrayList<String> result = new ArrayList<String>();
        Log.i("expiredEvents: ", query);
        try {
            Cursor c = ourDatabase.rawQuery(query, null);
            if (c != null) {
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    result.add(c.getString(c.getColumnIndexOrThrow("count")));
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        Log.i("expiredEvents: ", result.toString());
        return result;
    }

    protected void WipeExpiredEvents(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String query = "delete from eventi where `data_f` < \""+dateFormat.format(date)+"\";";

        try {
            ourDatabase.execSQL(query);
        }catch (Exception e){
        Log.i("WipeExpiredEvents", query+" - " + " - " + e.toString() );
        }
    }


    public void WipeTable(String tableName){ // svuota una tabella specificata, creato da me
        String query = null;

        try{
            ourDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);       // elimino la tabella

        }catch(Exception e){
            Log.e("DB_DropTable", e.toString() + " - " + query);
        }

        switch(tableName){
            case "eventi":
                query = query_tabella1;
                break;
            case "count_province":
                query = query_tabella2;
                break;
        }
        try{
            ourDatabase.execSQL(query);                                     // ricreo la tabella
        } catch(Exception e){
            Log.e("DB_WipeTable", e.toString() + " - " + query);
        }

    }

    public Cursor Execute(String Query, TipoQuery tipoCmd){
        Cursor c = null;

        try{
            switch(tipoCmd){
                case Comando:
                    ourDatabase.execSQL(Query);
                    break;
                case Selezione:
                    c = ourDatabase.rawQuery(Query,null);
                    break;

                /*
                Differenza tra execSQL e rawQuery:
                http://it.androids.help/q12553
                Eseguire una singola istruzione SQL che non è una SELECT o qualsiasi altra istruzione SQL che restituisce i dati .
public Cursor rawQuery (String sql, String[] selectionArgs)
Esegue l' SQL fornito e restituisce un cursore sopra il set di risultati .
Se si desidera ad esempio CREATE TABLE Che non restituisce i valori che è possibile utilizzare execSQL(), se volete un Cursor, come conseguenza dell'uso rawQuery() ( = SELECT dichiarazioni ) .

                 */
            }
        }catch (Exception e){
            Log.i("DB_CursorExecute", e.toString());
        }

        return c;
    }


    protected String getAttributes(String table){
        switch(table){
            case "eventi": return " `count`,`titolo`, `descrizione`, `data_i`, `data_f`, `zona`, `latitudine`, `longitudine`, `struttura`, `via`, `immagine`, `gratuito`, `costo`, `su_prenotazione`, `tipo_prenotazione`, `da_eta`, `a_eta`, `tipo_eta`, `external_urls`, `youtube_url`, `contatti`, `hashtag`, `datainserimento`, `username`, `speciale`, `preferito`, `categorie` ";
            case "count_province": return " `provincia`,`count`, `lastupd` ";
            default: return "-wrong table-";
        }
    }
}