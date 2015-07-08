package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import data.Formation;
import data.Tutoriel;


public class DetailSelection extends Activity {

    ImageButton btnRetourPrevious, btnShare;
    TextView detailTitre;
    Button btnContact, btnDevis;
    WebView pageView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_selection);
        // ImageButon
        btnRetourPrevious = (ImageButton) findViewById(R.id.btnRetourPrevious);
        btnShare  = (ImageButton) findViewById(R.id.btnShare);
        // TextView
        detailTitre = (TextView) findViewById(R.id.detailTitre);
        // Button
        btnContact = (Button) findViewById(R.id.btnGoContact);
        btnDevis = (Button) findViewById(R.id.btnGoDevis);
        // WebView
        pageView = (WebView) findViewById(R.id.webView );


        String titre = new String("Formations");
        if(getIntent() != null && getIntent().getExtras()!=null && getIntent().getExtras().getString("detailTitre")!=null)
            titre = getIntent().getExtras().getString("detailTitre");
        detailTitre.setText(titre);
        // Listeners
        pageView.getSettings().setJavaScriptEnabled(true);

        pageView.getSettings().setLoadWithOverviewMode(true);
        pageView.getSettings().setUseWideViewPort(true);
        pageView.getSettings().setBuiltInZoomControls(true);

        // Get list of Items
        String htmlcode = getIntent().getExtras().getString("htmlcode");
//        pageView.loadUrl("https://www.mistra.fr/formations-initiations/formation-conception-objet.html");
        pageView.loadDataWithBaseURL("https://www.mistra.fr/", htmlcode, "text/html", "utf-8", null);
        if (Build.VERSION.SDK_INT >= 11) {
            pageView.setBackgroundColor(0x01000000);
        } else {
            pageView.setBackgroundColor(0x00000000);
        }

        btnRetourPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailSelection.this, Home.class);//par defaut

                String c=getIntent().getExtras().getString("whoIwas");
                if(c!=null) {
                    if (c.equals(Formation.class.toString())) {
                        i = new Intent(DetailSelection.this, ListFormations.class);
                    } else if (c.equals(Tutoriel.class.toString())) {
                        //TODO faire un truc ici pour savoir si on back a la home ou a la sublist
                        i = new Intent(DetailSelection.this, ListTutoriels.class);
                    } else if (c.equals(Devis.class.toString())) {
                        i = new Intent(DetailSelection.this, Devis.class);
                     } else if (c.equals(SubListView.class.toString())) {
                        //TODO bug, la sublistview n'a pas les différentes catégorie du Tuto. A corriger ! pour la peine, on renvoit à la home
                        // i = new Intent(DetailSelection.this, SubListView.class);
                        i = new Intent(DetailSelection.this, Home.class);
                    }
                }
                i.putExtra("whoIam", SubListView.class.toString());
                startActivity(i);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailSelection.this, Contact.class);
                i.putExtra("whoIam", SubListView.class.toString());
                startActivity(i);
            }
        });

        btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailSelection.this, Devis.class);
                i.putExtra("objetDevis",detailTitre.getText());
                i.putExtra("whoIam", SubListView.class.toString());
                startActivity(i);
            }
        });

        //


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_selection, menu);
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
