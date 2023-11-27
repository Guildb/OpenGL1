package com.example.opengl1

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import freemap.openglwrapper.Camera
import freemap.openglwrapper.GLMatrix
import freemap.openglwrapper.GPUInterface
import freemap.openglwrapper.OpenGLUtils
import java.io.IOException
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class OpenGLView(ctx: Context, aSet: AttributeSet)  :GLSurfaceView(ctx, aSet), GLSurfaceView.Renderer {

    init {
        setEGLContextClientVersion(2) // specify OpenGL ES 2.0
        setRenderer(this) // set the renderer for this GLSurfaceView
    }

    val gpu = GPUInterface("default Shader")

    var fbuf: FloatBuffer? = null
    var indexBuffer: ShortBuffer? = null

    val red = floatArrayOf(1f, 0f, 0f, 1f)
    val blue = floatArrayOf(0f, 0f, 1f, 1f)
    val yellow = floatArrayOf(1f, 1f, 0f, 1f)

    //create a camera object
    val camera = Camera()

    //create a variable to hold the view matrix
    val viewMatrix = GLMatrix()

    //Create a variable to hold the projection matrix
    val projectionMatrix = GLMatrix()

    //Create a variable to hold the rotation matrix
    val rotationMatrix = GLMatrix()

    // creating the 2 cubes variables
    val cube1 = Cube(1f,0f,0f)
    val cube2 = Cube(-1f,0f,0f)



    // We initialise the rendering here
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background colour (red=0, green=0, blue=0, alpha=1)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Enable depth testing - will cause nearer 3D objects to automatically
        // be drawn over further objects
        GLES20.glClearDepthf(1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        try {
            val success = gpu.loadShaders(context.assets, "vertex.glsl", "fragment.glsl")
            if (!success) {
                Log.e("OpenGLBasic", gpu.lastShaderError)
            }
            val triangle = floatArrayOf(
                0f, 0f,0f,
                1f, 0f ,0f,
                0f, 1f, 0f
            )
            val doubleTriangle = floatArrayOf(
                0f, 0f,0f,
                1f, 0f ,0f,
                0f, 1f, 0f,
                0f,0f,0f,
                -1f,0f,0f,
                0f,-1f,0f
            )
            val triangle1and2 = floatArrayOf(
                0f, 0f,-3f,
                1f, 0f ,-3f,
                0.5f, 1f, -3f,
                -0.5f, 0f,-6f,
                0.5f, 0f ,-6f,
                0f, 1f, -6f
            )
            val square = floatArrayOf(
                0f,0f,-2f,
                1f,0f,-2f,
                1f,1f,-2f,
                0f,1f,-2f
            )

            fbuf = OpenGLUtils.makeFloatBuffer(square)

            val indices = shortArrayOf(2,3,0)
            indexBuffer = OpenGLUtils.makeShortBuffer(indices)
            //selects this shader
            gpu.select()
        } catch (e: IOException) {
            Log.e("OpenGLBasic", e.stackTraceToString())
        }
    }

    // Draws the current frame
    // Is called multiple times per second
    // your actual scene drawing should go in here
    override fun onDrawFrame(unused: GL10) {
        //CLear any previous settings from the previous frame
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Only run code if buffer is not null
        fbuf?.apply{
            //val ref_aVertex = gpu.getAttribLocation("aVertex")
            //val ref_uColour = gpu.getUniformLocation("uColour")
            val ref_uViewMatrix = gpu.getUniformLocation("uMvMtx")
            val ref_uProjMatrix = gpu.getUniformLocation("uPerspMtx")

            val attrVarRef= gpu.getAttribLocation("aVertex")
            val colourVarRef = gpu.getAttribLocation("aColour")

            //reset the view matrix to the identity matrix
            viewMatrix.setAsIdentityMatrix()

            //viewMatrix.translate(0f, 0f, -1f)
            // define the translation for the view matrix needed with respect to the current camera cords
            // camera.position x y or z
            //do rotation first and translation after, rotate arround y axis

            viewMatrix.rotateAboutAxis(-camera.rotation,'y')
            viewMatrix.translate(-camera.position.x,-camera.position.y,-camera.position.z )


            gpu.sendMatrix(ref_uViewMatrix, viewMatrix)
            gpu.sendMatrix(ref_uProjMatrix, projectionMatrix)

            //---------------------------------------------------------------------------//

            //code to draw the triangles
            //gpu.specifyBufferedDataFormat(ref_aVertex, this, 0)
            //gpu.setUniform4FloatArray(ref_uColour, blue)
            //gpu.drawBufferedTriangles(0,3)
            //gpu.setUniform4FloatArray(ref_uColour, yellow)
            //gpu.drawBufferedTriangles(3,3)

            //---------------------------------------------------------------------------//

            //code to draw a square
            //draw first as red using the indexbuffer
            //gpu.setUniform4FloatArray(ref_uColour, red)
            //gpu.drawIndexedBufferedData(fbuf!!,indexBuffer!!, 0, ref_aVertex)
            //draw secound as blue using the indecesbuffer
            //gpu.specifyBufferedDataFormat(ref_aVertex, fbuf!!,0)
            //gpu.setUniform4FloatArray(ref_uColour,blue)
            //gpu.drawBufferedTriangles(0,3)

            //---------------------------------------------------------------------------//

            //code to call the cube class to draw a cube

            cube1.render(gpu, attrVarRef, colourVarRef)
            cube2.render(gpu, attrVarRef, colourVarRef)
        }
    }

    // Is called whenever the resolution changes (on a mobile device this will occur when the device is rotated
    override fun onSurfaceChanged(unused: GL10, w: Int, h: Int) {
        GLES20.glViewport(0, 0, w, h)
        val hfov = 60.0f
        val aspect : Float = w.toFloat()/h
        projectionMatrix.setProjectionMatrix(hfov, aspect, 0.001f, 100f)
    }




}