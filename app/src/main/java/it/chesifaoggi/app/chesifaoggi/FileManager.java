package it.chesifaoggi.app.chesifaoggi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by gaia on 01/05/16.
 */
public class FileManager { // Non è stato ancora inserito da nessuna parte. Dovrebbe andare in EventoActivity e EventiActivity
    public FileManager(String fileName){
        this.fileName = fileName;
    }


    public void setInternalPath(Context c){
        setPath(c.getFilesDir().toString());
    }

    public void setExternalPath(){
        String filepath = Environment.getExternalStorageDirectory().toString() + "/it.chesifaoggi.app/Downloads/";

        setPath(filepath);
    }

    public void setPath(String path){
        filepath=path;
    }

    public String getPath(){
        return filepath;
    }

    public File startDownload(String url){

        return null;
    }

    public File getFile(String fileName){
        File f = new File(filepath+fileName);
        return f;
    }

    public Bitmap getImage(String fileName){
        File f = getFile(fileName);
        if(f==null)
          return BitmapFactory.decodeFile(
                //PicDownloader.getImage("http://findicons.com/files/icons/806/happy_valentines_day_2/80/broken_heart.png", id + ".png").toString()
                PicDownloader.getImage("http://www.chesifaoggi.it/ud/e-insert/1412378121.jpg", fileName + ".png").toString()
          );
        return new BitmapFactory().decodeFile(f.getAbsolutePath());
    }

    public void deleteFile(String filename){
        File f = new File(filepath+fileName);
        f.delete();
    }

    public boolean fileExists(String filename){
        File f = new File(filepath+filename);
        Log.i("fileExists:", filepath + filename);
        return f.exists();
    }

    String fileName;
    String filepath = Environment.getExternalStorageDirectory().toString() + "/it.chesifaoggi.app/Downloads/";
}
