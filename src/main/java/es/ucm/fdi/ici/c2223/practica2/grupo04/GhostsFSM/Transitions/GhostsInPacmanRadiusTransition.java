package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsInPacmanRadiusTransition implements Transition {

	GHOST ghost;
	public GhostsInPacmanRadiusTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return input.isPacmanInVecinity(ghost) && !input.isGhostEdible(ghost);
	}
	
	@Override
	public String toString() {
		return ghost.toString() + "is in pacman radius and is not edible";
	}

}
