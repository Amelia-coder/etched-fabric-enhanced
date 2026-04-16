package gg.moonflower.etched.core.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import gg.moonflower.etched.common.entity.WorkAtNoteBlock;
import gg.moonflower.etched.core.Etched;
import gg.moonflower.etched.core.registry.EtchedVillagers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.ai.behavior.WorkAtPoi;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerGoalPackages.class)
public class VillagerGoalPackagesMixin {

    @Unique
    private static final Logger ETCHED_LOGGER = LoggerFactory.getLogger("etched");

    @Unique
    private static final ThreadLocal<VillagerProfession> etched$capturedProfession = new ThreadLocal<>();

    @Inject(method = "getWorkPackage", at = @At("HEAD"))
    private static void etched$capture(
            VillagerProfession profession,
            float speed,
            CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>>> cir
    ) {
        etched$capturedProfession.set(profession);
        ETCHED_LOGGER.debug("[Etched] getWorkPackage HEAD, profession={}", profession);
    }

    @ModifyVariable(
            method = "getWorkPackage",
            ordinal = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/behavior/VillagerGoalPackages;getMinimalLookBehavior()Lcom/mojang/datafixers/util/Pair;"
            )
    )
    private static WorkAtPoi etched$modifyWorkPoi(WorkAtPoi value) {
        VillagerProfession captured = etched$capturedProfession.get();

        if (captured != null && BuiltInRegistries.VILLAGER_PROFESSION
                .getKey(captured)
                .equals(new ResourceLocation(Etched.MOD_ID, "bard"))) {
            ETCHED_LOGGER.debug("[Etched] Replacing WorkAtPoi with WorkAtNoteBlock for bard");
            return new WorkAtNoteBlock();
        }

        return value;
    }

    @Inject(method = "getWorkPackage", at = @At("RETURN"))
    private static void etched$clear(
            VillagerProfession profession,
            float speed,
            CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>>> cir
    ) {
        ETCHED_LOGGER.debug("[Etched] getWorkPackage RETURN, profession={}", profession);
        etched$capturedProfession.remove();
    }
}