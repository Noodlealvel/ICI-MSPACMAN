;Facts asserted
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot inDefense (type Symbol))
	(slot inAttack (type Symbol))
	(slot inAgressive (type Symbol))
	(slot distanceToPacman (type NUMBER)))
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot inDefense (type Symbol))
	(slot inAttack (type Symbol))
	(slot inAgressive (type Symbol))
	(slot distanceToPacman (type NUMBER)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot inDefense (type Symbol))
	(slot inAttack (type Symbol))
	(slot inAgressive (type Symbol))
	(slot distanceToPacman (type NUMBER)))

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot inDefense (type Symbol))
	(slot inAttack (type Symbol))
	(slot inAgressive (type Symbol))
	(slot distanceToPacman (type NUMBER)))

(deftemplate MSPACMAN 
    (slot pacmanInTunnel (type SYMBOL))
    (slot noPPills (type SYMBOL)) 
    (slot eatenPPill (type SYMBOL))
    (slot pacmanNearPPill (type SYMBOL))
    (slot noPPills (type SYMBOL))
    (slot nearestGhost (type NUMBER))
    (slot secondNearestGhost (type NUMBER))
    (slot secondFurthestGhost (type NUMBER))
    (slot furthestGhost (type NUMBER)))
    
;Action Fact
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))
	(slot flanks (type SYMBOL))
)
	
;Rules

(defrule BLINKYwait
	(BLINKY (inLair true)) 
	=>  
	(assert 
		(ACTION (id BLINKYwait) (info "BLINKY espera en lair") (priority 100))
	)
)

(defrule BLINKYAgressive
	(BLINKY (edible false) (inDefense false) (inAttack false) (inAgressive false))
	(MSPACMAN (noPPills true)) 
	=>  
	(assert 
		(BLINKY (inAgressive true))
	)
)

(defrule BLINKYAttacks
	(BLINKY (edible false) (inDefense false) (inAttack false) (inAgressive false) (chaseDistance true))
	(MSPACMAN (pacmanNearPPill false)) 
	=>  
	(assert 
		(BLINKY (inAttack true))
	)
)

(defrule BLINKYdefenseEdible 
	(BLINKY (inDefense false) (inAttack false) (inAgressive false) (edible true))
	=>
	(assert
		(BLINKY (inDefense true))
	)
)

(defrule BLINKYdefensePacmanNearPPill 
	(BLINKY (inDefense false) (inAttack false) (inAgressive false))
	(MSPACMAN (pacmanNearPPill true))
	=>
	(assert
		(BLINKY (inDefense true))
	)
)

(defrule BLINKYchasesNoGhosts
	(BLINKY (inAttack true) (noGhostsInPath true))
	=>
	(assert
		(ACTION (id BLINKYchase) (info "BLINKY persigue de manera directa al estar el camino vacío") (priority 40) (flanks false))
	)
)

(defrule BLINKYchasesTunnel
	(BLINKY (inAttack true))
	(MSPACMAN (pacmanInTunnel true))
	=>
	(assert
		(ACTION (id BLINKYchase) (info "BLINKY persigue de manera directa al estar en túnel") (priority 40) (flanks false))
	)
)

(defrule BLINKYflanks
	(BLINKY (inAttack true) (noGhostsInPath false) (justBehind false))
	=>
	(assert
		(ACTION (id BLINKYchase) (info "BLINKY flanquea") (priority 40) (flanks false))
	)
)

(defrule BLINKYstopsChasing
	(BLINKY (inAttack true) (justBehind true))
	=>
	(assert
		(ACTION (id BLINKYstopChasing) (info "BLINKY para de perseguir") (priority 60))
	)
)

(defrule BLINKYdefendLastPills
	(BLINKY (inAgressive true) (justDistance false))
	=>
	(assert
		(ACTION (id BLINKYdefendLastPills) (info "BLINKY protege las últimas pills") (priority 50))
	)
)

(defrule BLINKYkillPacman
	(BLINKY (inAgressive true) (chaseDistance false))
	=>
	(assert
		(ACTION (id BLINKYkillPacman) (info "BLINKY va a terminar con pacman") (priority 50))
	)
)

(defrule BLINKYregroup
	(BLINKY (chaseDistance false) (edible false) (inAgressive false)) 
	(MSPACMAN (pacmanNearPPill true))
	=>  
	(assert 
		(ACTION (id BLINKYregroup) (info "BLINKY se acerca a pacman porque no es comestible y esta lejos") (priority 90))
	)
)


