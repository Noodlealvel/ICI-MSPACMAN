package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacmanInput extends Input{

	//Variables para almacenar informacion del game y saber cuando transicionar
	
	private GHOST nearestGhost;
	private GHOST nearestEdibleGhost;
	
	//private final static int ghostCloseRange = 15;
	private final static int ghostCloseMedium = 30;
	private final static int PPDistance = 30;
	private final static int EatLimit = 80;
	private int[] activePowerPills;
	private int[] activePills;
	private int[] pacmanNeighbors;
	private int pacmanPos;
	private boolean ghostsTogether;
	private boolean ghostsApart;
	private Map <GHOST,Integer> ghostEdibleTimes;
	private boolean PPeaten;
	private boolean noPPsleft;
	private boolean lotsofPPinZone;
	private boolean levelchange;
	private boolean ghostInPath;
	private int nextScore;
	private boolean ghostClose;
	private boolean edibleGhostClose;
	private boolean noPillsNear;
	private boolean dontChase;
	private boolean multipleGhostsClose;
	private boolean PPClose;
	private boolean ghostsFlanking;
	
	
	public MsPacmanInput(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void parseInput() {
		
		this.PPeaten=false;
		this.noPillsNear=true;
		this.dontChase=false;
		this.ghostsFlanking =false;
		
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
			
			shortestDistance = -1;
			distanceGhost = 0;
			for (GHOST ghost : GHOST.values()) {
					distanceGhost = game.getDistance(pacmanPos, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
					if((shortestDistance == -1 || distanceGhost < shortestDistance) && game.isGhostEdible(ghost) && distanceGhost <= EatLimit) {
						nearestGhost = ghost;
						shortestDistance = distanceGhost;
					}
				}
			
			if (nearestGhost!=null)
			{
				this.nearestEdibleGhost=nearestGhost;
				this.edibleGhostClose=true;
			}
			else 
			{
				this.edibleGhostClose=false;
			}
			
			//Para saber cuando no merece la pena perseguir y pasar de ataque a standard
			if(nearestEdibleGhost==null)
			{
				if (game.getGhostCurrentEdibleScore() < 800)
				{
					this.dontChase=true;
				}
			}
			
			//Cuantos fantasmas hay cerca de pacman
			int ghostsNearPacman = 0;
			for(GHOST ghost: GHOST.values()) {
				if(game.isGhostEdible(ghost)==false) {
					double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.EUCLID); 
					if(distance <= ghostCloseMedium) {
						ghostsNearPacman++;
					}
				}
			}
			
			if (ghostsNearPacman >=2)
				this.multipleGhostsClose = true;
			
			//Para saber si pacman tiene cerca una PP
				double powerPillDistance;
				shortestDistance = -1;
				int nearestPPill = -1;
				for(int pillNode : game.getActivePowerPillsIndices()) {
					powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, DM.EUCLID);
					if(powerPillDistance < shortestDistance || shortestDistance == -1) {
						shortestDistance = powerPillDistance;
						nearestPPill = pillNode;
						this.PPClose=true;
					}
				}
				
				//Para saber si hay fantasmas flanqueando
				List<Integer> nearPills = new ArrayList<Integer>();
				
				int ghostsInPath = 0;
				for(int pillNode : game.getPillIndices()) {
					if (game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, game.getPacmanLastMoveMade(), DM.EUCLID) <= 30 ) {
						nearPills.add(Integer.valueOf(pillNode));
					}
				}
				
				for (Integer pillNode : nearPills) {
					
					ghostsInPath = 0;
					int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), pillNode.intValue(), game.getPacmanLastMoveMade());
					for(int node : path) {
						for (GHOST ghosts : GHOST.values()) {
							if(game.getGhostCurrentNodeIndex(ghosts) == node) {
								ghostsInPath++;
							}
						}
					}
				}
				//Posible mejora
				if (ghostsInPath >= 2)
				{
					this.ghostsFlanking=true;
				}
			
	}

	//Getters
	public boolean getGhostClose() {
		return ghostClose;
	}
	
	public boolean getEdibleGhostClose()
	{
		return edibleGhostClose;
	}
	
	public boolean getPPEaten() {
		return PPeaten;
	}
	
	public boolean getNoPillsNear()
	{
		return noPillsNear;
	}
	
	public boolean getDontChase()
	{
		return dontChase;
	}
	
	public boolean getMultipleGhostClose()
	{
		return multipleGhostsClose;
	}

	public boolean getPPClose()
	{
		return PPClose;
	}
	
	public boolean getGhostsFlanking()
	{
		return ghostsFlanking;
	}
}
