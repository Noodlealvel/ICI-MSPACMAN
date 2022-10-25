package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM;

import java.util.Arrays;
import java.util.Map;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacmanInput extends Input{

	//Variables para almacenar informacion del game y saber cuando transicionar
	
	private GHOST nearestGhost;
	
	//private final static int ghostCloseRange = 15;
	private final static int ghostCloseMedium = 30;
	private final static int PPDistance = 90;
	private int[] activePowerPills;
	private int[] activePills;
	private int[] pacmanNeighbors;
	private int pacmanPos;
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
	private boolean ghostClose;
	private boolean noPillsNear;
	
	
	
	public MsPacmanInput(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void parseInput() {
		
		this.PPeaten=false;
		this.noPillsNear=true;
		
		activePowerPills=game.getActivePowerPillsIndices();
		pacmanPos=game.getPacmanCurrentNodeIndex();
		for (int pill: activePowerPills)
		{
			if (pacmanPos == pill)
			{
				this.PPeaten=true;
			}
		}
		
		activePills=game.getActivePillsIndices();
		pacmanNeighbors= game.getNeighbouringNodes(pacmanPos, game.getPacmanLastMoveMade());
		for (int node: pacmanNeighbors)
		{
			if (Arrays.asList(activePills).contains(node))
			{
				noPillsNear=false;
			}
			
		}
		
		GHOST nearestGhost = null;
			double shortestDistance = -1;
			double distanceGhost = 0;
			for (GHOST ghost : GHOST.values()) {
					distanceGhost = game.getDistance(pacmanPos, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
					if((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= ghostCloseMedium) {
						nearestGhost = ghost;
						shortestDistance = distanceGhost;
					}
				}
			
			if (nearestGhost!=null)
			{
				this.nearestGhost=nearestGhost;
				this.ghostClose=true;
			}
			else 
			{
				this.ghostClose=false;
			}
			
			
			
	}

	public boolean getGhostClose() {
		return ghostClose;
	}
	
	public boolean getPPEaten() {
		return PPeaten;
	}
	
	public boolean getNoPillsNear()
	{
		return noPillsNear;
	}
	

}
