package es.ucm.fdi.ici.c2223.practica5.grupo04;

import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.internal.POType;


public class ExecutorTest {

	public static void main(String[] args) {
		Executor executor = new Executor.Builder()
				 .setTickLimit(4000)
				 .setVisual(true)
				 .setScaleFactor(2.0)
				 .setSightLimit(0)
				 .build();
		PacmanController pacMan = new MsPacMan();
		GhostController ghosts = new Ghosts();

		System.out.println(executor.runGame(pacMan, ghosts, 10));
	}
   }