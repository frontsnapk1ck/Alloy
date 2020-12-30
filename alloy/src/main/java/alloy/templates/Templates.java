package alloy.templates;

import java.util.List;
import java.util.Map;
import java.util.Set;

import alloy.command.util.PunishType;
import alloy.gameobjects.RankUp;
import alloy.gameobjects.Warning;
import alloy.gameobjects.player.Building;
import alloy.gameobjects.player.Player;
import alloy.handler.BankHandeler;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.job.SpamRunnable;
import io.FileReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import utility.StringUtil;

public class Templates {

	public static Template noPermission(DisPerm p , User u)
    {
        String s = "The user " + u.getAsMention() + " does not have the permsion `" + p.getName() + "`";
        Template t = new Template("No Permissions" , s);
        return t;
    }

    public static Template bankTransferMinimum()
    {
        String s = "you must transfer a minimum of $" + BankHandeler.MINIUM_BALACE;
        Template t = new Template( "Minimum Bank Trasnfer" , s);
        return t;
    }

    public static Template bankTransferSuccess(Player from, Player to, int amount, String message)
    {
		String s = from.getAsMention() + " has trasnfred `$" + amount +"` to " + to.getAsMention();
		if (!message.equalsIgnoreCase(""))
			s +="\n" + message;
        Template t = new Template("Bank Trasnfer Sucsess", s);
		return t;
    }

    public static Template bankTransferFailed()
    {
		Template t = new Template( "Bank Trasnfer Failed" , "the transfer has failed");
        return t;
    }

    public static Template bankInsufficientFunds(User author, int amount)
    {
		String s = author.getAsMention() + ", you do not have enough funds to make that payment";
		Template t = new Template( "Bank Trasnfer Failed" , s );
        return t;	
    }

    public static Template prefixIs(String prefix)
    {
		Template t = new Template("Prefix", "the prefix is currently\n`" + prefix + "`");
        return t;
    }

    public static Template modlogNotFound()
    {
		Template t = new Template("Mod Log not found" , "the mod log could not be found for this server");
        return t;
		
    }

	public static Template moderationActionEmpty(TextChannel chan, PunishType punishType) 
	{
		Template t = new Template("moderationActionEmpty", "moderationActionEmpty");
		return t;
	}

	public static Template cannotModerateSelf() 
	{
		Template t = new Template("Cannot Moderate Self" , "you cannot moderate me, i am too powerfull");
		return t;
	}

	public static Template moderationActionFailed(PunishType punishType) 
	{
		Template t = new Template("Moderation Action Failed", "the moderation action `" + punishType + "` has failed, lol");
		return t;
	}

	public static Template moderationActionSucsess(TextChannel chan, Member targetUser, String verb) 
	{
		Template t = new Template("Moderation Action Sucsess", "the moderation action `" + verb + "` has secseeded, lol.\nused in " + chan.getAsMention() + " agaisnt " + targetUser.getAsMention() );
		return t;
	}

	public static Template bankCurrentBalance(Player p) 
	{
		Template t = new Template("Current Balance", p.getAsMention() + "'s balance is `$" + p.getBal() + "`");
		return t;
		
	}

	public static Template argumentsNotRecognized(Message msg) 
	{
		Template t = new Template("Arguments not Recongnized", "`" + msg.getContentRaw() + "`\nthat didn't make sense to me, lol");
		return t;
	}

	public static Template daySuccess(Guild guild) 
	{
		Template t = new Template("Day Sucsess", "the day has advanced in this server");
		return t;
		
	}

	public static Template moderationLog(TextChannel chan, Guild guild, User author, PunishType punishType, String[] args) 
	{
		String out = "";
		for (String string : args) 
			out += string + " ";	
		Template t = new Template("Moderation Action Used", author.getAsMention() + " used " + punishType + " in the channel " + chan.getAsMention() + "\n`" + out + "`");
		return t;
		
	}

	public static Template invalidNumberFormat(String[] args) 
	{
		String out = "";
		for (String string : args) 
			out += string + " ";

		Template t = new Template("Invalid Number format", " this is wrong, lol\n`" + out + "`");
		return t;
		
	}

	public static Template onlyPositiveNumbers(int amount) 
	{
		Template t = new Template("Only Positive Numbers", "i dont think that `" + amount +"` is positive" );
		return t;
	}

	public static Template spamRunnableCreated(SpamRunnable r) 
	{
		Template t = new Template("ID to stop this spam" , "to stop this spam, use the command \n`!spam stop " + r.getID() + "`");
		return t;
	}

	public static Template invalidUse(MessageChannel channel) 
	{
		Template t = new Template("Invalid Use" , "you cant do that, thats illeagle, almost like my creatror spelling things correctly. we all know thats illeagl");
		return t;
		
	}

