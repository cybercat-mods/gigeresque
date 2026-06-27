**Eggmorphing and Victim Transport**

Alien now correctly carries victims (players and mobs) to the nearest web block
Alien only targets floor-level web blocks — ignores ceiling and wall webs
If no web is found, the alien kills the victim after a short time
Multiple aliens no longer interfere with each other while carrying victims
After placing a victim, alien correctly resets its state and resumes hunting

 **Nest Web Block**

Web now pulls players and mobs toward the center of the block
Web blocks jumping for players and mobs (Jumping used to allow the player to break free from the web)
Web applies Mining Fatigue to players
Mob AI is disabled while inside the web block
Mob AI is restored when the web is broken or the mob exits it

 **Nest Building**

A lone alien builds nests rarely — once every 60 seconds (no more nest spam)
Two aliens nearby build more actively
Three or more aliens nearby build without restrictions (original behavior)
Alien does not build nests while chasing or attack a target
 **Attack and Behavior**

Alien does not attack targets already hosting a facehugger
Alien does not attack targets with Impregnation effect
Attack is interrupted immediately when a facehugger jumps on the target
Alien does not attack targets being carried by another alien
Alien does not attack targets already inside nest web
After placing a victim, alien correctly switches to new targets

 **Patrolling**

Added patrolling behavior — alien actively wanders in search of targets when idle
Alien searches for random points within a 24 block radius

 **Chestburster**

Added fleeing from players when they approach within 12 blocks

 **Facehugger**

Facehugger does not attack targets being carried by an alien
If facehugger is killed while attached to a victim - victim still gets infected

 **Acid**

Mobs being carried by an alien are immune to acid damage (Protects the alien's targets from accidental death by acid while breaking blocks)
Mobs with Eggmorphing effect are immune to acid damage
