
package es.ucm.fdi.ici.c2223.practica4.grupo04;

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
        GhostController ghosts = new es.ucm.fdi.ici.c2122.practica1.grupo01.Ghosts();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 40)
        );
        
    }
}
