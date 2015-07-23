package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.Locale;
import java.util.ResourceBundle;

import data.Formation;
import data.Tutoriel;


public class Contact extends Activity {

    private Context context;

    private ImageButton btnRetour;

    private LinearLayout ll_adresse;
    private LinearLayout ll_numTel;
    private LinearLayout ll_email;



    private ImageButton btnTwitter;
    private ImageButton btnFacebook;
    private ImageButton btnGoogle;
    private ImageButton btnLinkedin;
    private ImageButton btnViadeo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_contact);

        ll_adresse = (LinearLayout) findViewById(R.id.ll_adresse);
        ll_numTel = (LinearLayout) findViewById(R.id.ll_numTel);
        ll_email = (LinearLayout) findViewById(R.id.ll_email);

        btnRetour = (ImageButton) findViewById(R.id.btnRetourHome);
        btnTwitter = (ImageButton) findViewById(R.id.btn_twitter);
        btnFacebook = (ImageButton) findViewById(R.id.btn_facebook);
        btnGoogle = (ImageButton) findViewById(R.id.btn_google);
        btnLinkedin = (ImageButton) findViewById(R.id.btn_linkedin);
        btnViadeo = (ImageButton) findViewById(R.id.btn_viadeo);

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contact.this, Home.class);

                /*if(getIntent() != null && getIntent().getExtras() != null) {
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
                }*/
                startActivity(i);
                finish();
            }
        });

        ll_numTel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean hasPhone = getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
                if(hasPhone) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0182522525"));
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_adresse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    // Create a Uri from an intent string. Use the result to create an Intent.
                    Uri gmmIntentUri = Uri.parse("geo:48.8661697,2.3630310999999438?q=" + Uri.encode("Mistra Formation"));

                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                        finish();
                    } else {
                        //final String adr = new String("19 rue b&eacute;ranger, 75003 paris");
                        final String adr = getResources().getString(R.string.adresseMistra);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.fr/maps/place/" + adr));
                        startActivity(intent);
                        finish();
                    }


                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(context,"Vous n'avez pas les applications nécessaires pour afficher la géolocalisation du lieu.",Toast.LENGTH_LONG).show();
                }
            }
        });

        ll_email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO il faut créer une vue spéciale pour une prise de contact et non pas fait appel au devis. Au pire, appeler la Composer Box
                /*Intent i = new Intent(Contact.this, Devis.class);
                i.putExtra("whoIam", Formation.class.toString());
                startActivity(i);
                finish();*/

                final String to = getResources().getString(R.string.emailMistra);
                final String subject = getResources().getString(R.string.contact_envoie_email);
                //final String message = etCommentaire.getText().toString();
                final StringBuilder message = new StringBuilder("Demande de contact appli Mistra \n\n");

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ toCc});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{toCci});
                //email.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/file.pdf");
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message.toString());
                email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                email.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(email, getResources().getString(R.string.devis_envoie_mail)));
                    //startActivity(email);
                    //
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, getResources().getString(R.string.devis_aucun_client_mail), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnFacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent facebookIntent;
                //TODO la parti du "try" commentée ne peut etre testée via genymotion mais pire, je ne peut pas avoir l'id du profil de mistra pour le moment.
                    /*try {
                        context.getPackageManager().getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
                        facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/254175194653125")); //Trys to make intent with FB's URI
                        startActivity(facebookIntent);
                    } catch (Exception e) {*/
                facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MistraFormation")); //catches and opens a url to the desired page
                startActivity(facebookIntent);
                finish();
                //}

            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mistraformation")); //catches and opens a url to the desired page
                startActivity(intent);
                finish();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+MistraFr/posts")); //catches and opens a url to the desired page
                startActivity(intent);
                finish();
            }
        });

        btnLinkedin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/2248161")); //catches and opens a url to the desired page
                startActivity(intent);
                finish();
            }
        });

        btnViadeo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://fr.viadeo.com/fr/company/mistra")); //catches and opens a url to the desired page
                startActivity(intent);
                finish();
            }
        });
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
