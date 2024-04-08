package pcd.ass01.mvcsim;

import pcd.ass01.simengineconc.AbstractConcurrentSimulation;

public abstract class AbstractRoadSimulation extends AbstractConcurrentSimulation {

    public AbstractRoadSimulation(int threads) {
        super(threads);
    }
    public AbstractRoadSimulation() {
        super();
    }
    private int numberOfCars;
    public void setCars(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }
    public int getCars() {
        return this.numberOfCars;
    }
}
