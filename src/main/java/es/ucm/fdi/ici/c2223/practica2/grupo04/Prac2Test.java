package es.ucm.fdi.ici.c2223.practica2.grupo04;

import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class Prac2Test {
	public static void main(String[] args) {
		 Executor executor = new Executor.Builder()
		 .setTickLimit(4000)
		 .setVisual(true)
		 .setScaleFactor(3.0)
		 .setSightLimit(0)
		 .build();
		 PacmanController pacMan = new MsPacMan();
		 GhostController ghosts = new es.ucm.fdi.ici.c2122.practica1.grupo01.Ghosts();

		 System.out.println(
		 executor.runGame(pacMan, ghosts, 10)
		 );
		 }
}
