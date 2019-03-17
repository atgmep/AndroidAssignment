package com.mobile.makefive.model;

import com.mobile.makefive.entity.TblAccount;

import java.util.ArrayList;
import java.util.List;

public class AppData {

    public String dataStr;

    private List<GameData> matches = new ArrayList();


    public List<GameData> getMatches() {
        return matches;
    }


    public GameData getMatch(String id) {
        for (GameData match : matches) {
            if (match.getId().equals(id)) {
                return match;
            }
        }
        return null;
    }


    public GameData newMatch(TblAccount tblAccount) {
        GameData gameData = new GameData(tblAccount);
        matches.add(gameData);
        return gameData;
    }


    public boolean removeMatch(String id) {
        for (GameData match : matches) {
            if (match.getId().equals(id)) {
                matches.remove(match);
                return true;
            }
        }
        return false;
    }


}
