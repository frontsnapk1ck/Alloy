package botcord.components.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import botcord.event.BotCordListener;

@SuppressWarnings("serial")
public abstract class BotCordButton extends JButton implements BotCordComponent {

    private List<BotCordListener> listeners;

    public BotCordButton() 
    {
        super();
        this.listeners = new ArrayList<BotCordListener>();
    }

    public void setListeners(List<BotCordListener> listeners) {
        this.listeners = listeners;
    }

    public List<BotCordListener> getListeners() {
        return listeners;
    }

    public void addListener(BotCordListener l)
    {
        this.listeners.add(l);
    }
    
    public boolean rmListener(BotCordListener l)
    {
        return this.listeners.remove(l);
    }
}
