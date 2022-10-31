package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsChaseToFlankTransition implements Transition{
	GHOST ghost;
	public GhostsChaseToFlankTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput)in;
		return  input.ghostsInPath(ghost) || (input.nearToPacman(ghost) && input.sameLastMovement(ghost));
	}
	
	@Override
	public String toString() {
		return ghost.toString() + " has other ghosts in the closest path to Pacman";
	}
}
