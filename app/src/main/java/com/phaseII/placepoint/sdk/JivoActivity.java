package com.phaseII.placepoint.sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import com.phaseII.placepoint.R;

import java.util.Locale;

//**********
public class JivoActivity extends AppCompatActivity implements JivoDelegate{

    //**************
    JivoSdk jivoSdk;
    Toolbar toolbar;
    TextView mTitle;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jivo);
        setToolBar();
        String lang = Locale.getDefault().getLanguage().indexOf("ru") >= 0 ? "ru": "en";

        //*********************************************************
        jivoSdk = new JivoSdk((WebView) findViewById(R.id.webview), lang);
        jivoSdk.delegate = this;
        jivoSdk.prepare();
    }

    private void setToolBar() {

        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        back = toolbar.findViewById(R.id.back) ;

        mTitle.setText("Chat Support");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true) ;
    }

    //*********************************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEvent(String name, String data) {
        if(name.equals("url.click")){
            if(data.length() > 2){
                String url = data.substring(1, data.length() - 1);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        }
    }


}
