package cl.duocuc.streaminglab_app.ui.theme

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cl.duocuc.streaminglab_app.MainActivity
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.viewmodel.PaymentState
import cl.duocuc.streaminglab_app.viewmodel.PaymentViewModel

/**
 * Fragmento para gestionar el proceso de pago de un plan de streaming.
 *
 * Muestra la información del plan seleccionado, permite elegir un método de pago,
 * ingresar los datos de la tarjeta y ejecutar la transacción mediante [PaymentViewModel].
 */
class PaymentFragment : Fragment() {

    /** Nombre del plan seleccionado. */
    private var planName: String = "Plan Desconocido"

    /** Precio del plan seleccionado. */
    private var planPrice: String = "N/A"

    /** ViewModel para manejar la lógica de pago y su estado. */
    private val paymentVM: PaymentViewModel by viewModels()

    companion object {
        private const val ARG_PLAN_NAME = "plan_name"
        private const val ARG_PLAN_PRICE = "plan_price"

        /**
         * Crea una nueva instancia de [PaymentFragment] con los argumentos del plan.
         *
         * @param planName Nombre del plan seleccionado.
         * @param planPrice Precio del plan seleccionado.
         * @return Nueva instancia de [PaymentFragment].
         */
        fun newInstance(planName: String, planPrice: String): PaymentFragment {
            val fragment = PaymentFragment()
            val args = Bundle()
            args.putString(ARG_PLAN_NAME, planName)
            args.putString(ARG_PLAN_PRICE, planPrice)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            planName = it.getString(ARG_PLAN_NAME, planName)
            planPrice = it.getString(ARG_PLAN_PRICE, planPrice)
        }
    }

    /**
     * Infla la vista del fragmento, configura los elementos de UI,
     * listeners, validaciones y observadores del [PaymentViewModel].
     *
     * @param inflater Inflater para inflar la vista.
     * @param container Contenedor padre del fragmento.
     * @param savedInstanceState Estado previamente guardado.
     * @return Vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        // Views XML
        val tvPlanName = view.findViewById<TextView>(R.id.tvPlanNamePayment)
        val tvPlanPrice = view.findViewById<TextView>(R.id.tvPlanPricePayment)
        val spinnerMethod = view.findViewById<Spinner>(R.id.spinnerPaymentMethod)
        val etCard = view.findViewById<EditText>(R.id.etCardNumber)
        val etCvv = view.findViewById<EditText>(R.id.etCardCVV)
        val btnPay = view.findViewById<Button>(R.id.btnPay)
        val btnBack = view.findViewById<Button>(R.id.btnBackPayment)
        val progress = view.findViewById<ProgressBar>(R.id.progressBarPayment)

        // Mostrar info del plan
        tvPlanName.text = "Plan seleccionado: $planName"
        tvPlanPrice.text = "Precio: $planPrice"

        // Configurar spinner de métodos de pago
        val paymentOptions = arrayOf("Tarjeta de crédito", "Tarjeta de débito")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMethod.adapter = adapter

        // Función de validación visual para EditText
        fun EditText.addValidation() {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        this@addValidation.setBackgroundResource(R.drawable.edittext_error_bg)
                    } else {
                        this@addValidation.setBackgroundResource(R.drawable.edittext_bg)
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        etCard.addValidation()
        etCvv.addValidation()

        // Observador de estados de pago
        paymentVM.paymentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                PaymentState.Loading -> {
                    progress.visibility = View.VISIBLE
                    btnPay.isEnabled = false
                }

                is PaymentState.Success -> {
                    progress.visibility = View.GONE
                    btnPay.isEnabled = true
                    Toast.makeText(requireContext(), "Pago realizado correctamente", Toast.LENGTH_LONG).show()
                    (activity as? MainActivity)?.showHomeScreen()
                }

                is PaymentState.Error -> {
                    progress.visibility = View.GONE
                    btnPay.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        // Acción del botón "Pagar"
        btnPay.setOnClickListener {
            val method = spinnerMethod.selectedItem.toString()
            val card = etCard.text.toString().trim()
            val cvv = etCvv.text.toString().trim()

            var hasError = false
            if (card.length != 16) {
                etCard.setBackgroundResource(R.drawable.edittext_error_bg)
                hasError = true
            }
            if (cvv.length != 3) {
                etCvv.setBackgroundResource(R.drawable.edittext_error_bg)
                hasError = true
            }

            if (hasError) {
                Toast.makeText(requireContext(), "Por favor revisa los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Llama a ViewModel → Repository → API/DB
                paymentVM.registerPayment(
                    name = planName,
                    price = planPrice,
                    method = method,
                    cardNumber = card
                )
            }
        }

        // Acción del botón "Volver"
        btnBack.setOnClickListener {
            (activity as? MainActivity)?.showPlans()
        }

        return view
    }
}
