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
import android.widget.TextView;
import android.widget.Toast;

import com.example.visape.footwho.R;
import com.example.visape.footwho.domain.classes.Competition;
import com.example.visape.footwho.domain.controller.CompetitionController;
import com.example.visape.footwho.presentation.adapter.MyCompetitionsAdapter;

import java.util.ArrayList;

/**
 * Created by Visape on 12/11/2017.
 */

public class MyCompetitionsFragment extends Fragment {

    private ListView myCompetitionsListView;
    private TextView errorMessage;
    private MyCompetitionsAdapter myCompetitionsAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycompetitions, container, false);

        myCompetitionsListView = (ListView) view.findViewById(R.id.my_competitions_list_view);
        errorMessage = (TextView) view.findViewById(R.id.error_my_competitions_list);

        loadMyCompetitions();
        setListenerToListView();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void loadMyCompetitions () {
        ArrayList<Competition> myCompetitions = CompetitionController.getInstance().getMyCompetitions(getContext());

        myCompetitionsAdapter = new MyCompetitionsAdapter(getContext(), myCompetitions);
        myCompetitionsListView.setAdapter(myCompetitionsAdapter);

        if (myCompetitionsListView.getCount() == 0) {
            errorMessage.setText("You haven't any competition in your list...");
            errorMessage.setVisibility(View.VISIBLE);
        }
    }

    private void setListenerToListView () {

        myCompetitionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: ON CLICK COMPETITION SHOW LEAGUE TABLE AND MATCHDAYS
            }
        });

        myCompetitionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Competition competition = (Competition) parent.getAdapter().getItem(position);

                new AlertDialog.Builder(getContext())
                        .setMessage("Do you want to delete " + competition.getCaption() + " from your competitions?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CompetitionController.getInstance().deleteCompetition(getContext(),competition.getId());
                                Toast.makeText(getContext(), competition.getCaption() + " was deleted from your competitions!", Toast.LENGTH_SHORT).show();
                                myCompetitionsAdapter.remove(competition);
                                myCompetitionsAdapter.notifyDataSetChanged();
                                if (myCompetitionsListView.getCount() == 0) {
                                    errorMessage.setText("You haven't any competition in your list...");
                                    errorMessage.setVisibility(View.VISIBLE);
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });
    }
}
