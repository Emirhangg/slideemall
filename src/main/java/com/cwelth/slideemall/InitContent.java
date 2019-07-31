package com.cwelth.slideemall;

import com.cwelth.slideemall.blocks.*;
import com.cwelth.slideemall.items.ItemLiquidModule;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InitContent {

    @GameRegistry.ObjectHolder("slideemall:blockslider")
    public static BlockSlider blockSlider;

    @GameRegistry.ObjectHolder("slideemall:blockhm")
    public static BlockHiddenManager blockHiddenManager;

    @GameRegistry.ObjectHolder("slideemall:itemliquidmodule")
    public static ItemLiquidModule itemLiquidModule;


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        blockSlider.initModel();
        blockHiddenManager.initModel();

    }

    @SideOnly(Side.CLIENT)
    public static void initBlockItemModels() {
        blockSlider.initItemModel();
        blockHiddenManager.initItemModel();

    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        itemLiquidModule.initItemModel();
    }
}
