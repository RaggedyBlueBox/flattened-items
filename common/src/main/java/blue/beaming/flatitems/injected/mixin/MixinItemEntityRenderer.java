package blue.beaming.flatitems.injected.mixin;

import blue.beaming.flatitems.FlatItems;
import blue.beaming.flatitems.injected.interfaces.BakedQuadSupplier;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemEntityRenderer.class) public class MixinItemEntityRenderer {
    // 26.3: vanilla submit() spins via rotate(Axis, float); the old mulPose(Quaternionfc) call is gone.
    @Redirect(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotate(Lcom/mojang/math/Axis;F)V"))
    private void census(PoseStack instance, Axis axis, float angle, @Local(argsOnly = true) ItemEntityRenderState iers, @Local(argsOnly = true) CameraRenderState crs) {
        if (!FlatItems.settings().enabled() || iers.count == 0) {
            cancel(instance, axis, angle);
            return;
        }

        final var affect = FlatItems.settings().affect3D();
        var is3D = false;
        List<BakedQuad>[] lists = ((BakedQuadSupplier) iers.item).flat_items$quads();

        for (final var ql : lists) for (final var q : ql) {
            is3D = Mth.abs(q.position0().z() - q.position2().z()) > 0.0625f;
            if (!affect && is3D) {
                cancel(instance, axis, angle);
                return;
            } else if (is3D) {
                unspin(instance, iers, crs);
                if (!FlatItems.settings().renderSides()) flatten(iers.item);
                if (FlatItems.settings().enlarge3D()) instance.scale(2f, 2f, 2f);
                return;
            }
        }

        unspin(instance, iers, crs);
        if (!FlatItems.settings().renderSides()) flatten(iers.item);
    }

    @Unique private static void unspin(PoseStack pose, ItemEntityRenderState iers, CameraRenderState crs) {
        FlatItems.settings().facing().unspin(pose, iers, crs);
    }

    // 26.3: layers expose immutable ItemQuads; rebuild via setQuads(split(...)) instead of in-place removal.
    @Unique private static void flatten(ItemStackRenderState stack) {
        for (final var layer : ((BakedQuadSupplier) stack).flat_items$layers()) {
            final var kept = new ArrayList<BakedQuad>();
            for (final var q : ((LayerRenderStateAccessor) (Object) layer).flat_items$getQuads().all()) {
                if (q.direction() == Direction.SOUTH) {
                    kept.add(new BakedQuad(q.position0(), q.position1(), q.position2(), q.position3(), q.packedUV0(), q.packedUV1(), q.packedUV2(), q.packedUV3(), q.direction(), q.materialInfo()));
                }
            }
            layer.setQuads(ItemQuads.split(kept));
        }
    }

    @Unique private static void cancel(PoseStack pose, Axis axis, float angle) {
        pose.rotate(axis, angle);
    }
}
