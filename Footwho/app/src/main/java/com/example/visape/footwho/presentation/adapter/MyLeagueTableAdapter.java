package com.example.visape.footwho.presentation.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.LeagueTeam;

import java.util.ArrayList;

/**
 * Created by Visape on 21/11/2017.
 */

public class MyLeagueTableAdapter extends ArrayAdapter<LeagueTeam> {
    private Context context;
    ArrayList<LeagueTeam> leagueTable;

    public MyLeagueTableAdapter(Context context, ArrayList<LeagueTeam> leagueTable) {
        super(context, R.layout.list_league_table, leagueTable);
        this.context = context;
        this.leagueTable = leagueTable;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.list_league_table, parent, false);

        TextView rank = (TextView) row.findViewById(R.id.rank);
        TextView team = (TextView) row.findViewById(R.id.team);
        TextView playedGames = (TextView) row.findViewById(R.id.playedGames);
        TextView points = (TextView) row.findViewById(R.id.points);
        TextView goals = (TextView) row.findViewById(R.id.goals);
        TextView goalsAgainst = (TextView) row.findViewById(R.id.goalsAgainst);
        TextView goalsDifference = (TextView) row.findViewById(R.id.goalsDifference);

        LeagueTeam leagueTeam = leagueTable.get(position);

        rank.setText(String.valueOf(leagueTeam.getRank()));
        team.setText(leagueTeam.getTeam());
        playedGames.setText(String.valueOf(leagueTeam.getPlayedGames()));
        points.setText(String.valueOf(leagueTeam.getPoints()));
        goals.setText(String.valueOf(leagueTeam.getGoals()));
        goalsAgainst.setText(String.valueOf(leagueTeam.getGoalsAgainst()));
        goalsDifference.setText(String.valueOf(leagueTeam.getGoalsDifference()));

        return row;
    }
}
