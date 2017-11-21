package com.example.visape.footwho.presentation.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Competition;
import com.example.visape.footwho.domain.classes.Team;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 16/11/2017.
 */

public class MyCompetitionsTeamsAdapter extends BaseExpandableListAdapter{

    private Context context;
    private ArrayList<Competition> competitions;
    private HashMap<Integer, ArrayList<Team>> teams;
    private HashMap<Team, Drawable> images;

    public MyCompetitionsTeamsAdapter (Context context, ArrayList<Competition> competitions) {
        this.competitions = competitions;
        this.context = context;
        teams = new HashMap<>();
        for (int i = 0; i < competitions.size(); ++i) {
            teams.put(competitions.get(i).getId(), new ArrayList<Team>());
        }
        images = new HashMap<>();
    }

    public void addTeams(Competition competition, ArrayList<Team> teamsOfCompetition) {
        teams.put(competition.getId(), teamsOfCompetition);

        for(int i = 0; i < teamsOfCompetition.size(); ++i) {
            HttpImageRequestTask httpImageRequestTask = new HttpImageRequestTask();
            httpImageRequestTask.execute(teamsOfCompetition.get(i).getCrestUrl());

            while (httpImageRequestTask.getStatus() != AsyncTask.Status.FINISHED);

            try {
                images.put(teamsOfCompetition.get(i), httpImageRequestTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                //TODO: HANDLE ERROR
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Team> getTeams(Competition competition) {
        return teams.get(competition.getId());
    }

    @Override
    public int getGroupCount() {
        return competitions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return teams.get(competitions.get(groupPosition).getId()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return competitions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return teams.get(competitions.get(groupPosition).getId()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Competition header = (Competition) getGroup(groupPosition);

        String competitionCaption = header.getCaption();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_competition_teams, null);
        }

        TextView competitionNameTextView = (TextView) convertView.findViewById(R.id.captionTeamsLbl);
        competitionNameTextView.setText(competitionCaption);

        ImageView arrowImage = (ImageView) convertView.findViewById(R.id.ic_arrow);
        if (isExpanded) {
            arrowImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_expanded));
        } else {
            arrowImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_not_expanded));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Team team = (Team) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_teams, null);
        }

        TextView teamName = (TextView) convertView.findViewById(R.id.teamNameLbl);
        ImageView teamImage = (ImageView) convertView.findViewById(R.id.teamImageView);

        teamName.setText(team.getName());
        teamImage.setImageDrawable(images.get(team));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
