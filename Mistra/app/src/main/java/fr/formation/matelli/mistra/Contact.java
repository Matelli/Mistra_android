package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.text.Normalizer;

import data.Formation;
import data.Tutoriel;


public class Contact extends Activity {

    ImageButton btnRetour;

    ImageButton btnTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        btnRetour = (ImageButton) findViewById(R.id.btnRetourHome);
        btnTwitter = (ImageButton) findViewById(R.id.btn_twitter);

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contact.this, Home.class);

                if(getIntent() != null && getIntent().getExtras() != null) {
                    String c = getIntent().getExtras().getString("whoIam");
                    if (c != null) {
                        if (c.equals(Formation.class.toString())) {
                            i = new Intent(Contact.this, ListFormations.class);
                        } else if (c.equals(Tutoriel.class.toString())) {
                            i = new Intent(Contact.this, ListTutoriels.class);
                        } else if (c.equals(Devis.class.toString())) {
                            i=new Intent(Contact.this, Devis.class);
                        }
                    }
                }
                startActivity(i);
                finish();
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Contact.this,"kikoo",Toast.LENGTH_LONG).show();
                shareIt();
            }
        });
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Partager avec"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);

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
