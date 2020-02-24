package com.example.calculadora2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import kotlin.math.pow

//Héctor Granja Cortés

class MainActivity : AppCompatActivity() {

    private var operando1: String = ""
    private var operando2: String = ""
    private var operador: String = ""
    private var operadorPulsado: String = ""
    private var valorActual: String = ""
    private var resultado: Double = 0.0
    private val format = DecimalFormat("0.##############")
    private var limpiarPantalla: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val orientation = getResources().getConfiguration().orientation

        if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mostrarBotonesExtra(false)
        } else {
            mostrarBotonesExtra(true)
        }

        if (savedInstanceState != null) {

            resultado = savedInstanceState.getDouble("RESULTADO", 0.0)
            valorActual = savedInstanceState.getString("VALORACTUAL", "")
            operador = savedInstanceState.getString("OPERADOR", "")
            operando1 = savedInstanceState.getString("OPERANDO1", "")
            operando2 = savedInstanceState.getString("OPERANDO2", "")
            limpiarPantalla = savedInstanceState.getBoolean("LIMPIARPANTALLA", false)

            if (valorActual != "") {
                actualizaTVResultado(valorActual.toDouble())
            }
        }

        button0.setOnClickListener { asignarOperandos("0"); }
        button1.setOnClickListener { asignarOperandos("1"); }
        button2.setOnClickListener { asignarOperandos("2"); }
        button3.setOnClickListener { asignarOperandos("3"); }
        button4.setOnClickListener { asignarOperandos("4"); }
        button5.setOnClickListener { asignarOperandos("5"); }
        button6.setOnClickListener { asignarOperandos("6"); }
        button7.setOnClickListener { asignarOperandos("7"); }
        button8.setOnClickListener { asignarOperandos("8"); }
        button9.setOnClickListener { asignarOperandos("9"); }

        buttonSumar.setOnClickListener {
            operadorPulsado = "+"
            calcularOperacion()
        }

        buttonRestar.setOnClickListener {
            operadorPulsado = "-"
            calcularOperacion()
        }

        buttonMultiplicar.setOnClickListener {
            operadorPulsado = "*"
            calcularOperacion()
        }

        buttonDividir.setOnClickListener {
            operadorPulsado = "/"
            calcularOperacion()
        }


        buttonPorcent.setOnClickListener {
            calcularOperacionDirecta("%")
        }

        buttonX2.setOnClickListener {
            calcularOperacionDirecta("X2")
        }

        buttonX3.setOnClickListener {
            calcularOperacionDirecta("X3")
        }

        buttonXy.setOnClickListener {
            operadorPulsado = "Xy"
            calcularOperacion()
        }


        buttonIgual.setOnClickListener {
            operadorPulsado = "="
            calcularOperacion()
        }

        buttonC.setOnClickListener {
            reiniciarValores()
        }

        buttonComa.setOnClickListener {
            if (!valorActual.contains(".")) {
                valorActual += "."
            }
        }
    }

    override fun onSaveInstanceState(estado: Bundle?) {

        if (estado != null) {

            estado.putDouble("RESULTADO", resultado)
            estado.putString("VALORACTUAL", valorActual)
            estado.putString("OPERADOR", operador)
            estado.putString("OPERANDO1", operando1)
            estado.putString("OPERANDO2", operando2)
            estado.putBoolean("LIMPIARPANTALLA", limpiarPantalla)
        }

        super.onSaveInstanceState(estado)
    }

    private fun asignarOperandos(valorBoton: String) {

        if (limpiarPantalla) {
            valorActual = ""
            limpiarPantalla = false
        }

        valorActual += valorBoton;

        if (!operador.isEmpty() && !operando1.isEmpty()) {
            operando2 = valorActual
        }
        actualizaTVResultado(valorActual.toDouble())
    }

    private fun calcularOperacion() {

        if (operando1.isEmpty() || operador == "=") {
            operando1 = valorActual
        } else if (operando2.isEmpty()) {
            operando2 = valorActual
        } else {

            when (operador) {
                "+" -> resultado = operando1.toDouble() + operando2.toDouble()
                "-" -> resultado = operando1.toDouble() - operando2.toDouble()
                "*" -> resultado = operando1.toDouble() * operando2.toDouble()
                "/" -> resultado = operando1.toDouble() / operando2.toDouble()
                "Xy" -> resultado = operando1.toDouble().pow(operando2.toDouble())
            }

            actualizaTVResultado(resultado)
            valorActual = resultado.toString()
            operando1 = resultado.toString()
        }
        operador = operadorPulsado
        limpiarPantalla = true
        operando2 = ""
    }

    private fun calcularOperacionDirecta(operador: String) {

        if(valorActual != ""){

            when (operador) {
                "%" ->  resultado = valorActual.toDouble() * 0.01
                "X2" -> resultado = valorActual.toDouble().pow(2)
                "X3" -> resultado = valorActual.toDouble().pow(3)
            }

            actualizaTVResultado(resultado)
            valorActual = resultado.toString()
        }

    }

    private fun actualizaTVResultado(valorActualizar: Double) {

        val valorFormateado = format.format(valorActualizar)

        if (valorFormateado.length > 14) {
            val toast = Toast.makeText(
                applicationContext, "El número $valorFormateado es demasiado grande " +
                        "para mostrarse en la calculadora", Toast.LENGTH_LONG
            );
            toast.show();
        } else {
            textViewResultado.text = valorFormateado
        }
    }

    private fun reiniciarValores() {
        valorActual = ""
        operador = ""
        operando1 = ""
        operando2 = ""
        resultado = 0.0
        actualizaTVResultado(0.0)
    }

    private fun mostrarBotonesExtra(valor: Boolean) {

        if (!valor) {
            buttonPorcent.visibility = View.GONE
            buttonX2.visibility = View.GONE
            buttonX3.visibility = View.GONE
            buttonXy.visibility = View.GONE
        } else {
            buttonPorcent.visibility = View.VISIBLE
            buttonX2.visibility = View.VISIBLE
            buttonX3.visibility = View.VISIBLE
            buttonXy.visibility = View.VISIBLE
        }

    }
}
