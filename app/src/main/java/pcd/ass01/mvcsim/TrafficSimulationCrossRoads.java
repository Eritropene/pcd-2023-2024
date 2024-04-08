package pcd.ass01.mvcsim;

import pcd.ass01.concsimtrafficbase.*;

import java.util.Random;

public class TrafficSimulationCrossRoads extends AbstractRoadSimulation {

    public TrafficSimulationCrossRoads(int threads) {
        super(threads);
    }
    public TrafficSimulationCrossRoads() {
        super();
    }
    @Override
    public void setup() {
        Random gen = new Random(1);

        this.setupTimings(0, 1);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        TrafficLight tl1 = env.createTrafficLight(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);

        int r1Length = Math.max(1500, 500 + getCars()*10); // size scales with number of cars
        Road r1 = env.createRoad(new P2d(0,300), new P2d(r1Length,300));
        r1.addTrafficLight(tl1, 740);

//        CarAgent car1 = new CarAgentExtended("car-1", env, r1, 0, 0.1, 0.3, 6);
//        this.addAgent(car1);
//        CarAgent car2 = new CarAgentExtended("car-2", env, r1, 100, 0.1, 0.3, 5);
//        this.addAgent(car2);
        for (int i = 0; i < getCars()/2; i++) {

            String carId = "car-" + i;
            double initialPos = i*10;
            double carAcceleration = 1; //  + gen.nextDouble()/2;
            double carDeceleration = 0.3; //  + gen.nextDouble()/2;
            double carMaxSpeed = 5; // 4 + gen.nextDouble();

            var car = new CarAgentExtended(carId, env, r1, initialPos, carAcceleration, carDeceleration, carMaxSpeed);
            this.addAgent(car);
        }

        TrafficLight tl2 = env.createTrafficLight(new P2d(750,290),  TrafficLight.TrafficLightState.RED, 75, 25, 100);

        int r2Length = Math.max(600, 200 + getCars()*10); // size scales with number of cars
        Road r2 = env.createRoad(new P2d(750,0), new P2d(750,r2Length));
        r2.addTrafficLight(tl2, 290);

//        CarAgent car3 = new CarAgentExtended("car-3", env, r2, 0, 0.1, 0.2, 5);
//        this.addAgent(car3);
//        CarAgent car4 = new CarAgentExtended("car-4", env, r2, 100, 0.1, 0.1, 4);
//        this.addAgent(car4);
        for (int i = getCars()/2; i < getCars(); i++) {

            String carId = "car-" + i;
            double initialPos = i*10;
            double carAcceleration = 1; //  + gen.nextDouble()/2;
            double carDeceleration = 0.3; //  + gen.nextDouble()/2;
            double carMaxSpeed = 4; // 4 + gen.nextDouble();

            var car = new CarAgentExtended(carId, env, r2, initialPos, carAcceleration, carDeceleration, carMaxSpeed);
            this.addAgent(car);
        }
    }
}
