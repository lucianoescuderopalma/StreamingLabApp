package cl.duocuc.streaminglab_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

/**
 * ViewModel encargado de manejar la lógica de pagos dentro de la app.
 *
 * Se comunica con la UI mediante [paymentState] para notificar sobre
 * la progresión del pago: cargando, éxito o error.
 */
class PaymentViewModel : ViewModel() {

    /** LiveData que expone el estado actual del pago. */
    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> = _paymentState

    /** Scope de corrutinas ligado al ViewModel para operaciones asíncronas. */
    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    /**
     * Registra un pago simulado.
     *
     * @param name Nombre del plan o producto.
     * @param price Precio del plan o producto.
     * @param method Método de pago seleccionado.
     * @param cardNumber Número de tarjeta utilizado para la simulación.
     *
     * Simula un retraso de 2 segundos para imitar un proceso real de pago.
     * Si el número de tarjeta empieza con '4', se considera un pago exitoso.
     * De lo contrario, se emite un error.
     */
    fun registerPayment(name: String, price: String, method: String, cardNumber: String) {
        _paymentState.value = PaymentState.Loading

        viewModelScope.launch {
            delay(2000) // Simula llamada a API o DB

            if (cardNumber.startsWith("4")) {
                _paymentState.value = PaymentState.Success
            } else {
                _paymentState.value = PaymentState.Error("Pago fallido. Verifica los datos")
            }
        }
    }

    /** Cancela todas las corrutinas cuando el ViewModel es destruido. */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
