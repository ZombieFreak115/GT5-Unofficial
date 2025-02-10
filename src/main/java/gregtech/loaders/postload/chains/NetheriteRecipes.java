package gregtech.loaders.postload.chains;

import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.ForbiddenMagic;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class NetheriteRecipes {

    public static void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(1000))
            .fluidOutputs(Materials.NetherSemiFluid.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .noOptimize()
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherSemiFluid.getFluid(100000))
            .itemOutputs(ItemList.Heavy_Hellish_Mud.get(2))
            .fluidOutputs(
                Materials.NefariousGas.getFluid(4000),
                FluidUtils.getFluidStack("fluid.coalgas", 16000),
                FluidUtils.getFluidStack("fluid.anthracene", 7000),
                Materials.SulfurTrioxide.getGas(21000),
                Materials.SulfurDioxide.getGas(38000),
                Materials.NitrogenDioxide.getGas(14000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .noOptimize()
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(4000), Materials.NefariousGas.getFluid(16000))
            .fluidOutputs(Materials.NefariousOil.getFluid(12000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .noOptimize()
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Heavy_Hellish_Mud.get(32))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16000))
            .fluidOutputs(Materials.RichNetherWaste.getFluid(16000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .noOptimize()
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(EtFuturumRequiem.ID, "netherite_scrap", 0))
            .fluidInputs(Materials.RichNetherWaste.getFluid(2000))
            .itemOutputs(ItemList.Netherite_Crystal_Seed.get(1))
            .outputChances(1000)
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .noOptimize()
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Netherite_Crystal_Seed.get(1))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16000))
            .itemOutputs(ItemList.Brittle_Netherite_Crystal.get(1), ItemList.Netherite_Crystal_Seed.get(1))
            .outputChances(5000, 5000)
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        if (ForbiddenMagic.isModLoaded()) {
            // new ResearchItem( TODO add to coremod pr
            // "HELLISHMETAL",
            // "NEWHORIZONS",
            // new AspectList().add(Aspect.getAspect("infernus"), 15).add(Aspect.getAspect("lucrum"), 12)
            // .add(Aspect.getAspect("fames"), 6).add(Aspect.getAspect("ignis"), 3),
            // -6,
            // -7,
            // 3,
            // GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HellishMetal, 1))
            // .setConcealed().setRound().setPages(new ResearchPage("TConstruct.research_page.UNDYINGTOTEM.1"))
            // .registerResearchItem();
            // TCHelper.addResearchPage(
            // "HELLISHMETAL",
            // new ResearchPage(
            // Objects.requireNonNull(
            // TCHelper.findInfusionRecipe(
            // GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HellishMetal, 1)))));
            // ThaumcraftApi.addWarpToResearch("HELLISHMETAL", 3);

            ThaumcraftApi.addInfusionCraftingRecipe(
                "HELLISHMETAL",
                GTOreDictUnificator.get(OrePrefixes.block, Materials.HellishMetal, 1),
                1,
                new AspectList().add(Aspect.getAspect("ignis"), 8),
                MaterialsElements.getInstance().RHODIUM.getBlock(1),
                new ItemStack[] { getModItem(ForbiddenMagic.ID, "NetherShard", 1, 0),
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Thaumium, 1),
                    getModItem(ForbiddenMagic.ID, "NetherShard", 1, 0),
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Thaumium, 1), });

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialsElements.getInstance().RHODIUM.getIngot(1),
                    getModItem(ForbiddenMagic.ID, "NetherShard", 8, 0),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 8))
                .fluidInputs(Materials.Thaumium.getMolten(8 * 144))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HellishMetal, 1))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(electrolyzerRecipes);
        }

    }
}
