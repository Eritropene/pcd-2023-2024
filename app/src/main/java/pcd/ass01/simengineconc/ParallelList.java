package pcd.ass01.simengineconc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;

public class ParallelList<E> extends ArrayList<E> {

    private final List<Worker> workers = new ArrayList<>();
    private CyclicBarrier barrier;

    public ParallelList(int numberOfWorkers) {
        super();
        for (int i=0; i<numberOfWorkers; i++) {
            this.workers.add(i, new Worker(i));
        }
    }
    public ParallelList() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public void parallelForEach(Consumer<? super E> action) {
        if (this.isEmpty()) return;
        // decide how many workers use
        int numWorkers = Math.min(this.workers.size(), this.size());
        // sync n threads + the caller
        this.barrier = new CyclicBarrier(numWorkers + 1);
        // do action in workers
        for (int i=0; i<numWorkers; i++) {
            Worker worker = this.workers.get(i);
            worker.execute(() -> {
                try {
                    doAction(action, worker, numWorkers);
                } catch (BrokenBarrierException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        // wait for workers
        try {
            //System.out.println("[main barrier] waiting...");
            this.barrier.await();
            //System.out.println("[main barrier] done");
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void doAction(Consumer<? super E> action, Worker worker, int numWorkers) throws BrokenBarrierException, InterruptedException {
        // compute action on elements
        for (E e: subList(worker, numWorkers)) {
            action.accept(e);
        }
        // sync with other workers
        //System.out.println("[worker barrier] waiting...");
        barrier.await();
        //System.out.println("[worker barrier] waiting...");
    }
    private List<E> subList(Worker worker, int numWorkers) {
        int start = (worker.id() * this.size() + numWorkers - 1) / numWorkers;
        int end = ((worker.id()+1) * this.size() + numWorkers - 1) / numWorkers;
        return this.subList(start, end);
    }
}
