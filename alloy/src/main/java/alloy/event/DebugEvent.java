package alloy.event;

import utility.logger.Level;

public class DebugEvent {

    private Level level;
    private Throwable error;
    private String message;
    private String classname;
    private Thread thread;
    
    private final long time;

    public DebugEvent(String className, String message, Throwable error, Level level, Thread t) 
    {
        this.classname = className;
        this.message = message;
        this.error = error;
        this.level = level;
        this.thread = t;

        this.time = System.currentTimeMillis();
    }
    
    public String getClassname()
    {
        return classname;
    }

    public Throwable getError()
    {
        return error;
    }

    public Level getLevel()
    {
        return level;
    }

    public String getMessage()
    {
        return message;
    }

    public long getTime() 
    {
        return time;
    }

    public Thread getThread()
    {
        return thread;
    }

    public void setClassname(String classname)
    {
        this.classname = classname;
    }

    public void setError(Throwable error)
    {
        this.error = error;
    }

    public void setLevel(Level level)
    {
        this.level = level;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setThread(Thread thread)
    {
        this.thread = thread;
    }
    
    public boolean hasMessage()
    {
        return this.message != null;
    }
    
    public boolean hasError()
    {
        return this.error != null;
    }
    
    public boolean hasClassname()
    {
        return this.classname != null;
    }
    
    public boolean hasLevel()
    {
        return this.level != null;
    }

    public boolean hasThread()
    {
        return this.thread != null;
    }
    
}