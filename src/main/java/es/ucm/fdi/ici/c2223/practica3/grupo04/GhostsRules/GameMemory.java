package es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules;

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
