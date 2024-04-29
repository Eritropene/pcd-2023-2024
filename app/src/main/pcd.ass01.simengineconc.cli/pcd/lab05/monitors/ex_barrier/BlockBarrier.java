package pcd.lab05.monitors.ex_barrier;

public class BlockBarrier implements Barrier{

    private final int totalThreads;
    private int waitingThreads = 0;

    public BlockBarrier(final int nThreads) {
        totalThreads = nThreads;
    }
    @Override
    public synchronized void hitAndWaitAll() throws InterruptedException {
        waitingThreads++;
        if (waitingThreads < totalThreads) {
            wait();
        } else {
            waitingThreads = 0;
            notifyAll();
        }
    }
}
