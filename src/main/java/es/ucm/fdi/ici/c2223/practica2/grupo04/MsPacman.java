package es.ucm.fdi.ici.c2223.practica2.grupo04;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import es.ucm.fdi.ici.practica2.demofsm.mspacman.MsPacManInput;
import es.ucm.fdi.ici.practica2.demofsm.mspacman.actions.RandomAction;
import es.ucm.fdi.ici.practica2.demofsm.mspacman.transitions.RandomTransition;
import es.ucm.fdi.ici.practica2.grupo04.MsPacmanActions.*;
import es.ucm.fdi.ici.practica2.grupo04.MsPacmanTransitions.*;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacman extends PacmanController {

	FSM fsm;
	public MsPacman() {
		
		//Nombre de PacMan
		setName("MsPacMan 05");
		
		//Creamos maquina de estados
    	fsm = new FSM("MsPacMan");
    	GraphFSMObserver observer = new GraphFSMObserver(fsm.toString());
    	fsm.addObserver(observer);
    	
    	
    	//Creamos estado exterior de empezar mapa
    	SimpleState beginMap = new SimpleState("beginMap", new BeginMapAction());
    	
    	//Con sus transiciones
    	Transition ghostNearPacman = new GhostNearPacman();
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
    	Transition = new GhostNearPacman();
    	Transition = new PowerPillEaten();
    	Transition = new noPillsNearPacman();
    	
    	//Creamos las relaciones de estado - transicion - estado
    	cfsm1.add(searchBetterZone, muvhsahha, searchOptimalPath);
    	cfsm1.add(searchOptimalPath, muvhsahha, searchBetterZone);
    	cfsm1.add(searchOptimalPath, muvhsahha, eatLastPills);
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
    	Transition = new GhostNearPacman();
    	Transition = new PowerPillEaten();
    	Transition = new noPillsNearPacman();
    	
    	//Creamos las relaciones de estado - transicion - estado
    	cfsm2.add(searchOptimalPathTowardsEdibles, muvhsahha, ediblesAndTogether);
    	cfsm2.add(ediblesButApart, muvhsahha, searchOptimalPathTowardsEdibles);
    	cfsm2.add(ediblesButApart, muvhsahha, ediblesAndTogether);
    	cfsm2.add(ediblesAndTogether, muvhsahha, ediblesButApart);
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
    	
    	//Creamos las relaciones de estado - transicion - estado
    	cfsm3.add(flee, ghostsFlanking, searchPathWithoutGhosts);
    	cfsm3.add(searchPathWithoutGhosts, muvhsahha, flee);
    	cfsm3.add(chasePowerPill, muvhsahha, flee);
    	cfsm3.add(searchPathWithoutGhosts, severalGhostsCloseAndPP, chasePowerPill);
    	cfsm3.add(searchPathWithoutGhosts, severalGhostsCloseNoPP, searchZoneWithPPAndNoGhosts);
    	cfsm3.add(searchZoneWithPPAndNoGhosts, muvhsahha, flee);
    	cfsm3.ready(flee);
    	
    	CompoundState defense = new CompoundState("DEFENSE", cfsm3);
    	
    	
    	//Creamos relaciones externas de los estados compuestos y el suelto
    	
    	//Desde el estado exterior
    	fsm.add(beginMap, ghostNearPacman, defense);
    	fsm.add(beginMap, noPillsNearPacman, standard);
    	fsm.add(beginMap, powerpillEaten, attack);
    	
    	
    	Transition standardToAttack = new StandardToAttackTransition();
    	
    	//Desde standard
    	fsm.add(standard, standardToAttack, attack);
    	fsm.add(standard, ghostNearPacman, defense);
    	fsm.add(standard, tran4, beginMap);
    	
    	
    	Transition attackToStandard = new AttackToStandardTransition();
    	
    	//Desde ataque
    	fsm.add(attack, attackToStandard, standard);
    	fsm.add(attack, ghostNearPacman, defense);
    	fsm.add(attack, tran4, beginMap);
    
    	//Desde defensa
    	fsm.add(defense, tran4, standard);
    	fsm.add(defense, powerpillEaten, attack);
    	fsm.add(defense, tran4, beginMap);

    	fsm.ready(beginMap);
    	
    	
    	JFrame frame = new JFrame();
    	JPanel main = new JPanel();
    	main.setLayout(new BorderLayout());
    	main.add(observer.getAsPanel(true, null), BorderLayout.CENTER);
    	main.add(c1observer.getAsPanel(true, null), BorderLayout.SOUTH);
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
       	Input in = new MsPacManInput(game); 
    	return fsm.run(in);
    }

}
