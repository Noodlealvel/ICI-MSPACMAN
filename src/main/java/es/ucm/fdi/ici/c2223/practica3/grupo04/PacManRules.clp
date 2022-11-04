;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN 
    (slot edibleGhosts (type NUMBER)) 
    (slot nearestGhostEdible (type SYMBOL))
    (slot numberOfGhostsNear (type NUMBER))
    (slot activePowerPills (type NUMBER))
    (slot freeGhostsPath (type SYMBOL))
    (slot distance2Closest (type NUMBER))
    (slot spawnPoint (type SYMBOL)))
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) ) 