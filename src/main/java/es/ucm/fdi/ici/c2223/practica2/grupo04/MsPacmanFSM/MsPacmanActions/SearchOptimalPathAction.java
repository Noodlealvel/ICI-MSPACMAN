package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SearchOptimalPathAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman goes through";
	}

	@Override
	public MOVE execute(Game game) {
		
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), getBestPath(game), game.getPacmanLastMoveMade(), DM.EUCLID);	
	}
	
	private boolean nodeHasActivePill(Game game, int node) {
		return game.getPillIndex(node) != -1 && game.isPillStillAvailable(game.getPillIndex(node));
	}
			
	private int getBestPath(Game game) {
			
		int currentScore;
		int maxScore = Integer.MIN_VALUE;
		int chosenPath = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.PATH);
		List<Integer> nearPills = new ArrayList<Integer>();
			
		for(int pillNode : game.getPillIndices()) {
			if (nodeHasActivePill(game, pillNode) && game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, game.getPacmanLastMoveMade(), DM.EUCLID) <= 30 ) {
				nearPills.add(Integer.valueOf(pillNode));
			}
		}
		
		for (Integer pillNode : nearPills) {
			
			currentScore = 0;
			int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), pillNode.intValue(), game.getPacmanLastMoveMade());
			for(int node : path) {
				if(nodeHasActivePill(game, node)) {
					currentScore++;
				}
				else{
					currentScore--;
				}
			}
			if(currentScore > maxScore) {
				maxScore = currentScore;
				chosenPath = pillNode.intValue();
			}
		}
			
		
		return chosenPath;
	}

}
