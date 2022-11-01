package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsFarEdibleFarOthersTransition implements Transition {

	GHOST ghost;
	public GhostsFarEdibleFarOthersTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return !input.isPacmanInVecinity(ghost) && !input.closeToOtherGhosts(ghost);
	}
	
	@Override
	public String toString() {
		return ghost + " is far from Pacman, Edible, and far from other ghosts.";
	}
}