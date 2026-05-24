package robryo49.rase.block.entity.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import robryo49.rase.block.entity.custom.DryingRackBlockEntity;
import robryo49.rase.block.custom.DryingRackBlock;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    
    public DryingRackBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}
    
    @Override
    public void render(DryingRackBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        
        float[][] offsets = {
                { -0.25f, 0.0f, -0.25f },
                {  0.25f, 0.0f, -0.25f },
                { -0.25f, 0.0f,  0.25f },
                {  0.25f, 0.0f,  0.25f },
        };
        
        // Facing rotation — items rotate to align with the rack
        Direction facing = entity.getCachedState().get(DryingRackBlock.FACING);
        float yRot = switch (facing) {
            case NORTH -> 0f;
            case SOUTH -> 180f;
            case WEST  -> 90f;
            case EAST  -> 270f;
            default    -> 0f;
        };
        
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        
        for (int i = 0; i < DryingRackBlockEntity.SLOT_COUNT; i++) {
            ItemStack stack = entity.getItems().get(i);
            if (stack.isEmpty()) continue;
            
            float[] off = offsets[i];
            
            matrices.push();
            
            matrices.translate(0.5f + off[0], 1.0f, 0.5f + off[2]);
            
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yRot));
            
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
            
            matrices.scale(0.375f, 0.375f, 0.375f);
            
            itemRenderer.renderItem(
                    stack,
                    ModelTransformationMode.FIXED,
                    light,
                    overlay,
                    matrices,
                    vertexConsumers,
                    entity.getWorld(),
                    (int) entity.getPos().asLong()
            );
            
            matrices.pop();
        }
    }
}