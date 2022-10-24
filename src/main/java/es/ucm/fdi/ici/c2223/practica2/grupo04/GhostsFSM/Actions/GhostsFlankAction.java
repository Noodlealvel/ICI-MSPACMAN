package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsFlankAction implements Action {


	GHOST ghost;
	public GhostsFlankAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost.toString() + "Disperse";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			//TODO a�adir comportamiento, quiz�s que vaya a punto intermedio entre Pacman y PPill m�s cercana salvo que est� a cierto radioy/o llegue antes a ella que este.
			return null;
        }
		else
			return MOVE.NEUTRAL;
		}

}
