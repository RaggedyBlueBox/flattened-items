package blue.beaming.flatitems.injected.mixin;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.LayerRenderState.class) public interface LayerRenderStateAccessor {
    @Accessor("quads") ItemQuads flat_items$getQuads();
}