	public static Template caseNotFound(String caseId) 
	{
		Template t = new Template("case not found" , "could not find the case `" + caseId + "`" );
		return t;
		
	}

	public static Template caseReasonModified(String reason) 
	{
		Template t = new Template( "Case Reasom Moddified" , "the reason was moddified to\n\n" + reason + "");
		return t;
	}

	public static Template bulkDeleteSucsessfull(TextChannel channel, int size) 
	{
		Template t = new Template("Purge sucsess" , "deleted `" + size +"` messages in " + channel.getAsMention() );
		return t;
		
	}

	public static Template privateMessageFailed(Member m) 
	{
		Template t = new Template("Private Message Failed" , "i could not PM the member " + m.getAsMention() );
		return t;
	}

	public static Template getWarn(Warning w) 
	{
		Template t = new Template("Warning", "reason: " + w.getReason() +"\nissuer: " + w.getIssuer() + "\nid:" + w.getId());
		return t;
	}

	public static Template warnSucsess(Member m, Warning w, User author) 
	{
		Template t = new Template("Warn Sucsess", "the member " + m.getAsMention() + "has been warned\n\nwith the reason: `" + w.getReason() + "`");
		t.setFooterName(author.getAsTag());
		t.setFooterURL(author.getAvatarUrl());
		return t;
	}

	public static Template warnings(String table) 
	{
		Template t = new Template("Warnings",  table);
		return t;
	}

	public static Template warningNotFound(String string) 
	{
		Template t = new Template ("Warning not found" , "the warning with the id `" + string + "` could not be found");
		return t;
	}

	public static Template warningsRemovedSucsess(String string, Member warned) 
	{
		Template t = new Template("Warning removed Sucessfully" , "the warning has been removed from the user " + warned.getAsMention() );
		return t;
	}

	public static Template invalidBuildingName(String name) 
	{
		Template t = new Template("Invalid building name" ,"The name `" + name + "` is invalid");
		return t;
		
	}

	public static Template argumentsNotSupplied(String[] args, String[] usage) 
	{
		String got = "";
		for (String s : args) 
			got += s + " ";
		got = got.trim();
		
		String expected = "";
		if(usage != null)
		{
			for (String s : usage) 
				expected += s + " ";
		}
		expected = expected.trim();

		Template t = new Template("Arguments not supplied" , "expeted:\t " + expected + "\ngot:\t\t`" + got + "`");
		return t;
	}

	public static Template buildingsNameOutOfBounds(Building b) 
	{
		Template t = new Template("Building Name out of bounds", "the name for that building is too long bro \\**hehe*\\*");
		return t;
	}

	public static Template buildingSaveSucsess(List<Building> buildings) 
	{
		String names = "";
        String costs = "";
        String gener = "";

        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";

        }
		Template t = new Template("Building Save Sucsess", "All availible buildings");
		t.addFeild("Name" ,         names , true);
		t.addFeild("Cost" ,         costs , true);
		t.addFeild("Generation" ,   gener , true);

