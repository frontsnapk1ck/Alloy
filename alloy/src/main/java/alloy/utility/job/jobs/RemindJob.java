package alloy.utility.job.jobs;

import alloy.main.Sendable;
import alloy.main.SendableMessage;
import utility.event.Job;

public class RemindJob extends Job {

    private Sendable bot;
    private SendableMessage sm;

    public RemindJob(Sendable bot, SendableMessage sm) 
    {
        super();
        this.bot = bot;
        this.sm = sm;
    }

    @Override
    public void execute() 
    {
        this.bot.send(sm);
    }

    public SendableMessage getSM() 
    {
		return this.sm;
	}
    
}