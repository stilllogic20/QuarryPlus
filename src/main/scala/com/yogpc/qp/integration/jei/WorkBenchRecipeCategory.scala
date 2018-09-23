package com.yogpc.qp.integration.jei

import com.yogpc.qp.integration.jei.WorkBenchRecipeCategory._
import com.yogpc.qp.{QuarryPlus, QuarryPlusI}
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawableAnimated.StartDirection
import mezz.jei.api.gui.{IDrawable, IRecipeLayout}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation

class WorkBenchRecipeCategory(guiHelper: IGuiHelper) extends IRecipeCategory[WorkBenchRecipeWrapper] {
    //4, 13 => 175, 94

    val bar = guiHelper.createDrawable(backGround, xOff, 95, 160, 4)
    val animateBar = guiHelper.createAnimatedDrawable(bar, 300, StartDirection.LEFT, false)

    override val getBackground: IDrawable = guiHelper.createDrawable(backGround, xOff, yOff, 175, 94)

    override def setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: WorkBenchRecipeWrapper, ingredients: IIngredients): Unit = {
        val guiItemStack = recipeLayout.getItemStacks
        //7, 17 -- 7, 89
        for (i <- 0 until recipeWrapper.recipeSize) {
            if (i < 9) {
                guiItemStack.init(i, true, 7 + o * i - xOff, 7 - yOff)
            } else if (i < 18) {
                guiItemStack.init(i, true, 7 + o * (i - 9) - xOff, 7 + o - yOff)
            }
        }
        guiItemStack.init(recipeWrapper.recipeSize, false, 7 - xOff, 71 - yOff)
        guiItemStack.set(ingredients)
    }

    override def getTitle: String = QuarryPlusI.blockWorkbench.getLocalizedName

    override val getUid: String = UID

    override def drawExtras(minecraft: Minecraft): Unit = {
        animateBar.draw(minecraft, 8, 64)
    }

    override def getModName: String = QuarryPlus.Mod_Name
}

object WorkBenchRecipeCategory {
    final val UID = "quarryplus.workbenchplus"
    val backGround = new ResourceLocation(QuarryPlus.modID, "textures/gui/workbench_jei2.png")
    final val xOff = 0
    final val yOff = 0
    final val o = 18
}
