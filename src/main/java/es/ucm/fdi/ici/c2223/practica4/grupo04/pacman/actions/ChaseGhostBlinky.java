package es.ucm.fdi.ici.c2223.practica4.grupo04.pacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import es.ucm.fdi.ici.c2223.practica4.grupo04.MsPacManFuzzyMemory;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class ChaseGhostBlinky implements Action {

	@Override
	public String getActionId() {
		return "eatBLINKY";
	}
	
	@Override
	public MOVE execute(Game game) {

		int nearestedibleghost = MsPacManFuzzyMemory.getGhostsPositions()[0];
		if (nearestedibleghost > 0)
		{
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestedibleghost, game.getPacmanLastMoveMade(), DM.EUCLID);	
		}
			return MOVE.NEUTRAL;
	}
	
}
