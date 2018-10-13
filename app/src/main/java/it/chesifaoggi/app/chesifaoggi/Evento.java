package it.chesifaoggi.app.chesifaoggi;

import java.util.ArrayList;

/**
 * Created by gaia on 03/06/16.
 */
public class Evento {
        /*
         immagini:
            Se previsto, le immagini vengono scaricate in una temporanea,
            se dai setting è previsto salvataggio nel db, allora vedi: http://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
            sennò la fa vedere solo temporaneamente finché il telefono non viene spento: http://stackoverflow.com/questions/5899165/android-where-should-i-save-temporary-files
         */

        /*
        private Evento(boolean preferito, String id, String titolo, String immagine){
            super();
            this.id = id;
            this.titolo = titolo;
            this.selected = preferito;
            this.immagine = immagine;
        }*/

        /*
        private Evento(Evento breifing,
                       String descr,
                       String data_i, String data_f, String zona, String latitudine, String longitudine,
                       String struttura, String via, int gratuito, double costo,
                       int su_prenotazione, int tipo_prenotazione, int da_eta, int a_eta,
                       int tipo_eta, String external_urls, String youtube_url, String contatti,
                       String hashtag, String datainserimento, String username, String stato,
                       String speciale){
            super();
            this.id = breifing.getId();
            this.titolo = breifing.getTitolo();
            this.selected = breifing.isSelected();
            this.immagine = breifing.getImmagine();


            this.a_eta = a_eta;
            this.contatti = contatti;
            this.costo = costo;
            this.da_eta = da_eta;
            this.data_f = data_f;
            this.data_i = data_i;
            this.datainserimento = datainserimento;
            this.descr = descr;
            this.external_urls = external_urls;
            this.gratuito = gratuito;
            this.hashtag = hashtag;
            this.latitudine = latitudine;
            this.longitudine = longitudine;
            this.speciale = speciale;
            this.stato = stato;
            this.struttura = struttura;
            this.su_prenotazione = su_prenotazione;
            this.tipo_eta = tipo_eta;
            this.tipo_prenotazione = tipo_prenotazione;
            this.username = username;
            this.via = via;
            this.youtube_url = youtube_url;
            this.zona = zona;
        }
        */



    public Evento(boolean preferito, String id, String titolo, String descr,
                  String data_i, String data_f, String zona, String latitudine, String longitudine,
                  String struttura, String via, String immagine, int gratuito, double costo,
                  int su_prenotazione, int tipo_prenotazione, int da_eta, int a_eta,
                  int tipo_eta, String external_urls, String youtube_url, String contatti,
                  String hashtag, String datainserimento, String username, String stato,
                  String speciale, ArrayList<String> categorie) {
        super();
        this.id = id;
        this.titolo = titolo;
        this.selected = preferito;

        this.a_eta = a_eta;
        this.contatti = contatti;
        this.costo = costo;
        this.da_eta = da_eta;
        this.data_f = data_f;
        this.data_i = data_i;
        this.datainserimento = datainserimento;
        this.descr = descr;
        this.external_urls = external_urls;
        this.gratuito = gratuito;
        this.hashtag = hashtag;
        this.immagine = immagine;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.speciale = speciale;
        this.stato = "Confermato"; // ** da eliminare
        this.struttura = struttura;
        this.su_prenotazione = su_prenotazione;
        this.tipo_eta = tipo_eta;
        this.tipo_prenotazione = tipo_prenotazione;
        this.username = username;
        this.via = via;
        this.youtube_url = youtube_url;
        this.zona = zona;
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "a_eta=" + a_eta +
                ", selected=" + selected +
                ", id='" + id + '\'' +
                ", titolo='" + titolo + '\'' +
                ", descr='" + descr + '\'' +
                ", data_i='" + data_i + '\'' +
                ", data_f='" + data_f + '\'' +
                ", zona='" + zona + '\'' +
                ", latitudine='" + latitudine + '\'' +
                ", longitudine='" + longitudine + '\'' +
                ", struttura='" + struttura + '\'' +
                ", via='" + via + '\'' +
                ", immagine='" + immagine + '\'' +
                ", external_urls='" + external_urls + '\'' +
                ", youtube_url='" + youtube_url + '\'' +
                ", contatti='" + contatti + '\'' +
                ", hashtag='" + hashtag + '\'' +
                ", datainserimento='" + datainserimento + '\'' +
                ", username='" + username + '\'' +
                ", stato='" + stato + '\'' +
                ", speciale='" + speciale + '\'' +
                ", gratuito=" + gratuito +
                ", su_prenotazione=" + su_prenotazione +
                ", tipo_prenotazione=" + tipo_prenotazione +
                ", da_eta=" + da_eta +
                ", tipo_eta=" + tipo_eta +
                ", costo=" + costo +
                ", categorie=" + categorie.toString() +
                '}';
    }

    public ArrayList<String> getCategorie(){
        return this.categorie;
    }

    public void setCategorie(ArrayList<String> categorie){
        this.categorie = categorie;
    }


    public String getId()
    {
        return id;
    }

    public void setId(String code)
    {
        this.id = id;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)        {
        this.selected = selected;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getYoutube_url() {
        return youtube_url;
    }

    public void setYoutube_url(String youtube_url) {
        this.youtube_url = youtube_url;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getTipo_prenotazione() {
        return tipo_prenotazione;
    }

    public void setTipo_prenotazione(short tipo_prenotazione) {
        this.tipo_prenotazione = tipo_prenotazione;
    }

    public int getTipo_eta() {
        return tipo_eta;
    }

    public void setTipo_eta(short tipo_eta) {
        this.tipo_eta = tipo_eta;
    }

    public int getSu_prenotazione() {
        return su_prenotazione;
    }

    public void setSu_prenotazione(short su_prenotazione) {
        this.su_prenotazione = su_prenotazione;
    }

    public String getStruttura() {
        return struttura;
    }

    public void setStruttura(String struttura) {
        this.struttura = struttura;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getSpeciale() {
        return speciale;
    }

    public void setSpeciale(String speciale) {
        this.speciale = speciale;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public int getGratuito() {
        return gratuito;
    }

    public void setGratuito(short gratuito) {
        this.gratuito = gratuito;
    }

    public String getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(String external_urls) {
        this.external_urls = external_urls;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getDatainserimento() {
        return datainserimento;
    }

    public void setDatainserimento(String datainserimento) {
        this.datainserimento = datainserimento;
    }

    public String getData_i() {
        return data_i;
    }

    public void setData_i(String data_i) {
        this.data_i = data_i;
    }

    public String getData_f() {
        return data_f;
    }

    public void setData_f(String data_f) {
        this.data_f = data_f;
    }

    public int getDa_eta() {
        return da_eta;
    }

    public void setDa_eta(short da_eta) {
        this.da_eta = da_eta;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getContatti() {
        return contatti;
    }

    public void setContatti(String contatti) {
        this.contatti = contatti;
    }

    public int getA_eta() {
        return a_eta;
    }

    public void setA_eta(short a_eta) {
        this.a_eta = a_eta;
    }


    boolean selected = false;
    String id, titolo, descr, data_i, data_f, zona, latitudine, longitudine, struttura, via,
            immagine, external_urls, youtube_url, contatti, hashtag, datainserimento,
            username, stato, speciale;
    int gratuito, su_prenotazione, tipo_prenotazione, da_eta, a_eta, tipo_eta;
    double costo;
    ArrayList<String> categorie;
}

