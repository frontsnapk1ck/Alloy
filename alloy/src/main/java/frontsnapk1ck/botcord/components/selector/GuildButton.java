package frontsnapk1ck.botcord.components.selector;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.botcord.components.gui.BCButton;
import frontsnapk1ck.botcord.event.BCListener;
import frontsnapk1ck.botcord.event.PressEvent;
import frontsnapk1ck.botcord.event.SwitchTarget;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class GuildButton extends BCButton {

    private Guild guild;

    public GuildButton(Guild guild) {
        this.guild = guild;
        init();
        config();
    }

    @Override
    public void init() {
        this.setBackground(BCUtil.BACKGROUND);
        this.setBackground(null);
    }

    @Override
    public void config() 
    {
        setImage();
        this.configToolTip();
        this.setBorder(null);
    }

    private void setImage() 
    {
        try 
        {
            Image img = getImage(this.guild.getIconUrl());
            this.setIcon(new ImageIcon(img));
        }
        catch (IOException e) 
        {
            Alloy.LOGGER.error("GuildButton", e);
        }
    }

    public void updateImage() 
    {
        int w, h;
        w = this.getWidth();
        h = this.getHeight();

        if (w == 0 || h == 0)
            return;
        
        Icon ic = this.getIcon();
        if (ic instanceof ImageIcon) 
        {
            Image img = ((ImageIcon) ic).getImage();
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon newIc = new ImageIcon(img);
            this.setIcon(newIc);
        }
    }

    private void configToolTip() {
        Guild g = this.guild;
        String code = "<html><body><h1> " + g.getName() + "</h1>" + "<p>Owned by: " + g.getOwner().getUser().getAsTag()
                + "</p>" + "<p>Is Admin: <b>" + isAdmin(g) + "</b></p>" + "<p>Members: " + getMembers(g) + " | Bots: "
                + getBots(g) + "</p>" + "<p>Boost: " + g.getBoostTier() + " | Boosts: " + g.getBoostCount() + "</p>"
                + "</body></html>";
        this.setToolTipText(code);
    }

    private int getBots(Guild g) {
        int bot = 0;
        List<Member> members = g.getMembers();
        for (Member m : members) {
            if (m.getUser().isBot())
                bot++;
        }
        return bot;
    }

    private int getMembers(Guild g) {
        int mem = 0;
        List<Member> members = g.getMembers();
        for (Member m : members) {
            if (!m.getUser().isBot())
                mem++;
        }
        return mem;
    }

    private boolean isAdmin(Guild g) 
    {
        List<DisPerm> perms = DisPermUtil.parsePerms(g.getSelfMember().getPermissions());
        for (DisPerm p : perms) 
        {
            if (p.equals(DisPerm.ADMINISTRATOR))
                return true;
        }
        return false;
    }

    private Image getImage(String urlS) throws IOException 
    {
        if (urlS == null)
            urlS = BCUtil.DEFAULT_DISCORD_PHOTO;
        URL url = new URL(urlS);
        Image img = ImageIO.read(url);
        return img;
    }

    public Guild getGuild() 
    {
        return guild;
    }

    @Override
    public void update() 
    {
        this.updateImage();
        this.configToolTip();
    }

    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        PressEvent e = new PressEvent(SwitchTarget.GUILD);
        e.setData(getGuild());
        for (BCListener l : getListeners())
            l.onPress(e);
    }

}
