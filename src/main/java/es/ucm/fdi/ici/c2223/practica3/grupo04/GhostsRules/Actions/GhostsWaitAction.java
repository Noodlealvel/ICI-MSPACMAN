package es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions;

import pacman.game.Game;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsWaitAction implements RulesAction{
	GHOST ghost;
	public GhostsWaitAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost.toString() + "wait";
	}

	@Override
	public MOVE execute(Game game) {
			return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		
	}
}
