package es.ucm.fdi.ici.c2223.practica5.grupo04.pacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManDescription implements CaseComponent {

	public Integer id;
	
	public Double BlinkyDistance;
	public Double PinkyDistance;
	public Double InkyDistance;
	public Double SueDistance;
	
	public Double PacmanDBlinky;
	public Double PacmanDPinky;
	public Double PacmanDInky;
	public Double PacmanDSue;
	
	public Double PPillUp;
	public Double PPillDown;
	public Double PPillRight;
	public Double PPillLeft;
	
	public Double PillUp;
	public Double PillDown;
	public Double PillRight;
	public Double PillLeft;
	
	public Integer BlinkyTimeEdible;
	public Integer PinkyTimeEdible;
	public Integer InkyTimeEdible;
	public Integer SueTimeEdible;
	
	public Integer numPills;
	public Integer eatValue;
	
	private Integer score;	

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Double getBlinkyDistance() {
		return BlinkyDistance;
	}


	public void setBlinkyDistance(Double blinkyDistance) {
		BlinkyDistance = blinkyDistance;
	}


	public Double getPinkyDistance() {
		return PinkyDistance;
	}


	public void setPinkyDistance(Double pinkyDistance) {
		PinkyDistance = pinkyDistance;
	}


	public Double getInkyDistance() {
		return InkyDistance;
	}


	public void setInkyDistance(Double inkyDistance) {
		InkyDistance = inkyDistance;
	}


	public Double getSueDistance() {
		return SueDistance;
	}


	public void setSueDistance(Double sueDistance) {
		SueDistance = sueDistance;
	}


	public Double getPacmanDBlinky() {
		return PacmanDBlinky;
	}


	public void setPacmanDBlinky(Double pacmanDBlinky) {
		PacmanDBlinky = pacmanDBlinky;
	}


	public Double getPacmanDPinky() {
		return PacmanDPinky;
	}


	public void setPacmanDPinky(Double pacmanDPinky) {
		PacmanDPinky = pacmanDPinky;
	}


	public Double getPacmanDInky() {
		return PacmanDInky;
	}


	public void setPacmanDInky(Double pacmanDInky) {
		PacmanDInky = pacmanDInky;
	}


	public Double getPacmanDSue() {
		return PacmanDSue;
	}


	public void setPacmanDSue(Double pacmanDSue) {
		PacmanDSue = pacmanDSue;
	}


	public Double getPPillUp() {
		return PPillUp;
	}


	public void setPPillUp(Double pPillUp) {
		PPillUp = pPillUp;
	}


	public Double getPPillDown() {
		return PPillDown;
	}


	public void setPPillDown(Double pPillDown) {
		PPillDown = pPillDown;
	}


	public Double getPPillRight() {
		return PPillRight;
	}


	public void setPPillRight(Double pPillRight) {
		PPillRight = pPillRight;
	}


	public Double getPPillLeft() {
		return PPillLeft;
	}


	public void setPPillLeft(Double pPillLeft) {
		PPillLeft = pPillLeft;
	}


	public Double getPillUp() {
		return PillUp;
	}


	public void setPillUp(Double pillUp) {
		PillUp = pillUp;
	}


	public Double getPillDown() {
		return PillDown;
	}


	public void setPillDown(Double pillDown) {
		PillDown = pillDown;
	}


	public Double getPillRight() {
		return PillRight;
	}


	public void setPillRight(Double pillRight) {
		PillRight = pillRight;
	}


	public Double getPillLeft() {
		return PillLeft;
	}


	public void setPillLeft(Double pillLeft) {
		PillLeft = pillLeft;
	}


	public Integer getBlinkyTimeEdible() {
		return BlinkyTimeEdible;
	}


	public void setBlinkyTimeEdible(Integer blinkyTimeEdible) {
		BlinkyTimeEdible = blinkyTimeEdible;
	}


	public Integer getPinkyTimeEdible() {
		return PinkyTimeEdible;
	}


	public void setPinkyTimeEdible(Integer pinkyTimeEdible) {
		PinkyTimeEdible = pinkyTimeEdible;
	}


	public Integer getInkyTimeEdible() {
		return InkyTimeEdible;
	}


	public void setInkyTimeEdible(Integer inkyTimeEdible) {
		InkyTimeEdible = inkyTimeEdible;
	}


	public Integer getSueTimeEdible() {
		return SueTimeEdible;
	}


	public void setSueTimeEdible(Integer sueTimeEdible) {
		SueTimeEdible = sueTimeEdible;
	}


	public Integer getNumPills() {
		return numPills;
	}


	public void setNumPills(Integer numPills) {
		this.numPills = numPills;
	}


	public Integer getEatValue() {
		return eatValue;
	}


	public void setEatValue(Integer eatValue) {
		this.eatValue = eatValue;
	}


	public Integer getScore() {
		return score;
	}


	public void setScore(Integer score) {
		this.score = score;
	}


	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}
	

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", BlinkyDistance=" + BlinkyDistance + ", PinkyDistance=" + PinkyDistance + ", InkyDistance="
				+ InkyDistance + ", SueDistance=" + SueDistance + ", PacmanDBlinky=" + PacmanDBlinky + ", PacmanDPinky=" + PacmanDPinky + 
				", PacmanDInky=" + PacmanDInky + ", PacmanDSue=" + PacmanDSue + ", PPillUp=" + PPillUp + ", PPillDown=" + PPillDown +
				", PPillRight=" + PPillRight + ", PPillLeft=" + PPillLeft + ", PillUp=" + PillUp + ", PillDown=" + PillDown + ", PillRight=" + 
				PillRight + ", PillLeft=" + PillLeft + ", BlinkyTimeEdible=" + BlinkyTimeEdible + ", PinkyTimeEdible=" + PinkyTimeEdible +
				", InkyTimeEdible=" + InkyTimeEdible + ", SueTimeEdible=" + SueTimeEdible + ", numPills=" + numPills + ", eatValue=" + eatValue +
				"]";
	}


	
	

}
