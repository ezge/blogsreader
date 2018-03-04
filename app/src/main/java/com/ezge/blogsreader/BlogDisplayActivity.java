package com.ezge.blogsreader;

// BlogDisplayActivity: strings are retrieved from the BlogAdapter by using Intent and sets into the TextViews
// and an image URL into ImageLoader class to load images into the ImageView

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;


public class BlogDisplayActivity extends AppCompatActivity {

    String description;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_blog_display_item);

        Intent i = getIntent();
        description = i.getStringExtra( "description" );

        //WebView
        webView = (WebView) findViewById( R.id.webContent );
        webView.getSettings().setJavaScriptEnabled( true );

        webView.loadDataWithBaseURL("blarg://ignored", description, "text/html", "utf-8", "");
    }
}
