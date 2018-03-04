package com.ezge.blogsreader;

// BlogListActivity class displays list of blogs asynchronously

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BlogListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_blog_list );
        // Execute AsyncTask
        new DownloadJSON( getBaseContext() ).execute( getString( R.string.blogs_url ) );

    }


    private class DownloadJSON extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
        private final Context context;

        public DownloadJSON(Context c) {
            this.context = c;
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            ArrayList<HashMap<String, String>> arraylist = null;
            JSONObject jsonobject;
            JSONArray jsonarray;
            JSONfunctions json;
            String content = null;

            try {
                json = new JSONfunctions( context );
                content = json.getResponseFromURL( params[0] );

                if (content != null) {
                    jsonarray = new JSONArray( content );
                    arraylist = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        jsonobject = jsonarray.getJSONObject( i );
                        // Retrive JSON Objects
                        map.put( "id", jsonobject.getString( "id" ) );
                        map.put( "title", jsonobject.getString( "title" ) );
                        map.put( "description", jsonobject.getString( "description" ) );
                        map.put( "image_url", jsonobject.getString( "image_url" ) );

                        arraylist.add( map );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arraylist;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            ListView list;
            BlogAdapter adapter;

            super.onPostExecute( result );

            list = (ListView) findViewById( R.id.blogList );
            adapter = new BlogAdapter( BlogListActivity.this, result );
            list.setAdapter( adapter );
        }
    }
}


