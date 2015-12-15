package com.nganthoi.salai.tabgen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connectServer.CheckInternetConnection;
import connectServer.ConnectServer;
import customDialogManager.CustomDialogManager;

public class MainActivity extends Activity {
    Button signin;
    Intent intent;
    Context context=this;
    String msg, org,uname, passwd;
    EditText org_name,username,password;
    ProgressDialog progressDialog;
    TextView forgotPassword;
    InputStream is;
    ConnectServer cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = (Button) findViewById(R.id.signIn);
        org_name = (EditText) findViewById(R.id.organisation_name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        //forgotPassword.setPaintFlags(forgotPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        forgotPassword.setText(Html.fromHtml("<u><i>Forgot Password ?</i></u>"));
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidate()==true){
                    try {
                        uname = username.getText().toString().trim();
                        passwd = password.getText().toString().trim();
                        org = org_name.getText().toString().trim();
                        JSONObject jsonObject= new JSONObject();
                        jsonObject.put("name", org);
                        jsonObject.put("email", uname);//username.getText().toString()
                        jsonObject.put("password", passwd);//password.getText().toString()
                        progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setMessage("  Wait Please.....");
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
                intent = new Intent(Intent.ACTION_VIEW, forgotPassword);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public Boolean isValidate(){
        CheckInternetConnection ic = new CheckInternetConnection(this);
        CustomDialogManager cdm;
        Boolean testInternet = ic.isMobileInternetConnected()||ic.isWifiInternetConnected();
        if(org_name.getText().toString().trim().length()==0){
            msg="Please enter your organisation name";
            cdm =  new CustomDialogManager(MainActivity.this,"Organisation name empty",msg,false);
            cdm.showCustomDialog();
            return false;
        }
        else if(username.getText().toString().trim().length()==0){
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
        /*
        else if(!testInternet){
            msg="You device is not connected to internet. Please check your connection!";
            cdm =  new CustomDialogManager(MainActivity.this,"Internet Connection",msg,false);
            cdm.showCustomDialog();
            return false;
        }*/
        return true;
    }

    public class UserLogin extends AsyncTask<JSONObject,Void,String>{
        @Override
        protected void onPreExecute(){
            progressDialog.show();
        }

        @Override
        protected String doInBackground(JSONObject... jObj){
            cs = new ConnectServer("http://188.166.210.24:8065/api/v1/users/login");
            String result=null;
            is = cs.putData(jObj[0]);
            result = cs.convertInputStreamToString(is);
            return result;
        }

        protected void onProgressUpdate(){
            progressDialog.show();
        }

        @Override
        protected  void onPostExecute(String json){
            if(json!=null){
                JSONObject jObj=null;
                try {
                    jObj=new JSONObject(json);
                    if(cs.responseCode==200){
                        progressDialog.dismiss();
                        switch(jObj.getString("roles")){
                            case "system_admin":
                                intent = new Intent(context,SuperAdminActivity.class);
                                Toast.makeText(MainActivity.this, "Sucessfully login as Superadmin...", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                break;
                            case "admin":
                                intent = new Intent(context,Admin.class);
                                Toast.makeText(context,"Sucessfully login as Admin...",Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                break;
                            case "users":
                                Toast.makeText(context,"Sucessfully login as user...",Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(context,"Status Code: "+jObj.getInt("Status_code"),Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        progressDialog.dismiss();
                        CustomDialogManager error = new CustomDialogManager(context,"Login Failed",jObj.getString("message"),false);
                    }
                }catch(JSONException e){
                    System.out.println("JSON Exception occurs here: " + e.toString()+"\n the JSON is: "+jObj.toString());
                }
            }
            else
            {
                CustomDialogManager error = new CustomDialogManager(context,"Server Problem","Failed to connect server",false);
                error.showCustomDialog();

            }
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
