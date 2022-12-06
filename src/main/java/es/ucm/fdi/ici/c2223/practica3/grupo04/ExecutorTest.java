package es.ucm.fdi.ici.c2223.practica3.grupo04;


import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.internal.POType;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(true)
                .setPacmanPO(true)
                .setPacmanPOvisual(true) // visualización
                .setGhostsPOvisual(true) // visualización
                .setPOType(POType.LOS)
                .setSightLimit(200)
                .build();

        PacmanController pacMan = new es.ucm.fdi.ici.c2223.practica3.grupo04.MsPacMan();
        GhostController ghosts = new Ghosts();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 10)
        );
        
    }
}
