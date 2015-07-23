package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import dao.DBHandlerBlog;
import dao.DBHandlerTutoriel;
import data.Formation;
import data.Tutoriel;


public class Blog extends Activity {
    Context context;

    ListAdapter listAdapter;
    ImageButton btnRetourHome;
    ListView listView;
    RelativeLayout noArticles;

    DBHandlerBlog db;
    List<data.Blog> listOfBlogs = new ArrayList<data.Blog>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_blog);

        btnRetourHome = (ImageButton) findViewById(R.id.btnRetourHome);
        listView = (ListView) findViewById(R.id.expandablelisteBlog);
        noArticles = (RelativeLayout) findViewById(R.id.no_blogs);


        this.btnRetourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Blog.this, Home.class);

                /*if(getIntent() != null && getIntent().getExtras() != null) {
                    String c = getIntent().getExtras().getString("whoIam");
                    if (c != null) {
                        if (c.equals(Formation.class.toString())) {
                            i = new Intent(Blog.this, ListFormations.class);
                        } else if (c.equals(Tutoriel.class.toString())) {
                            i = new Intent(Blog.this, ListTutoriels.class);
                        } else if (c.equals(Devis.class.toString())) {
                            i=new Intent(Blog.this, Devis.class);
                        } else if (c.equals(SubListView.class.toString())) {
                            i = new Intent(Blog.this, SubListView.class);
                        }
                    }
                }*/

                //i.putExtra("whoIam", Blog.class.toString());
                startActivity(i);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final data.Blog item = (data.Blog) parent.getItemAtPosition(position);

                    data.Blog presentation = (data.Blog) item;

                    Intent i = new Intent(Blog.this, DetailSelection.class);
                    //i.putExtra("ListeFormation","detailTitre"+ article.getTitle());
                    //i.putExtra("ListeFormation","htmlcode"+ article.getTitle());
                    i.putExtra("detailTitre", presentation.getTitle());
                    i.putExtra("htmlcode", presentation.getContent());
                    i.putExtra("whoIwas", Blog.class.toString());
                    startActivity(i);
                    finish();


        }});

        remplirListData();

    }

    private void remplirListData() {
        // Creation d'un objet db
        this.db = new DBHandlerBlog(this.context);
        //r�cup�ration des infos depuis la DB
        this.listOfBlogs.addAll(this.db.getAll());

        if(this.listOfBlogs != null && this.listOfBlogs.size()>0) {
            noArticles.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            listAdapter = new CustomListView(Blog.this, listOfBlogs);
            listView.setAdapter(listAdapter);
        } else {
            //nous n'avons pas d'articles, donc, on affiche un message d'erreur
            noArticles.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }



        this.db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blog, menu);
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
