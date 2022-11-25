

import es.ucm.fdi.ici.practica4.demofuzzy.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(true)
                .setPacmanPO(true)
                .setPacmanPOvisual(true)
                .setVisual(true)
                .setScaleFactor(3.0)
                .build();

        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new GhostsRandom();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 40)
        );
        
    }
}
