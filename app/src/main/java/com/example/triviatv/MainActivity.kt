package com.example.triviatv

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var pantalla: LinearLayout

    private var preguntaActual = 0
    private var puntuacion = 0
    private var respondida = false

    private val preguntas = listOf(
        Pregunta(
            "¿Cuál es el planeta más grande del sistema solar?",
            listOf("Marte", "Júpiter", "Saturno", "Venus"), 1
        ),
        Pregunta(
            "¿En qué continente se encuentra Egipto?",
            listOf("Asia", "Europa", "África", "Oceanía"), 2
        ),
        Pregunta(
            "¿Quién escribió Don Quijote de la Mancha?",
            listOf(
                "Miguel de Cervantes",
                "Pablo Neruda",
                "Gabriel García Márquez",
                "Federico García Lorca"
            ), 0
        ),
        Pregunta(
            "¿Cuál es el océano más grande del mundo?",
            listOf("Atlántico", "Índico", "Ártico", "Pacífico"), 3
        ),
        Pregunta(
            "¿Cuántos lados tiene un hexágono?",
            listOf("Cinco", "Seis", "Siete", "Ocho"), 1
        )
    )

    private data class Pregunta(
        val texto: String,
        val opciones: List<String>,
        val respuestaCorrecta: Int
    )

    private val fondo = Color.rgb(11, 18, 35)
    private val tarjeta = Color.rgb(22, 33, 56)
    private val cian = Color.rgb(34, 211, 238)
    private val blanco = Color.rgb(241, 245, 249)
    private val gris = Color.rgb(148, 163, 184)
    private val verde = Color.rgb(34, 197, 94)
    private val rojo = Color.rgb(239, 68, 68)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.navigationBarColor = fondo
        window.statusBarColor = fondo

        mostrarInicio()
    }

    private fun prepararPantalla() {
        pantalla = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(45), dp(20), dp(45), dp(20))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(12, 24, 48),
                    fondo,
                    Color.rgb(16, 31, 55)
                )
            )
        }

        setContentView(pantalla)
    }

    private fun agregarTexto(
        contenido: String,
        tamano: Float,
        color: Int,
        negrita: Boolean = false
    ): TextView {

        val texto = TextView(this).apply {
            text = contenido
            textSize = tamano
            setTextColor(color)
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(5), dp(8), dp(5))

            if (negrita) {
                setTypeface(null, Typeface.BOLD)
            }
        }

        pantalla.addView(
            texto,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        return texto
    }

    private fun agregarEspacio(valor: Int) {
        pantalla.addView(
            View(this),
            LinearLayout.LayoutParams(1, dp(valor))
        )
    }

    private fun crearFondo(
        color: Int,
        radio: Int,
        borde: Int = Color.TRANSPARENT
    ): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radio).toFloat()

            if (borde != Color.TRANSPARENT) {
                setStroke(dp(2), borde)
            }
        }
    }

    private fun agregarBoton(
        contenido: String,
        colorBase: Int = tarjeta,
        colorSeleccion: Int = Color.rgb(8, 145, 178),
        accion: () -> Unit
    ): Button {

        val boton = Button(this).apply {
            text = contenido
            textSize = 17f
            isAllCaps = false
            setTextColor(blanco)
            typeface = Typeface.DEFAULT_BOLD
            isFocusable = true
            isFocusableInTouchMode = true
            gravity = Gravity.CENTER
            setPadding(dp(12), dp(5), dp(12), dp(5))

            background = crearFondo(colorBase, 14)

            setOnFocusChangeListener { vista, tieneFoco ->
                vista.background = crearFondo(
                    if (tieneFoco) colorSeleccion else colorBase,
                    14,
                    if (tieneFoco) cian else Color.TRANSPARENT
                )

                if (tieneFoco) {
                    setTextColor(Color.WHITE)
                }
            }

            setOnClickListener {
                accion()
            }
        }

        val parametros = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(58)
        )

        parametros.bottomMargin = dp(10)

        pantalla.addView(boton, parametros)

        return boton
    }

    private fun agregarTarjeta(
        titulo: String,
        descripcion: String
    ) {
        val contenedor = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = crearFondo(tarjeta, 16)
            setPadding(dp(12), dp(15), dp(12), dp(15))
        }

        val tituloVista = TextView(this).apply {
            text = titulo
            textSize = 20f
            setTextColor(cian)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }

        val descripcionVista = TextView(this).apply {
            text = descripcion
            textSize = 13f
            setTextColor(gris)
            gravity = Gravity.CENTER
        }

        contenedor.addView(tituloVista)
        contenedor.addView(descripcionVista)

        val parametros = LinearLayout.LayoutParams(
            0,
            dp(85),
            1f
        )

        parametros.setMargins(dp(6), 0, dp(6), 0)

        tarjetaTemporal().addView(contenedor, parametros)
    }

    private fun tarjetaTemporal(): LinearLayout {
        if (pantalla.childCount > 0) {
            val ultima = pantalla.getChildAt(pantalla.childCount - 1)
            if (ultima is LinearLayout && ultima.tag == "tarjetas") {
                return ultima
            }
        }

        val fila = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            tag = "tarjetas"
        }

        pantalla.addView(
            fila,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(90)
            )
        )

        return fila
    }

    private fun mostrarInicio() {
        prepararPantalla()

        agregarEspacio(10)

        agregarTexto(
            "TRIVIA",
            19f,
            gris,
            true
        )

        agregarTexto(
            "TV",
            58f,
            cian,
            true
        )

        agregarTexto(
            "Pon a prueba tus conocimientos",
            22f,
            blanco
        )

        agregarTexto(
            "Demuestra cuánto sabes de cultura general",
            15f,
            gris
        )

        agregarEspacio(25)

        agregarTarjeta("05", "PREGUNTAS")
        agregarTarjeta("04", "OPCIONES")
        agregarTarjeta("∞", "DIVERSIÓN")

        agregarEspacio(25)

        agregarBoton(
            "▶   COMENZAR PARTIDA",
            Color.rgb(8, 115, 155),
            Color.rgb(14, 165, 190)
        ) {
            preguntaActual = 0
            puntuacion = 0
            mostrarPregunta()
        }

        agregarTexto(
            "TRIVIATV  •  CULTURA GENERAL",
            12f,
            gris
        )

        enfocarPrimerBoton()
    }

    private fun mostrarProgreso() {
        val fila = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val texto = TextView(this).apply {
            text = "PROGRESO"
            textSize = 12f
            setTextColor(gris)
            typeface = Typeface.DEFAULT_BOLD
        }

        fila.addView(
            texto,
            LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        val numero = TextView(this).apply {
            text = "${preguntaActual + 1} / ${preguntas.size}"
            textSize = 14f
            setTextColor(cian)
            typeface = Typeface.DEFAULT_BOLD
        }

        fila.addView(numero)

        pantalla.addView(fila)

        val barraFondo = LinearLayout(this).apply {
            background = crearFondo(tarjeta, 8)
        }

        val barra = View(this).apply {
            background = crearFondo(cian, 8)
        }

        barraFondo.addView(
            barra,
            LinearLayout.LayoutParams(
                0,
                dp(7)
            ).apply {
                weight = 1f
            }
        )

        pantalla.addView(
            barraFondo,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(7)
            )
        )
    }

    private fun mostrarPregunta() {
        prepararPantalla()
        respondida = false

        val pregunta = preguntas[preguntaActual]

        agregarTexto(
            "TRIVIATV",
            22f,
            cian,
            true
        )

        agregarEspacio(10)

        mostrarProgreso()

        agregarTexto(
            "CULTURA GENERAL",
            13f,
            gris,
            true
        )

        agregarTexto(
            "Puntuación: $puntuacion puntos",
            15f,
            verde,
            true
        )

        agregarEspacio(20)

        agregarTexto(
            pregunta.texto,
            26f,
            blanco,
            true
        )

        agregarEspacio(20)

        val botones = mutableListOf<Button>()

        pregunta.opciones.forEachIndexed { indice, opcion ->

            val boton = agregarBoton(
                "${indice + 1}.   $opcion"
            ) {
                comprobarRespuesta(indice, botones)
            }

            botones.add(boton)
        }

        enfocarPrimerBoton()
    }

    private fun comprobarRespuesta(
        indice: Int,
        botones: List<Button>
    ) {
        if (respondida) return

        respondida = true

        val pregunta = preguntas[preguntaActual]
        val correcta = pregunta.respuestaCorrecta

        val esCorrecta = indice == correcta

        if (esCorrecta) {
            puntuacion++
        }

        botones.forEachIndexed { posicion, boton ->
            boton.isEnabled = false
            boton.setTextColor(Color.WHITE)

            boton.background = when (posicion) {
                correcta -> crearFondo(
                    Color.rgb(22, 101, 52),
                    14,
                    verde
                )

                indice -> crearFondo(
                    Color.rgb(127, 29, 29),
                    14,
                    rojo
                )

                else -> crearFondo(tarjeta, 14)
            }
        }

        agregarEspacio(10)

        agregarTexto(
            if (esCorrecta) "¡RESPUESTA CORRECTA!"
            else "RESPUESTA INCORRECTA",
            18f,
            if (esCorrecta) verde else rojo,
            true
        )

        if (!esCorrecta) {
            agregarTexto(
                "La respuesta era: ${pregunta.opciones[correcta]}",
                15f,
                blanco
            )
        }

        agregarBoton(
            if (preguntaActual < preguntas.size - 1)
                "SIGUIENTE PREGUNTA"
            else
                "VER RESULTADOS"
        ) {
            preguntaActual++

            if (preguntaActual < preguntas.size) {
                mostrarPregunta()
            } else {
                mostrarResultados()
            }
        }

        enfocarUltimoBoton()
    }

    private fun mostrarResultados() {
        prepararPantalla()

        agregarTexto(
            "TRIVIATV",
            24f,
            cian,
            true
        )

        agregarEspacio(25)

        agregarTexto(
            "¡PARTIDA TERMINADA!",
            28f,
            blanco,
            true
        )

        agregarEspacio(20)

        agregarTexto(
            "$puntuacion / ${preguntas.size}",
            55f,
            cian,
            true
        )

        val porcentaje = puntuacion * 100 / preguntas.size

        agregarTexto(
            "$porcentaje% de aciertos",
            19f,
            verde,
            true
        )

        agregarEspacio(15)

        val mensaje = when (puntuacion) {
            5 -> "¡Excelente! Lo sabes todo."
            4 -> "¡Muy buen trabajo!"
            3 -> "¡Buen intento!"
            2 -> "Puedes mejorar."
            else -> "¡Sigue practicando!"
        }

        agregarTexto(
            mensaje,
            21f,
            blanco
        )

        agregarEspacio(25)

        agregarBoton("↻   VOLVER A JUGAR") {
            preguntaActual = 0
            puntuacion = 0
            mostrarInicio()
        }

        enfocarPrimerBoton()
    }

    private fun enfocarPrimerBoton() {
        pantalla.post {
            for (i in 0 until pantalla.childCount) {
                val vista = pantalla.getChildAt(i)

                if (vista is Button && vista.isEnabled) {
                    vista.requestFocus()
                    return@post
                }
            }
        }
    }

    private fun enfocarUltimoBoton() {
        pantalla.post {
            for (i in pantalla.childCount - 1 downTo 0) {
                val vista = pantalla.getChildAt(i)

                if (vista is Button && vista.isEnabled) {
                    vista.requestFocus()
                    return@post
                }
            }
        }
    }

    private fun dp(valor: Int): Int {
        return (
                valor * resources.displayMetrics.density
                ).toInt()
    }
}
