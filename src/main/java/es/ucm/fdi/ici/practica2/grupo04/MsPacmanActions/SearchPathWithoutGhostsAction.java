package es.ucm.fdi.ici.practica2.grupo04.MsPacmanActions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SearchPathWithoutGhostsAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman searchs a path without ghosts";
	}

	@Override
	public MOVE execute(Game game) {

		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pathWithNoGhosts(game), game.getPacmanLastMoveMade(), DM.EUCLID);	
		
	}
	
	//Ahora da igual si las pills están activas o no, miras los caminos cogiendo tambien las pills inactivas
	//Despues miras si los ghosts estan en esos caminos dentro del rango. Si hay uno, se queda con el ultimo encontrado (da igual cual)
	//Si no hay, supuestamente no hay escapatoria, asi que se queda con el valor de la linea 33
	private int pathWithNoGhosts(Game game) {

		int currentScore;
		int chosenPath = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getPillIndices(), DM.PATH);
		List<Integer> nearPills = new ArrayList<Integer>();
			
		for(int pillNode : game.getPillIndices()) {
			if (game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, game.getPacmanLastMoveMade(), DM.EUCLID) <= 30 ) {
				nearPills.add(Integer.valueOf(pillNode));
			}
		}
		
		for (Integer pillNode : nearPills) {
			
			currentScore = 0;
			int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), pillNode.intValue(), game.getPacmanLastMoveMade());
			for(int node : path) {
				for (GHOST ghosts : GHOST.values()) {
					if(game.getGhostCurrentNodeIndex(ghosts) == node) {
						currentScore++;
					}
				}
			}
			if(currentScore == 0) {
				chosenPath = pillNode.intValue();
			}
		}
		
		return chosenPath;
	}

}