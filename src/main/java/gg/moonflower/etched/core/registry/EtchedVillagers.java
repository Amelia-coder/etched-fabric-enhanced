package gg.moonflower.etched.core.registry;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.fabric.RegistryObject;
import gg.moonflower.etched.core.Etched;
import gg.moonflower.etched.core.mixin.StructureTemplatePoolAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
//TODO: FIX
public class EtchedVillagers {
    private static Set<BlockState> getBlockStates(Block block) {
//        return ImmutableSet.copyOf((Collection)block.getStateDefinition().getPossibleStates());
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
    public static PoiType BARD_POI;
    public static VillagerProfession BARD_PROFESSION;

    public static void registers(){
//        Etched.LOGGER.info("[Etched] ETCHING_TABLE id: {}",
//                BuiltInRegistries.BLOCK.getKey(EtchedBlocks.ETCHING_TABLE.get()));
//// Должно быть: etched:etching_table
//// Если minecraft:air — REGISTRATE.register() ещё не вызван
//
//        Etched.LOGGER.info("[Etched] BARD_POI states: {}",
//                EtchedBlocks.ETCHING_TABLE.get().getStateDefinition().getPossibleStates());
//// Должны быть BlockState'ы EtchingTableBlock

        BARD_POI = PointOfInterestHelper.register(
                new ResourceLocation(Etched.MOD_ID, "bard"),
                1,
                32,
                ImmutableSet.copyOf(Blocks.NOTE_BLOCK.getStateDefinition().getPossibleStates())
        );

        BARD_PROFESSION = Registry.register(
                BuiltInRegistries.VILLAGER_PROFESSION,
                new ResourceLocation(Etched.MOD_ID, "bard"),
                new VillagerProfession(
                        Etched.MOD_ID + ":bard",
                        holder -> holder.is(BuiltInRegistries.POINT_OF_INTEREST_TYPE
                                .getResourceKey(BARD_POI).orElseThrow()),
                        holder -> holder.is(BuiltInRegistries.POINT_OF_INTEREST_TYPE
                                .getResourceKey(BARD_POI).orElseThrow()),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        null
                )
        );

        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,1,itemListings -> {
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_13,8,4,20));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_11,8,4,20));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_CAT,8,4,20));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.MUSIC_DISC_OTHERSIDE,8,4,20));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(Items.NOTE_BLOCK,1, 2, 16, 2));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedItems.MUSIC_LABEL.get()), 4, 2, 16, 1));
//            new VillagerTrades.ItemsForEmeralds(EtchedItems.MUSIC_LABEL.get(), 4, 2, 16, 1);
        });
        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,2,itemListings -> {
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedItems.BLANK_MUSIC_DISC.get()), 12, 2, 12, 15));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedBlocks.ETCHING_TABLE.get()), 12, 2, 12, 15));
        });
        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,3,itemListings -> {
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.CLAY), 6, 1, 16, 2));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.HAY_BLOCK), 12, 1, 8, 2));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.WHITE_WOOL), 8, 1, 32, 4));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.BONE_BLOCK), 24, 1, 8, 4));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.PACKED_ICE), 36, 1, 4, 8));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Blocks.GOLD_BLOCK), 48, 1, 2, 10));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.JUKEBOX), 26, 1, 4, 30));
        });

        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,4,itemListings -> {
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedItems.JUKEBOX_MINECART.get()), 28, 1, 4, 30));
            itemListings.add(new VillagerTrades.ItemsForEmeralds(new ItemStack(EtchedBlocks.ALBUM_JUKEBOX.get()), 30, 1, 4, 30));
        });
        TradeOfferHelper.registerVillagerOffers(BARD_PROFESSION,5,itemListings -> {
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 8, 40));
            itemListings.add(new VillagerTrades.EmeraldForItems(Items.AMETHYST_SHARD,  1, 8, 40));
        });



    }

        /**
         * Adds a simple trade for items or emeralds.
         *
         * @param item           The item to trade for
         * @param emeralds       The amount of emeralds to trade
         * @param itemCount      The amount of the item to trade
         * @param maxUses        The maximum amount of times this trade can be used before needing to reset
         * @param xpGain         The amount of experience gained by this exchange
         * @param sellToVillager Whether the villager is buying or selling the item for emeralds
         */
        /*
        public void add(ItemLike item, int emeralds, int itemCount, int maxUses, int xpGain, boolean sellToVillager) {
            this.add(new ItemTrade(() -> item, emeralds, itemCount, maxUses, xpGain, 0.05F, sellToVillager));
        } */

        /**
         * Adds a simple trade for items or emeralds.
         *
         * @param item            The item to trade for
         * @param emeralds        The amount of emeralds to trade
         * @param itemCount       The amount of the item to trade
         * @param maxUses         The maximum amount of times this trade can be used before needing to reset
         * @param xpGain          The amount of experience gained by this exchange
         * @param priceMultiplier The multiplier for how much the price deviates
         * @param sellToVillager  Whether the villager is buying or selling the item for emeralds
         */
        /*
        public void add(ItemLike item, int emeralds, int itemCount, int maxUses, int xpGain, float priceMultiplier, boolean sellToVillager) {
            this.add(new ItemTrade(() -> item, emeralds, itemCount, maxUses, xpGain, priceMultiplier, sellToVillager));
        } */

        /**
         * Adds a simple trade for items or emeralds.
         *
         * @param item           The item to trade for as a supplier
         * @param emeralds       The amount of emeralds to trade
         * @param itemCount      The amount of the item to trade
         * @param maxUses        The maximum amount of times this trade can be used before needing to reset
         * @param xpGain         The amount of experience gained by this exchange
         * @param sellToVillager Whether the villager is buying or selling the item for emeralds
         */
        /*        public void add(Supplier<? extends ItemLike> item, int emeralds, int itemCount, int maxUses, int xpGain, boolean sellToVillager) {
            this.add(new ItemTrade(item, emeralds, itemCount, maxUses, xpGain, 0.05F, sellToVillager));
        } */


        /**
         * Adds a simple trade for items or emeralds.
         *
         * @param item            The item to trade for as a supplier
         * @param emeralds        The amount of emeralds to trade
         * @param itemCount       The amount of the item to trade
         * @param maxUses         The maximum amount of times this trade can be used before needing to reset
         * @param xpGain          The amount of experience gained by this exchange
         * @param priceMultiplier The multiplier for how much the price deviates
         * @param sellToVillager  Whether the villager is buying or selling the item for emeralds
         */
        /*
        public void add(Supplier<? extends ItemLike> item, int emeralds, int itemCount, int maxUses, int xpGain, float priceMultiplier, boolean sellToVillager) {
            this.add(new ItemTrade(item, emeralds, itemCount, maxUses, xpGain, priceMultiplier, sellToVillager));
        } */

}