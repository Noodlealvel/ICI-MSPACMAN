package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM;

import java.util.Map;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacmanInput extends Input{

	//Variables para almacenar informacion del game y saber cuando transicionar
	
	private final static int ghostClose = 15;
	private final static int ghostCloseMedium = 30;
	private final static int PPDistance = 90;
	private boolean ghostsTogether;
	private boolean ghostsApart;
	private boolean edibleGhostClose;
	private Map <GHOST,Integer> ghostEdibleTimes;
	private boolean PPeaten;
	private boolean noPPsleft;
	private boolean lotsofPPinZone;
	private boolean levelchange;
	private boolean ghostInPath;
	private int nextScore;
	private boolean multipleGhostsClose;
	
	
	
	
	
	public MsPacmanInput(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseInput() {
		// TODO Auto-generated method stub
		
	}

}
