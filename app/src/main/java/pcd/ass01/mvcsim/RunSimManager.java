package pcd.ass01.mvcsim;

public class RunSimManager {
    public static void main(String[] args) {

        AbstractRoadSimulation simSingleRoad = new TrafficSimulationSingleRoad();
//        AbstractRoadSimulation simSingleRoadWithTrafficLight = ...
//        AbstractRoadSimulation simCrossRoads = ...

        RoadSimController controller = new RoadSimController();
        controller.addSimulation("Single Road", simSingleRoad);
//        controller.addSimulation("Single Road With Traffic Light", simSingleRoadWithTrafficLight);
//        controller.addSimulation("Cross Roads", simCrossRoads);

        RoadSimManagerView view = new RoadSimManagerView(controller);

        view.setVisible(true);
    }
}
