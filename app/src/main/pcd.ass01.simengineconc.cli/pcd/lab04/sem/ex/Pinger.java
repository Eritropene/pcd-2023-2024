package pcd.lab04.sem.ex;

import java.util.concurrent.Semaphore;

public class Pinger extends Thread {

	private final Semaphore pingEvent, pongEvent;
	public Pinger(Semaphore pingEvent, Semaphore pongEvent) {
		this.pingEvent = pingEvent;
		this.pongEvent = pongEvent;
	}	
	
	public void run() {
		while (true) {
			try {
				pongEvent.acquire();
				System.out.println("ping!");
				pingEvent.release();
				sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}