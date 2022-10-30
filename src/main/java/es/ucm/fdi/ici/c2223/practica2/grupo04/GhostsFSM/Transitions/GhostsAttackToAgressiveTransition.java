package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsAttackToAgressiveTransition implements Transition {

	GHOST ghost;
	public GhostsAttackToAgressiveTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return input.noPowerPills();
	}
	
	@Override
	public String toString() {
		return ghost.toString() + " goes from Attack to Agressive because there are no PPills";
	}

}
