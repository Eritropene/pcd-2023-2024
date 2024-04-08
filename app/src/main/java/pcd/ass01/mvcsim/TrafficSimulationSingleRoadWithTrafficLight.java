package pcd.ass01.mvcsim;

import pcd.ass01.concsimtrafficbase.*;

public class TrafficSimulationSingleRoadWithTrafficLight extends AbstractRoadSimulation {

    public TrafficSimulationSingleRoadWithTrafficLight(int threads) {
        super(threads);
    }
    public TrafficSimulationSingleRoadWithTrafficLight() {
        super();
    }
    @Override
    public void setup() {
        this.setupTimings(0, 1);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        int roadLength = Math.max(1500, 500 + getCars()*10); // size scales with number of cars
        Road r = env.createRoad(new P2d(0,300), new P2d(roadLength,300));

        TrafficLight tl = env.createTrafficLight(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
        r.addTrafficLight(tl, 740);

//        CarAgent car1 = new CarAgentExtended("car-1", env, r, 0, 0.1, 0.3, 6);
//        this.addAgent(car1);
//        CarAgent car2 = new CarAgentExtended("car-2", env, r, 100, 0.1, 0.3, 5);
//        this.addAgent(car2);
        for (int i = 0; i < getCars(); i++) {

            String carId = "car-" + i;
            double initialPos = i*10;
            double carAcceleration = 0.1; //  + gen.nextDouble()/2;
            double carDeceleration = 0.3; //  + gen.nextDouble()/2;
            double carMaxSpeed = 6; // 4 + gen.nextDouble();

            var car = new CarAgentExtended(carId, env, r, initialPos, carAcceleration, carDeceleration, carMaxSpeed);
            this.addAgent(car);
        }
    }
}
