package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import data.Formation;
import data.Tutoriel;


public class Devis extends Activity {

    private Activity activity;

    ImageButton btnRetour, btnSend;
    Button btnFormations, btnTutos,btnDevis, btnContact;
    EditText etObjet, etNom, etNumTel,etEmail, etVille, etSociete, etCommentaire;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView(R.layout.activity_devis);
        // ImageButton
        btnRetour = (ImageButton) findViewById(R.id.btnRetourHome);
        btnSend = (ImageButton) findViewById(R.id.btnSendDevis);
        // Button
        btnFormations = (Button) findViewById(R.id.btnFormationFooterDevis);
        btnTutos = (Button) findViewById(R.id.btnTutosFooterDevis);
        btnDevis  = (Button) findViewById(R.id.btnDevisFooterDevis);
        btnContact = (Button) findViewById(R.id.btnContactFooterDevis);
        // EditText
        etObjet = (EditText) findViewById(R.id.editObjet);
        etNom = (EditText) findViewById(R.id.editNom);
        etNumTel = (EditText) findViewById(R.id.editTel);
        etEmail = (EditText) findViewById(R.id.editEmail);
        etVille = (EditText) findViewById(R.id.editVille);
        etSociete = (EditText) findViewById(R.id.editSociete);
        etCommentaire = (EditText) findViewById(R.id.editCommentaire);

        initialisation();

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Devis.this, Home.class);

                if(getIntent() != null && getIntent().getExtras() != null) {
                    String c = getIntent().getExtras().getString("whoIam");
                    if (c != null) {
                        if (c.equals(Formation.class.toString())) {
                            i = new Intent(Devis.this, ListFormations.class);
                        } else if (c.equals(Tutoriel.class.toString())) {
                            i = new Intent(Devis.this, ListTutoriels.class);
                        } else if (c.equals(Devis.class.toString())) {
                            i=new Intent(Devis.this, Devis.class);
                        }
                    }
                }
                i.putExtra("whoIam", Devis.class.toString());
                startActivity(i);
                finish();
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Devis.this,"Devis envoyé", Toast.LENGTH_SHORT).show();
            }
        });

        btnFormations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Devis.this, ListFormations.class);
                i.putExtra("whoIam", Devis.class.toString());
                startActivity(i);
                finish();
            }
        });

        btnTutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Devis.this, ListTutoriels.class);
                i.putExtra("whoIam", Devis.class.toString());
                startActivity(i);
                finish();
            }
        });
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Devis.this, Contact.class);
                i.putExtra("whoIam", Devis.class.toString());
                startActivity(i);
                finish();
            }
        });

        etCommentaire.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {  }

            @Override
            public void afterTextChanged(Editable s) {
                if(etCommentaire.getText().length()>=500) {
                    Toast.makeText(activity,"Vous avez atteint le maximum de caractères !",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void initialisation() {

        //si l'on vient d'un item "Presentation" depuis "Formation", on a passé le nom de la formation donc on le champs objet avec
        if(getIntent()!=null && getIntent().getExtras()!=null &&  getIntent().getExtras().getString("objetDevis") != null) {
            etObjet.setText(getIntent().getExtras().getString("objetDevis"));
            etNom.requestFocus();//on donne le focus au champs suivant
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.devis, menu);
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
