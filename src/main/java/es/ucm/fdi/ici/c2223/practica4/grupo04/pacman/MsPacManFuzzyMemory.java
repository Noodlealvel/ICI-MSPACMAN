package es.ucm.fdi.ici.c2223.practica4.grupo04.pacman;

public class MsPacManFuzzyMemory {

	
	//GUARDAR DISTANCIAS Y POSICIONES PARA USARLAS EN LAS ACCIONES PARA COMER A PINKY O HUIR O YOKSE
	
	public static int[] ghostsPositions;
	public static int pacManPosition;
	
	public MsPacManFuzzyMemory(int[] ghostsPositions, int pacManPosition) {
		
		this.ghostsPositions =  ghostsPositions;
		this.pacManPosition = pacManPosition;		
	}

	public static int[] getGhostsPositions() {
		return ghostsPositions;
	}

	public static int getPacManPosition() {
		return pacManPosition;
	}

	public void setGhostsPositions(int[] ghostsPositions) {
		this.ghostsPositions = ghostsPositions;
	}

	public void setPacManPosition(int pacManPosition) {
		this.pacManPosition = pacManPosition;
	}
	
}
