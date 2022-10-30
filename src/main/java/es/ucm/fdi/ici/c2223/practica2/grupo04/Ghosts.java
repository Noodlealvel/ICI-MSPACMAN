package es.ucm.fdi.ici.c2223.practica2.grupo04;

import java.awt.BorderLayout;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsEdibleTransition;
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
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPacmanFarTransition2;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPacmanNearTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPacmanTunnelTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPathWithGhostsTransition;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Transitions.GhostsPathWithoutGhostsTransition;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.observers.ConsoleFSMObserver;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import es.ucm.fdi.ici.practica2.demofsm.ghosts.GhostsInput;
import gate.fsm.Transition;
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
			GhostsPacmanFarTransition2 pacmanFar2 = new GhostsPacmanFarTransition2(ghost);
			GhostsPacmanNearTransition pacmanNear = new GhostsPacmanNearTransition(ghost);
			GhostsLowEdibleTimeTransition lowEdibleTime = new GhostsLowEdibleTimeTransition(ghost);
			GhostsPacmanAndTunnelNearTransition pacmanAndTunnelNear = new GhostsPacmanAndTunnelNearTransition(ghost);
			
			GhostsNoPowerPillsTransition noPP = new GhostsNoPowerPillsTransition(ghost);
			
			GhostsFarAndNotEdibleTransition farAndNoEdible = new GhostsFarAndNotEdibleTransition(ghost);
			
			GhostsLevelChangeTransition levelChange = new GhostsLevelChangeTransition(ghost);
			
			GhostsEatenTransition eaten = new GhostsEatenTransition(ghost);
			
			GhostsOutOfLairTransition outOfLair = new GhostsOutOfLairTransition(ghost);
			
			GhostsEdibleTransition edible = new GhostsEdibleTransition(ghost);
			
			GhostsInPacmanRadiusTransition inPacmanRadius = new GhostsInPacmanRadiusTransition(ghost);

			//ataque
			FSM ataque = new FSM("Ataque");
			GraphFSMObserver ataqueObserver = new GraphFSMObserver(ataque.toString());
			ataque.addObserver(ataqueObserver);
			
			ataque.add(stopChase, pacmanTunnel, chase);
			ataque.add(stopChase, pathWithGhosts, flank);
			ataque.add(chase, justBehindPacman, stopChase);
			ataque.add(flank, pathWithoutGhosts, chase);
			
			ataque.ready(chase);
			CompoundState ataqueState = new CompoundState("Ataque", ataque);
			
			//defensa
			FSM defensa = new FSM("Defensa");
			GraphFSMObserver defensaObserver = new GraphFSMObserver(defensa.toString());
			defensa.addObserver(defensaObserver);
			
			defensa.add(flee, farAndCloseGhosts, disperse);
			defensa.add(flee, lowEdibleTime, goToPP);
			defensa.add(flee, pacmanFar, fleeFromPP);
			defensa.add(flee, pacmanAndTunnelNear, tunnel);
			defensa.add(disperse, otherGhostsFar, flee);
			defensa.add(fleeFromPP, pacmanNear, flee);
			defensa.add(tunnel,pacmanFar2 , fleeFromPP);

			defensa.ready(flee);
			CompoundState defensaState = new CompoundState("Defensa", defensa);
			
			//agresivo
			FSM agresivo = new FSM("Agresivo");
			GraphFSMObserver agresivoObserver = new GraphFSMObserver(agresivo.toString());
			agresivo.addObserver(agresivoObserver);
			
			agresivo.add(defendPills, pacmanFar, kill);
			agresivo.add(kill, pacmanNear, defendPills);
		
			agresivo.ready(defendPills);
			CompoundState agresivoState = new CompoundState("Agresivo", agresivo);
			
			//externos			
			fsm.add(defensaState, notEdible, ataqueState);
			fsm.add(ataqueState, danger, defensaState);
			fsm.add(defensaState, noPP, agresivoState);
			fsm.add(agresivoState, levelChange , wait);
			fsm.add(wait, outOfLair, ataqueState);
			fsm.add(defensaState, eaten, wait);
			fsm.add(defensaState, farAndNoEdible, regroup);
			fsm.add(regroup, edible, defensaState );
			fsm.add(regroup, inPacmanRadius , ataqueState);	
			
			fsm.ready(wait);
			
			//graphObserver.showInFrame(new Dimension(800,600));
			
			JFrame frame = new JFrame();
	    	JPanel main = new JPanel();
	    	main.setLayout(new BorderLayout());
	    	main.add(ataqueObserver.getAsPanel(true, null), BorderLayout.NORTH);
	    	main.add(defensaObserver.getAsPanel(true, null), BorderLayout.SOUTH);
	    	main.add(agresivoObserver.getAsPanel(true, null), BorderLayout.EAST);
	    	main.add(graphObserver.getAsPanel(true, null), BorderLayout.CENTER);
	    	frame.getContentPane().add(main);
	    	frame.pack();
	    	frame.setVisible(true);
	    	
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
