package com.example.visape.footwho.presentation.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Competition;
import com.example.visape.footwho.domain.controller.CompetitionController;
import com.example.visape.footwho.domain.controller.TeamController;
import com.example.visape.footwho.presentation.adapter.MyCompetitionsAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 24/10/2017.
 */

public class OtherCompetitionsFragment extends Fragment {

    private ListView otherCompetitionsListView;
    private ProgressBar progressBar;
    private TextView errorMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_othercompetitions, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.indeterminateBar);
        otherCompetitionsListView = (ListView) view.findViewById(R.id.other_competitions_list_view);
        errorMessage = (TextView) view.findViewById(R.id.error_other_competitions);

        //Load competitions from the API-REST
        loadCompetitions();
        //Set listener to add competitions to MyCompetitons
        setListenerToListView();

        return view;
    }

    private void setListenerToListView () {
        otherCompetitionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Competition competition = (Competition) parent.getAdapter().getItem(position);

                if (CompetitionController.getInstance().isCompetition(getContext(), competition.getId())) {
                    Toast.makeText(getContext(),competition.getCaption() + " is already in your competitions!", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Do you want to add " + competition.getCaption() + " to your competitions?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CompetitionController.getInstance().storeCompetition(getContext(),competition);
                                    Toast.makeText(getContext(),competition.getCaption() + " was added to your competitions!", Toast.LENGTH_SHORT).show();
                                    //TODO: UPDATE LIST VIEW AFTER ADD THE COMPETITION
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                }

            }
        });
    }

    private void loadCompetitions () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Competition> competitions = new ArrayList<Competition>();

                //show progress bar
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                try {
                    competitions = CompetitionController.getInstance().getAllCompetitions();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    //TODO: SHOW ERROR ON THE UI
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //TODO: SHOW ERROR ON THE UI
                }

                //Show list on the UI
                final ArrayList<Competition> finalCompetitions = competitions;
                //Prevent error
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);

                        //if id of competition 0 is -1 or competitions is empty, error message
                        if (finalCompetitions.isEmpty()) {
                            errorMessage.setText("You have no internet connection...");
                            errorMessage.setVisibility(View.VISIBLE);
                        } else if (finalCompetitions.get(0).getId() == -1) {
                            errorMessage.setText(finalCompetitions.get(0).getLeague());
                            errorMessage.setVisibility(View.VISIBLE);
                        } else {
                            errorMessage.setVisibility(View.GONE);
                            MyCompetitionsAdapter myCompetitionsAdapter = new MyCompetitionsAdapter(getContext(), finalCompetitions);
                            otherCompetitionsListView.setAdapter(myCompetitionsAdapter);
                            //TODO: PUT A FILL STAR (FAV) TO THE COMPETITIONS THAT ARE ALREADY ADDED AND AN EMPTY STAR TO THE OTHERS
                        }
                    }
                });
            }
        }).start();
    }
}
