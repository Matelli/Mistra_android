package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import data.Formation;
import data.Tutoriel;


public class Devis extends Activity {

    private Activity activity;

    ImageButton btnRetour, btnSend;
    //Button btnFormations, btnTutos,btnDevis, btnContact;
    EditText etObjet, etNom, etNumTel,etEmail, etVille, etSociete, etCommentaire;

    TextView compteur;
    final private int MAX_CAR = 500;
    //final private String TEXT_COMMENT_PREFIXE = getResources().getString(R.string.devis_comment_prefixe);
    //final private String TEXT_COMMENT_SUFFIXE = getResources().getString(R.string.devis_comment_suffixe);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView(R.layout.activity_devis);
        // ImageButton
        btnRetour = (ImageButton) findViewById(R.id.btnRetourHome);
        btnSend = (ImageButton) findViewById(R.id.btnSendDevis);
        // Button
        /*btnFormations = (Button) findViewById(R.id.btnFormationFooterDevis);
        btnTutos = (Button) findViewById(R.id.btnTutosFooterDevis);
        btnDevis  = (Button) findViewById(R.id.btnDevisFooterDevis);
        btnContact = (Button) findViewById(R.id.btnContactFooterDevis);*/
        // EditText
        etObjet = (EditText) findViewById(R.id.editObjet);
        etNom = (EditText) findViewById(R.id.editNom);
        etNumTel = (EditText) findViewById(R.id.editTel);
        etEmail = (EditText) findViewById(R.id.editEmail);
        etVille = (EditText) findViewById(R.id.editVille);
        etSociete = (EditText) findViewById(R.id.editSociete);
        etCommentaire = (EditText) findViewById(R.id.editCommentaire);

        compteur = (TextView) findViewById(R.id.Devis_compteur);


        initialisation();

        etNumTel.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                activerSendBouton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!telephoneValid(etNumTel.getText())) {
                    etNumTel.setError(getString(R.string.devis_error_numTel));
                }
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                activerSendBouton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emailValid(etEmail.getText())) {
                    etEmail.setError(getString(R.string.devis_error_email));
                }
            }
        });

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Devis.this, Home.class);

                /*if (getIntent() != null && getIntent().getExtras() != null) {
                    String c = getIntent().getExtras().getString("whoIam");
                    if (c != null) {
                        if (c.equals(Formation.class.toString())) {
                            i = new Intent(Devis.this, ListFormations.class);
                        } else if (c.equals(Tutoriel.class.toString())) {
                            i = new Intent(Devis.this, ListTutoriels.class);
                        } else if (c.equals(Devis.class.toString())) {
                            i = new Intent(Devis.this, Devis.class);
                        }
                    }
                }*/
                //i.putExtra("whoIam", Devis.class.toString());
                startActivity(i);
                finish();
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendEmail();
                Toast.makeText(Devis.this, getResources().getString(R.string.devis_envoye), Toast.LENGTH_SHORT).show();
            }
        });


        /*btnFormations.setOnClickListener(new View.OnClickListener() {
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
        });*/

        etCommentaire.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int nbTxt = etCommentaire.getText().length();
                final StringBuilder str = new StringBuilder(getResources().getString(R.string.devis_comment_prefixe)).append(" ").append((MAX_CAR - nbTxt)).append(" ").append(getResources().getString(R.string.devis_comment_suffixe));
                compteur.setText(str);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etCommentaire.getText().length() >= MAX_CAR) {
                    Toast.makeText(activity, getResources().getString(R.string.devis_max_caractere), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void sendEmail()
    {
        //final String to = "contact@mistra.fr";
        final String to = getResources().getString(R.string.emailMistra);
        //final String subject = "Demande de devis";
        final String subject = getResources().getString(R.string.devis);
        //final String message = etCommentaire.getText().toString();
        final StringBuilder message = new StringBuilder("Demande de devis via appli Mistra \n\n");

        if(etObjet.getText()!=null && etObjet.getText().length()>0) {
            message.append(getResources().getString(R.string.devis_objet)).append(" ").append(etObjet.getText().toString()).append("\n");
        }
        if(etNom.getText()!=null && etNom.getText().length()>0) {
            message.append(getResources().getString(R.string.devis_nom)).append(" ").append(etNom.getText().toString()).append("\n");
        }

        message.append(getResources().getString(R.string.devis_email)).append(" ").append(etEmail.getText()).append("\n\n");
        message.append(getResources().getString(R.string.devis_telephone)).append(" ").append(etNumTel.getText().toString()).append("\n");

        if(etVille.getText()!=null && etVille.getText().length()>0) {
            message.append(getResources().getString(R.string.devis_ville)).append(" ").append(etVille.getText().toString()).append("\n");
        }
        if(etSociete.getText()!=null && etSociete.getText().length()>0) {
            message.append(getResources().getString(R.string.devis_societe)).append(" ").append(etSociete.getText().toString()).append("\n");
        }

        if (etCommentaire.getText()!=null && etCommentaire.getText().length()>0) {
            message.append(getResources().getString(R.string.devis_commentaire)).append("\n").append(etCommentaire.getText().toString());
        }


        //String toCc = "email de destinataire en CC";
        //String toCci = "email de destinataire en CCi";
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
            Toast.makeText(this, getResources().getString(R.string.devis_aucun_client_mail), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Méthode qui se charge de vérifier si les champs obligatoire (téléphone et email) sont valide !
     */
    private void activerSendBouton() {
        if(telephoneValid(etNumTel.getText()) && emailValid(etEmail.getText())) {
            btnSend.setEnabled(true);
            btnSend.setAlpha(1f);
        } else {
            btnSend.setEnabled(false);
            btnSend.setAlpha(0.2f);
        }
    }

    private boolean telephoneValid(CharSequence tel) {
        if(tel != null && tel.length() >=10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Méthode qui vérifie que le Text passé en parametre est valide pour un Email
     * @param text : l'adresse/texte à vérifier
     * @return True si le text est un email valide !
     */
    private boolean emailValid(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
        }

    }

    private void initialisation() {
        //si l'on vient d'un item "Presentation" depuis "Formation", on a passé le nom de la formation donc on le champs objet avec
        if(getIntent()!=null && getIntent().getExtras()!=null &&  getIntent().getExtras().getString("objetDevis") != null) {
            etObjet.setText(getIntent().getExtras().getString("objetDevis"));
            etNom.requestFocus();//on donne le focus au champs suivant
        }

        btnSend.setEnabled(false);//a la création on le désactive
        btnSend.setAlpha(0.2f);

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
