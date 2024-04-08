package pcd.ass01.mvcsim;

import pcd.ass01.concsimtrafficbase.CarAgentExtended;
import pcd.ass01.concsimtrafficbase.Road;
import pcd.ass01.concsimtrafficbase.RoadsEnv;
import pcd.ass01.simengineconc.AbstractConcurrentSimulation;
import pcd.ass01.simengineconc.AbstractEnvironment;

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
