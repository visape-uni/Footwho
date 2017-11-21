package com.example.visape.footwho.domain.classes;

import java.io.Serializable;

/**
 * Created by Visape on 13/10/2017.
 */

public class Team implements Serializable {
    private int id;
    private String name;
    private String code;
    private String shortName;
    private String squadMarketValue;
    private String crestUrl;
    private int idCompetition;
    private String country;

    public Team(int id, String name, String code, String shortName, String squadMarketValue, String crestUrl, int idCompetition, String country) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.shortName = shortName;
        this.squadMarketValue = squadMarketValue;
        this.crestUrl = crestUrl;
        this.idCompetition = idCompetition;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    public void setSquadMarketValue(String squadMarketValue) {
        this.squadMarketValue = squadMarketValue;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

    public int getIdCompetition() {
        return idCompetition;
    }

    public void setIdCompetition(int idCompetition) {
        this.idCompetition = idCompetition;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", shortName='" + shortName + '\'' +
                ", squadMarketValue='" + squadMarketValue + '\'' +
                ", crestUrl='" + crestUrl + '\'' +
                '}';
    }
}
