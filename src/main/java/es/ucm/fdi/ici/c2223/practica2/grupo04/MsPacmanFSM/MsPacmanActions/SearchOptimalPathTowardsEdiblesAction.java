package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SearchOptimalPathTowardsEdiblesAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman hunts the ghosts";
	}

	//Simplemente cambia respecto a las actions parecidas en que le da igual el tiempo que les queda para ser comidos
	@Override
	public MOVE execute(Game game) {
		GHOST nearestedibleghost;
		nearestedibleghost= MsPacmanUtils.getNearestGhost(game, game.getPacmanCurrentNodeIndex(), true);
		if (nearestedibleghost!=null)
		{
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestedibleghost), game.getPacmanLastMoveMade(), DM.EUCLID);	
		}
			return MOVE.NEUTRAL;
	}

}
