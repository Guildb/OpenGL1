package com.example.opengl1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val glview = findViewById<OpenGLView>(R.id.glview)

        findViewById<Button>(R.id.plusX).setOnClickListener{
            glview.camera.translate(1f,0f,0f)
        }

        findViewById<Button>(R.id.minusX).setOnClickListener{
            glview.camera.translate(-1f,0f,0f)
        }

        findViewById<Button>(R.id.plusY).setOnClickListener{
            glview.camera.translate(0f,1f,0f)
        }

        findViewById<Button>(R.id.minusY).setOnClickListener{
            glview.camera.translate(0f,-1f,0f)
        }

        findViewById<Button>(R.id.plusZ).setOnClickListener{
            glview.camera.translate(0f,0f,1f)
        }

        findViewById<Button>(R.id.minusZ).setOnClickListener{
            glview.camera.translate(0f,0f,-1f)
        }

        findViewById<Button>(R.id.rotateclock).setOnClickListener{
            glview.camera.rotate(-10f)
        }

        findViewById<Button>(R.id.rotateanticlock).setOnClickListener{
            glview.camera.rotate(10f)
        }

        findViewById<Button>(R.id.foward).setOnClickListener{
            val rad = glview.camera.rotation*(Math.PI/180)
            glview.camera.translate(-Math.sin(rad).toFloat(), 0f,-Math.cos(rad).toFloat())

        }

        findViewById<Button>(R.id.backwards).setOnClickListener{
            val rad = glview.camera.rotation*(Math.PI/180)
            glview.camera.translate(Math.sin(rad).toFloat(), 0f,Math.cos(rad).toFloat())
        }
    }
}