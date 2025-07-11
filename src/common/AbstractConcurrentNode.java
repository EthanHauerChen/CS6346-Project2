package common;

import java.util.concurrent.Semaphore;

public class AbstractConcurrentNode {
    public final Semaphore rwQueue = new Semaphore(Integer.MAX_VALUE);
    public final Semaphore rwMutex = new Semaphore(1);
    public final Semaphore rcMutex = new Semaphore(1);
    public int readCount = 0;
    public Integer key;

    public AbstractConcurrentNode(Integer key) {
        this.key = key;
    }

    public void enterQueueAsReader() throws InterruptedException {
        this.rwQueue.acquire();
        this.rwQueue.release();
        this.incrementReadCountAndWait();
    }

    public void enterQueueAsWriter() throws InterruptedException {
        this.rwQueue.acquire();
        this.rwMutex.acquire();
        this.rwQueue.release();
    }

    public <T> T exitRead(T object) throws InterruptedException {
        this.decrementReadCountAndWait();
        return object;
    }

    public <T> T exitWrite(T object) {
        rwMutex.release();
        return object;
    }

    public void incrementReadCountAndWait() throws InterruptedException {
        this.rcMutex.acquire();
        this.readCount++;
        if (readCount == 1) this.rwMutex.acquire();
        this.rcMutex.release();
    }

    public void decrementReadCountAndWait() throws InterruptedException {
        this.rcMutex.acquire();
        this.readCount--;
        if (readCount == 0) this.rwMutex.release();
        this.rcMutex.release();
    }
}
