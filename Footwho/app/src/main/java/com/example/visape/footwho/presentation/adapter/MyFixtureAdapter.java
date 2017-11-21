package com.example.visape.footwho.presentation.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Fixture;

import java.util.ArrayList;

/**
 * Created by Visape on 20/11/2017.
 */

public class MyFixtureAdapter extends ArrayAdapter<Fixture> {

    private Context context;
    ArrayList<Fixture> fixtures;


    public MyFixtureAdapter(Context context, ArrayList<Fixture> fixtures) {
        super(context, R.layout.list_fixtures, fixtures);
        this.context = context;
        this.fixtures = fixtures;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.list_fixtures, parent, false);

        TextView localTeam = (TextView) row.findViewById(R.id.localTeam);
        TextView goalsLocalTeam = (TextView) row.findViewById(R.id.goalsLocalTeam);
        TextView goalsAwayTeam = (TextView) row.findViewById(R.id.goalsAwayTeam);
        TextView awayTeam = (TextView) row.findViewById(R.id.awayTeam);

        Fixture fixture = fixtures.get(position);

        localTeam.setText(fixture.getHomeTeamName());
        if (fixture.getGoalsHomeTeam() != null ) {
            goalsLocalTeam.setText(String.format(" %s", fixture.getGoalsHomeTeam()));
        } else {
            goalsLocalTeam.setText(" ");
        }
        if (fixture.getGoalsAwayTeam() != null ) {
            goalsAwayTeam.setText(String.format("%s ", fixture.getGoalsAwayTeam()));
        } else {
            goalsAwayTeam.setText(" ");
        }
        awayTeam.setText(fixture.getAwayTeamName());

        return row;
    }
}
