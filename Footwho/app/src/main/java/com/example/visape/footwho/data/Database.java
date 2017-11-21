package com.example.visape.footwho.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.visape.footwho.domain.classes.Competition;
import com.example.visape.footwho.domain.classes.Team;

import java.util.ArrayList;

/**
 * Created by Visape on 13/10/2017.
 */

public class Database extends SQLiteOpenHelper {

    private static final String TEAMS_TABLE = "MY_TEAMS";
    private static final String COMPETITIONS_TABLE = "MY_COMPETITIONS";

    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_CREST_URL = "CRESTURL";
    private static final String COL_ID_COMPETITION = "ID_COMPETITION";
    private static final String COL_COUNTRY = "COUNTRY";



    private static final String COL_LEAGUE = "LEAGUE";
    private static final String COL_CAPTION = "CAPTION";
    private static final String COL_CURRENT_MATCHDAY = "CURRENT_MATCHDAY";
    private static final String COL_NUMBER_MATCHDAY = "NUMBER_MATCHDAY";

    public Database(Context context) {
        super(context, "footwho.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String myteamssql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER, %s TEXT)", TEAMS_TABLE, COL_ID, COL_NAME, COL_CREST_URL, COL_ID_COMPETITION, COL_COUNTRY);
        String mycompetitionssql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)", COMPETITIONS_TABLE, COL_ID, COL_LEAGUE, COL_CAPTION, COL_CURRENT_MATCHDAY, COL_NUMBER_MATCHDAY);

        db.execSQL(myteamssql);
        db.execSQL(mycompetitionssql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storeTeam (Team team) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, team.getId());
        values.put(COL_NAME, team.getName());
        values.put(COL_CREST_URL, team.getCrestUrl());
        values.put(COL_ID_COMPETITION, team.getIdCompetition());
        values.put(COL_COUNTRY, team.getCountry());

        db.insert(TEAMS_TABLE, null, values);

        db.close();
    }

    public void deleteTeam (int teamId) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TEAMS_TABLE, COL_ID + "=" + String.valueOf(teamId), null);

        db.close();
    }

    public void deleteCompetition (int competitionId) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(COMPETITIONS_TABLE, COL_ID + "=" + String.valueOf(competitionId), null);

        db.close();
    }

    public boolean isTeam (int teamId) {

        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s = %d", TEAMS_TABLE, COL_ID, teamId);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() != 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Team> getTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", TEAMS_TABLE);
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            int teamId = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String teamName= cursor.getString(cursor.getColumnIndex(COL_NAME));
            String crestUrl= cursor.getString(cursor.getColumnIndex(COL_CREST_URL));
            int compId = cursor.getInt(cursor.getColumnIndex(COL_ID_COMPETITION));
            String country = cursor.getString(cursor.getColumnIndex(COL_COUNTRY));

            teams.add(new Team(teamId, teamName,null,null,null,crestUrl, compId, country));
        }

        db.close();
        return teams;
    }

    public void storeCompetition (Competition competition) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, competition.getId());
        values.put(COL_LEAGUE, competition.getLeague());
        values.put(COL_CAPTION, competition.getCaption());
        values.put(COL_CURRENT_MATCHDAY, competition.getCurrentMatchday());
        values.put(COL_NUMBER_MATCHDAY, competition.getNumberOfMatchdays());

        db.insert(COMPETITIONS_TABLE, null, values);

        db.close();
    }

    public ArrayList<Competition> getCompetitions() {
        ArrayList<Competition> competitions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", COMPETITIONS_TABLE);
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            int competitionId = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String competitionName= cursor.getString(cursor.getColumnIndex(COL_LEAGUE));
            String competitionCaption = cursor.getString(cursor.getColumnIndex(COL_CAPTION));
            int competitionCurrentMatchday = cursor.getInt(cursor.getColumnIndex(COL_CURRENT_MATCHDAY));
            int competitionNumberMatchdays = cursor.getInt(cursor.getColumnIndex(COL_NUMBER_MATCHDAY));

            competitions.add(new Competition(competitionId, competitionName, competitionCaption, competitionCurrentMatchday, competitionNumberMatchdays));
        }

        db.close();
        return competitions;
    }

    public boolean isCompetition(int competitionId) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s = %d", COMPETITIONS_TABLE, COL_ID, competitionId);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() != 0) {
            return true;
        }
        return false;
    }
}
