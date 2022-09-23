package es.ucm.fdi.ici.c2223.grupo04.PabloPardosAlvaroVelasco;

import es.ucm.fdi.ici.c2223.practica0.grupoIndividual.Ghosts;
import es.ucm.fdi.ici.c2223.practica0.grupoIndividual.MsPacman;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class Prac1Test {
	public static void main(String[] args) {
		 Executor executor = new Executor.Builder()
		 .setTickLimit(4000)
		 .setVisual(true)
		 .setScaleFactor(3.0)
		 .setSightLimit(0)
		 .build();
		 PacmanController pacMan = new AlgoritmicPacman();
		 GhostController ghosts = new AlgoritmicGhosts();

		 System.out.println(
		 executor.runGame(pacMan, ghosts, 20)
		 );
		 }
}
