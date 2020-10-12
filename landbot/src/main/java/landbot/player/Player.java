package landbot.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import landbot.builder.BuildingBuilder;
import landbot.io.Saver;

public class Player {

    private long id;
    private HashMap<String , List<Building>> owned;
    private List<String> buildingTypes;
    private int balance;
    private String guildname;

    public Player ( long id , int balance , String guildName )
    {
        this.balance = balance;
        this.id = id;
        this.guildname = guildName;

        this.makeBuildingTypes();
    }

    private void makeBuildingTypes() 
    {
        List<Building> b = BuildingBuilder.loadBuildings(this.guildname);
        this.buildingTypes = new ArrayList<String>();
        for (Building building : b)
            this.buildingTypes.add(building.getName());
    }

    public HashMap<String , List<Building>> getOwned() 
    {
        return this.owned;
    }

    public List<Building> getOwned(String s) 
    {
        if (this.owned.containsKey(s))
            this.owned.get(s);
        return null;
    }

    public long getId() {
        return id;
    }

    private void save ()
    {
        String[] out = new String[this.buildingTypes.size() + 1];
        out[0] = "" + this.balance;
        
        for (int i = 0; i < this.buildingTypes.size(); i++)
        {
            String s = this.buildingTypes.get(i);

            String tmp = "";
            tmp += s;
            tmp += ">";
            tmp += this.owned.get(s).size();

            out[i+1] = tmp;            
        }

        String path = "landbot\\res\\server\\" + guildname + "\\users\\" + this.id + ".txt";
        Saver.saveOverwite(path, out);
    }

    public void setHash(HashMap<String, List<Building>> owned2) 
    {
        this.owned = owned2;
	}

    public int getNumType(Building b) 
    {
		return this.owned.get(b.getName()).size();
	}

    public int getTotalOwned() 
    {
        int i = 0;
        for (String string : buildingTypes) 
        {
            i += this.owned.get(string).size();
        }
        return i;
	}

    public boolean buyBuilding(Building b) 
    {
        int cost = b.getCost();
        String key = b.getName();

        if (this.balance >= cost )
        {
            this.balance -= cost;
            this.owned.get(key).add(b);
            this.save();
            return true;
        }
        return false;

	}

    public int getBal() 
    {
		return this.balance;
	}

    public void addBal(int bal) 
    {
        this.balance += bal;
        this.save();
	}

    public List<Building> getListOwned() 
    {
        List<Building> tmp = new ArrayList<Building>();

        for (String s : this.buildingTypes) 
        {
            if (this.owned.get(s).size() != 0)
                tmp.addAll(this.owned.get(s));
        }

        return tmp;
    }

    public List<String> getTypes() 
    {
		return this.buildingTypes;
	}

    public void removeBuilding(Building b) 
    {
        String key = b.getName();
        if (this.owned.get(key) != null)
        {
            int numOwned = this.owned.get(key).size();
            int refund = numOwned * b.getCost();
            this.owned.remove(key);
            this.buildingTypes.remove(key);
            this.addBal(refund);
        }
	}

    public void day() 
    {
        int bal = 0;
        for (String string : buildingTypes) 
        {
            List<Building> b = this.owned.get(string);
            if (b != null && b.size() > 0)
            {
                int cost = b.get(0).getCost();
                int tmp = b.size() * cost;
                bal += tmp;
            }  
        }
        this.addBal(bal);
	}


    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof Player)
        {
            Player p = (Player)obj;
            return p.getId() == this.id;
        }
        return false;
    }
}
