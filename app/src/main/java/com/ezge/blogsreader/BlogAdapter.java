package com.ezge.blogsreader;

/*In this custom listview adapter class, string arrays are passed into the BlogAdapter and set into the TextViews
    and ImageView followed by the positions. On listview item click will pass the string arrays and position to a new activity.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ezge on 2/21/2018.
 */

public class BlogAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> hash = new HashMap<String, String>();

    public BlogAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView title;
        TextView description;
        ImageView image;
        Spanned htmlAsSpanned;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.activity_blog_item, parent, false);

        hash = data.get(position);

        title = (TextView) itemView.findViewById(R.id.txtTitle);
        description = (TextView) itemView.findViewById(R.id.txtDescription);

        image = (ImageView) itemView.findViewById(R.id.img);

        htmlAsSpanned = Html.fromHtml(hash.get("description"));

        title.setText(hash.get("title"));
        description.setText(htmlAsSpanned);

        imageLoader.DisplayImage(hash.get("image_url"), image);

        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String id;
                String content = null;
                JSONfunctions json;
                JSONObject jsonObject = null;

                hash = data.get(position);

                try{
                    // displays single blog with current id
                    Resources res = context.getResources();
                    id = hash.get( "id" );
                    json = new JSONfunctions( context );
                    content = json.getResponseFromURL(res.getString( R.string.blog_display_url) + id );

                    jsonObject = new JSONObject(content);
                    if (jsonObject != null) {
                        Intent intent = new Intent( context, BlogDisplayActivity.class );
                        intent.putExtra( "description", jsonObject.getString( "content" ) );

                        // Start BlogDisplayActivity Class
                        context.startActivity( intent );
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return itemView;
    }
}
