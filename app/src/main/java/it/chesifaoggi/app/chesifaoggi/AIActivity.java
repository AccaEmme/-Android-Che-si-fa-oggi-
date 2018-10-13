package it.chesifaoggi.app.chesifaoggi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gaia on 25/02/16.
 */
public class AIActivity extends AppCompatActivity {
    ArrayList<Consiglio> consigli;
    ListView lv_consigli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aigioia);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
*/


        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(Chesifaoggi.this);


        //PreferenceManager.setDefaultValues(this, R.xml.mypreference, false);
        //myPreference = PreferenceManager.getDefaultSharedPreferences(this);
        lv_consigli= (ListView) findViewById(R.id.listview_AI_consigli);
        lv_consigli.setVisibility(View.INVISIBLE);
        onClickButton();

    }

    public void onClickButton(){
        Button myButton = (Button) findViewById(R.id.btn_gioia);

        myButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            RadioButton radioButton = null;

                                            Consigli c = new Consigli();
                                            consigli = c.getConsigli();

                                            radioButton = (RadioButton) findViewById(R.id.radiobutton_1_1_sonosolo);
                                            if (radioButton.isChecked()) {
                                                consigli = c.trueValue("radiobutton_1_1_sonosolo", true);
                                                c.setConsigli(consigli);
                                            } else {
                                                radioButton = (RadioButton) findViewById(R.id.radiobutton_1_2_coppia);
                                                if (radioButton.isChecked()) {
                                                    consigli = c.trueValue("radiobutton_1_2_coppia", true);
                                                    c.setConsigli(consigli);
                                                } else {
                                                    radioButton = (RadioButton) findViewById(R.id.radiobutton_1_3_gruppo);
                                                    if (radioButton.isChecked()) {
                                                        consigli = c.trueValue("radiobutton_1_3_gruppo", true);
                                                        c.setConsigli(consigli);
                                                    }
                                                }
                                            }

                                            radioButton = (RadioButton) findViewById(R.id.radiobutton_2_1_aperto);
                                            if (radioButton.isChecked()) {
                                                consigli = c.trueValue("radiobutton_2_1_aperto", true);
                                                c.setConsigli(consigli);
                                            } else {
                                                radioButton = (RadioButton) findViewById(R.id.radiobutton_2_2_chiuso);
                                                if (radioButton.isChecked()) {
                                                    consigli = c.trueValue("radiobutton_2_2_chiuso", true);
                                                    c.setConsigli(consigli);
                                                }
                                            }

                                            radioButton = (RadioButton) findViewById(R.id.radiobutton_3_1_attivo);
                                            if (radioButton.isChecked()) {
                                                consigli = c.trueValue("radiobutton_3_1_attivo", true);
                                                c.setConsigli(consigli);
                                            } else {
                                                radioButton = (RadioButton) findViewById(R.id.radiobutton_3_2_passivo);
                                                if (radioButton.isChecked()) {
                                                    consigli = c.trueValue("radiobutton_3_2_passivo", true);
                                                    c.setConsigli(consigli);
                                                }
                                            }

                                            radioButton = (RadioButton) findViewById(R.id.radiobutton_4_1_rilassante);
                                            if (radioButton.isChecked()) {
                                                consigli = c.trueValue("radiobutton_4_1_rilassante", true);
                                                c.setConsigli(consigli);
                                            } else {
                                                radioButton = (RadioButton) findViewById(R.id.radiobutton_4_2_movimentata);
                                                if (radioButton.isChecked()) {
                                                    consigli = c.trueValue("radiobutton_4_2_movimentata", true);
                                                    c.setConsigli(consigli);
                                                }
                                            }

                                            radioButton = (RadioButton) findViewById(R.id.radiobutton_5_1_maltempo);
                                            if (radioButton.isChecked()) {
                                                consigli = c.trueValue("radiobutton_5_1_maltempo", true);
                                                c.setConsigli(consigli);
                                            } else {
                                                radioButton = (RadioButton) findViewById(R.id.radiobutton_5_2_beltempo);
                                                if (radioButton.isChecked()) {
                                                    consigli = c.trueValue("radiobutton_5_2_beltempo", true);
                                                    c.setConsigli(consigli);
                                                }
                                            }

                                            radioButton = (RadioButton) findViewById(R.id.radiobutton_6_1_fame);
                                            if (radioButton.isChecked()) {
                                                consigli = c.trueValue("radiobutton_6_1_fame", true);
                                                c.setConsigli(consigli);
                                            } else {
                                                radioButton = (RadioButton) findViewById(R.id.radiobutton_6_2_nofame);
                                                if (radioButton.isChecked()) {
                                                    consigli = c.trueValue("radiobutton_6_2_nofame", true);
                                                    c.setConsigli(consigli);
                                                }
                                            }

                                            radioButton = (RadioButton) findViewById(R.id.radiobutton_7_1_mattina);
                                            if (radioButton.isChecked()) {
                                                consigli = c.trueValue("radiobutton_7_1_mattina", true);
                                                c.setConsigli(consigli);
                                            } else {
                                                radioButton = (RadioButton) findViewById(R.id.radiobutton_7_2_pomeriggio);
                                                if (radioButton.isChecked()) {
                                                    consigli = c.trueValue("radiobutton_7_2_pomeriggio", true);
                                                    c.setConsigli(consigli);
                                                } else {
                                                    radioButton = (RadioButton) findViewById(R.id.radiobutton_7_3_sera);
                                                    if (radioButton.isChecked()) {
                                                        consigli = c.trueValue("radiobutton_7_3_sera", true);
                                                        c.setConsigli(consigli);
                                                    }
                                                }
                                            }

                                            lv_consigli.setVisibility(View.VISIBLE);
                                            populatingLV();
                                        }
                                    }
        );
    }

    public void populatingLV(){
        List<String> stringhe_consigli = new ArrayList<String>();


        for(Consiglio s : consigli){
            stringhe_consigli.add(s.getTesto());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                stringhe_consigli );

        lv_consigli.setAdapter(arrayAdapter);
    }


    public class Consiglio{
        public Consiglio(String testo, boolean solo, boolean coppia, boolean gruppo, boolean aperto, boolean chiuso, boolean attivo, boolean passivo, boolean rilassante, boolean movimentata, boolean maltempo, boolean beltempo, boolean fame, boolean nofame, boolean mattina, boolean pomeriggio, boolean sera){
            this.aperto = aperto;
            this.attivo = attivo;
            this.beltempo = beltempo;
            this.chiuso = chiuso;
            this.coppia = coppia;
            this.fame = fame;
            this.gruppo = gruppo;
            this.maltempo = maltempo;
            this.mattina = mattina;
            this.movimentata = movimentata;
            this.nofame = nofame;
            this.passivo = passivo;
            this.pomeriggio = pomeriggio;
            this.rilassante = rilassante;
            this.sera = sera;
            this.solo = solo;
            this.testo = testo;

        }

        public String getTesto(){
            return this.testo;
        }

        public boolean isAperto() {
            return aperto;
        }

        public boolean isAttivo() {
            return attivo;
        }

        public boolean isBeltempo() {
            return beltempo;
        }

        public boolean isChiuso() {
            return chiuso;
        }

        public boolean isCoppia() {
            return coppia;
        }

        public boolean isFame() {
            return fame;
        }

        public boolean isGruppo() {
            return gruppo;
        }

        public boolean isMaltempo() {
            return maltempo;
        }

        public boolean isMattina() {
            return mattina;
        }

        public boolean isMovimentata() {
            return movimentata;
        }

        public boolean isNofame() {
            return nofame;
        }

        public boolean isPassivo() {
            return passivo;
        }

        public boolean isPomeriggio() {
            return pomeriggio;
        }

        public boolean isRilassante() {
            return rilassante;
        }

        public boolean isSera() {
            return sera;
        }

        public boolean isSolo() {
            return solo;
        }


        String testo;
        boolean solo, coppia, gruppo, aperto, chiuso, attivo, passivo, rilassante, movimentata, maltempo, beltempo, fame, nofame, mattina, pomeriggio, sera;
    }

    public class Consigli{
        public Consigli(){
            this.consigli = new ArrayList<Consiglio>();
            consigli.add(new Consiglio("Cinema",            false, true, true, false, true, false, true, true, false, true, true, false, true, false, true, true));
            consigli.add(new Consiglio("Bar",               true, false, true, false, true, true, true, true, false, true, true, true, true, false, true, true));
            consigli.add(new Consiglio("Giochi di società\n\trisiko, giochi di ruolo, obbligo o verità, nomi cose città", false, false, true, false, true, true, false, true, false, true, true, false, true, false, true, true));
            consigli.add(new Consiglio("Passeggiata",       true, true, true, true, false, true, false, true, false, false, true, true, true, true, true, true));
            consigli.add(new Consiglio("Videogiochi",       true, true, true, false, false, true, true, true, false, true, true, true, true, true, true, true));
            consigli.add(new Consiglio("Conosci nuove persone", true, true, false, true, true, true, false, true, true, true, true, true, true, true, true, true));
            consigli.add(new Consiglio("Leggere un libro",  true, false, false, true, true, true, false, true, false, true, true, false, true, true, true, true));
            consigli.add(new Consiglio("Scrivi una lettera",  true, false, false, true, true, true, false, true, false, true, true, false, true, true, true, true));
            consigli.add(new Consiglio("Colora",  true, false, false, true, true, true, false, true, false, true, true, false, true, true, true, true));
            consigli.add(new Consiglio("Bowling",           false, true, true, false, true, true, false, true, false, true, false, false, true, false, true, true));
            consigli.add(new Consiglio("Pattinare:\n\tper strada, sul ghiaccio",         true, true, true, true, true, true, false, false, true, false, true, false, true, false, true, true));
            consigli.add(new Consiglio("Shopping\n\tonline o nei negozi", true, true, false, true, true, true, false, true, true, true, true, false, true, true, true, false));
            consigli.add(new Consiglio("Chiama un amico che non senti da tanto tempo", true, true, true, true, true, true, false, false, true, true, true, true, true, true, true, true));
            consigli.add(new Consiglio("Riorganizza la stanza", true, true, false, false, false, true, false, false, true, true, false, false, true, true, true, true));
            consigli.add(new Consiglio("Cibo:\n\tpizza,crepes,cinese, etnico, gelato, grigliata, vegan", true, true, true, true, true, true, true, true, false, true, true, true, false, false, false, true));
            consigli.add(new Consiglio("Ballo: discoteca, salsa, ...",            false, true, true, false, true, false, true, true, false, true, true, false, true, false, true, true));
            consigli.add(new Consiglio("Musica: live, karaoke",            true, true, true, false, true, false, true, true, false, true, true, false, true, false, true, true));
            consigli.add(new Consiglio("Fai da te: prendi spunto da pinterest o youtube", true, true, true, false, true, true, false, true, false, true, true, false, true, false, true, true));
            //consigli.add(new Consiglio("conosci persone nuove", true));
        }

        public ArrayList<Consiglio> getConsigli(){
            return this.consigli;
        }

        private ArrayList<Consiglio> just_sonosolo(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                    if (c.isSolo() != var) result.remove(c);
            }
            Log.i("1.1", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_coppia(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isCoppia() != var) result.remove(c);
            }
            Log.i("1.2", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_gruppo(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isGruppo() != var) result.remove(c);
            }
            Log.i("1.3", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_aperto(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isAperto() != var) result.remove(c);
            }
            Log.i("2.1", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_chiuso(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isChiuso() != var) result.remove(c);
            }
            Log.i("2.2", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_attivo(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isAttivo() != var) result.remove(c);
            }
            Log.i("3.1", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_passivo(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isPassivo() != var) result.remove(c);
            }
            Log.i("3.2", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_rilassante(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isRilassante() != var) result.remove(c);
            }
            Log.i("4.1", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_movimentata(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isMovimentata() != var) result.remove(c);
            }
            Log.i("4.2", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_maltempo(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isMaltempo() != var) result.remove(c);
            }
            Log.i("5.1", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_beltempo(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isBeltempo() != var) result.remove(c);
            }
            Log.i("5.2", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_fame(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isFame() != var) result.remove(c);
            }
            Log.i("6.1", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_nofame(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isNofame() != var) result.remove(c);
            }
            Log.i("6.2", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_mattina(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isMattina() != var) result.remove(c);
            }
            Log.i("7.1", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_pomeriggio(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isPomeriggio() != var) result.remove(c);
            }
            Log.i("7.2", this.toString());
            return result;
        }

        private ArrayList<Consiglio> just_sera(boolean var){  // ritorna solo i consigli con
            ArrayList<Consiglio> result = new ArrayList<Consiglio>();
            result.addAll(consigli);
            for (Consiglio c : consigli) {
                if (c.isSera() != var) result.remove(c);
            }
            Log.i("7.3", this.toString());
            return result;
        }


        public ArrayList<Consiglio> trueValue(String radio, boolean bool){ // return just true values
            switch(radio){
                case "radiobutton_1_1_sonosolo":    return just_sonosolo(bool);
                case "radiobutton_1_2_coppia":      return just_coppia(bool);
                case "radiobutton_1_3_gruppo":      return just_gruppo(bool);
                case "radiobutton_2_1_aperto":      return just_aperto(bool);
                case "radiobutton_2_2_chiuso":      return just_chiuso(bool);
                case "radiobutton_3_1_attivo":      return just_attivo(bool);
                case "radiobutton_3_2_passivo":     return just_passivo(bool);
                case "radiobutton_4_1_rilassante":  return just_rilassante(bool);
                case "radiobutton_4_2_movimentata": return just_movimentata(bool);
                case "radiobutton_5_1_maltempo":    return just_maltempo(bool);
                case "radiobutton_5_2_beltempo":    return just_beltempo(bool);
                case "radiobutton_6_1_fame":        return just_fame(bool);
                case "radiobutton_6_2_nofame":      return just_nofame(bool);
                case "radiobutton_7_1_mattina":     return just_mattina(bool);
                case "radiobutton_7_2_pomeriggio":  return just_pomeriggio(bool);
                case "radiobutton_7_3_sera":        return just_sera(bool);

                default: return null;
            }

        }

        public void setConsigli(ArrayList<Consiglio> arrayList){
            this.consigli=arrayList;
        }


        public String toString(){
            String s = "";
            for(Consiglio c:consigli){
                s+=c.getTesto()+"|";
            }
            return s;
        }

        ArrayList<Consiglio> consigli;
    }
}

/*
ArrayList<Consiglio>
 dove Consiglio(frase, boolean aperto, boolean )

Consigli
 è un ArrayList di Consiglio, l'init di base aggiunge tutto, il get torna la stessa lista senza alcuni valori

   public ArrayList<Consiglio> getAperto(boolean false, ArrayList<Consiglio> consigli){
    for(consigli : c){
     if(c.isAperto(false)) consigli.remove(c);
    }

    return consigli;
   }

 */