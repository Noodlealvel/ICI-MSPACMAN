package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

public class GameMemory {

	private int currentLevel = -1;
	private int lastLevel = -1;

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void setLastLevel() {
		this.lastLevel = this.currentLevel;
	}

	public int getLastLevel() {
		return lastLevel;
	}
	
}
