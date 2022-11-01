package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class EdiblesAndTogetherAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman chases nearest ghost";
	}

	@Override
	public MOVE execute(Game game) {
		GHOST nearestedibleghost;
		nearestedibleghost= MsPacmanUtils.getNearestGhost(game, game.getPacmanCurrentNodeIndex(), MsPacmanInput.EatLimit, true);
		if (nearestedibleghost!=null)
		{
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestedibleghost), game.getPacmanLastMoveMade(), DM.EUCLID);	
		}
			return MOVE.NEUTRAL;
	}
}
