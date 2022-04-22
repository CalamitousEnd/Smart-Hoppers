package me.final_cataclysm.smarthoppers.mixin;

import me.final_cataclysm.smarthoppers.config.General;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uppers.tiles.UpperBlockEntity;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(UpperBlockEntity.class)
public abstract class UpperBlockEntityMixin extends BlockEntity {
    public UpperBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    public abstract void setCooldown(int pCooldownTime);

    @Unique
    private static int getCooldown(int initialValue, UpperBlockEntity blockEntity) {
        if (blockEntity != null) {
            return General.InsertExtractCooldown2.get();
        }
        return initialValue;
    }
    @ModifyConstant(method = "tryMoveItems", remap = false, constant = @Constant(intValue = 8))
    private static int modifyInsertAndExtractCooldown(int initialValue, Level level, BlockPos pos, BlockState state, UpperBlockEntity blockEntity, BooleanSupplier supplier) {
        return UpperBlockEntityMixin.getCooldown(initialValue, blockEntity);
    }
    @Redirect(method = "tryMoveInItem", remap = false, at = @At(value = "INVOKE", target = "Luppers/tiles/UpperBlockEntity;setCooldown(I)V"))
    private static void setTransferCooldown(UpperBlockEntity blockEntity, int pCooldownTime) {
        ((UpperBlockEntityMixin) (Object) blockEntity).setCooldown(General.TransferCooldown2.get());
    }
    @Inject(
            method = "getContainerAt(Lnet/minecraft/world/level/Level;DDD)Lnet/minecraft/world/Container;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ), cancellable = true
    )
        private static void injectInventoryToUpper(Level world, double x, double y, double z, CallbackInfoReturnable<Inventory> info) {
        List<Entity> list = world.getEntities((Entity) null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), entity -> entity instanceof Player);

        if (!list.isEmpty()) {
            Player entityPlayer = (Player) list.get(world.random.nextInt(list.size()));
            info.setReturnValue(entityPlayer.getInventory());
        }
    }
}
