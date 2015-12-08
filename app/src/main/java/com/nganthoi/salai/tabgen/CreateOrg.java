package com.nganthoi.salai.tabgen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/*import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;*/

public class CreateOrg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_org);
        EditText org_name = (EditText) findViewById(R.id.org_name);
        EditText org_email = (EditText) findViewById(R.id.org_email);
        EditText comp_name = (EditText) findViewById(R.id.comp_name);
        /*
        List <NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("name",org_name.getText().toString()));
        parameters.add(new BasicNameValuePair("email",org_email.getText().toString()));
        parameters.add(new BasicNameValuePair("company_name",comp_name.getText().toString()));
        ConnectServer cs=new ConnectServer("http://dockerhost:8065/api/v1/organisation/create");
        InputStream isr=cs.putData(parameters);*/
    }
}