		return t;
	}

	public static Template workOptionAddSuccess(String[] args) 
	{
		String out = "";
		for (String string : args) 
			out += string + " ";
		
		Template t = new Template("Work Option Add Sucsess", "you've added the following to the work optoins in this server\n\n" + out);
		return t;
	}

	public static Template numberOutOfBounds(IndexOutOfBoundsException e) 
	{
		Template t = new Template("Number out of bounds", "lol, that number threw an error, youre lucky i caught it\n\n" + e.getMessage() );
		return t;
	}

	public static Template buildingsRemoveSuccess(Building b) 
	{
		Template t = new Template("Building Remove Sucsess" , "removed the building " + b.getName() );
		return t;
	}

	public static Template workRemoveSuccess(String s) 
	{
		Template t = new Template("WOrk Remove Sucsess" , "removed the Work Option `" + s + "`" );
		return t;
	}

	public static Template spamRunnableStoped(Long id) 
	{
		Template t = new Template("Spam Stopped" , "the spam with the id `" + id + "` has stopeed");
		return t;
	}

	public static Template spamRunnableIdNotFound(Long id) 
	{
		Template t = new Template("Spam ID not found", "could not find the id `" + id + "`");
		return t;	
	}

	public static Template voiceJoinSucsess(VoiceChannel vc) 
	{
		Template t = new Template("Voice Join Sucsess", "joined channel " + vc.getName() );
		return t;
	}

	public static Template voiceJoinFail(VoiceChannel vc) 
	{
		Template t = new Template("Voice Join Fail", "could not join channel " + vc.getName() );
		return t;		
	}

	public static Template voiceMemberNotInChannel(Member m) 
	{
		Template t = new Template("Voice Join Fail" , "you have to be in a voice channel to use that command");
		return t;
	}

	public static Template showXPCooldown(int cooldown)  
	{
		Template t = new Template("XP cooldown Time" , "the xp cooldown time in this guild is currently `" + cooldown + "`");
		return t;
	}

	public static Template showCooldown(int cooldown)  
	{
		Template t = new Template("Cooldown Time" , "the cooldown time in this guild is currently `" + cooldown + "`");
		return t;
		
	}

	public static Template invalidNumberFormat(String num)  
	{
		Template t = new Template("Invalid Number Format" , "`" + num + "` isnt a number now, is it?");
		return t;
	}

	public static Template viewStartingBalance(int startingBalance)  
	{
		Template t = new Template("Starting Balance", "the starting blacance in this server is `$" + startingBalance + "`" );
		return t;
	}

	public static Template workOptions(Guild g) 
	{
		String path = AlloyUtil.getGuildPath(g);
		String[] wo = FileReader.read(path + AlloyUtil.SUB + AlloyUtil.SETTINGS_FOLDER + AlloyUtil.SUB + AlloyUtil.WORK_OPTIONS_FILE);
		String out = "";
		for (String string : wo)
			out += string + "\n";
		
		Template t = new Template("Work Options", "the work options are \n\n" + out);
		return t;
	}

	public static Template buildingsList(Guild g)
    {
		List<Building> buildings = AlloyUtil.loadBuildings(g);
		String names = "";
        String costs = "";
        String gener = "";

        for (Building b : buildings) 
        {
            names += "" + b.getName() + "\n";
            costs += "" + b.getCost() + "\n";
            gener += "" + b.getGeneration() + "\n";

        }
		Template t = new Template("Building list", "All availible buildings");
		t.addFeild("Name" ,         names , true);
		t.addFeild("Cost" ,         costs , true);
		t.addFeild("Generation" ,   gener , true);

		return t;
	}

	public static Template voiceDisconnectSucsess(VoiceChannel vc)
    {
		Template t = new Template("Voice Disconnect Sucsess", "I have left " + vc.getName() );
		return t;
	}

	public static Template voiceDisconnectFail()
    {
		Template t = new Template("Voice Disconnect Fail", "I have not left the vc i am in" );
		return t;
	}

	public static Template onCooldown(Member m)
    {
		Template t = new Template("Slow down there", "you are on cooldown " + m.getAsMention() );
		return t;
	}

	public static Template sayAdmin(String out, Message msg)
    {
		Template t = new Template ("Anouncement" , out );
		t.setFooterName(msg.getAuthor().getAsTag());
		t.setFooterURL(msg.getAuthor().getAvatarUrl());
		return t;
		
	}

	public static Template help(String out) 
	{
		Template t = new Template("HELP", out);
		return t;
	}

	public static Template helpSentTemplate ()
	{
		Template t = new Template("Help has been sent" , "albeit in the form of a DM");
		return t;
	}

	public static Template infoSelf() 
	{
		Template t = new Template ("My name is alloy" , "I am Alloy and i am a got frontsnapk1ck has been working on for a little while now. if you have any question feel free to reach out to him and join the offical Alloy Support Server here https://discord.gg/7UNxyXRxBh \n\nthanks!");	
		t.setFooterName("frontsnapk1ck");
		t.setFooterURL("https://cdn.discordapp.com/avatars/312743142828933130/7c63b41c5ed601b3314c1dce0d0e0065.png");
		return t;
		
	}

	public static Template infoUser(Member m)
    {
		Template t = new Template("User Info", "coming soon");
		return t;
	}

	public static Template infoServer(Guild g)
    {
		Template t = new Template("Server Info", "coming soon");
		return t;
	}

	public static Template roleNotFound(String role)
    {
		Template t = new Template("Role not found", " i couldnt find the role " + role);
		return t;
	}

	public static Template infoRole(Role r)
    {
		Template t = new Template("Role Info", "coming soon");
		return t;
	}

	public static Template invite()
    {
		Template t = new Template("TODO", "Thanks for thinking of me");
		String rickroll = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
		t.setTitle("CLick here to invite me" , rickroll);
		return t;
	}

	public static Template inviteActual(Member m)
    {
		String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=762825892006854676&permissions=435678326&scope=bot";
		Template t = new Template("This is your actual invite" , "my creator will never miss an opertunity to rick roll someone\n\n" + inviteLink);
		return t;
		
	}

	public static Template uptime(String realitiveTime)
    {
		Template t = new Template("Uptime" , "i have been up for `" + realitiveTime + "`");
		return t;
	}

	public static Template noBlacklistedChannels()
    {
		Template t = new Template("No blacklisted Channels" , "this guild doesnt have any blacklisted channels ");
		return t;	
	}

	public static Template listBlackListedChannels(List<Long> blacklisted)
    {
		String out = "";
		for (Long id : blacklisted) 
			out += "<#" + id + ">\n";
	
		Template t = new Template("Black Listed Channels for XP ", out);
		return t;
	}

	public static Template invalidChannel(String c)
    {
		Template t = new Template("Invalid Channel", c);
		return t;
	}

	public static Template channelIsNotBlacklisted(String c)
    {
		Template t = new Template("Channel is not blacklisted", "the channel " + c + " is not blacklisted");
		return t;
	}

	public static Template blackListRemoveSucsess(String c)
    {
		Template t = new Template("Blacklist Removed Sucsess", "removed channel " + c + " from the blacklisted channels");
		return t;
	}

	public static Template blackListAddSucsess(String c)
    {
		Template t = new Template("Blacklist Add Sucsess", "added channel " + c + " to the blacklisted channels");
		return t;
	}

	public static Template leaderboard(List<String> lb)
    {
		String out = "";
		for (String string : lb) 
			out += string + "\n";
		
		Template t = new Template("leaderboard" , out);
		return t;
	}

	public static Template userNotFound(String user) 
	{
		Template t = new Template("User not found" , "i could not find the user " + user );
		return t;
	}

	public static Template rank(Member target, int level, String progress)
    {
		Template t = new Template("Rank", target.getAsMention() + "\nlevel: `" + level + "`\nxp: `" + progress + "`");
		return t;
	}

	public static Template invalidRole(String string)
    {
		Template t = new Template ("invalid role" , "i did not reconglize the role " + string);
		return t;
	}

	public static Template duplicateRankup(int level)
    {
		Template t = new Template ("duplicate level detected" , "you may have tried to make a level get announced twice - level" + level );
		return t;
	}

	public static Template levelNotFound(String string)
    {
		Template t = new Template("Level not found", string);
		return t;
	}

	public static Template rankupAddSucess(RankUp ru)
    {
		Template t = new Template("rank up add sucsess" , "added a rankup messaged to level " + ru.getLevel() );
		return t;
	}

	public static Template rankupRemoveSucess(RankUp toRm)
	{
		Template t = new Template("rankup remove sucsess", "removed a rankup messaged from level " + toRm.getLevel() );
		return t;
	}

	public static Template viewRankUps(List<RankUp> rus)
	{
		String out = "";
		for (RankUp rankUp : rus) 
			out += "level `" + rankUp.getLevel() + "`\n";
		Template t = new Template("Levels with Rankups" , out);
		return t;
	}

	public static Template xpSetSucsess(Member target, int xp)
    {
		Template t = new Template("XP set Secsess", "set " + target.getAsMention() + "'s xp to `" + xp + "`");
		return t;
	}

	public static Template cannotModerateModerators() 
	{
		Template t = new Template("Cannot Moderate Moderators", "sorry, i cant moderate the moderators. if you need to punish them remove thier perms first");
		return t;
	}

	public static Template showBuildings(User author, Map<Building, Integer> owned) 
	{
		Set<Building> bs = owned.keySet();
		String[][] data = new String[3][bs.size()];

		int i = 0;
		for (Building building : bs) 
		{
			int num = owned.get(building);
			String name = building.getName();
			int gener = building.getGeneration();

			data[0][i] = "" + name + "\n";
			data[1][i] = "" + gener + "\n";
			data[2][i] = "" + num + "\n";

			i++;
		}

		Template t = new Template(author.getAsTag() , "the buildings you own" );
		t.addFeild("Name", StringUtil.joinStrings(data[0]) , true);
		t.addFeild("Generation", StringUtil.joinStrings(data[1]) , true);
		t.addFeild("Qauntity", StringUtil.joinStrings(data[2]) , true);
		return t;
	}

	public static Template buildingNameNotRecognized(String args) 
	{
		Template t = new Template("Building Name Not recognized", 
									"i couldnt find a buildin with the name " + args + " in this guild\nmake sure to check spelling");
		return t;
	}

	public static Template buildingBuySucsess(Building tobuy, User author) 
	{
		Template t = new Template("Building Bought", 
									"you have bought the building `" + tobuy.getName() + "` " + author.getAsMention() + " for the price of `$" + tobuy.getCost() + "`" );
		return t;
	}

	public static Template workSucsess(String option, int amt) 
	{
		Template t = new Template("Work", option + "\nyou made $" + amt);
		return t;
	}

	public static Template spamChannelChanged(TextChannel target) 
	{
		Template t = new Template("Spam Channel Changed" , "the spam channel has been changed to " + target.getAsMention());
		return t;
	}

	public static Template channelisAlreadyBlacklisted(TextChannel channel) 
	{
		Template t = new Template( "Channel Already Blacklisted" , "this channel " + channel.getAsMention() + " is already blackelisted" );
		return t;
	}

}