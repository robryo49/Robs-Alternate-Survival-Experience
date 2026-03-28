package robryo49.rase.block.custom.forge;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.ModBlocks;


public class SmithingAnvilBlock extends AnvilBlock {
	
	private final SmithingAnvilMaterials material;
	private ModBlocks.SmithingAnvilBlockSet smithingAnvilBlockSet;
	
	public SmithingAnvilBlock(Settings settings, SmithingAnvilMaterials material) {
		super(settings);
		this.material = material;
	}
	
	public SmithingAnvilMaterials getMaterial() { return material; }
	
	public void setAnvilBlockSet(ModBlocks.SmithingAnvilBlockSet set) { this.smithingAnvilBlockSet = set; }
	public ModBlocks.SmithingAnvilBlockSet getAnvilBlockSet() { return smithingAnvilBlockSet; }
	
	@Nullable
	public BlockState getDamagedVersion(BlockState original) {
		if (original.isOf(smithingAnvilBlockSet.NORMAL())) {
			return smithingAnvilBlockSet.CHIPPED().getDefaultState()
					.with(SmithingAnvilBlock.FACING, original.get(SmithingAnvilBlock.FACING));
		} else if (original.isOf(smithingAnvilBlockSet.CHIPPED())) {
			return smithingAnvilBlockSet.DAMAGED().getDefaultState()
					.with(SmithingAnvilBlock.FACING, original.get(SmithingAnvilBlock.FACING));
		} else {
			return null;
		}
	}
}