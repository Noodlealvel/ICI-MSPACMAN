package es.ucm.fdi.ici.c2223.practica5.grupo04.pacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	
	double BlinkyDistance;
	double PinkyDistance;
	double InkyDistance;
	double SueDistance;
	
	double PacmanDBlinky;
	double PacmanDPinky;
	double PacmanDInky;
	double PacmanDSue;
	
	Integer PPillUp;
	Integer PPillDown;
	Integer PPillRight;
	Integer PPillLeft;
	
	Integer PillUp;
	Integer PillDown;
	Integer PillRight;
	Integer PillLeft;
	
	Integer BlinkyTimeEdible;
	Integer PinkyTimeEdible;
	Integer InkyTimeEdible;
	Integer SueTimeEdible;
	
	Integer numPills;
	Integer eatValue;
	

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

	public Integer getPPillUp() {
		return PPillUp;
	}

	public void setPPillUp(Integer pPillUp) {
		PPillUp = pPillUp;
	}

	public Integer getPPillDown() {
		return PPillDown;
	}

	public void setPPillDown(Integer pPillDown) {
		PPillDown = pPillDown;
	}

	public Integer getPPillRight() {
		return PPillRight;
	}

	public void setPPillRight(Integer pPillRight) {
		PPillRight = pPillRight;
	}

	public Integer getPPillLeft() {
		return PPillLeft;
	}

	public void setPPillLeft(Integer pPillLeft) {
		PPillLeft = pPillLeft;
	}

	public Integer getPillUp() {
		return PillUp;
	}

	public void setPillUp(Integer pillUp) {
		PillUp = pillUp;
	}

	public Integer getPillDown() {
		return PillDown;
	}

	public void setPillDown(Integer pillDown) {
		PillDown = pillDown;
	}

	public Integer getPillRight() {
		return PillRight;
	}

	public void setPillRight(Integer pillRight) {
		PillRight = pillRight;
	}

	public Integer getPillLeft() {
		return PillLeft;
	}

	public void setPillLeft(Integer pillLeft) {
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
