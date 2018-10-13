package it.chesifaoggi.app.chesifaoggi;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PicDownloader {
    static String filepath = Environment.getExternalStorageDirectory().toString() + "/it.chesifaoggi.app/Downloads/";

    public static boolean deleteFile(String filename){
        File f = new File(filepath+filename);
        return f.delete();
    }

    public static boolean deleteAllFiles(){
        try{
            File f = new File(filepath);
            if (f.isDirectory()) {
                String[] children = f.list();
                for (int i = 0; i < children.length; i++) {
                    new File(f, children[i]).delete();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void checkDir(){
        boolean success = false;
        File folder = new File(filepath);
        if(!folder.exists()){
            success = folder.mkdirs();
        }
        if (!success){
            Log.e("checkDir","Folder not created.");
        } else{
            Log.i("checkDir","Folder created!");
        }
    }

    public static boolean fileExists(String filename){
        File f = new File(filepath+filename);
        Log.i("fileExists:", filepath+filename);
        return f.exists();
    }

    public static File getFile(String filename){
        checkDir();
        Log.i("PicDownloader", "Apro: "+filepath+filename);
        File f = new File(filepath+filename);
        if(!f.exists()) Log.e("PicDownloader: ", "non esiste: "+filepath+filename);
        return f;
    }

    public static boolean isExternalStorageWritable() { /* Checks if external storage is available for read and write */
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() { /* Checks if external storage is available to at least read */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public static File getImage(String imageUrl, String fileName) {
        File file = null;
        if (!PicDownloader.isExternalStorageWritable()) {
            Log.e("isExternalStorageWritable: ", "false");
            return null;
        }

        if (!PicDownloader.isExternalStorageReadable()) {
            Log.e("isExternalStorageReadable: ", "false");
            return null;
        }


        try {
            HttpURLConnection c = null;
            URL u = new URL(imageUrl);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(10000);
            c.setReadTimeout(10000);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    Log.d("INFORMATION..", "FILE CONNECTED....");
                    //set the path where we want to save the file
                    //in this case, going to save it on the root directory of the
                    //sd card.
                    File SDCardRoot = new File( filepath );
                    SDCardRoot.mkdirs();
                    Log.i("SDCardRoot free space:", SDCardRoot.getFreeSpace()+"" );

                    //create a new file, specifying the path, and the filename
                    //which we want to save the file as.
                    file = new File(SDCardRoot, fileName);

                    //this will be used to write the downloaded data into the file we created
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    Log.d("INFORMATION..", "WRINTING TO FILE DOWNLOADED...." + file);
                    //this will be used in reading the data from the internet
                    InputStream inputStream = c.getInputStream();

                    //this is the total size of the file
                    int totalSize = c.getContentLength();
                    //variable to store total downloaded bytes
                    int downloadedSize = 0;

                    //create a buffer...
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0; //used to store a temporary size of the buffer

                    //now, read through the input buffer and write the contents to the file
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        //add the data in the buffer to the file in the file output stream (the file on the sd card
                        fileOutput.write(buffer, 0, bufferLength);
                        //add up the size so we know how much is downloaded
                        downloadedSize += bufferLength;
                        Log.d("INFORMATION..", "FILE DOWNLOADED....");
                        //this is where you would do something to report the prgress, like this maybe
                        //updateProgress(downloadedSize, totalSize);

                    }
                    //close the output stream when done
                    fileOutput.close();
                    Log.d("INFORMATION..", "FILE DOWNLOADING COMPLETED....");
                    //catch some possible errors...
                    return file;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            Log.i("PicDownloader", e.toString());
        }
        return null;
    }
}