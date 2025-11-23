package cl.duocuc.streaminglab_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.duocuc.streaminglab_app.ui.HomeFragment
import cl.duocuc.streaminglab_app.ui.theme.RegisterFragment
import cl.duocuc.streaminglab_app.ui.theme.PlansFragment
import cl.duocuc.streaminglab_app.ui.theme.PaymentFragment
import cl.duocuc.streaminglab_app.ui.theme.PlanDetailFragment
import cl.duocuc.streaminglab_app.model.Plan
import cl.duocuc.streaminglab_app.ui.NoticiasFragment

/**
 * MainActivity: Actividad principal de la aplicación StreamingLab.
 *
 * Esta actividad actúa como contenedor de todos los fragmentos de la app y
 * maneja la navegación entre ellos. Se encarga de reemplazar fragmentos,
 * pasar datos mediante Bundle, y aplicar animaciones de transición.
 *
 * Fragmentos manejados:
 *  - HomeFragment: Pantalla de bienvenida y navegación inicial.
 *  - RegisterFragment: Pantalla de registro de nuevos usuarios.
 *  - PlansFragment: Lista de planes disponibles.
 *  - PlanDetailFragment: Detalle de un plan seleccionado.
 *  - PaymentFragment: Pantalla de pago de un plan.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Método llamado al crear la actividad.
     * Inicializa la UI y muestra la pantalla principal (HomeFragment).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mostrar la pantalla principal al iniciar
        showHomeScreen()
    }

    /**
     * Muestra el HomeFragment (pantalla principal).
     * No se agrega al back stack para que sea la raíz de la navegación.
     */
    fun showHomeScreen() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_left,  // Animación entrada
                R.anim.slide_out_right  // Animación salida
            )
            .replace(R.id.mainContainer, HomeFragment())
            .commit()
    }

    /**
     * Muestra el RegisterFragment (pantalla de registro).
     * Se agrega al back stack para permitir volver al HomeFragment.
     */
    fun showRegister() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,  // Entrada desde derecha
                R.anim.slide_out_left,  // Salida hacia izquierda
                R.anim.slide_in_left,   // Entrada al volver
                R.anim.slide_out_right  // Salida al volver
            )
            .replace(R.id.mainContainer, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }

    /**
     * Muestra el PlansFragment (lista de planes disponibles).
     * Se agrega al back stack para permitir regresar al HomeFragment.
     */
    fun showPlans() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.mainContainer, PlansFragment())
            .addToBackStack(null)
            .commit()
    }

    /**
     * Muestra el PaymentFragment para realizar el pago de un plan.
     *
     * @param planName Nombre del plan seleccionado.
     * @param planPrice Precio del plan seleccionado.
     */
    fun showPayment(planName: String, planPrice: String) {
        val paymentFragment = PaymentFragment().apply {
            arguments = Bundle().apply {
                putString("plan_name", planName)   // Clave para el nombre del plan
                putString("plan_price", planPrice) // Clave para el precio del plan
            }
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.mainContainer, paymentFragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Muestra el PlanDetailFragment para ver los detalles de un plan.
     *
     * @param plan Plan seleccionado cuyo detalle se mostrará.
     */
    fun showPlanDetail(plan: Plan) {
        val fragment = PlanDetailFragment.newInstance(plan)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.mainContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
    // -------------------------
    // NUEVO: Función para mostrar noticias
    // -------------------------
    fun showNoticias() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.mainContainer, NoticiasFragment())
            .addToBackStack(null)
            .commit()
    }
}
