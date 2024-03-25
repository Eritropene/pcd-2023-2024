package pcd.lab05.monitors.ex_latch;

public class BlockLatch implements Latch {

    private int waitingThreads;
    private final int totalThreads;

    public BlockLatch(final int totalThreads) {
        this.totalThreads = totalThreads;
        waitingThreads = totalThreads;
    }
    @Override
    public synchronized void countDown() {
        this.waitingThreads--;
        if (waitingThreads <= 0) {
            waitingThreads = totalThreads;
            notifyAll();
        }
    }

    @Override
    public synchronized void await() throws InterruptedException {
        wait();
    }
}
