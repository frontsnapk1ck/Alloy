package frontsnapk1ck.alloy.io.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import frontsnapk1ck.alloy.gameobjects.RankUp;
import frontsnapk1ck.alloy.utility.settings.RankUpSettings;
import frontsnapk1ck.io.DataLoader;
import frontsnapk1ck.io.FileReader;

public class RankupLoaderText extends DataLoader<RankUp , String> {
    

    @Override
    public List<RankUp> loadALl(String file) 
    {
        String[] rankupsComp = FileReader.read(file);
        String[][] rankupsSplit = splitRankups(rankupsComp);
        List<RankUp> rankups = loadRankups(rankupsSplit);

        Collections.sort(rankups);
        return rankups;
    }

    private List<RankUp> loadRankups(String[][] rankupsSplit) 
    {
        List<RankUp> out = new ArrayList<RankUp>();
        for (String[] strings : rankupsSplit) 
            out.add(loadRankup(strings));
        return out;
    }

    private RankUp loadRankup(String[] arr) 
    {
        int rank = Integer.parseInt(arr[0]);
        Long rankID = 0l;
        if (!arr[1].equals("") )
            rankID = Long.parseLong(arr[1]);
        String message = "";

        for (int i = 2; i < arr.length; i++) 
            message += arr[i];    
        
            RankUpSettings settings = new RankUpSettings();
            settings.setId(rankID)
                    .setLevel(rank)
                    .setMessage(message);
    
            RankUp ru = new RankUp(settings);

        return ru;

    }

    private String[][] splitRankups(String[] rankupsComp) 
    {
        String[][] out = new String[rankupsComp.length][];

        int i = 0;
        for (String s : rankupsComp) 
        {
            out[i] = s.split(":");
            i++;
        }

        return out;
    }

}
