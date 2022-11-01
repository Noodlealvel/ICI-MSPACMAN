package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BeginMapAction implements Action{

	
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	
	@Override
	public String getActionId() {
		return "MsPacman begins map";
	}

	@Override
	public MOVE execute(Game game) {
		int PPill = getNearestPP (game, 60);
		if(PPill!=-1 )
		{
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), PPill, game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		return allMoves[rnd.nextInt(allMoves.length)];
	}

	public int getNearestPP(Game game, int limit) {
		double powerPillDistance;
		double shortestDistance = -1;
		int nearestPPill = -1;
		for (int pillNode : game.getActivePowerPillsIndices()) {
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, DM.PATH);
			if ((powerPillDistance < shortestDistance && powerPillDistance < limit) || shortestDistance == -1) {
				shortestDistance = powerPillDistance;
				nearestPPill = pillNode;
			}
		}
		return nearestPPill;
	}
}
