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


	public double getBlinkyDistance() {
		return BlinkyDistance;
	}


	public void setBlinkyDistance(double blinkyDistance) {
		BlinkyDistance = blinkyDistance;
	}


	public double getPinkyDistance() {
		return PinkyDistance;
	}


	public void setPinkyDistance(double pinkyDistance) {
		PinkyDistance = pinkyDistance;
	}


	public double getInkyDistance() {
		return InkyDistance;
	}


	public void setInkyDistance(double inkyDistance) {
		InkyDistance = inkyDistance;
	}


	public double getSueDistance() {
		return SueDistance;
	}


	public void setSueDistance(double sueDistance) {
		SueDistance = sueDistance;
	}


	public double getPacmanDBlinky() {
		return PacmanDBlinky;
	}


	public void setPacmanDBlinky(double pacmanDBlinky) {
		PacmanDBlinky = pacmanDBlinky;
	}


	public double getPacmanDPinky() {
		return PacmanDPinky;
	}


	public void setPacmanDPinky(double pacmanDPinky) {
		PacmanDPinky = pacmanDPinky;
	}


	public double getPacmanDInky() {
		return PacmanDInky;
	}


	public void setPacmanDInky(double pacmanDInky) {
		PacmanDInky = pacmanDInky;
	}


	public double getPacmanDSue() {
		return PacmanDSue;
	}


	public void setPacmanDSue(double pacmanDSue) {
		PacmanDSue = pacmanDSue;
	}


	public double getPPillUp() {
		return PPillUp;
	}


	public void setPPillUp(double pPillUp) {
		PPillUp = pPillUp;
	}


	public double getPPillDown() {
		return PPillDown;
	}


	public void setPPillDown(double pPillDown) {
		PPillDown = pPillDown;
	}


	public double getPPillRight() {
		return PPillRight;
	}


	public void setPPillRight(double pPillRight) {
		PPillRight = pPillRight;
	}


	public double getPPillLeft() {
		return PPillLeft;
	}


	public void setPPillLeft(double pPillLeft) {
		PPillLeft = pPillLeft;
	}


	public double getPillUp() {
		return PillUp;
	}


	public void setPillUp(double pillUp) {
		PillUp = pillUp;
	}


	public double getPillDown() {
		return PillDown;
	}


	public void setPillDown(double pillDown) {
		PillDown = pillDown;
	}


	public double getPillRight() {
		return PillRight;
	}


	public void setPillRight(double pillRight) {
		PillRight = pillRight;
	}


	public double getPillLeft() {
		return PillLeft;
	}


	public void setPillLeft(double pillLeft) {
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
