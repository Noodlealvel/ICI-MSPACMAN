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
	private final int ghostCloseMedium = 40;
	private final int PPDistance = 60;
	private final int EatLimit = 55;
	private final int PPDistanceInZone = 130;
	private int[] activePowerPills;
	private int[] pacmanNeighbors;
	private int pacmanPos;
	private boolean PPeaten;
	private boolean multiplePPsInZone;
	private boolean ghostClose;
	private boolean edibleGhostClose;
	private boolean noPillsNear;
	private boolean dontChase;
	private boolean multipleGhostsClose;
	private boolean lessGhostsClose;
	private boolean PPClose;
	private boolean ghostsFlanking;
	private boolean levelChange;
	private boolean PPBlocked;
	private boolean noPPsleft;
	private boolean fewPillsleft;
	private boolean edibleGhostsTogether;
	private boolean lessPPsInZone;
	public MsPacmanInput(Game game) {
		super(game);
		
	}

	@Override
	public void parseInput() {

		this.noPillsNear = true;
		this.dontChase = false;
		this.ghostsFlanking = false;
		this.edibleGhostsTogether=false;
		this.lessPPsInZone=false;
		this.fewPillsleft= game.getNumberOfActivePills() <= 15;
		this.noPPsleft = game.getNumberOfActivePowerPills() == 0;
		this.activePowerPills = game.getActivePowerPillsIndices();
		this.pacmanPos = game.getPacmanCurrentNodeIndex();
		this.PPeaten = game.wasPowerPillEaten();

		pacmanNeighbors = game.getNeighbouringNodes(pacmanPos, game.getPacmanLastMoveMade());
		for (int node : pacmanNeighbors) {
			if (game.isPillStillAvailable(node)) {
				noPillsNear = false;
			}

		}
		
		nearestGhost = MsPacmanUtils.getNearestGhost(game, game.getPacmanCurrentNodeIndex(), true);

		if (nearestGhost != null) {
			this.nearestEdibleGhost = nearestGhost;
			this.edibleGhostClose = true;
		} else {
			this.edibleGhostClose = false;
		}

		// Para saber cuando no merece la pena perseguir y pasar de ataque a standard
		if (nearestEdibleGhost == null) {
			if (game.getGhostCurrentEdibleScore() < 800) {
				this.dontChase = true;
				}
			}
		

		// Cuantos fantasmas hay cerca de pacman
		List<GHOST> ghostsNearPacman = new ArrayList<GHOST>();
		for (GHOST ghost : GHOST.values()) {
			if (game.isGhostEdible(ghost) == false && game.getGhostLairTime(ghost) == 0) {
				double distance = game.getDistance(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH);
				if (distance <= ghostCloseMedium) {
					ghostsNearPacman.add(ghost);
				}
			}
		}

		if (ghostsNearPacman.size() >= 2)
			this.multipleGhostsClose = true;
		else
			this.lessGhostsClose=true;
		
		List<GHOST> eliminado = new ArrayList<GHOST>();
		//Para saber si esos fantasmas están flanqueando	
		if (ghostsNearPacman != null) {
			for (GHOST ghost : ghostsNearPacman) {
				if (ghost != null && game.getGhostLairTime(ghost) == 0) {
					int[] ghostspath = game.getShortestPath(game.getPacmanCurrentNodeIndex(),
							game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
					for (int node1 : ghostspath) {
						for (GHOST ghosts : GHOST.values()) {
							if (game.getGhostCurrentNodeIndex(ghosts) == node1) {
								eliminado.add(ghosts);
							}
						}
					}
				}
			}
			for(GHOST g : eliminado) {
				ghostsNearPacman.remove(g);
			}

			switch (game.getNeighbouringNodes(pacmanPos, game.getPacmanLastMoveMade()).length) {
			case 2:
				if (ghostsNearPacman.size() >= 2) {
					this.ghostsFlanking = true;
					break;
				}
			case 3: {
				if (ghostsNearPacman.size() >= 3) {
					this.ghostsFlanking = true;
					break;
				}
			}
			case 1:
			default:
			}
		}
		
		//Para saber si hay fantasmas comestibles juntos
		List<GHOST> ghostsNearghost;
		for (GHOST ghost : GHOST.values()) {
			ghostsNearghost = new ArrayList<GHOST>();
			if (game.isGhostEdible(ghost) == true && game.getDistance(pacmanPos, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID) < EatLimit) {
				for (GHOST ghosts : GHOST.values()) {
					if (game.isGhostEdible(ghost) == false && game.getGhostLairTime(ghost) == 0) {
						double distance = game.getDistance(game.getGhostCurrentNodeIndex(ghost),
								game.getGhostCurrentNodeIndex(ghosts), game.getGhostLastMoveMade(ghost), DM.PATH);
						if (distance <= ghostCloseMedium) {
							ghostsNearghost.add(ghost);
						}
					}
				}

				if (ghostsNearghost.size() >= 3) {
					this.edibleGhostsTogether = true;
					break;
				}
			}
		}
		
		
		
		// Para saber si pacman tiene cerca una PP
		int closestPP = MsPacmanUtils.getNearestPP(game, PPDistance);
		this.PPClose = closestPP != -1;

		// Para saber si dicha PP está bloqueada por fantasmas

		if(closestPP!=-1)
		{
			int[] PPpath = game.getShortestPath(game.getPacmanCurrentNodeIndex(), closestPP,
					game.getPacmanLastMoveMade());
			for (int node : PPpath) {
				for (GHOST ghosts : GHOST.values()) {
					if (game.getGhostCurrentNodeIndex(ghosts) == node) {
						this.PPBlocked = true;
						break;
					}
				}
			}
		}
	

		// Para saber si hay muchas PPs en la zona de Pacman
		
		int PPsinzone = 0;
		if(activePowerPills.length>=2)
		{
			for (int PPillnode: activePowerPills)
			{
				if (game.getShortestPathDistance(pacmanPos, PPillnode, game.getPacmanLastMoveMade()) < PPDistanceInZone)
				{
					PPsinzone++;
				}
			}
			
			if (PPsinzone>=2)
			{
				this.multiplePPsInZone=true;
			}	
			else
			{
				this.lessPPsInZone=true;
			}
		}
		
		// Para saber si cambiamos de nivel
		if ((game.getNumberOfActivePills() == game.getNumberOfPills())
				&& (game.getNumberOfActivePowerPills() == game.getNumberOfPowerPills())) {
			this.levelChange = true;
		}
		
		GHOST nearestGhost = MsPacmanUtils.getNearestGhostAtDistance(game, pacmanPos, ghostCloseMedium, false);

		if (nearestGhost != null) {
			this.nearestGhost = nearestGhost;
			this.ghostClose = true;
		} else {
			this.ghostClose = false;
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
	
	public boolean getLessGhostsClose()
	{
		return lessGhostsClose;
	}

	public boolean getPPClose()
	{
		return PPClose;
	}
	
	public boolean getGhostsFlanking()
	{
		return ghostsFlanking;
	}
	
	public boolean getLevelChange()
	{
		return levelChange;
	}
	
	public boolean getPPBlocked()
	{
		return PPBlocked;
	}

	public boolean getNoPPsleft() {
		return noPPsleft;
	}

	public boolean getFewPillsleft() {
		return fewPillsleft;
	}
	
	public boolean getMultiplePPsInZone()
	{
		return multiplePPsInZone;
	}
	
	public boolean getLessPPsInZone()
	{
		return lessPPsInZone;
	}
	
	public boolean getEdibleGhostsTogether()
	{
		return edibleGhostsTogether;
	}
	
}
