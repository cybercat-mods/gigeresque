package mods.cybercat.gigeresque.common.block.material;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;

public class MaterialBuilder {
	private PistonBehavior pistonBehavior;
	private boolean blocksMovement;
	private boolean burnable = false;
	private boolean liquid = false;
	private boolean replaceable = false;
	private boolean solid;
	private MapColor color;
	private boolean blocksLight;

	MaterialBuilder(MapColor color) {
		pistonBehavior = PistonBehavior.NORMAL;
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
		pistonBehavior = PistonBehavior.DESTROY;
		return this;
	}

	public MaterialBuilder blocksPiston() {
		pistonBehavior = PistonBehavior.BLOCK;
		return this;
	}

	public Material build() {
		return new Material(color, liquid, solid, blocksMovement, blocksLight, burnable, replaceable, pistonBehavior);
	}
}
