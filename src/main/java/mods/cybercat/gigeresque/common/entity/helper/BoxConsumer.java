package mods.cybercat.gigeresque.common.entity.helper;

import it.unimi.dsi.fastutil.floats.FloatArrays;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class BoxConsumer implements Shapes.DoubleLineConsumer {
	public int capacity = 16;
	public int size = 0;
	public float[] erx = new float[this.capacity];
	public float[] ery = new float[this.capacity];
	public float[] erz = new float[this.capacity];
	public float[] ecx = new float[this.capacity];
	public float[] ecy = new float[this.capacity];
	public float[] ecz = new float[this.capacity];
	public final Vec3 p;
	public final float boxScale;

	public BoxConsumer(Vec3 p, float boxScale) {
		this.p = p;
		this.boxScale = boxScale;
	}

	@Override
	public void consume(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		if(this.size == this.capacity) {
			this.capacity = (int) Math.max(Math.min((long) this.capacity + (this.capacity >> 1), it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE), this.capacity);
			this.erx = FloatArrays.forceCapacity(this.erx, this.capacity, this.size);
			this.ery = FloatArrays.forceCapacity(this.ery, this.capacity, this.size);
			this.erz = FloatArrays.forceCapacity(this.erz, this.capacity, this.size);
			this.ecx = FloatArrays.forceCapacity(this.ecx, this.capacity, this.size);
			this.ecy = FloatArrays.forceCapacity(this.ecy, this.capacity, this.size);
			this.ecz = FloatArrays.forceCapacity(this.ecz, this.capacity, this.size);
		}

		this.erx[this.size] = 1.0f / ((float) (maxX - minX) / 2 * this.boxScale);
		this.ery[this.size] = 1.0f / ((float) (maxY - minY) / 2 * this.boxScale);
		this.erz[this.size] = 1.0f / ((float) (maxZ - minZ) / 2 * this.boxScale);

		this.ecx[this.size] = (float) ((minX + maxX) / 2 - this.p.x);
		this.ecy[this.size] = (float) ((minY + maxY) / 2 - this.p.y);
		this.ecz[this.size] = (float) ((minZ + maxZ) / 2 - this.p.z);

		this.size++;
	}
}