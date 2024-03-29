package frontsnapk1ck.botcord.components.channel;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import frontsnapk1ck.botcord.components.gui.BCPanel;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import frontsnapk1ck.utility.Util;

public class ChannelSelectorPanel extends BCPanel {

    private Guild guild;
    private List<ChannelGroup> groups;
    private List<BCListener> listeners;

    private int maxH = 0;

    public ChannelSelectorPanel(Guild guild) 
    {
        this.guild = guild;
        init();
        config();
	}

    @Override
    public void init() 
    {
        this.setBackground(BCUtil.CHANNEL_SELECTOR);
    }

    @Override
    public void config() 
    {
        this.configToolTip();
        this.makeGroups();
        this.configGroups();
        this.updateBounds();
        this.updateLayout();
    }
    
    private void updateLayout() 
    {
        int comps = 0;
        for (ChannelGroup cg : groups) 
            comps += cg.getNumComps();
        this.setLayout(new GridLayout(comps,1,0,10));
    }

    private void configGroups() 
    {
        for (ChannelGroup channelGroup : groups)
        {
            this.add(channelGroup.getLabel());
            for (ChannelButton b : channelGroup.getButtons())
                this.add(b);
        }
    }

    private void updateBounds() 
    {
        // int y = 0;
        // int x = 0;
        int w = this.getWidth();
        for (ChannelGroup cg : groups) 
        {
            int h = cg.getNumComps() * ChannelGroup.COMP_HEIGHT;

            // 
            cg.setSize( w, h );
            // y += h;

            // this.maxH = y;
        }
        // this.setSize(this.getWidth(),this.getMaxH());
        // System.err.println(this.getSize() + "\t\tChannel Selector Panel Size");
    }

    private void makeGroups() 
    {
        List<Category> catagories = this.guild.getCategories();
        List<GuildChannel> categorized = new ArrayList<GuildChannel>();
        List<GuildChannel> missing = new ArrayList<GuildChannel>();
        Util.copy( missing ,this.guild.getChannels());
        List<ChannelGroup> channelGroups = new ArrayList<ChannelGroup>();
        for (Category category : catagories) 
        {
            categorized.addAll(category.getChannels());
            channelGroups.add(new ChannelGroup(category));
        }
        for (GuildChannel c : missing) 
        {
            if (c instanceof Category)
                categorized.add(c);
        }
        missing.removeAll(categorized);
        if (missing.size() != 0)
            channelGroups.add(new ChannelGroup(missing));
        this.groups = channelGroups;
    }


    private void configToolTip() 
    {
        this.setToolTipText("Channel Selector");
    }

    public void updateListeners(List<BCListener> listeners) 
    {
        this.listeners = listeners;
        for (ChannelGroup g : this.groups)
            g.updateListeners(listeners);
    }
    
    public List<BCListener> getListeners() {
        return listeners;
    }

    @Override
    public void update() 
    {
        updateBounds();
        for (ChannelGroup channelGroup : groups) 
            channelGroup.update();
    }

    public int getMaxH() {
        return maxH;
    }

    @Override
    public void setSize(int width, int height) 
    {
        super.setSize(width, height);
    }
    
}
