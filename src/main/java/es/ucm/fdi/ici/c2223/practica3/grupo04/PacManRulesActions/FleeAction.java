package es.ucm.fdi.ici.c2223.practica3.grupo04.PacManRulesActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FleeAction implements Action, RulesAction {

	@Override
	public String getActionId() {
		return "MsPacman flees";
	}

	@Override
	public MOVE execute(Game game) {
		GHOST nearestghost;
		nearestghost = MsPacmanUtils.getNearestGhostAtDistance(game, game.getPacmanCurrentNodeIndex(), MsPacmanInput.ghostCloseMedium, false);
		if(nearestghost!=null)
		{
			return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(nearestghost), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		return MOVE.NEUTRAL;
	}
	
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}
