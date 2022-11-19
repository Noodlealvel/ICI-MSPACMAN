package es.ucm.fdi.ici.c2223.practica3.grupo04;

import es.ucm.fdi.ici.c2122.practica1.grupo01.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTest {
	public static void main(String[] args) {
		Executor executor = new Executor.Builder()
				 .setTickLimit(4000)
				 .setVisual(true)
				 .setScaleFactor(2.0)
				 .setSightLimit(0)
				 .build();
		PacmanController pacMan = new es.ucm.fdi.ici.c2223.practica3.grupo04.MsPacMan();
		GhostController ghosts = new es.ucm.fdi.ici.c2122.practica1.grupo06.Ghosts();

		System.out.println(executor.runGame(pacMan, ghosts, 10));
	}
}