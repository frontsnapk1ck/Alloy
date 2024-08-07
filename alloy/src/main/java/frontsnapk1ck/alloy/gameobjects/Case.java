package frontsnapk1ck.alloy.gameobjects;

import frontsnapk1ck.alloy.command.util.PunishType;
import frontsnapk1ck.alloy.utility.settings.CaseSettings;
import frontsnapk1ck.io.Saver;

public class Case extends GameObject {

    public static final String NUM = "num";
    public static final String ISSUER = "issuer";
    public static final String REASON = "reason";
    public static final String PUNISH_TYPE = "punish type";
    public static final String MESSAGE_ID = "message";
    public static final String TARGET = "target";


    private CaseSettings settings;
    
    public Case( CaseSettings settings )
    {
        this.settings = settings;
        this.save();
    }

	public Long getIssuer() 
    {
        return this.settings.getIssuer();
    }

    public int getNum() 
    {
        return this.settings.getNum();
    }

    public PunishType getPunishType() 
    {
        return this.settings.getPunishType();
    }

    public String getReason() 
    {
        return this.settings.getReason();
    }

    public long getMessageId() 
    {
        return this.settings.getMessageId();
    }

    public long getTargetId()
    {
        return this.settings.getTargetId();
    }

    public void setReason(String reason) 
    {
        this.settings.setReason(reason);
        this.save();
    }

    @Override
    public void save() 
    {
        String[] out = new String[]
        {
            Case.ISSUER + ":" + this.settings.getIssuer() ,
            Case.NUM + ":" + this.settings.getNum() ,
            Case.PUNISH_TYPE + ":" + this.settings.getPunishType() ,
            Case.REASON + ":" + this.settings.getReason() ,
            Case.MESSAGE_ID + ":" + this.settings.getMessageId() ,
            Case.TARGET + ":" + this.settings.getTargetId() ,
        };

        String path = this.settings.getPath();
        Saver.saveNewFile(path, out);
	}

    @Override
    public Case copy() 
    {
        return new Case( this.settings.copy() );
    }

    @Override
    public GameObject getData() 
    {
        return this;
    }
    
}
