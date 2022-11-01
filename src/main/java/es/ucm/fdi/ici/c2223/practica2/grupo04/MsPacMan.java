package es.ucm.fdi.ici.c2223.practica2.grupo04;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions.*;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanTransitions.*;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	FSM fsm;
	public MsPacMan() {
		
		//Nombre de PacMan
		setName("MsPacMan04");
		setTeam("Team04");
		
		//Creamos maquina de estados
    	fsm = new FSM("MsPacMan");
    	GraphFSMObserver observer = new GraphFSMObserver(fsm.toString());
    	fsm.addObserver(observer);
    	
    	
    	//Creamos estado exterior de empezar mapa
    	SimpleState beginMap = new SimpleState("beginMap", new BeginMapAction());
    	
    	//Con sus transiciones
    	Transition ghostClose = new GhostClose();
    	Transition powerpillEaten = new PowerPillEaten();
    	Transition noPillsNearPacman = new noPillsNearPacman();
 
    	
    	//Creamos primer estado compuesto: standard
    	FSM cfsm1 = new FSM("STANDARD");
    	GraphFSMObserver c1observer = new GraphFSMObserver(cfsm1.toString());
    	cfsm1.addObserver(c1observer);
    	
    	//Estados de standard
    	SimpleState searchBetterZone = new SimpleState("searchBetterZone", new SearchBetterZoneAction());
    	SimpleState searchOptimalPath = new SimpleState("searchOptimalPath", new SearchOptimalPathAction());
    	SimpleState eatLastPills = new SimpleState("eatLastPills", new EatLastPillsAction());
    	
    	//Transiciones internas de standard
    	Transition fewPPsInZone = new FewPPsInZone();
    	Transition multiplePPsInZone = new MultiplePPsInZone();
    	Transition fewPillsAndNoPPsLeft = new FewPillsAndNoPPsleft();
    	
    	//Creamos las relaciones de estado - transicion - estado
    	cfsm1.add(searchBetterZone, multiplePPsInZone, searchOptimalPath);
    	cfsm1.add(searchOptimalPath, fewPPsInZone, searchBetterZone);
    	cfsm1.add(searchOptimalPath, fewPillsAndNoPPsLeft, eatLastPills);
    	cfsm1.ready(searchOptimalPath);
    	
    	CompoundState standard = new CompoundState("STANDARD", cfsm1);
    	
    	
    	//Creamos estado compuesto ataque
    	FSM cfsm2 = new FSM("ATTACK");
    	GraphFSMObserver c2observer = new GraphFSMObserver(cfsm2.toString());
    	cfsm2.addObserver(c2observer);
    	
    	//Estados de ataque
    	SimpleState searchOptimalPathTowardsEdibles = new SimpleState("searchOptimalPathTowardsEdibles", new SearchOptimalPathTowardsEdiblesAction());
    	SimpleState ediblesAndTogether = new SimpleState("ediblesAndTogether", new EdiblesAndTogetherAction());
    	SimpleState ediblesButApart = new SimpleState("ediblesButApart", new EdiblesButApartAction());
    	
    	//Transiciones internas de ataque
    	Transition edibleGhostClose = new EdibleGhostClose();
    	Transition edibleGhostsTogether = new EdibleGhostsTogether();
    	Transition edibleGhostsTogether2 = new EdibleGhostsTogether2();
    	Transition edibleGhostsApart = new EdibleGhostsApart();
    	
    	//Creamos las relaciones de estado - transicion - estado
    	cfsm2.add(searchOptimalPathTowardsEdibles, edibleGhostsTogether, ediblesAndTogether);
    	cfsm2.add(ediblesButApart, edibleGhostClose, searchOptimalPathTowardsEdibles);
    	cfsm2.add(ediblesButApart, edibleGhostsTogether2, ediblesAndTogether);
    	cfsm2.add(ediblesAndTogether, edibleGhostsApart, ediblesButApart);
    	cfsm2.ready(searchOptimalPathTowardsEdibles);
    	
    	CompoundState attack = new CompoundState("ATTACK", cfsm2);
    	
    	
    	//Creamos estado compuesto de defensa
    	FSM cfsm3 = new FSM("DEFENSE");
    	GraphFSMObserver c3observer = new GraphFSMObserver(cfsm3.toString());
    	cfsm3.addObserver(c3observer);
    	
    	//Estados de defensa
    	SimpleState flee = new SimpleState("flee", new FleeAction());
    	SimpleState chasePowerPill = new SimpleState("chasePowerPill", new ChasePowerPillAction());
    	SimpleState searchPathWithoutGhosts = new SimpleState("searchPathWithoutGhosts", new SearchPathWithoutGhostsAction());
    	SimpleState searchZoneWithPPAndNoGhosts = new SimpleState("searchZoneWithPPAndNoGhosts", new SearchZoneWithPPAndNoGhostsAction());
    	
    	//Transiciones de defensa
    	Transition ghostsFlanking = new GhostsFlanking();
    	Transition severalGhostsCloseAndPP = new SeveralGhostsCloseAndPP();
    	Transition severalGhostsCloseNoPP = new SeveralGhostsCloseNoPP();
    	Transition powerpillBlocked = new PowerPillBlocked();
    	Transition lessGhostsClose = new LessGhostsClose();
    	
    	//Creamos las relaciones de estado - transicion - estado
    	cfsm3.add(flee, ghostsFlanking, searchPathWithoutGhosts);
    	cfsm3.add(searchPathWithoutGhosts, ghostClose, flee);
    	cfsm3.add(chasePowerPill, powerpillBlocked, flee);
    	cfsm3.add(searchPathWithoutGhosts, severalGhostsCloseAndPP, chasePowerPill);
    	cfsm3.add(searchPathWithoutGhosts, severalGhostsCloseNoPP, searchZoneWithPPAndNoGhosts);
    	cfsm3.add(searchZoneWithPPAndNoGhosts, lessGhostsClose, flee);
    	cfsm3.ready(flee);
    	
    	CompoundState defense = new CompoundState("DEFENSE", cfsm3);
    	
    	
    	//Creamos relaciones externas de los estados compuestos y el suelto
    	
    	//Desde el estado exterior
    	fsm.add(beginMap, ghostClose, defense);
    	fsm.add(beginMap, noPillsNearPacman, standard);
    	fsm.add(beginMap, powerpillEaten, attack);
    	
    	
    	Transition standardToAttack = new StandardToAttackTransition();
    	Transition levelChange = new LevelChange();
    	Transition ghostClose2 = new GhostClose2();
    	
    	//Desde standard
    	fsm.add(standard, standardToAttack, attack);
    	fsm.add(standard, ghostClose2, defense);
    	fsm.add(standard, levelChange, beginMap);
    	
    	
    	Transition attackToStandard = new AttackToStandardTransition();
    	Transition ghostClose3= new GhostClose3();
    	Transition levelChange2 = new LevelChange2();
    	//Desde ataque
    	fsm.add(attack, attackToStandard, standard);
    	fsm.add(attack, ghostClose3, defense);
    	fsm.add(attack, levelChange2, beginMap);
    
    	
    	Transition ghostsTooFar = new GhostsTooFar();
    	Transition powerPillEaten2 = new PowerPillEaten2();
    	Transition levelChange3 = new LevelChange3();
    	//Desde defensa
    	fsm.add(defense, ghostsTooFar, standard);
    	fsm.add(defense, powerPillEaten2, attack);
    	fsm.add(defense, levelChange3, beginMap);

    	fsm.ready(beginMap);
    	
    	
    	
    	JFrame frame = new JFrame();
    	JPanel main = new JPanel();
    	main.setLayout(new BorderLayout());
    	main.add(observer.getAsPanel(true, null), BorderLayout.EAST);
    	main.add(c1observer.getAsPanel(true, null), BorderLayout.SOUTH);
    	main.add(c2observer.getAsPanel(true, null), BorderLayout.NORTH);
    	main.add(c3observer.getAsPanel(true, null), BorderLayout.WEST);
    	frame.getContentPane().add(main);
    	frame.pack();
    	frame.setVisible(true);
	}
	
	
	public void preCompute(String opponent) {
    		fsm.reset();
    }
	
	
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
       	Input in = new MsPacmanInput(game); 
    	return fsm.run(in);
    }

}
