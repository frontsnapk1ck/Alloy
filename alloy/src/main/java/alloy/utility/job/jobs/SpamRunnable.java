package alloy.utility.job.jobs;

import java.util.ArrayList;
import java.util.List;

import alloy.main.Alloy;
import alloy.utility.event.SpamFinishEvent;
import alloy.utility.event.SpamFinishListener;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.event.Job;

public class SpamRunnable extends Job {
    
    private boolean stop;
    private int reps;
    private String message;
    private TextChannel channel;
    private List<SpamFinishListener> listeners;

    private int i;


    public SpamRunnable(int reps, String message, TextChannel c, Long num) 
    {
        super("" + num);
        this.listeners = new ArrayList<SpamFinishListener>();

        this.reps = reps;
        this.message = message;
        this.channel = c;
	}

    protected void alertListeners() 
    {
        SpamFinishEvent e = new SpamFinishEvent();
        e.setRunnable(this);
        for (SpamFinishListener l : listeners) 
            l.onSpamFinishEvent(e);
    }

    protected void cooldown() 
    {
        try 
        {
            Thread.sleep(1250);
        } 
        catch (InterruptedException e) 
        {
            Alloy.LOGGER.error("SpamRunnable" , e );
            e.printStackTrace();
        }
    }

    public void addListener( SpamFinishListener l)
    {
        this.listeners.add(l);
    }

    public boolean removeListener( SpamFinishListener l)
    {
        return this.listeners.remove(l);
    }

    public Long getIDLong() 
    {
		return Long.parseLong(getID());
	}

    public void stop() 
    {
        this.stop = true;
	}

    @Override
	public void execute() 
    {
        for (i = 0; i < this.reps; i++) 
        {
            if (this.stop)
            {
                this.alertListeners();
                return;
            }

            this.channel.sendMessage(message).queue();

            cooldown();
        }
        this.alertListeners();
    }

    public TextChannel getChannel() {
        return channel;
    }

    public List<SpamFinishListener> getListeners() {
        return listeners;
    }

    public String getMessage() {
        return message;
    }

    public int getReps() {
        return reps;
    }

    public boolean isStop() {
        return stop;
    }

	public int getCurrent() {
		return this.i;
	}
}
