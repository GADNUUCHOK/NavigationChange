package com.example.work_gadnuuk.navigation;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class Parsing extends Fragment {

    TextView contentView;
    String contentText=null;
    WebView webView;

    /**@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Parsing() {

    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parsing,container,false);
        contentView = (TextView) view.findViewById(R.id.content);
        webView = (WebView) view.findViewById(R.id.webView);

        if(contentText!=null){
            contentView.setText(contentText);
            webView.loadData(contentText, "text/html charset=utf-8", "utf-8");
        }

        Button btnFetch = (Button)view.findViewById(R.id.download);
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentText==null){
                    contentView.setText("Загрузка...");
                    new ProgressTask().execute("http://quotes.zennex.ru/api/v3/bash/quotes?sort=time");
                }

            }
        });
        return view;
    }

    private class ProgressTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path){
            String content;
            try{
                content = getContent(path[0]);
            }
            catch (IOException ex){
                content = ex.getMessage();
            }
            return content;
        }
        @Override
        protected void onPostExecute(String content) {

            contentText=content;
            contentView.setText(content);
            webView.loadData(content, "text/html; charset=utf-8", "utf-8");
            Toast.makeText(getActivity(), "Данные загружены", Toast.LENGTH_SHORT).show();
        }

        private String getContent(String path) throws IOException {
            BufferedReader reader=null;
            try{
                URL url = new URL(path);
                HttpURLConnection c=(HttpURLConnection)url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) !=null) {
                    buf.append(line + "/n");
                }
                return(buf.toString());
            }
            finally {
                if (reader != null) {
                    reader.close();

                }
            }
        }
    }
}
