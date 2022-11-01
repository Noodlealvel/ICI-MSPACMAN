package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanTransitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.fsm.Transition;

public class FewPPsInZone implements Transition {

	@Override
	public boolean evaluate(Input in) {
		MsPacmanInput input = (MsPacmanInput) in;
		return input.getLessPPsInZone();
	}

	public String toString()
	{
		return "FEW PPS IN ZONE";
	}
	
}
