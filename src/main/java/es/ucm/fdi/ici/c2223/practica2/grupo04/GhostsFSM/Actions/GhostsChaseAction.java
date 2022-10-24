package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsChaseAction implements Action {


	GHOST ghost;
	public GhostsChaseAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost + "AgressiveChase";
	}


	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			//TODO Añadir comportamiento para que no se cruce con otros fantasmas 
			return null;
        }
		else
			return MOVE.NEUTRAL;
	}

}
