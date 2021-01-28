package com.app.metown.Models;

import org.json.JSONArray;

public class TownModel {

    String TownID, TownName, Alphabet;
    JSONArray TownList;

    public String getTownID() {
        return TownID;
    }

    public void setTownID(String townID) {
        TownID = townID;
    }

    public String getTownName() {
        return TownName;
    }

    public void setTownName(String townName) {
        TownName = townName;
    }

    public String getAlphabet() {
        return Alphabet;
    }

    public void setAlphabet(String alphabet) {
        Alphabet = alphabet;
    }

    public JSONArray getTownList() {
        return TownList;
    }

    public void setTownList(JSONArray townList) {
        TownList = townList;
    }
}
