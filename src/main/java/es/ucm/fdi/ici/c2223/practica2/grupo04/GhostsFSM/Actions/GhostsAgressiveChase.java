package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsAgressiveChase implements Action {

	GHOST ghost;
	public GhostsAgressiveChase( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return "GhostsAgressiveChase";
	}


	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			//TODO Comportamiento por implementar 
			return null;
        }
		else
			return MOVE.NEUTRAL;
	}

}
