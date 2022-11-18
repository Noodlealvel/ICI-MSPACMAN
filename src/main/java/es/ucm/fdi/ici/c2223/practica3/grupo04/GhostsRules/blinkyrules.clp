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
	(slot distanceToPacman (type NUMBER)))

(deftemplate MSPACMAN 
    (slot pacmanInTunnel (type SYMBOL))
    (slot noPPills (type SYMBOL)) 
    (slot eatenPPill (type SYMBOL))
    (slot pacmanNearPPill (type SYMBOL))
    (slot noPPills (type SYMBOL)))
    
(deftemplate BLINKY_STATE
	(slot inDefense (type SYMBOL))
	(slot inAttack (type SYMBOL))
	(slot inAgressive (type SYMBOL)))
    
;Action Fact
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))
	(slot flankstrategy (type SYMBOL))
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
	(BLINKY (edible false))
	(MSPACMAN (noPPills true)) 
	=>  
	(assert (BLINKY_STATE (inAgressive true) (inDefense false) (inAttack false)))
)

(defrule BLINKYAttacks
	(BLINKY (edible false) (chaseDistance true))
	(MSPACMAN (pacmanNearPPill false)) 
	=>  
	(assert (BLINKY_STATE (inAgressive false) (inDefense false) (inAttack true)))
)

(defrule BLINKYdefense_Edible 
	(BLINKY (edible true))
	=>
	(assert (BLINKY_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule BLINKYdefense_PacmanNearPPill 
	(MSPACMAN (pacmanNearPPill true))
	=>
	(assert (BLINKY_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule BLINKYchases_NoGhosts
	(BLINKY (noGhostsInPath true))
	(BLINKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id BLINKYchase) (info "BLINKY persigue de manera directa al estar el camino vacío") (priority 40) (flankstrategy false))
	)
)

(defrule BLINKYchases_Tunnel
	(BLINKY_STATE (inAttack true))
	(MSPACMAN (pacmanInTunnel true))
	=>
	(assert
		(ACTION (id BLINKYchase) (info "BLINKY persigue de manera directa al estar en túnel") (priority 40) (flankstrategy false))
	)
)

(defrule BLINKYflanks
	(BLINKY (noGhostsInPath false) (justBehind false))
	(BLINKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id BLINKYchase) (info "BLINKY flanquea") (priority 60) (flankstrategy true))
	)
)

(defrule BLINKYstopsChasing
	(BLINKY (justBehind true))
	(BLINKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id BLINKYstopChasing) (info "BLINKY para de perseguir") (priority 50))
	)
)

(defrule BLINKYdefendLastPills
	(BLINKY (justBehind true))
	(BLINKY_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id BLINKYdefendLastPills) (info "BLINKY protege las últimas pills") (priority 55))
	)
)

(defrule BLINKYkillPacman
	(BLINKY (chaseDistance true))
	(BLINKY_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id BLINKYkillPacman) (info "BLINKY va a terminar con pacman") (priority 50))
	)
)

(defrule BLINKYregroup
	(BLINKY (chaseDistance false) (edible false))
	(BLINKY_STATE (inAgressive false)) 
	(MSPACMAN (pacmanNearPPill true))
	=>  
	(assert 
		(ACTION (id BLINKYregroup) (info "BLINKY se acerca a pacman porque no es comestible y esta lejos") (priority 80))
	)
)

(defrule BLINKYflee
	(BLINKY (pacmanClose true)) 
	(BLINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id BLINKYflee) (info "BLINKY huye de pacman porque esta cerca") (priority 70))
	)
)

(defrule BLINKYsearchForTunnel
	(BLINKY (pacmanInVecinity true) (nearTunnel true)) 
	(BLINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id BLINKYsearchForTunnel) (info "BLINKY busca tunel") (priority 30))
	)
)

(defrule BLINKYfleeFromPPill_LowTime
	(BLINKY (lowEdibleTime true)) 
	(BLINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id BLINKYfleeFromPPill) (info "A BLINKY le queda poco tiempo comestible y huye de powerpills cercanas") (priority 60))
	)
)

(defrule BLINKYfleeFromPPill_PacmanFar
	(BLINKY (pacmanClose false)) 
	(BLINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id BLINKYfleeFromPPill) (info "BLINKY esta lejos de pacman y este huye de powerpills cercanas") (priority 50))
	)
)

(defrule BLINKYdisperse
	(BLINKY (pacmanClose false) (ghostsClose true)) 
	(BLINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id BLINKYdisperse) (info "BLINKY se dispersa") (priority 70))
	)
)

(defrule BLINKYgoToPPill
	(BLINKY (lowEdibleTime true))
	(BLINKY_STATE (inDefense true)) 
	=>  
	(assert 
		(ACTION (id BLINKYdisperse) (info "BLINKY va a interceptar a pacman en PPill") (priority 75))
	)
)

