package pcd.ass01.mvcsim;

import pcd.ass01.concsimtrafficbase.*;

public class TrafficSimulationSingleRoad extends AbstractRoadSimulation {

    public TrafficSimulationSingleRoad(int threads) {
        super(threads);
    }
    public TrafficSimulationSingleRoad() {
        super();
    }
    @Override
    public void setup() {
        this.setupTimings(0, 1);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        int roadLength = Math.max(1500, 500 + getCars()*10); // size scales with number of cars
        Road road = env.createRoad(new P2d(0,300), new P2d(roadLength,300));

        for (int i = 0; i < getCars(); i++) {

            String carId = "car-" + i;
            double initialPos = i*10;
            double carAcceleration = 1 + random().nextDouble()/2;
            double carDeceleration = 0.3 + random().nextDouble()/2;
            double carMaxSpeed =  5 + random().nextDouble();

            var car = new CarAgentBasicRandom(carId, env, road, initialPos, carAcceleration, carDeceleration, carMaxSpeed);
            this.addAgent(car);
        }
    }
}
