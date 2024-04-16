v.0.5.66

***THE MOD WILL BE MOVING TO 1.20.4/5 AND 1.20.1 WILL BE SUNSET OUTSIDE ANY MAJOR CRASHES***

## FIXES
- Fixes Acid not generating with random thickness.
- Fixes Goo blood not generating with random thickness.
- Fixes Goo blood not using gooEffectTickTimer configuration, which it halves the gooEffectTickTimer value.
- Fixes Xenos getting stuck in the water.
- Fixes Light breaking from trying to break lights it can't see directly.
- Fixes Food eating from trying to pathfind to food it can't see directly.
- Fixes Crop breaking from trying to break crops it can't see directly.
- Fixes Spores being placed without checking enableDevEntites is true.
- Fixes missing lang for enableDevEntites.

## NEW
- Adds item tag for "acidresistant", items that won't break down in acid.

## CHANGES
- Moves acid generation to use entity method, reducing duplicated code/chance of errors.
- Cleans up some entity code as per the merges down to AlienEntity in last update.
- Moves all block breaking in ticks to tasks.
- enableDevEntites now unhides all eggs in creative tab. 
  - (Any entity in this group with a bug report submitted will be closed as they are ***heavy*** WIP)
- Acid effect will only damage entities not in the resistant tag.
- Acid will damage armor boots that aren't in the acidresistant tag.
- Moves instance checks to check for tags instead of the entities.

Please note that Neos are still heavily WIP and not considered released until posted. This is why you can't find eggs in the creative menu without the config option.
No bug reports will be accepted for them until they have reached what we feel is release status ready.