# v0.8.16

## ☢️ Breaking Changes
- Configuration file will be reset, this is due to a much-needed cleanup of the config.

## ✨ What's New
- Added a new config option for disabling attack targeting in peaceful mode for just players.
- Added a new config option for disabling attack targeting in peaceful mode for all entities
  - Facehuggers and eggs will still target mobs (not players) normally in peaceful mode.
- Added a new config option to enable an entity check for resin to start egg morphing entities.
- Added a new config option to configure the entity range for resin to start egg morphing entities.

## ♻️ Changes
- Reworked Config grouping.
- Moved peaceful removal to a new config option.
- Disables Pandora spawning in peaceful mode.
- Tweaked Facehugger to remove old dev target removal code.

## 🐞 Fixes
- Fixed acid not respecting AVP acid immune blocks.

## 🛠 Data Pack
- Moved from `#avp:acid_immune` to per block tags from AVP

## 🔬 Technical Changes
- N/A