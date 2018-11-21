package com.yogpc.qp.utils

import com.yogpc.qp.tile.EnergyUsage
import com.yogpc.qp.{Config, QuarryPlus}
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos

trait Reason {
    def isEnergyIsuue: Boolean

    def usage: Option[EnergyUsage] = None

    override def toString: String
}

object Reason {

    private[this] final val Nano = 1000000000l

    def apply(energyUsage: EnergyUsage, required: Double, amount: Double): Reason = new EnergyReasonImpl(energyUsage, (required * Nano).toLong, (amount * Nano).toLong)

    def apply(pos: BlockPos, state: IBlockState): Reason = new BreakCanceled(pos, state)

    def print[T]: Reason => Option[T] = r => {
        if (Config.content.debug) {
            QuarryPlus.LOGGER.info(r.toString)
        }
        None
    }

    class EnergyReasonImpl(energyUsage: EnergyUsage, required: Long, amount: Long) extends Reason {
        override def isEnergyIsuue: Boolean = true

        override def usage: Option[EnergyUsage] = Some(energyUsage)

        override def toString: String = {
            s"Action of ${usage.get.toString} required ${required * 10 / Nano} RF but machine has ${amount * 10 / Nano} RF."
        }
    }

    class BreakCanceled(pos: BlockPos, state: IBlockState) extends Reason {
        override def isEnergyIsuue: Boolean = false

        override def toString: String = s"Breaking $state at ${pos.getX}, ${pos.getY}, ${pos.getZ} was canceled."
    }

}