package fr.formation.matelli.mistra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by hdmytrow on 13/07/2015.
 */
public class CustomListView extends ArrayAdapter<data.Blog> {

    private Context context;
    private List<data.Blog> blogs;

    public CustomListView(Context context, List<data.Blog> blogs) {
        super(context, -1, blogs);
        this.context = context;
        this.blogs = blogs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_blog_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.titre);
        TextView textDescription = (TextView) rowView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconBlog);

        textView.setText(blogs.get(position).getTitle());
        textDescription.setText(Html.fromHtml(blogs.get(position).getDescription()));


        if(!blogs.get(position).getImage().isEmpty()) {
            //ce bitmap sert à "resize" la zone de l'image "picasso" en fonction de l'image par defaut que l'on utilisera pour le device.
            //c-a-d, qu'en fonction de la résolution est densité, les image par defaut sont différente, on resize donc, en fonction de celle qu'on aurait utilisé par defaut.
            Bitmap d = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Log.e("kikoolol-img", "(w: "+d.getWidth()+" | h: "+d.getHeight()+") "+blogs.get(position).getImage());

            Picasso.with(context).load(blogs.get(position).getImage())
                    .resize(d.getWidth(),d.getHeight())
                    .centerInside()
                    .placeholder(R.drawable.app_icon)
                    .into(imageView);
        }



        return rowView;
    }


}

