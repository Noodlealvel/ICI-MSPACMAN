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

	public void setBlinkyDistance(Double blinkyDistance) {
		BlinkyDistance = blinkyDistance;
	}

	public void setPinkyDistance(Double pinkyDistance) {
		PinkyDistance = pinkyDistance;
	}

	public void setInkyDistance(Double inkyDistance) {
		InkyDistance = inkyDistance;
	}

	public void setSueDistance(Double sueDistance) {
		SueDistance = sueDistance;
	}

	public void setPacmanDBlinky(Double pacmanDBlinky) {
		PacmanDBlinky = pacmanDBlinky;
	}

	public void setPacmanDPinky(Double pacmanDPinky) {
		PacmanDPinky = pacmanDPinky;
	}

	public void setPacmanDInky(Double pacmanDInky) {
		PacmanDInky = pacmanDInky;
	}

	public void setPacmanDSue(Double pacmanDSue) {
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

	public void setGhostsCloseIndex(Double ghostsCloseIndex2) {
		this.ghostsCloseIndex = ghostsCloseIndex2;
	}

	public Integer getScore() {
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

	public Double getBlinkyDistance() {
		return BlinkyDistance;
	}
	
	public Double getInkyDistance() {
		return InkyDistance;
	}
	
	public Double getSueDistance() {
		return SueDistance;
	}

	public Double getPinkyDistance() {
		return PinkyDistance;
	}

	public Boolean getNoPPills() {
		return noPPills;
	}

	public Double getPacmanDBlinky() {
		return PacmanDBlinky;
	}
	
	public Double getPacmanDInky() {
		return PacmanDInky;
	}
	
	public Double getPacmanDPinky() {
		return PacmanDPinky;
	}
	
	public Double getPacmanDSue() {
		return PacmanDSue;
	}

	public Integer getTimeEdible() {
		
		return timeEdible;
	}

	public Double getGhostsCloseIndex() {
		return ghostsCloseIndex;
	}

	public Integer getPPillDistance() {
		return PPillDistance;
	}


	
	

}
