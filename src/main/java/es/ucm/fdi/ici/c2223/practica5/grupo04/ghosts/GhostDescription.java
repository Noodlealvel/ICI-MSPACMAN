package es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;

public class GhostDescription implements CaseComponent {

	Integer id;
	
GHOST ghost;
	
	Integer level;

	Double BlinkyDistance;
	Double PinkyDistance;
	Double InkyDistance;
	Double SueDistance;
	
	Double PacmanDBlinky;
	Double PacmanDPinky;
	Double PacmanDInky;
	Double PacmanDSue;
	
	Integer PPillDistance;

	Integer timeEdible;
	
	Boolean noPPills;
	
	Double ghostsCloseIndex;

	private Integer score;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setBlinkyDistance(double blinkyDistance) {
		BlinkyDistance = blinkyDistance;
	}

	public void setPinkyDistance(double pinkyDistance) {
		PinkyDistance = pinkyDistance;
	}

	public void setInkyDistance(double inkyDistance) {
		InkyDistance = inkyDistance;
	}

	public void setSueDistance(double sueDistance) {
		SueDistance = sueDistance;
	}

	public void setPacmanDBlinky(double pacmanDBlinky) {
		PacmanDBlinky = pacmanDBlinky;
	}

	public void setPacmanDPinky(double pacmanDPinky) {
		PacmanDPinky = pacmanDPinky;
	}

	public void setPacmanDInky(double pacmanDInky) {
		PacmanDInky = pacmanDInky;
	}

	public void setPacmanDSue(double pacmanDSue) {
		PacmanDSue = pacmanDSue;
	}
	

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}
	

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", level=" + level +", ghost=" + ghost + 
				", BlinkyDistance=" + BlinkyDistance + ", PinkyDistance=" + PinkyDistance 
				+ ", InkyDistance=" + InkyDistance + ", SueDistance=" + SueDistance + 
				", PacmanDBlinky=" + PacmanDBlinky + ", PacmanDPinky=" + PacmanDPinky + 
				", PacmanDInky=" + PacmanDInky + ", PacmanDSue=" + PacmanDSue + ", noPPills=" + noPPills +
				", nearestPPill=" + PPillDistance + ", ghostCloseIndex=" + ghostsCloseIndex + ", edibleTime=" + timeEdible +
				"]";
	}

	public void setLevel(Integer level2) {
		this.level = level2;
	}

	public void setGhost(GHOST ghost2) {
		this.ghost = ghost2;
	}

	public void setPPillDistance(Integer pPillDistance2) {
		this.PPillDistance = pPillDistance2;
	}

	public void setTimeEdible(Integer timeEdible2) {
		this.timeEdible = timeEdible2;
		
	}

	public void setNoPPills(Boolean noPPills2) {
		this.noPPills = noPPills2;
	}

	public void setCloseIndex(double ghostsCloseIndex2) {
		this.ghostsCloseIndex = ghostsCloseIndex2;
	}

	public int getScore() {
		return score;
	}

	public void setScore(Integer score2) {
		score = score2;
		
	}

	public GHOST getGhost() {
		return ghost;
	}

	public Integer getLevel() {
		return level;
	}

	public double getBlinkyDist() {
		return BlinkyDistance;
	}
	
	public double getInkyDist() {
		return InkyDistance;
	}
	
	public double getSueDist() {
		return SueDistance;
	}

	public double getPinkyDist() {
		return PinkyDistance;
	}

	public Boolean getNoPPills() {
		return noPPills;
	}

	public double getPacBlinkyDist() {
		return PacmanDBlinky;
	}
	
	public double getPacInkyDist() {
		return PacmanDInky;
	}
	
	public double getPacPinkyDist() {
		return PacmanDPinky;
	}
	
	public double getPacSueDist() {
		return PacmanDSue;
	}

	public int getTimeEdible() {
		
		return timeEdible;
	}

	public double getCloseIndex() {
		return ghostsCloseIndex;
	}

	public int getNearestPPillDist() {
		return PPillDistance;
	}


	
	

}
