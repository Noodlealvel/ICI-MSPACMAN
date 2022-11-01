package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanTransitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.fsm.Transition;

public class EdibleGhostClose implements Transition {

	@Override
	public boolean evaluate(Input in) {
		MsPacmanInput input = (MsPacmanInput) in;
		return input.getEdibleGhostClose();
	}
	
	public String toString()
	{
		return "EDIBLE GHOSTS CLOSE";
	}

	
}
