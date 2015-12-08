package com.nganthoi.salai.tabgen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectServer.CheckInternetConnection;
import connectServer.ConnectServer;
import customDialogManager.CustomDialogManager;

public class MainActivity extends Activity {
    Button signin, forgotPassword;
    Intent intent;
    Context context;
    String msg, uname, passwd;
    EditText username,password;
    ProgressDialog progressDialog;
    TextView status;
    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        signin = (Button) findViewById(R.id.signIn);
        forgotPassword = (Button) findViewById(R.id.forgotPassword);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);
                if(isValidate()==true){
                    try {
                        uname = username.getText().toString().trim();
                        passwd = password.getText().toString().trim();
                        JSONObject jsonObject= new JSONObject();
                        jsonObject.put("name", "org2");
                        jsonObject.put("email", uname);//username.getText().toString()
                        jsonObject.put("password", passwd);//password.getText().toString()
                        progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setMessage("Wait Please...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        UserLogin ul = new UserLogin();
                        ul.execute(jsonObject);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri forgotPassword = Uri.parse("http://188.166.210.24:8065/org2/reset_password");
                intent = new Intent(Intent.ACTION_VIEW,forgotPassword);
                startActivity(intent);
            }
        });
    }
    public void ForgotPasswd(){

    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public Boolean isValidate(){
        CheckInternetConnection ic = new CheckInternetConnection(this);
        CustomDialogManager cdm;
        Boolean testInternet = ic.isMobileInternetConnected()||ic.isWifiInternetConnected();
        if(username.getText().toString().trim().length()==0){
            msg="Please enter your email ID";
            cdm =  new CustomDialogManager(MainActivity.this,"Email empty",msg,false);
            cdm.showCustomDialog();
            return false;
        }
        else if(!isValidEmail(username.getText().toString())){
            msg = "Your email is invalid, please check.";
            cdm =  new CustomDialogManager(MainActivity.this,"Invalid email",msg,false);
            cdm.showCustomDialog();
            return false;
        }
        else if(password.getText().toString().trim().length()==0){
            msg="Please enter your password";
            cdm =  new CustomDialogManager(MainActivity.this,"Password empty",msg,false);
            cdm.showCustomDialog();
            return false;
        }
        else if(!testInternet){
            msg="You device is not connected to internet. Please check your connection!";
            cdm =  new CustomDialogManager(MainActivity.this,"Internet Connection",msg,false);
            cdm.showCustomDialog();
            return false;
        }
        return true;
    }
    public void parseJSONArray(String json) {
        if(json==null)
            return;
        else{
            JSONObject jObj=null;
            try {
                jObj=new JSONObject(json);
                //JSONArray jsonArray1 = jObj.getJSONArray("notify_props");
                switch(jObj.getString("roles")){
                    case "admin":
                        intent = new Intent(context,Admin.class);
                        Toast.makeText(MainActivity.this,"Sucessfully login as admin...",Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        break;
                    case "superadmin":
                        intent = new Intent(context,SuperAdminActivity.class);
                        Toast.makeText(context,"Sucessfully login as superadmin...",Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        break;
                    case "users":
                        Toast.makeText(context,"Sucessfully login as user...",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(context,"Status Code: "+jObj.getInt("Status_code"),Toast.LENGTH_LONG).show();
                }
            }catch(JSONException e){
                System.out.println("JSON Exception occurs here: " + e.toString()+"\n the JSON is: "+jObj.toString());
            }
        }
    }

    private class UserLogin extends AsyncTask<JSONObject,Void,String>{
        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }

        @Override
        protected String doInBackground(JSONObject... jObj){
            ConnectServer cs = new ConnectServer("http://188.166.210.24:8065/api/v1/users/login");
            String result=null;
            is = cs.putData(jObj[0]);
            if(is!=null){
                try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                    StringBuilder sb = new StringBuilder();
                    String line=null;
                    while((line=reader.readLine())!=null){
                        sb.append(line +"\n");
                    }
                    is.close();
                    //Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_LONG).show();
                    result = sb.toString();
                }catch(Exception e){
                    Toast.makeText(MainActivity.this,"Inappropriate data from server:" + e.toString(),Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(MainActivity.this,"Login failed!",Toast.LENGTH_LONG).show();
            }
            return result;
        }

        protected void onProgressUpdate(){

        }

        @Override
        protected  void onPostExecute(String json){
            if(json!=null)
                parseJSONArray(json);
            progressDialog.dismiss();
        }
    }
    private boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
