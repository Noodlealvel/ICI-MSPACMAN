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
	(slot flankstrategy (type SYMBOL))
)
	
;Rules

(defrule INKYwait
	(INKY (inLair true)) 
	=>  
	(assert 
		(ACTION (id INKYwait) (info "INKY espera en lair") (priority 100))
	)
)

(defrule INKYAgressive
	(INKY (edible false) (inDefense false) (inAttack false) (inAgressive false))
	(MSPACMAN (noPPills true)) 
	=>  
	(assert 
		(INKY (inAgressive true))
	)
)

(defrule INKYAttacks
	(INKY (edible false) (inDefense false) (inAttack false) (inAgressive false) (chaseDistance true))
	(MSPACMAN (pacmanNearPPill false)) 
	=>  
	(assert 
		(INKY (inAttack true))
	)
)

(defrule INKYdefense_Edible 
	(INKY (inDefense false) (inAttack false) (inAgressive false) (edible true))
	=>
	(assert
		(INKY (inDefense true))
	)
)

(defrule INKYdefense_PacmanNearPPill 
	(INKY (inDefense false) (inAttack false) (inAgressive false))
	(MSPACMAN (pacmanNearPPill true))
	=>
	(assert
		(INKY (inDefense true))
	)
)

(defrule INKYchases_NoGhosts
	(INKY (inAttack true) (noGhostsInPath true))
	=>
	(assert
		(ACTION (id INKYchase) (info "INKY persigue de manera directa al estar el camino vacío") (priority 40) (flanks false))
	)
)

(defrule INKYchases_Tunnel
	(INKY (inAttack true))
	(MSPACMAN (pacmanInTunnel true))
	=>
	(assert
		(ACTION (id INKYchase) (info "INKY persigue de manera directa al estar en túnel") (priority 40) (flankstrategy false))
	)
)

(defrule INKYflanks
	(INKY (inAttack true) (noGhostsInPath false) (justBehind false))
	=>
	(assert
		(ACTION (id INKYchase) (info "INKY flanquea") (priority 60) (flankstrategy true))
	)
)

(defrule INKYstopsChasing
	(INKY (inAttack true) (justBehind true))
	=>
	(assert
		(ACTION (id INKYstopChasing) (info "INKY para de perseguir") (priority 50))
	)
)

(defrule INKYdefendLastPills
	(INKY (inAgressive true) (justBehind true))
	=>
	(assert
		(ACTION (id INKYdefendLastPills) (info "INKY protege las últimas pills") (priority 55))
	)
)

(defrule INKYkillPacman
	(INKY (inAgressive true) (chaseDistance true))
	=>
	(assert
		(ACTION (id INKYkillPacman) (info "INKY va a terminar con pacman") (priority 50))
	)
)

(defrule INKYregroup
	(INKY (chaseDistance false) (edible false) (inAgressive false)) 
	(MSPACMAN (pacmanNearPPill true))
	=>  
	(assert 
		(ACTION (id INKYregroup) (info "INKY se acerca a pacman porque no es comestible y esta lejos") (priority 80))
	)
)

(defrule INKYflee
	(INKY (nearPacman true) (inDefense true)) 
	=>  
	(assert 
		(ACTION (id INKYflee) (info "INKY huye de pacman porque esta cerca") (priority 70))
	)
)

(defrule INKYsearchForTunnel
	(INKY (pacmanInVecinity true) (inDefense true) (nearTunnel true)) 
	=>  
	(assert 
		(ACTION (id INKYsearchForTunnel) (info "INKY busca tunel") (priority 30))
	)
)

(defrule INKYfleeFromPPill_LowTime
	(INKY (lowEdibleTime true) (inDefense true)) 
	=>  
	(assert 
		(ACTION (id INKYfleeFromPPill) (info "A INKY le queda poco tiempo comestible y huye de powerpills cercanas") (priority 60))
	)
)

(defrule INKYfleeFromPPill_PacmanFar
	(INKY (pacmanClose false) (inDefense true)) 
	=>  
	(assert 
		(ACTION (id INKYfleeFromPPill) (info "INKY esta lejos de pacman y este huye de powerpills cercanas") (priority 50))
	)
)

(defrule INKYdisperse
	(INKY (pacmanClose false) (ghostsClose true) (inDefense true)) 
	=>  
	(assert 
		(ACTION (id INKYdisperse) (info "INKY se dispersa") (priority 70))
	)
)

(defrule INKYgoToPPill
	(INKY (lowEdibleTime true) (inDefense true)) 
	=>  
	(assert 
		(ACTION (id INKYdisperse) (info "INKY va a interceptar a pacman en PPill") (priority 75))
	)
)

