package com.example.visape.footwho.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Competition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Visape on 24/10/2017.
 */

public class MyCompetitionsAdapter extends ArrayAdapter<Competition>{

    private ArrayList<Competition> competitions;
    private Map<String, String> relationLeagueCountry = new HashMap<>();
    private Context context;


    public MyCompetitionsAdapter(Context context, ArrayList<Competition> competitions) {
        super(context, R.layout.list_competitions, competitions);
        this.competitions = competitions;
        this.context = context;

        setRelationLeagueCountry();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater inflater = LayoutInflater.from(context);
        row = inflater.inflate(R.layout.list_competitions, parent, false);

        TextView competitionCaptionTextView = (TextView) row.findViewById(R.id.captionLbl);
        TextView competitionMatchdayTextView = (TextView) row.findViewById(R.id.matchdayLbl);
        TextView competitionCountryTextView = (TextView) row.findViewById(R.id.countryLbl);

        Competition competition = competitions.get(position);

        competitionCaptionTextView.setText(competition.getCaption());
        competitionMatchdayTextView.setText(String.valueOf(": " + competition.getCurrentMatchday()) + '/' + String.valueOf(competition.getNumberOfMatchdays()));

        String country = relationLeagueCountry.get(competition.getLeague());
        if (country.isEmpty()) country = "Unknown country";
        competitionCountryTextView.setText(": " + country);
        return row;
    }

    private void setRelationLeagueCountry () {
        relationLeagueCountry.put("BL1", "Germany");
        relationLeagueCountry.put("BL2", "Germany");
        relationLeagueCountry.put("BL3", "Germany");
        relationLeagueCountry.put("DFB", "Germany");
        relationLeagueCountry.put("PL", "England");
        relationLeagueCountry.put("ELC", "England");
        relationLeagueCountry.put("EL1", "England");
        relationLeagueCountry.put("EL2", "England");
        relationLeagueCountry.put("SA", "Italy");
        relationLeagueCountry.put("SB", "Italy");
        relationLeagueCountry.put("PD", "Spain");
        relationLeagueCountry.put("SD", "Spain");
        relationLeagueCountry.put("CDR", "Spain");
        relationLeagueCountry.put("FL1", "France");
        relationLeagueCountry.put("FL2", "France");
        relationLeagueCountry.put("DED", "Netherlands");
        relationLeagueCountry.put("PPL", "Portugal");
        relationLeagueCountry.put("GSL", "Greece");
        relationLeagueCountry.put("BSA", "Brasil");
        relationLeagueCountry.put("CL", "Europe");
        relationLeagueCountry.put("EL", "Europe");
        relationLeagueCountry.put("EC", "Europe");
        relationLeagueCountry.put("WC", "World-Cup");
        relationLeagueCountry.put("AAL", "Australia");
    }
}
