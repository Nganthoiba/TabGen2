package connectServer;

import android.os.StrictMode;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            String message = conn.getResponseMessage();
            System.out.println("Response Code: "+statusCode+"\nResponse message: "+message);
            if(statusCode == 200/*HttpURLConnection.HTTP_OK*/){
                isr = new BufferedInputStream(conn.getInputStream());
            }
            else {
                isr = null;
                System.out.println("Response Code: "+statusCode+"\n");
                convertInputStreamToString(new BufferedInputStream(conn.getErrorStream()));
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
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            os = conn.getOutputStream();
            osw = new OutputStreamWriter(os);
            osw.write(parameters.toString());
            osw.flush();
            int statusCode = conn.getResponseCode();
            String message = conn.getResponseMessage();
            System.out.println("Response Code: "+statusCode+"\nResponse message: "+message);
            if(statusCode == 200) {
                isr = new BufferedInputStream(conn.getInputStream());
            }
            else{
                isr = null;
                System.out.println("Input Stream has been set to null..");
                convertInputStreamToString(new BufferedInputStream(conn.getErrorStream()));
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

    public void convertInputStreamToString(InputStream inputStream){
        String result=null;
        if(inputStream!=null){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line=null;
                while((line=reader.readLine())!=null){
                    sb.append(line +"\n");
                }
                inputStream.close();
                result = sb.toString();
                System.out.println("Login Failed JSON String: "+result);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("We have found an exception: \n"+e.toString());
            }
        }
    }

}
