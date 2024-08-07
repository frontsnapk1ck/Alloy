package frontsnapk1ck.alloy.command.configuration;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.gameobjects.Server;
import frontsnapk1ck.alloy.gameobjects.player.Building;
import frontsnapk1ck.alloy.handler.command.ConfigHandler;
import frontsnapk1ck.alloy.handler.command.EconHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.utility.settings.BuildingSettings;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.utility.StringUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BuildingCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() {
        return DisPerm.MANAGER;
    }

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission())) 
        {
            AlloyTemplate t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Server s = AlloyUtil.loadServer(g);
        if (s.getRoleAssignOnBuy() && !DisPermUtil.checkPermission(g.getSelfMember(), DisPerm.MANAGE_ROLES))
        {
            s.changeAssignRolesOnBuy(false);

            AlloyTemplate t = Templates.assignRolesOnBuy(false);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length == 0) 
        {
            ConfigHandler.viewBuildings(g, channel, bot);
            return;
        }

        if (args[0].equalsIgnoreCase("add"))
            addBuilding(data);
        else if (args[0].equalsIgnoreCase("remove"))
            removeBuilding(data);
        else if (args[0].equalsIgnoreCase("reset"))
            resetBuilding(data);
    }

    private void addBuilding(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 4) {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int cost = -1;
        int generation = -1;
        String name = StringUtil.joinStrings(args, 3);

        try {
            cost = Integer.parseInt(args[1]);
            generation = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            AlloyTemplate t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!EconHandler.validName(name , g)) 
        {
            AlloyTemplate t = Templates.invalidBuildingName(name);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        List<Building> buildings = AlloyUtil.loadBuildings(g);
        BuildingSettings settings = new BuildingSettings();
        settings.setCost(cost).setGeneration(generation).setName(name);

        Building b = new Building(settings);

        if (EconHandler.nameOutOfBounds(b, buildings)) {
            AlloyTemplate t = Templates.buildingsNameOutOfBounds(b);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        EconHandler.saveBuilding(g, b);
        buildings = AlloyUtil.loadBuildings(g);

        AlloyTemplate t = Templates.buildingSaveSuccess(buildings);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

    }

    private void removeBuilding(AlloyInputData data) {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 2) {
            AlloyTemplate t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        try {
            int i = Integer.parseInt(args[1]);
            Building b = EconHandler.removeBuilding(i - 1, g);
            AlloyTemplate t = Templates.buildingsRemoveSuccess(b);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        } catch (NumberFormatException e) {
            AlloyTemplate t = Templates.invalidNumberFormat(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        } catch (IndexOutOfBoundsException e) {
            AlloyTemplate t = Templates.numberOutOfBounds(e);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        ConfigHandler.viewBuildings(g, channel, bot);

    }

    private void resetBuilding(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        EconHandler.removeAllBuildings(g);
        
        EconHandler.copyOverBuildings(g);

        ConfigHandler.viewBuildings(g, channel, bot);

    }

}
