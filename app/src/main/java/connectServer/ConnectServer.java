package connectServer;

import android.os.StrictMode;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lenovo on 30-Nov-15.
 */
public class ConnectServer {
    //String API_URL="";
    InputStream isr=null;
    URL api_url=null;
    HttpURLConnection conn=null;
    public ConnectServer(String web_api){
        try{
            api_url =new URL(web_api);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public InputStream getData(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            conn = (HttpURLConnection) api_url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            int statusCode = conn.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                isr = new BufferedInputStream(conn.getInputStream());
            }
            else {
                isr = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return isr;
    }

    public InputStream putData(JSONObject parameters){
        OutputStream os;
        OutputStreamWriter osw;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            conn = (HttpURLConnection) api_url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            os = conn.getOutputStream();
            osw = new OutputStreamWriter(os);
            osw.write(parameters.toString());
            osw.flush();
            int statusCode = conn.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK) {
                isr = new BufferedInputStream(conn.getInputStream());
            }
            else{
                isr = null;
            }
            osw.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception occurs here...." + e.toString());
            isr = null;
        }
        return isr;
    }

    public InputStream stringToInputStream(String string){
        InputStream is=null;
        try{
            is = new ByteArrayInputStream(string.getBytes("UTF-8"));
        }catch(UnsupportedEncodingException ex){
            System.out.println("Encoding error "+ex.toString());
        }
        return is;
    }

}
