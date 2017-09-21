package com.yogpc.qp

import net.minecraft.client.renderer.VertexBuffer
import net.minecraft.util.math.{Vec3d, Vec3i}

package object render {

    implicit class BufferBuilderHelper(val buffer: VertexBuffer) extends AnyVal {
        def pos(vec: Vec3d): VertexBuffer = buffer.pos(vec.xCoord, vec.yCoord, vec.zCoord)

        def colored(): VertexBuffer = buffer.color(255, 255, 255, 255)
    }

    implicit class Vec3dHelper(val vec3d: Vec3d) extends AnyVal {
        def +(o: Vec3d): Vec3d = vec3d add o

        def +(o: Vec3i): Vec3d = vec3d addVector(o.getX, o.getY, o.getZ)

        def -(o: Vec3d): Vec3d = vec3d subtract o

        def -(o: Vec3i): Vec3d = vec3d subtract(o.getX, o.getY, o.getZ)
    }

}