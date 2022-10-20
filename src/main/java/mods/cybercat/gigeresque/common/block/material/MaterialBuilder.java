package mods.cybercat.gigeresque.common.block.material;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;

public class MaterialBuilder {
	private PushReaction pistonBehavior;
	private boolean blocksMovement;
	private boolean burnable = false;
	private boolean liquid = false;
	private boolean replaceable = false;
	private boolean solid;
	private MaterialColor color;
	private boolean blocksLight;

	MaterialBuilder(MaterialColor color) {
		pistonBehavior = PushReaction.NORMAL;
		blocksMovement = true;
		solid = true;
		blocksLight = true;
		this.color = color;
	}

	public MaterialBuilder liquid() {
		liquid = true;
		return this;
	}

	public MaterialBuilder notSolid() {
		solid = false;
		return this;
	}

	public MaterialBuilder allowsMovement() {
		blocksMovement = false;
		return this;
	}

	public MaterialBuilder lightPassesThrough() {
		blocksLight = false;
		return this;
	}

	public MaterialBuilder burnable() {
		burnable = true;
		return this;
	}

	public MaterialBuilder replaceable() {
		replaceable = true;
		return this;
	}

	public MaterialBuilder destroyedByPiston() {
		pistonBehavior = PushReaction.DESTROY;
		return this;
	}

	public MaterialBuilder blocksPiston() {
		pistonBehavior = PushReaction.BLOCK;
		return this;
	}

	public Material build() {
		return new Material(color, liquid, solid, blocksMovement, blocksLight, burnable, replaceable, pistonBehavior);
	}
}
