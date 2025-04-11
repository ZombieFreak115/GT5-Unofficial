package gregtech.common.covers;

import static gregtech.api.util.GTUtility.getDescLoc;
import static gregtech.api.util.GTUtility.sendChatToPlayer;
import static gregtech.common.gui.mui1.cover.NeedMaintainanceUIFactory.maintLoc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.common.gui.mui1.cover.NeedMaintainanceUIFactory;

public class CoverNeedMaintainance extends CoverLegacyData {

    public CoverNeedMaintainance(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public static boolean isRotor(ItemStack rotor) {
        return (rotor != null && rotor.getItem() instanceof MetaGeneratedTool
            && rotor.getItemDamage() >= 170
            && rotor.getItemDamage() <= 176);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        boolean needsRepair = false;
        if (coverable instanceof IGregTechTileEntity tTileEntity) {
            final IMetaTileEntity mTileEntity = tTileEntity.getMetaTileEntity();
            if (mTileEntity instanceof MTEMultiBlockBase multi) {
                final int ideal = multi.getIdealStatus();
                final int real = multi.getRepairStatus();
                final ItemStack tRotor = multi.getRealInventory()[1];
                final int coverVar = this.coverData >>> 1;
                if (coverVar < 5) {
                    if (ideal - real > coverVar) needsRepair = true;
                } else if (coverVar == 5 || coverVar == 6) {
                    if (isRotor(tRotor)) {
                        long tMax = MetaGeneratedTool.getToolMaxDamage(tRotor);
                        long tCur = MetaGeneratedTool.getToolDamage(tRotor);
                        if (coverVar == 5) {
                            needsRepair = (tCur >= tMax * 8 / 10);
                        } else {
                            long mExpectedDamage = Math.round(
                                Math.min(
                                    multi.mEUt / multi.damageFactorLow,
                                    Math.pow(multi.mEUt, multi.damageFactorHigh)));
                            needsRepair = tCur + mExpectedDamage * 2 >= tMax;
                        }
                    } else {
                        needsRepair = true;
                    }
                }
            }
        }
        if (this.coverData % 2 == 0) {
            needsRepair = !needsRepair;
        }

        coverable.setOutputRedstoneSignal(coverSide, (byte) (needsRepair ? 0 : 15));
        coverable.setOutputRedstoneSignal(coverSide.getOpposite(), (byte) (needsRepair ? 0 : 15));
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = (this.coverData + (aPlayer.isSneaking() ? -1 : 1)) % 14;
        if (this.coverData < 0) {
            this.coverData = 13;
        }
        switch (this.coverData) {
            case 0 -> sendChatToPlayer(aPlayer, maintLoc(1, false));
            case 1 -> sendChatToPlayer(aPlayer, maintLoc(1, true));
            case 2 -> sendChatToPlayer(aPlayer, maintLoc(2, false));
            case 3 -> sendChatToPlayer(aPlayer, maintLoc(2, true));
            case 4 -> sendChatToPlayer(aPlayer, maintLoc(3, false));
            case 5 -> sendChatToPlayer(aPlayer, maintLoc(3, true));
            case 6 -> sendChatToPlayer(aPlayer, maintLoc(4, false));
            case 7 -> sendChatToPlayer(aPlayer, maintLoc(4, true));
            case 8 -> sendChatToPlayer(aPlayer, maintLoc(5, false));
            case 9 -> sendChatToPlayer(aPlayer, maintLoc(5, true));
            case 10 -> sendChatToPlayer(aPlayer, getDescLoc("need_maint_rotor_lo"));
            case 11 -> sendChatToPlayer(aPlayer, getDescLoc("need_maint_rotor_lo") + getDescLoc("inverted_b"));
            case 12 -> sendChatToPlayer(aPlayer, getDescLoc("need_maint_rotor_hi"));
            case 13 -> sendChatToPlayer(aPlayer, getDescLoc("need_maint_rotor_hi") + getDescLoc("inverted_b"));
        }
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 60;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new NeedMaintainanceUIFactory(buildContext).createWindow();
    }

}
