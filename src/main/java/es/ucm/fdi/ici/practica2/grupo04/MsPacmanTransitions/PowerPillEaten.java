package es.ucm.fdi.ici.practica2.grupo04.MsPacmanTransitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.fsm.Transition;

public class PowerPillEaten implements Transition {

	@Override
	public boolean evaluate(Input in) {
		MsPacmanInput input = (MsPacmanInput) in;
		return input.getPPEaten();
	}

	public String toString()
	{
		return "POWER PILL EATEN";
	}
}
