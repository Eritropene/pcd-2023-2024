package pcd.ass01.simengineconc;

public class Worker {
    private final int id;
    public Worker(int id) {
        this.id = id;
    }

    public void execute(Runnable action) {
        new Thread(action).start();
    }

    public int id() {
        return this.id;
    }
}
