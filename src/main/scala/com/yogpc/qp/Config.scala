package com.yogpc.qp

import java.io.File

import com.yogpc.qp.tile.WorkbenchRecipes
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.config.{ConfigElement, Configuration}
import net.minecraftforge.fml.client.config.IConfigElement
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object Config {

    val instance = this
    private var mContent: Content = _
    private var configuration: Configuration = _

    @SubscribeEvent
    def onChange(configChangedEvent: ConfigChangedEvent): Unit = {
        if (configChangedEvent.getModID == QuarryPlus.modID) {
            QuarryPlus.LOGGER.info("QuarryPlus Config loaded!")
            sync()
        }
    }

    def sync(): Unit = {
        mContent = new Content
        QuarryPlusI.blockBreaker.setTickRandomly(!mContent.placerOnlyPlaceFront)
        QuarryPlusI.blockPlacer.setTickRandomly(!mContent.placerOnlyPlaceFront)
    }

    def setConfigFile(file: File): Unit = {
        this.configuration = new Configuration(file)
        sync()
    }

    def getElements: java.util.List[IConfigElement] = {
        val list = new java.util.LinkedList[IConfigElement]()
        list.addAll(new ConfigElement(configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements)
        list
    }

    def content = mContent

    val DisableSpawnerController_key = "DisableSpawnerController"
    val DisableChunkDestroyer_key = "DisableChunkDestroyer"
    val SpawnerControllerEntityBlackList_key = "SpawnerControllerEntityBlackList"
    val RecipeDifficulty_key = "RecipeDifficulty"
    val PlacerOnlyPlaceFront_key = "PlacerOnlyPlaceFront"
    val NoEnergy_key = "NoEnergy"
    val RemoveBedrock_Key = "RemoveBedrock"
    val DEBUG_key = "DEBUG"

    class Content {

        import scala.collection.JavaConverters._

        val disableController = configuration.get(Configuration.CATEGORY_GENERAL, DisableSpawnerController_key, false).setRequiresMcRestart(true).getBoolean
        val disableChunkDestroyer = configuration.get(Configuration.CATEGORY_GENERAL, DisableChunkDestroyer_key, true).setRequiresMcRestart(true).getBoolean
        val spawnerBlacklist = configuration.get(Configuration.CATEGORY_GENERAL, SpawnerControllerEntityBlackList_key, Array("minecraft:ender_dragon", "minecraft:wither"))
          .getStringList.map(new ResourceLocation(_)).toSet.asJava
        val recipeDifficulty = configuration.get(Configuration.CATEGORY_GENERAL, RecipeDifficulty_key, 2d)
        recipeDifficulty.setComment("Default is 2.0")
        recipeDifficulty.setMinValue(1d)
        WorkbenchRecipes.difficulty = recipeDifficulty.getDouble(2d)

        val placerOnlyPlaceFront = configuration.get(Configuration.CATEGORY_GENERAL, PlacerOnlyPlaceFront_key, true).getBoolean
        val noEnergy = configuration.getBoolean(NoEnergy_key, Configuration.CATEGORY_GENERAL, false, NoEnergy_key)
        PowerManager.loadConfiguration(configuration)

        val removeBedrock = configuration.getBoolean(RemoveBedrock_Key, Configuration.CATEGORY_GENERAL, false, RemoveBedrock_Key)
        val debug = configuration.getBoolean(DEBUG_key, Configuration.CATEGORY_GENERAL, false, DEBUG_key)

        if (configuration.hasChanged)
            configuration.save()
    }

}
