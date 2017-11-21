package com.example.visape.footwho.presentation.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Competition;
import com.example.visape.footwho.domain.classes.Team;
import com.example.visape.footwho.domain.controller.CompetitionController;
import com.example.visape.footwho.domain.controller.TeamController;
import com.example.visape.footwho.presentation.adapter.MyCompetitionsAdapter;
import com.example.visape.footwho.presentation.adapter.MyCompetitionsTeamsAdapter;
import com.example.visape.footwho.presentation.adapter.MyTeamsAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Visape on 24/10/2017.
 */

public class OtherTeamsFragment extends Fragment {

    private ExpandableListView competitionsList;
    private MyCompetitionsTeamsAdapter competitionsAdapter;
    private ProgressBar progressBar;
    private TextView errorMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otherteams, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.indeterminateBar);
        competitionsList = (ExpandableListView) view.findViewById(R.id.competitions_teams_list_view);
        errorMessage = (TextView) view.findViewById(R.id.error_other_teams);

        loadCompetitions();
        setListenerToListView();

        return view;
    }

    private void loadCompetitions () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Competition> competitions = new ArrayList<Competition>();
                //SHOW PROGRESS BAR
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

                final ArrayList<Competition> finalCompetitions = competitions;
                //Prevent error
                if(getActivity() == null)
                    return;
                //Hide progress bar and show information
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
                            competitionsAdapter = new MyCompetitionsTeamsAdapter(getContext(), finalCompetitions);
                            competitionsList.setAdapter(competitionsAdapter);
                        }
                    }
                });
            }
        }).start();
    }

    private void setListenerToListView () {
        //OnGroupClick request the API for the teams of the competition clicked
        competitionsList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(final ExpandableListView parent, View v, final int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    final Competition competition = (Competition) parent.getExpandableListAdapter().getGroup(groupPosition);
                    if (competitionsAdapter.getTeams(competition).size() == 0) {
                        //Show progress dialog
                        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Loading...", "Loading, wait please", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Team> teamsOfCompetition = new ArrayList<>();

                                try {
                                    teamsOfCompetition = TeamController.getInstance().getTeamsOfCompetition(competition.getId(), competition.getLeague());
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                    //TODO: ERROR CONTROL
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    //TODO: CONTROL MESSAGE
                                }

                                competitionsAdapter.addTeams(competition, teamsOfCompetition);

                                final ArrayList<Team> finalTeamsOfCompetition = teamsOfCompetition;

                                if (getActivity() == null)
                                    return;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //if id of team 0 is -1 or tems is empty, error message
                                        if (finalTeamsOfCompetition.isEmpty()) {
                                            errorMessage.setText("You have no internet connection...");
                                            errorMessage.setVisibility(View.VISIBLE);
                                        } else if (finalTeamsOfCompetition.get(0).getId() == -1) {
                                            errorMessage.setText(finalTeamsOfCompetition.get(0).getName());
                                            errorMessage.setVisibility(View.VISIBLE);
                                            competitionsList.setVisibility(View.GONE);
                                        } else {
                                            competitionsList.setVisibility(View.VISIBLE);
                                            parent.expandGroup(groupPosition);
                                            //TODO: PUT A FILL STAR (FAV) TO THE TEAMS THAT ARE ALREADY ADDED AND AN EMPTY STAR TO THE OTHERS
                                        }
                                        //Hide progress bar
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }).start();
                    } else {
                        parent.expandGroup(groupPosition);
                    }
                }

                return true;
            }
        });

        //OnChildClick add team clicked to my teams
        competitionsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final Team team = (Team) parent.getExpandableListAdapter().getChild(groupPosition,childPosition);

                if (TeamController.getInstance().isTeam(getContext(), team.getId())) {
                    Toast.makeText(getContext(),team.getName() + " is already in your teams!", Toast.LENGTH_SHORT).show();
                    return true;
                }

                new AlertDialog.Builder(getContext())
                        .setMessage("Do you want to add " + team.getName() + " to your teams?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TeamController.getInstance().storeTeam(getContext(), team);

                                Toast.makeText(getContext(),team.getName() + " was added to your teams!", Toast.LENGTH_SHORT).show();
                                //TODO: UPDATE LIST VIEW AFTER ADD THE TEAM, PUT STAR IN THE TEAM
                            }
                        }).setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });
    }
}
