package cl.duocuc.streaminglab_app.viewmodel

/**
 * Representa los distintos estados de un proceso de pago.
 *
 * Se utiliza en [PaymentViewModel] para notificar a la UI sobre
 * la progresión del pago.
 */
sealed class PaymentState {

    /** Estado cuando el pago está en proceso. */
    object Loading : PaymentState()

    /** Estado cuando el pago se ha completado correctamente. */
    object Success : PaymentState()

    /**
     * Estado cuando ocurre un error durante el pago.
     *
     * @param message Mensaje descriptivo del error.
     */
    data class Error(val message: String) : PaymentState()
}
