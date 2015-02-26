package fr.formation.matelli.mistra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import services.UpdateDBService;


public class Home extends Activity {
    private Button btnFormation;
    private Button btnTutos;
    private Button btnBlog;
    private Button btnDevis;
    private Button btnContact;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnFormation = (Button) findViewById(R.id.buttonFormation);
        btnTutos = (Button) findViewById(R.id.buttonTutos);
        btnDevis = (Button) findViewById(R.id.buttonDevis);
        btnBlog = (Button) findViewById(R.id.buttonBlog);
        btnContact = (Button) findViewById(R.id.buttonContact);

        btnFormation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ListFormations.class);
                startActivity(i);
            }
        });

        btnTutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ListTutoriels.class);
                startActivity(i);
            }
        });

        btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(Home.this, Devis.class);
                startActivity(i);
            }
        });

        btnBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Blog.class);
                startActivity(i);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Contact.class);
                startActivity(i);
            }
        });


        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Chargement");
        this.progressDialog.setCancelable(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //on lance
        //this.progressDialog.show();
        //on arrete
        //if (this.progressDialog.isShowing()) {
        //    this.progressDialog.dismiss();
        //}



        startService(new Intent(Home.this, UpdateDBService.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
