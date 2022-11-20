package es.ucm.fdi.ici.c2223.practica3.grupo04.PacManRulesActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class EdiblesButApartAction implements Action, RulesAction {

	final static private int EATTIMELIMIT = 20;
	
	@Override
	public String getActionId() {
		return "MsPacman chases ghost carefully";
	}

	@Override
	public MOVE execute(Game game) {
		GHOST nearestedibleghost;
		nearestedibleghost= MsPacmanUtils.getNearestGhost(game, game.getPacmanCurrentNodeIndex(), EatLimiterTimer(game), true);
		if (nearestedibleghost!=null)
		{
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestedibleghost), game.getPacmanLastMoveMade(), DM.EUCLID);	
		}
			return MOVE.NEUTRAL;
	}

	private int EatLimiterTimer(Game game) {
		switch (game.getGhostCurrentEdibleScore()) {
	    case 1600:
	    	return (int) (EATTIMELIMIT * 0.5);
	    case 800:
	    	return (int) (EATTIMELIMIT * 0.6);
	    case 400:
	    	return (int) (EATTIMELIMIT * 0.8);
	    case 200:
	    	return (int) (EATTIMELIMIT * 1.5);
	    default:
	    	return EATTIMELIMIT;
		}
	}
	
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}