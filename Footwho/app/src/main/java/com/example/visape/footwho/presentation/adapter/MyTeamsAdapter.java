package com.example.visape.footwho.presentation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Team;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by Visape on 16/10/2017.
 */

public class MyTeamsAdapter extends ArrayAdapter<Team> {

    private ArrayList<Team> teams;
    private Context context;
    private ImageView imageView;
    private HashMap<Team, Drawable> images;

    public MyTeamsAdapter(Context context, ArrayList<Team> teams) {
        super(context,R.layout.list_teams,teams);
        this.teams = teams;
        this.context = context;
        this.images = new HashMap<>();

        for(int i = 0; i < teams.size(); ++i) {
            HttpImageRequestTask httpImageRequestTask = new HttpImageRequestTask();
            httpImageRequestTask.execute(teams.get(i).getCrestUrl());

            while (httpImageRequestTask.getStatus() != AsyncTask.Status.FINISHED);

            try {
                images.put(teams.get(i), httpImageRequestTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                //TODO: HANDLE ERROR
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        LayoutInflater inflater = LayoutInflater.from(context);
        row = inflater.inflate(R.layout.list_teams, parent, false);

        TextView teamNameTextView = (TextView) row.findViewById(R.id.teamNameLbl);
        teamNameTextView.setText(teams.get(position).getName());

        imageView = (ImageView) row.findViewById(R.id.teamImageView);
        imageView.setImageDrawable(images.get(teams.get(position)));

        return row;
    }


    //CLASS TO GET THE PICTURE FROM A URL
    private class HttpImageRequestTask extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... params) {
            try {
                String urlString = params[0];

                //if it have no url, show error image
                if(urlString == null) {
                    return ContextCompat.getDrawable(context, R.drawable.ic_image_not_available);
                }
                //Change Http to https
                if (urlString.startsWith("http:")) {
                    urlString = urlString.replace("http:", "https:");
                }

                URL url = new URL(urlString);
                InputStream inputStream = url.openConnection().getInputStream();

                if (urlString.endsWith("svg")) {
                    return SVGParser.getSVGFromInputStream(inputStream).createPictureDrawable();
                } else if(urlString.endsWith("png"))  {
                    return Drawable.createFromStream(inputStream, "src");
                }
            } catch (Exception e) {
                Log.e("DEBUG", e.getMessage(), e);
                //TODO: HANDLE ERROR
            }
            return ContextCompat.getDrawable(context, R.drawable.ic_image_not_available);
        }
    }
}
