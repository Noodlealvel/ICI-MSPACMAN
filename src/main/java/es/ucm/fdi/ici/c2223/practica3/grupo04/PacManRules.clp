;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN 
    (slot pacmanPos (type NUMBER)) 
    (slot PPeaten (type SYMBOL))
    (slot multiplePPsInZone (type SYMBOL))
    (slot ghostClose (type SYMBOL))
    (slot edibleGhostClose (type SYMBOL))
    (slot noPillsNear (type SYMBOL))
    (slot dontChase (type SYMBOL))
    (slot multipleGhostsClose (type SYMBOL)) 
    (slot lessGhostsClose (type SYMBOL))
    (slot PPClose (type SYMBOL))
    (slot ghostsFlanking (type SYMBOL))
    (slot levelChange (type SYMBOL))
    (slot PPBlocked (type SYMBOL))
    (slot noPPsleft (type SYMBOL))
    (slot fewPillsleft (type SYMBOL))
    (slot edibleGhostsTogether (type SYMBOL))
    (slot lessPPsInZone (type SYMBOL))
)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ) 

(defrule AttackToStandard
	(MSPACMAN (dontChase true))
	=> 
	(assert (ACTION (id SearchOptimalPath) (info "PACMAN TRANSITIONS TO STANDARD") (priority 80)))
)

(defrule EdibleGhostsClose
	(MSPACMAN (edibleGhostClose true))
	=> 
	(assert (ACTION (id SearchOptimalPathTowardsEdibles) (info "EDIBLE GHOST CLOSE") (priority 90)))
)

(defrule EdibleGhostsApart
    (MSPACMAN (edibleGhostsTogether false))
    =>
    (assert (ACTION (id EdiblesButApart) (info "EDIBLE GHOSTS APART") (priority 90)))
)

(defrule EdibleGhostsTogether
	(MSPACMAN (edibleGhostsTogether true))
	=> 
	(assert (ACTION (id EdiblesAndTogether) (info "EDIBLE GHOSTS TOGETHER") (priority 90)))
)

(defrule FewPillsAndNoPPsLeft
	(MSPACMAN (fewPillsleft true))
    (and
        (MSPACMAN (noPPsleft true)))
	=> 
	(assert (ACTION (id EatLastPills) (info "FEW PILLS AND NO PPS LEFT") (priority 80)))
)

(defrule FewPillsInZone
	(MSPACMAN (lessPPsInZone true))
	=> 
	(assert (ACTION (id SearchBetterZone) (info "FEW PPS IN ZONE") (priority 30)))
)

(defrule GhostClose
	(MSPACMAN (ghostClose true))
	=> 
	(assert (ACTION (id Flee) (info "GHOST CLOSE") (priority 85)))
)

(defrule GhostsFlanking
	(MSPACMAN (ghostsFlanking true))
	=> 
	(assert (ACTION (id SearchPathWithoutGhosts) (info "GHOSTS ARE FLANKING") (priority 90)))
)

(defrule GhostsTooFar
	(MSPACMAN (ghostClose false))
	=> 
	(assert (ACTION (id SearchOptimalPath) (info "GHOSTS ARE OUTSIDE DANGER RADIUS") (priority 80)))
)

(defrule LessGhostsClose
	(MSPACMAN (lessGhostsClose true))
	=> 
	(assert (ACTION (id Flee) (info "FEW PPS IN ZONE") (priority 85)))
)

(defrule LevelChange
	(MSPACMAN (levelChange true))
	=> 
	(assert (ACTION (id BeginMap) (info "LEVEL CHANGED") (priority 100)))
)

(defrule MultiplePPsInZone
	(MSPACMAN (multiplePPsInZone true))
	=> 
	(assert (ACTION (id SearchOptimalPath) (info "MULTIPLE PPS IN ZONE") (priority 70)))
)

(defrule NoPillsNearPacman
	(MSPACMAN (noPillsNear true))
	=> 
	(assert (ACTION (id SearchOptimalPath) (info "NO PILLS NEXT TO PACMAN") (priority 80)))
)

(defrule PowerPillBlocked
	(MSPACMAN (PPBlocked true))
	=> 
	(assert (ACTION (id Flee) (info "POWER PILL IS BLOCKED") (priority 85)))
)

(defrule PowerPillEaten
	(MSPACMAN (PPeaten true))
	=> 
	(assert (ACTION (id SearchOptimalPathTowardsEdibles) (info "POWER PILL EATEN") (priority 90)))
)

(defrule SeveralGhostsCloseAndPP
	(MSPACMAN (multipleGhostsClose true))
    (and
        (MSPACMAN (PPClose true)))
	=> 
	(assert (ACTION (id ChasePowerPill) (info "SEVERAL GHOSTS CLOSE AND NEAR PP") (priority 90)))
)

(defrule SeveralGhostsCloseNoPP
	(MSPACMAN (multipleGhostsClose true))
    (and
        (MSPACMAN (PPClose false)))
	=> 
	(assert (ACTION (id SearchZoneWithPPAndNoGhosts) (info "SEVERAL GHOSTS CLOSE AND FAR FROM PP") (priority 70)))
)

(defrule StandardToAttackTransition
	(MSPACMAN (PPeaten true))
    (or
        (MSPACMAN (edibleGhostClose true)))
	=> 
	(assert (ACTION (id SearchOptimalPathTowardsEdibles) (info "PACMAN TRANSITIONS TO ATTACK") (priority 90)))
)





