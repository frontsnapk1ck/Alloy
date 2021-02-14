package botcord.components.member;

import botcord.components.gui.BCButton;
import net.dv8tion.jda.api.entities.Member;

@SuppressWarnings("serial")
public class MemberButton extends BCButton {

    private Member member;

    public MemberButton(Member m) 
    {
        super();
        this.member = m;
        init();
        config();
    }

    @Override
    public void init() 
    {
        this.setBackground(null);
		this.setBorder(null);
    }

    @Override
    public void config() 
    {
		this.configToolTip();
		configListener();
		configText();
    }

    private void configText() 
    {
        this.setText(this.member.getEffectiveName());
    }

    private void configToolTip() 
    {
        this.setToolTipText(this.member.getEffectiveName());
    }

    @Override
    public void update() 
    {
        configText();
        configToolTip();
    }

    @Override
    protected void configListener() 
    {
        //TODO popup menu coming soon
    }

    public Member getMember() 
    {
        return member;
    }
    
}