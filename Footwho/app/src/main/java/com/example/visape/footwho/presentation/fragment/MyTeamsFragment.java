package com.example.visape.footwho.presentation.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.visape.footwho.domain.classes.Team;
import com.example.visape.footwho.domain.controller.CompetitionController;
import com.example.visape.footwho.domain.controller.TeamController;
import com.example.visape.footwho.presentation.activity.MainActivity;
import com.example.visape.footwho.presentation.activity.TeamActivity;
import com.example.visape.footwho.presentation.adapter.MyTeamsAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by Visape on 13/10/2017.
 */

public class MyTeamsFragment extends Fragment {

    private ListView myTeamsListView;
    private TextView errorMessage;
    private MyTeamsAdapter adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myteams, container, false);

        myTeamsListView = (ListView) view.findViewById(R.id.my_teams_list_view);
        errorMessage = (TextView) view.findViewById(R.id.error_my_teams_list);
        progressBar = (ProgressBar) view.findViewById(R.id.indeterminateBar);

        displayMyTeams();
        setListenersToListView();


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void displayMyTeams () {

        //Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList <Team> myTeams = TeamController.getInstance().getMyTeams(getContext());
                adapter = new MyTeamsAdapter(getContext(),myTeams);

                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myTeamsListView.setAdapter(adapter);
                        if (myTeamsListView.getCount() == 0) {
                            errorMessage.setText("You haven't any team in your list...");
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    private void setListenersToListView () {
        //Normal Click: display fragment of team
        //Long click: delete team
        myTeamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TeamActivity.class);
                intent.putExtra("team", (Team) parent.getAdapter().getItem(position));
                startActivity(intent);
            }
        });
        myTeamsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Team team = (Team) parent.getAdapter().getItem(position);

                new AlertDialog.Builder(getContext())
                        .setMessage("Do you want to delete " + team.getName() + " from your teams?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TeamController.getInstance().deleteTeam(getContext(),team.getId());
                                Toast.makeText(getContext(), team.getName() + " was deleted from your teams!", Toast.LENGTH_SHORT).show();
                                adapter.remove(team);
                                adapter.notifyDataSetChanged();
                                if (myTeamsListView.getCount() == 0) {
                                    errorMessage.setText("You haven't any team in your list...");
                                    errorMessage.setVisibility(View.VISIBLE);
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();


                return true;
            }
        });
    }
}
