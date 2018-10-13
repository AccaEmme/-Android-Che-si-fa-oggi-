package it.chesifaoggi.app.chesifaoggi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gaia on 12/02/16.
 * http://fundoocode.net/android-listview-checkbox-example-onitemclicklistener-and-onclicklistener/
 */
public class SettingsActivity extends PreferenceActivity {
    private ArrayList<ListViewCheckboxed> stateList, nuovaLista;
    // Listview Adapter
    ArrayAdapter<ListViewCheckboxed> adapter;

    // Search EditText
    EditText inputSearch;
    MyCustomAdapter dataAdapter = null;

    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    SharedPreferences myPreference = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
         /*
        da fare:
         V Rinominare Settings in SettingsActivity e accorpare il settings e layout
         - Cerca eventi: usa posizione
         - entro raggio: 0 Km
         - preferiti
         - listview eventi con cuoricino per preferito
         - tab eventi N giorni

         - pubblicità: se c'è mostrare quella locale, altrimenti AdMob

    V   rinominare Settings_provincia in ListViewWithCheckBox


         */

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_settings);
        addPreferencesFromResource(R.xml.mypreference);
        myPreference = PreferenceManager.getDefaultSharedPreferences(this);
        wipeDBButton();

        wipeExpiredButton();
        deleteAllPicturesButton();


    }

    private void deleteAllPicturesButton(){
        Preference wipeDBButton = (Preference) findPreference((getString(R.string.wipe_pictures)));
        wipeDBButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle(getString(R.string.settingsactivity_wipe_pictures_title))
                        .setMessage(getString(R.string.settingsactivity_wipe_pictures_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(PicDownloader.deleteAllFiles() == false)
                                    Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), getString(R.string.settingsactivity_wipe_pictures_deleted),Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }

    private void deletePictures(ArrayList<String> ids){
        for(String s:ids) PicDownloader.deleteFile(s);
    }


    private void wipeExpiredButton() {
        Preference wipeDBButton = (Preference) findPreference((getString(R.string.wipe_expired)));
        wipeDBButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Eliminazione eventi scaduti")
                        .setMessage("Attenzione: eliminando gli eventi scaduti perderai anche i relativi eventi preferiti se presenti, continuare?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DBLayer db = new DBLayer(SettingsActivity.this);
                                db.open();
                                db.WipeExpiredEvents();
                                db.close();
                                deletePictures( db.expiredEvents() );           // elimina locandine solo degli eventi scaduti
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }


    private void wipeDBButton(){
        Preference wipeDBButton = (Preference) findPreference( (getString(R.string.wipe_db) ));
        wipeDBButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Eliminazione eventi")
                        .setMessage("Attenzione: eliminando gli eventi perderai anche i relativi eventi preferiti se presenti, continuare?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DBLayer db = new DBLayer(SettingsActivity.this);
                                db.open();
                                //db.WipeExpiredEvents();
                                db.WipeTable("eventi");
                                db.close();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                if(PicDownloader.deleteAllFiles() == false)
                    Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Locandine eliminate",Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //(myPreference.getBoolean("wipe_db", true))
    }




    private class MyCustomAdapter extends ArrayAdapter<ListViewCheckboxed> {
        private ArrayList<ListViewCheckboxed> stateList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ListViewCheckboxed> stateList) {
            super(context, textViewResourceId, stateList);
            this.stateList = new ArrayList<ListViewCheckboxed>();
            this.stateList.addAll(stateList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); // Context.
                convertView = vi.inflate(R.layout.checkboxedlistview, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);

                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        ListViewCheckboxed _state = (ListViewCheckboxed) cb.getTag();

                        Toast.makeText(getApplicationContext(), getString(R.string.settings_haiselezionato) + cb.getText(),
                                Toast.LENGTH_LONG).show();

                        _state.setSelected(cb.isChecked());
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListViewCheckboxed state = stateList.get(position);
            if( (state.getCode().equals(""))){
                holder.code.setText("");
            } else {
                holder.code.setText(" (" + state.getCode() + ")");
            }
            holder.name.setText(state.getName());
            holder.name.setChecked(state.isSelected());
            holder.name.setTag(state);

            return convertView;
        }
    }
/*
    private void checkButtonClickProvince() {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append( getString(R.string.settings_haiselezionato)    +"\n");

                ArrayList<ListViewCheckboxed> stateList = dataAdapter.stateList;
                ListViewCheckboxed state;

                for(int i=0;i<stateList.size();i++) {
                    state = stateList.get(i);

                    if(state.isSelected()) {
                        responseText.append("\n" + state.getName());
                    }
                }
                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });
    }
*/
    private class ListViewCheckboxed {
        String code = null;
        String name = null;
        boolean selected = false;

        public ListViewCheckboxed(String name, boolean selected) {
            super();
            this.code = "";
            this.name = name;
            this.selected = selected;
        }

        public ListViewCheckboxed(String code, String name, boolean selected) {
            super();
            this.code = code;
            this.name = name;
            this.selected = selected;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}