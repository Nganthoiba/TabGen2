package com.nganthoi.salai.tabgen;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import connectServer.ConnectServer;
import customDialogManager.CustomDialogManager;


public class CreateOrg extends AppCompatActivity {
    EditText org_name,org_email,comp_name;
    String displ_name=null,organisation_name=null,email=null,company_name=null;
    Button createOrg;
    Context _context=this;
    ConnectServer connectServer=new ConnectServer("http://188.166.210.24:8065/api/v1/organisation/create");
    ProgressDialog progDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_org);

        org_name = (EditText) findViewById(R.id.org_name);
        org_email = (EditText) findViewById(R.id.org_email);
        comp_name = (EditText) findViewById(R.id.company_name);
        createOrg = (Button) findViewById(R.id.createOrg);

        organisation_name = org_name.getText().toString()+"";
        displ_name=organisation_name;
        email = org_email.getText().toString()+"";
        company_name = comp_name.getText().toString()+"";

        createOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                organisation_name = org_name.getText().toString()+"";
                displ_name=organisation_name;
                email = org_email.getText().toString()+"";
                company_name = comp_name.getText().toString()+"";
                System.out.println("Organisation details: ");
                System.out.println("Organisation name: "+organisation_name);
                System.out.println("Email ID: "+email);
                System.out.println("Company Name: "+company_name);
                if(isValidate()){
                    JSONObject jObj = new JSONObject();
                    progDialog = new ProgressDialog(v.getContext());
                    progDialog.setMessage("Wait please...");
                    progDialog.setIndeterminate(true);
                    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    try{
                        jObj.put("display_name",displ_name);
                        jObj.put("name",organisation_name);
                        jObj.put("email",email);
                        jObj.put("company_name",company_name);
                        new CreateOrganisation().execute(jObj);
                    }
                    catch(JSONException e){
                        System.out.println(e.toString());
                    }
                }
            }
        });


    }

    private Boolean isValidate(){
        CustomDialogManager validationDialog;
        if(org_name.getText().toString().trim().length()==0){
            validationDialog = new CustomDialogManager(_context,"Empty fields","Please give an organisation name",false);
            validationDialog.showCustomDialog();
            return false;
        }
        else if(org_email.getText().toString().trim().length()==0){
            validationDialog = new CustomDialogManager(_context,"Empty fields","Please give an email",false);
            validationDialog.showCustomDialog();
            return false;
        }
        else if(comp_name.getText().toString().trim().length()==0){
            validationDialog = new CustomDialogManager(_context,"Empty fields","Please enter your company name",false);
            validationDialog.showCustomDialog();
            return false;
        }
        else return true;
    }

    public class CreateOrganisation extends AsyncTask<JSONObject,Void,String>{
        @Override
        protected void onPreExecute(){
            progDialog.show();
        }

        @Override
        protected String doInBackground(JSONObject... jObj){
            InputStream is = connectServer.putData(jObj[0]);
            return connectServer.convertInputStreamToString(is);
        }

        protected void onProgressUpdate(){
            progDialog.show();
        }

        @Override
        protected void onPostExecute(String jsonString){
            if(jsonString!=null)
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if(connectServer.responseCode==200){
                        String createdOrgDetails = "ID: "+jsonObject.getString("id")+"\n"+
                                "Organisation Name: "+jsonObject.getString("display_name")+"\n"+
                                "Email: "+jsonObject.getString("email")+"\n"+
                                "Company Name: "+jsonObject.getString("company_name");
                        CustomDialogManager cdm = new CustomDialogManager(_context,"Organisation Created","The organization "+
                                "has been created successfully with the following details:\n"+createdOrgDetails,true);
                        cdm.showCustomDialog();
                    }
                    else if(connectServer.responseCode==-1){
                        CustomDialogManager cdmError = new CustomDialogManager(_context,"Failed to create organisation"
                                ,"Unable to contact server",false);
                        cdmError.showCustomDialog();
                    }
                    else {
                        CustomDialogManager cdmError = new CustomDialogManager(_context,"Failed to create organisation","Some error occur.",false);
                        cdmError.showCustomDialog();
                    }
                }catch(JSONException jsonExp){
                    System.out.println(jsonExp.toString());
                }
            progDialog.dismiss();
        }
    }
}


