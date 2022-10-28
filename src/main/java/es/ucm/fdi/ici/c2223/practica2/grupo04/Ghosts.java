package es.ucm.fdi.ici.c2223.practica2.grupo04;

import java.awt.Dimension;
import java.util.EnumMap;

import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsChaseAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsDefendLastPillsAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsDisperseAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsFlankAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsFleeAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsFleeFromPPillAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsGoToPPillAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsKillPacmanAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsRegroupAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsSearchForTunnelAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsStopChasingAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions.GhostsWaitAction;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsDangerTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsEatenTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsFarAndCloseGhostsTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsFarAndNotEdibleTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsInPacmanRadiusTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsJustBehindPacmanTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsLevelChangeTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsLowEdibleTimeTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsNoPowerPillsTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsNotEdibleTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsOtherGhostsFarTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsOutOfLairTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPacmanAndTunnelNearTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPacmanFarTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPacmanNearTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPacmanTunnelTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPathWithGhostsTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPathWithoutGhostsTransition;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.observers.ConsoleFSMObserver;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.GhostsInput;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.GhostsNotEdibleAndPacManFarPPill;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.transitions.PacManNearPPillTransition;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	EnumMap<GHOST,FSM> fsms;
	public Ghosts()
	{
		setName("Ghosts 04");

		fsms = new EnumMap<GHOST,FSM>(GHOST.class);
		for(GHOST ghost: GHOST.values()) {
			FSM fsm = new FSM(ghost.name());
			fsm.addObserver(new ConsoleFSMObserver(ghost.name()));
			GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
			fsm.addObserver(graphObserver);

			//ataque
			SimpleState chase = new SimpleState(new GhostsChaseAction(ghost));
			SimpleState stopChase = new SimpleState(new GhostsStopChasingAction(ghost));
			SimpleState flank = new SimpleState(new GhostsFlankAction(ghost));
			
			//defensa
			SimpleState flee = new SimpleState(new GhostsFleeAction(ghost));
			SimpleState disperse = new SimpleState(new GhostsDisperseAction(ghost));
			SimpleState fleeFromPP = new SimpleState(new GhostsFleeFromPPillAction(ghost));
			SimpleState goToPP = new SimpleState(new GhostsGoToPPillAction(ghost));
			SimpleState tunnel = new SimpleState(new GhostsSearchForTunnelAction(ghost));

			//persecucion agresiva
			SimpleState defendPills = new SimpleState(new GhostsDefendLastPillsAction(ghost));
			SimpleState kill = new SimpleState(new GhostsKillPacmanAction(ghost));
			
			SimpleState regroup = new SimpleState(new GhostsRegroupAction(ghost));
			
			SimpleState wait = new SimpleState(new GhostsWaitAction(ghost));
			
			
			//ataque
			GhostsPathWithoutGhostsTransition pathWithoutGhosts = new GhostsPathWithoutGhostsTransition(ghost);
			GhostsPathWithGhostsTransition pathWithGhosts = new GhostsPathWithGhostsTransition(ghost);
			GhostsPacmanTunnelTransition pacmanTunnel = new GhostsPacmanTunnelTransition(ghost);
			GhostsJustBehindPacmanTransition justBehindPacman = new GhostsJustBehindPacmanTransition(ghost);
			
			GhostsNotEdibleTransition notEdible = new GhostsNotEdibleTransition(ghost);
			GhostsDangerTransition danger = new GhostsDangerTransition(ghost);
			
			//defensa
			GhostsFarAndCloseGhostsTransition farAndCloseGhosts = new GhostsFarAndCloseGhostsTransition(ghost);
			GhostsOtherGhostsFarTransition otherGhostsFar = new GhostsOtherGhostsFarTransition(ghost);
			GhostsPacmanFarTransition pacmanFar = new GhostsPacmanFarTransition(ghost);
			GhostsPacmanNearTransition pacmanNear = new GhostsPacmanNearTransition(ghost);
			GhostsLowEdibleTimeTransition lowEdibleTime = new GhostsLowEdibleTimeTransition(ghost);
			GhostsPacmanAndTunnelNearTransition pacmanAndTunnelNear = new GhostsPacmanAndTunnelNearTransition(ghost);
			
			GhostsNoPowerPillsTransition noPP = new GhostsNoPowerPillsTransition(ghost);
			
			GhostsFarAndNotEdibleTransition farAndNoEdible = new GhostsFarAndNotEdibleTransition(ghost);
			
			GhostsLevelChangeTransition levelTransition = new GhostsLevelChangeTransition(ghost);
			
			GhostsEatenTransition eaten = new GhostsEatenTransition(ghost);
			
			GhostsOutOfLairTransition outOfLair = new GhostsOutOfLairTransition(ghost);
			
			GhostsInPacmanRadiusTransition inPacmanRadius = new GhostsInPacmanRadiusTransition(ghost);

					
			fsm.add(chase, edible, runAway);
			fsm.add(chase, near, runAway);
			fsm.add(runAway, toChaseTransition, chase);
			
			fsm.ready(chase);
			
			graphObserver.showInFrame(new Dimension(800,600));
			
			fsms.put(ghost, fsm);
		}
	}
	
	public void preCompute(String opponent) {
    	for(FSM fsm: fsms.values())
    		fsm.reset();
    }
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);
		
		GhostsInput in = new GhostsInput(game);
		
		for(GHOST ghost: GHOST.values())
		{
			FSM fsm = fsms.get(ghost);
			MOVE move = fsm.run(in);
			result.put(ghost, move);
		}
		
		return result;
		
	
		
	}

}
