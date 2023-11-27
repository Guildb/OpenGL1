package com.example.opengl1

import freemap.openglwrapper.GPUInterface
import freemap.openglwrapper.OpenGLUtils
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube(val x: Float, val y:Float, val z:Float) {
    val vertexBuf: FloatBuffer
    val indexBuffer: ShortBuffer

    init {

        vertexBuf = OpenGLUtils.makeFloatBuffer(
            floatArrayOf(
                x, y + 0.5f, z,
                x + 0.5f, y + 0.5f, z,
                x + 0.5f, y + 0.5f, z + 0.5f,
                x, y + 0.5f, z + 0.5f,
                x, y, z,
                x + 0.5f, y, z,
                x + 0.5f, y, z + 0.5f,
                x, y, z + 0.5f,
            )
        )
        indexBuffer = OpenGLUtils.makeShortBuffer(
            shortArrayOf(
                0, 1, 4, 4, 1, 5,
                3, 2, 6, 3, 7, 6,
                0, 3, 2, 0, 2, 1,
                4, 7, 5, 5, 6, 7,
                7, 3, 0, 7, 0, 4,
                5, 2, 1, 5, 2, 6
            )
        )
    }

    fun render(gpu: GPUInterface, aVertex: Int ){
        gpu.drawIndexedBufferedData(vertexBuf,indexBuffer, 0, aVertex)
    }
}