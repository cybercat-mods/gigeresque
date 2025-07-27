# v0.8.9

## ☢️ Breaking Changes
- N/A

## ✨ What's New
- Added back in Egg morphing overlay (fade not working at this time).
- Added back in Young textures on Classic and Runner. (Shaders untested)
- Added back in blood overlay on bursters.
- Added a tooltip to Black Fluid Bucket to state its creative mode only.

## ♻️ Changes
- Bumped required Azurelib to latest.
- Modified `gig_dungeon_biomes` structure tag to have a fallback for biomes if the mountain tag is missing.

## 🐞 Fixes
- Fixed a fatal crash occurring when rejoining a server with Gigeresque. Credit bvanseg
- Fixed entombed interlopers are not attacking properly. Credit bvanseg
- Fixed aliens targeting entities that are already being grabbed. Credit bvanseg
- Fixed aliens targeting players in spectator or creative. Credit bvanseg
- Fixed aliens targeting non-attackable targets. Credit bvanseg
- Fixed aliens targeting invulnerable targets. Credit bvanseg
- Fixed entombed interlopers are always grabbing targets instead of attacking them. Credit bvanseg
  - Entombed interlopers will now only grab targets when their health is less than 50%. Credit bvanseg
  - Entombed interlopers now only have a 33% chance to grab a target with < 50% health. Credit bvanseg
- Fixed drowning when being facehugged in water.
- Fixed raid mixin not properly respecting `enablePandoraEffects` config option.
- Fixed egg morphing overlay is not fading in as you are being egg morphed.
- Fixed dna overlay is not fading in as you are being changed by the dna effect.
- Fixed Black Fluid consuming buckets. Can only be replaced by blocks.

## 🛠 Data Pack
- Added `nest_cross_blocks` tag for nest cross blocks for mixin usage.

## 🔬 Technical Changes
- Neo and its Adolescent moved scale to from render to builder.
- Bursters moved scale to from render to builder.
- Stalker alpha moved to builder.