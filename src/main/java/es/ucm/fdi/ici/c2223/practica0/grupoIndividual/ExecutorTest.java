package es.ucm.fdi.ici.c2223.practica0.grupoIndividual;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTest {

	public static void main(String[] args) {
		 Executor executor = new Executor.Builder()
		 .setTickLimit(4000)
		 .setVisual(true)
		 .setScaleFactor(3.0)
		 .setSightLimit(0)
		 .build();
		 PacmanController pacMan = new MsPacman();
		 GhostController ghosts = new Ghosts();

		 System.out.println(
		 executor.runGame(pacMan, ghosts, 20)
		 );
		 }
}
