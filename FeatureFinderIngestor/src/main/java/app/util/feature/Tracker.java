package app.util.feature;

public class Tracker {
    Integer threadAlive;
    Integer filesProcessed;

    public Tracker() {
        this.threadAlive = 0;
        this.filesProcessed = 0;
    }

    public Integer getThreadAlive() {
        return this.threadAlive;
    }

    public synchronized void incrementThreadAlive() {
        this.threadAlive++;
    }

    public Integer getFilesProcessed() {
        return this.filesProcessed;
    }

    public synchronized void jobCompleted() {
       filesProcessed++;
       threadAlive--;
    }
}