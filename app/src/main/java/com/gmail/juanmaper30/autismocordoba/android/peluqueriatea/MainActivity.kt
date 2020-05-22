package com.gmail.juanmaper30.autismocordoba.android.peluqueriatea

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), PantallaPrincipalFragment.Callbacks,
    VamosPeluqueriaPaso1Fragment.Callbacks, VamosPeluqueriaPaso2Fragment.Callbacks,
    VamosPeluqueriaPaso3Fragment.Callbacks, VamosPeluqueriaPaso4Fragment.Callbacks,
    VamosPeluqueriaPaso5Fragment.Callbacks, VamosPeluqueriaPaso6Fragment.Callbacks,
    VamosPeluqueriaPaso7Fragment.Callbacks, VamosPeluqueriaPaso8Fragment.Callbacks,
    VamosPeluqueriaPaso9Fragment.Callbacks{


    /* Me creo el viewmodel que guarda informacion sobre el indice del paso a mostrar en
        Vamos a la peluqueria */
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aqui bloqueo la actividad para que solo se muestre en modo landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
          window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
          )

        setContentView(R.layout.activity_main)

        val fragmentoActual = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragmentoActual == null) {
            val fragmento = PantallaPrincipalFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragmento)
                .commit()
        }
        Log.i(TAG, "Actividad creada")
    }

    /* Sobrescribo la funcion de PantallaPrincipalFragment que uso como interfaz para saber que se
        ha pulsado el boton del modulo de consejos. Al ser llamada, monto el modulo Consejos */
    override fun moduloConsejosSeleccionado() {
        Log.i(TAG, "Montando modulo consejos")
        val fragmentoConsejos = ConsejosFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragmentoConsejos)
            .addToBackStack(null)
            .commit()
    }

    /* Sobrescribo la funcion de PantallaPrincipalFragment que uso como interfaz para saber que se
        ha pulsado el boton del modulo de vamos a la peluqueria. Al ser llamada, monto
        el modulo Vamos a la peluqueria */
    override fun moduloVamosPeluqueriaSeleccionado() {
        Log.i(TAG, "Montando modulo vamos a la peluqueria")
        mainActivityViewModel.reiniciar()
        val fragmentoVamosPeluqueria = VamosPeluqueriaPaso1Fragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragmentoVamosPeluqueria)
            .addToBackStack(null)
            .commit()
    }

    /* Sobrescribo la funcion de VamosPeluqueriaPaso1Fragment que uso como interfaz para saber
        que se ha pulsado el boton siguiente. Al ser llamada, monto el siguiente fragmento
        de la secuencia de Vamos a la peluqueria */
    override fun vamosPeluqueriaMontarSiguienteFragmento() {
        // Si estoy en el ultimo consejo, desmonto los fragmentos y vuelvo a la pantalla principal
        if (mainActivityViewModel.estoyEnUltimoConsejo) {
            Log.d(TAG, "Terminado vamosPeluqueria fragmento. Saliendo")
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        // Si no, monto el fragmento correspondiente segun el indice del viewmodel
        else {
            mainActivityViewModel.incrementarIndice()

            Log.i(TAG, "Montando vamosPeluqueria fragmento ${mainActivityViewModel.indicePasoActual}")

            val fragmentoVamosPeluqueria = when(mainActivityViewModel.indicePasoActual) {
                1 -> VamosPeluqueriaPaso1Fragment()
                2 -> VamosPeluqueriaPaso2Fragment()
                3 -> VamosPeluqueriaPaso3Fragment()
                4 -> VamosPeluqueriaPaso4Fragment()
                5 -> VamosPeluqueriaPaso5Fragment()
                6 -> VamosPeluqueriaPaso6Fragment()
                7 -> VamosPeluqueriaPaso7Fragment()
                8 -> VamosPeluqueriaPaso8Fragment()
                9 -> VamosPeluqueriaPaso9Fragment()
                else -> VamosPeluqueriaPaso1Fragment()
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentoVamosPeluqueria)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun vamosPeluqueriaDecrementarIndiceCallback() {
        mainActivityViewModel.decrementarIndice()
        Log.i(TAG, "Indice decrementado a:${mainActivityViewModel.indiceInternoLista}")
    }

}
