package es.ucm.fdi.ici.c2223.practica3.grupo04.PacManRulesActions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class SearchZoneWithPPAndNoGhostsAction implements Action, RulesAction {

	@Override
	public String getActionId() {
		return "MsPacman searchs a zone with PP and no ghosts";
	}

	@Override
	public MOVE execute(Game game) {
		
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pathWithPPAndNoGhosts(game), game.getPacmanLastMoveMade(), DM.EUCLID);	
		
	}
	
	//Seguimos mirando que zona de Powerpills nos combiene y si tiene fantasmas cerca del punto al que queremos ir
	//Si el cuadrante de powerpills idoneo tiene fantasmas cerca, va a la powerpill mas cercana directamente
	private int pathWithPPAndNoGhosts(Game game) {

		int[] powerpills = game.getActivePowerPillsIndices();
		int[] path;
		int pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
		int currentScore, chosenPath = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.PATH);
		List<Integer> nearPills = new ArrayList<Integer>();
		
		switch(game.getActivePowerPillsIndices().length) {
			case 1:
			case 4:
				pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
				break;
			case 2:
				if(game.getShortestPathDistance(powerpills[0], powerpills[1]) > MsPacmanInput.PPDistanceInZone) {
					pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
				}
				else {
					path = game.getShortestPath(powerpills[0], powerpills[1]);
					pill = path[path.length/2];
				}
				break;
			case 3:
				if(game.getShortestPathDistance(powerpills[0], powerpills[1]) < 70) {
					path = game.getShortestPath(powerpills[0], powerpills[1]);
					pill = path[path.length/2];
				}
				if(game.getShortestPathDistance(powerpills[1], powerpills[2]) < 70) {
					path = game.getShortestPath(powerpills[1], powerpills[2]);
					pill = path[path.length/2];
				}
				if(game.getShortestPathDistance(powerpills[2], powerpills[0]) < 70) {
					path = game.getShortestPath(powerpills[2], powerpills[0]);
					pill = path[path.length/2];
				}
				
				break;
				
			default:
				pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
		}
		
		//Guardamos los puntos cercanos al punto que queremos ir
		for (int pillNode : game.getPillIndices()) {
			if (game.getDistance(pill, pillNode, game.getPacmanLastMoveMade(),
					DM.EUCLID) <= 30) {
				nearPills.add(Integer.valueOf(pillNode));
			}
		}

		for (Integer pillNode : nearPills) {

			currentScore = 0;
			int[] path2 = game.getShortestPath(game.getPacmanCurrentNodeIndex(), pillNode.intValue(),
					game.getPacmanLastMoveMade());
			for (int node : path2) {
				for (GHOST ghosts : GHOST.values()) {
					if (game.getGhostCurrentNodeIndex(ghosts) == node) {
						currentScore++;
					}
				}
			}
			if (currentScore == 0) {
				chosenPath = pillNode.intValue();
			}
		}

		return chosenPath;
	}
	
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}

}