package me.final_cataclysm.smarthoppers.mixin;

import me.final_cataclysm.smarthoppers.config.General;
import me.final_cataclysm.smarthoppers.utils.SimpleContainerInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends BlockEntity {
    public HopperBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    public abstract void setCooldown(int pCooldownTime);

    @Unique
    private static int getCooldown(int initialValue, HopperBlockEntity blockEntity) {
        if (blockEntity != null) {
            return General.InsertExtractCooldown.get();
        }
        return initialValue;
    }

    @ModifyConstant(method = "tryMoveItems", constant = @Constant(intValue = 8))
    private static int modifyInsertAndExtractCooldown(int initialValue, Level world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, BooleanSupplier booleanSupplier) {
        return HopperBlockEntityMixin.getCooldown(initialValue, blockEntity);
    }

    @Redirect(method = "tryMoveInItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setCooldown(I)V"))
    private static void setTransferCooldown(HopperBlockEntity blockEntity, int pCooldownTime) {
        ((HopperBlockEntityMixin) (Object) blockEntity).setCooldown(General.TransferCooldown.get());
    }

    @Inject(
            method = "getContainerAt(Lnet/minecraft/world/level/Level;DDD)Lnet/minecraft/world/Container;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ), cancellable = true
    )
    private static void injectInventoryToHopper(Level world, double x, double y, double z, CallbackInfoReturnable<Inventory> info) {
        List<Entity> list = world.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), entity -> hasCompatibleInventory(entity));

        if (!list.isEmpty()) {
            Entity entity = list.get(world.random.nextInt(list.size()));
            info.setReturnValue(getInventory(entity));
        }
    }

    private static Inventory getInventory(Entity entity) {
        if(entity instanceof Player) {
            return ((Player) entity).getInventory();
        }
        if(entity instanceof AbstractHorse) {
            return new SimpleContainerInventory(((AbstractHorseInterface)entity).getInventory());
        }
        throw new IllegalStateException("Unable to get the inventory from entity of type " + entity.getClass().toString());
    }

    private static boolean hasCompatibleInventory(Entity entity) {
        return entity instanceof AbstractHorse || entity instanceof Player;
    }
}
