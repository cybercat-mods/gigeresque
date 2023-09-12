#If a player enter the radius of an Egg while sneaking, it will deactivate.
execute at @a[predicate=!giger_egg:player_not_sneaking] as @e[type=gigeresque:egg,distance=..12,nbt=!{NoAI:1b},nbt={hasFacehugger:1b},nbt=!{isHatching:1b}] run data merge entity @s {NoAI:1b}

#Will activate the Egg if close to 3 blocks regardless
execute at @a as @e[type=gigeresque:egg,distance=..3,nbt={NoAI:1b}] run data merge entity @s {NoAI:0b}

#Once the Egg is Hatching, there is no turning back.
execute at @a as @e[type=gigeresque:egg,distance=..12,nbt={NoAI:1b}] if data entity @s {isHatched:1b} run data merge entity @s {NoAI:0b}

#If a deactivated Egg receive damage, it will hatch.
execute at @a as @e[type=gigeresque:egg,nbt={NoAI:1b},nbt=!{HurtTime:0s}] run data merge entity @s {NoAI:0b}

#If a player unsneak within the radius of an Egg, it will reactivate.
execute at @a[predicate=giger_egg:player_not_sneaking] as @e[type=gigeresque:egg,distance=..10,nbt={NoAI:1b}] run data merge entity @s {NoAI:0b}

#If an Egg is surrounded by blocks, it can't detect anything or anyone outside.
execute at @a as @e[type=gigeresque:egg,distance=..12] at @s unless block ~1 ~ ~ minecraft:air unless block ~-1 ~ ~ minecraft:air unless block ~ ~ ~1 minecraft:air unless block ~ ~ ~-1 minecraft:air unless block ~ ~-1 ~ minecraft:air unless block ~ ~1 ~ minecraft:air run data merge entity @s {NoAI:1b}
