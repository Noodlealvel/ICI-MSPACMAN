package es.ucm.fdi.ici.c2223.practica1.grupo05;

import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public final class MsPacMan extends PacmanController{

	
	private Game game;
	
	//Variables sobre Pacman
	private MOVE nextMovePacMan;
	private int nextPill;
	private boolean danger = false;
	
	//Variables sobre los fantasmas	
	private GHOST edibleGhost = null;
	private GHOST hunterGhost = null;
	
	//Rangos de busqueda de fantasmas
	private int hunterRange = 40;
	private int edibleRange = 55;
	
	public int getNextPill() {
		return this.nextPill;
	}
	
	public void setNextPill(int nextPill) {
		this.nextPill = nextPill;
	}
	
	public MOVE getNextMovePacMan() {
		return this.nextMovePacMan;
	}
	
	public void setNextMovePacMan(MOVE nextMovePacMan) {
		this.nextMovePacMan = nextMovePacMan;
	}
	
	public GHOST getEdibleGhost() {
		return this.edibleGhost;
	}
	
	public void setEdibleGhost(GHOST comestible) {
		this.edibleGhost = comestible;
	}
	
	public GHOST getHunterGhost() {
		return this.hunterGhost;
	}
	
	public void setHunterGhost(GHOST perseguido) {
		this.hunterGhost = perseguido;
	}
	
	public boolean getDanger() {
		return this.danger;
	}
	
	public void setDanger(boolean danger) {
		this.danger = danger;
	}
	
	public int getHunterRange() {
		return this.hunterRange;
	}
	
	public void setHunterRange(int hunterRange) {
		this.hunterRange = hunterRange;
	}
	
	public int getEdibleRange() {
		return this.edibleRange;
	}
	
	public void setEdibleRange(int edibleRange) {
		this.edibleRange = edibleRange;
	}
	
	
	//Nombre del equipo y comportamiento
	public MsPacMan(){

			 super();
			 setName("MsPacMan05");
			 setTeam("Team05");
	 }
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		this.game = game;
		
		
		//Si tenemos pocas vidas vamos a lo seguro
		if(game.getPacmanNumberOfLivesRemaining() <= 2) {
			
			//Marcamos estado de tener cuidado y cambiamos rangos
			if (!getDanger()) {
				setDanger(true);
				setHunterRange(85);
				setEdibleRange(45);
			}
		}
		
		//Si hay algun fantasma comestible y muy cerca, lo priorizamos
		getNearestChasingOrEdibleGhosts(0);
		//Comprobamos que es el que esta mas cerca (anterior funcion) y que hay tiempo de ir a por el
		if(getEdibleGhost() != null && game.getGhostEdibleTime(getEdibleGhost()) > 25) {
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(getEdibleGhost()), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		/*//Si hay un perseguidor en el rango, escapamos
		getNearestChasingOrEdibleGhosts(1);
		if(getHunterGhost() != null) {
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(getHunterGhost()), game.getPacmanLastMoveMade(), DM.PATH);
		}*/
		
		//Calculamos la PowerPill mas cercana/pill
		setNextPill(moveTowardsNearestPill());
			
		//Calculamos el proximo MOVE
		pacManNextMove();
		
		
		return getNextMovePacMan();
	}
	
	
	//Funcion que permite moverse hacia la pill o la powerPill mas cercana
	private int moveTowardsNearestPill() {
		
		int nextPill;
		getNearestChasingOrEdibleGhosts(1);
		
		//Si no te persiguen o no hay powerpills, coges pills
		if(getHunterGhost() == null || game.getNumberOfActivePowerPills() == 0) {
			nextPill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.PATH);
			
		}
		//Si te persiguen y hay powerpills activas, priorizas powerpills
		else {
			nextPill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.PATH);
		}
		
		return nextPill;
	}
	
	
	//Funcion que permite conocer el proximo movimiento en funcion de la informacion obtenida
	private void pacManNextMove() {
		
		int i = 0, pathIsFound = 0;
		
		//Obtenemos los nodos vecinos a partir de la posicion de Pacman y su ultimo MOVE
		int[] neighbourghood = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		
		//Para guardar el camino usamos una matriz
		int[][] path = new int[neighbourghood.length][];
		
		//Para cada nodo vecino...
		for(i = 0; i<neighbourghood.length; i++){
			
			//...Comprobamos los caminos hacia powerpills/pills que no tengan fantasmas
			
			//Guardamos en la matriz el camino mas corto
			path[i] = game.getShortestPath(neighbourghood[i], getNextPill());
			
			getNearestChasingOrEdibleGhosts(1);
			
			//Si el camino tiene la pill o powerpill que buscamos, no hay fantasmas y no esta perseguido...
			if(pathContainsPills(getNextPill(), path[i]) && !pathContainsGhosts(path[i]) /*&& getHunterGhost() == null*/){
				pathIsFound++;
			}
			//Si no lo descartamos
			else {
				path[i] = null;
			}
		}
		
		//Si hemos encontrado caminos
		if(pathIsFound != 0) {
			
			//Si hay solo uno, es ya el mas corto
			if(path.length == 1) {
				setNextMovePacMan(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path[0][0], DM.PATH));
			}
			else {
				// Nodo con el cual se llega por el camino mas rapido, ya que puede haber varios
				int node = 0;
	            int minimum = 99999;
	            
	            for(int[] a : path){
	            
	            	//Comprobamos primero si nos servia
	                if(a != null){
	                	
	                	//Obtenemos longitud para saber el mas corto                  
	                    if(a.length < minimum) {
	                    	
	                        minimum = a.length;
	                        
	                        //El siguiente movimiento de ese camino mas corto es el primer nodo
	                        node = a[0];
	                        setNextMovePacMan(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), node, DM.PATH));
	                    }
	                }
	            }
			}
		}
		
		//Si no hemos encontrado el camino, huimos del mas cercano
		else {
			getNearestChasingOrEdibleGhosts(1);
			if(getHunterGhost() != null) {
				setNextMovePacMan(game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(getHunterGhost()), DM.PATH));
			}
		}
	}
	
	//Funcion que permite saber los fantasmas que persiguen y los comestibles
	private void getNearestChasingOrEdibleGhosts(int option) {
		
		int [] ghostPositions = new int[4];
		int i = 0;
		
		//Si option es 0, buscamos los fantasmas comestibles
		if(option == 0) {
			
			//Guardamos los fantasmas comestibles
			for (GHOST ghost : GHOST.values()) {
				if(game.isGhostEdible(ghost)) {
					ghostPositions[i] = game.getGhostCurrentNodeIndex(ghost);
					i++;
				}
			}
			
			setEdibleGhost(null);
			
			//Si hay...
			if(ghostPositions.length > 0) {
				
				//Obtenemos el fantasma mas cercano de los que hay
				int nearest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostPositions, DM.PATH);
				
				//Si el fantasma es el mas cercano y esta dentro del rango...
				for (GHOST ghost : GHOST.values()) {
					if(game.getGhostCurrentNodeIndex(ghost) == nearest && game.getDistance(nearest, game.getPacmanCurrentNodeIndex(), DM.PATH) <= getEdibleRange()) {
						setEdibleGhost(ghost);
					}
				}
			}
		}
		
		//Si es 1, buscamos los fantasmas perseguidores
		else {
			
			//Guardamos los fantasmas que no son comestibles y no estan en la carcel
			for (GHOST ghost : GHOST.values()) {
				if(game.isGhostEdible(ghost) == false && game.getGhostLairTime(ghost) == 0) {
					ghostPositions[i] = game.getGhostCurrentNodeIndex(ghost);
					i++;
				}
			}
			
			setHunterGhost(null);
			
			//Si hay...
			if(ghostPositions.length > 0) {
				
				//Obtenemos el fantasma mas cercano de los que hay
				int nearest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostPositions, DM.PATH);

				//Si el fantasma es el mas cercano y esta dentro del rango...
				for (GHOST ghost : GHOST.values()) {
					if(game.getGhostCurrentNodeIndex(ghost) == nearest && game.getDistance(nearest, game.getPacmanCurrentNodeIndex(), DM.PATH) <= getHunterRange()){
						setHunterGhost(ghost);
					}
				}
			}
		}
	}
	
	
	//Funcion que nos permite saber si el camino escogido tiene la pill que queremos
	private boolean pathContainsPills(int pill, int[] path) {
		
		boolean contains = false;
		
		if (path != null) {
			for (int node : path) {
				if (pill == node) {
					contains = true;
				}
			}
		}
		return contains;
	}
	
	
	//Funcion que nos permite comprobar si en el camino escogido hay fantasmas
	private boolean pathContainsGhosts(int[] path) {
		
		boolean contains = false;
		
		if (path != null) {
			for (int node : path) {
				for (GHOST ghosts : GHOST.values()) {
					if (game.getGhostCurrentNodeIndex(ghosts) == node) {
						contains = true;
					}
				}
			}
		}
		return contains;
	}
	
}
