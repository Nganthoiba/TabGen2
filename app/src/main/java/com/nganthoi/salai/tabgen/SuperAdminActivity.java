package com.nganthoi.salai.tabgen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import connectServer.ConnectServer;
import customDialogManager.CustomDialogManager;

public class SuperAdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context _context=this;
    ProgressDialog progDialog;
    ConnectServer connectServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_superadmin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "No action is assigned yet for this button", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.super_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.CreateOrganisation){
            startActivity(new Intent(_context,CreateOrg.class));
        }
        if(id == R.id.CreateOrgUnit){
            CustomDialogManager cdm = new CustomDialogManager(_context,"Under Development","This action is under devolopment",true);
            cdm.showCustomDialog();
        }
        if (id == R.id.logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
            alertDialogBuilder.setTitle("Logout ?");
            alertDialogBuilder.setMessage("Are you sure to logout?");
            alertDialogBuilder.setIcon(R.drawable.failure_icon);
            alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(_context,MainActivity.class));
                    finish();
                }
            });
            alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        if(id == R.id.findByOrgName){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
            alertDialogBuilder.setTitle("Find an organisation");
            alertDialogBuilder.setMessage("Please enter your organisation name below:");

            progDialog = new ProgressDialog(_context);
            progDialog.setMessage("Wait please...");
            progDialog.setIndeterminate(true);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            final EditText orgName = new EditText(_context);
            orgName.setPadding(15, 0, 5, 0);
            orgName.setHeight(40);
            alertDialogBuilder.setView(orgName);
            alertDialogBuilder.setPositiveButton("FIND", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String organisationName = orgName.getText().toString();
                    if (organisationName.trim().length() == 0) {
                        Toast.makeText(_context, "You have to enter organisation name.", Toast.LENGTH_LONG).show();
                    } else {
                        connectServer = new ConnectServer("http://188.166.210.24:8065/api/v1/organisation/findByName");
                        JSONObject jobj = new JSONObject();
                        try {
                            jobj.put("name",organisationName);
                            new FindOrg().execute(jobj);
                        } catch (JSONException e) {
                            System.out.println("JSON Exception: "+e.toString());
                        }
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class FindOrg extends AsyncTask<JSONObject,Void,String> {
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
                        CustomDialogManager cdm = new CustomDialogManager(_context,"Organisation Details",createdOrgDetails,true);
                        cdm.showCustomDialog();
                    }
                    else if(connectServer.responseCode==-1){
                        CustomDialogManager cdmError = new CustomDialogManager(_context,"Failed to search organisation"
                                ,"Unable to contact server",false);
                        cdmError.showCustomDialog();
                    }
                    else {
                        CustomDialogManager cdmError = new CustomDialogManager(_context,"Organisation Not Found",jsonObject.getString("message"),false);
                        cdmError.showCustomDialog();
                    }
                }catch(JSONException jsonExp){
                    System.out.println(jsonExp.toString());
                }
            progDialog.dismiss();
        }
    }
}
