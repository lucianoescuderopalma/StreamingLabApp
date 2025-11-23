package cl.duocuc.streaminglab_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cl.duocuc.streaminglab_app.model.Usuario
import cl.duocuc.streaminglab_app.model.UsuarioEntity
import cl.duocuc.streaminglab_app.repository.UserRoomRepository
import cl.duocuc.streaminglab_app.repository.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel encargado de la gestión de usuarios en la app.
 *
 * Permite registrar, actualizar, eliminar y consultar usuarios,
 * así como mantener el usuario actualmente logueado y la lista completa de usuarios.
 *
 * @param application Contexto de la aplicación para acceso a la DB.
 */
class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    // DAO y repositorio para operaciones con Room
    private val dao = AppDatabase.getDatabase(application).usuarioDao()
    private val repository = UserRoomRepository(dao)

    /** Usuario actualmente logueado */
    private val _usuarioActual = MutableLiveData<Usuario?>()
    val usuarioActual: LiveData<Usuario?> get() = _usuarioActual

    /** Lista completa de usuarios */
    private val _usuarios = MutableLiveData<List<Usuario>>()
    val usuarios: LiveData<List<Usuario>> get() = _usuarios

    /**
     * Carga todos los usuarios desde la base de datos local y actualiza [_usuarios].
     */
    fun cargarUsuarios() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listaEntities = repository.obtenerUsuarios()
                val lista = listaEntities.map { Usuario(it.nombre, it.email, it.password) }
                _usuarios.postValue(lista)
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error cargando usuarios: ${e.message}")
            }
        }
    }

    /**
     * Verifica si un correo electrónico ya está registrado.
     *
     * @param email Correo a verificar.
     * @return true si existe, false si no.
     */
    suspend fun emailExiste(email: String): Boolean = withContext(Dispatchers.IO) {
        repository.obtenerUsuarioPorEmail(email) != null
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario Usuario a registrar.
     * @param callback Callback indicando éxito o error.
     */
    fun registrar(usuario: Usuario, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entity = UsuarioEntity(
                    nombre = usuario.nombre,
                    email = usuario.email,
                    password = usuario.password
                )
                repository.registrarUsuario(entity)
                _usuarioActual.postValue(usuario)
                callback(true)
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error registrando usuario: ${e.message}")
                callback(false)
            }
        }
    }

    /**
     * Actualiza los datos de un usuario existente por email.
     *
     * @param usuario Usuario con nuevos datos.
     */
    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingEntity = repository.obtenerUsuarioPorEmail(usuario.email)
                if (existingEntity != null) {
                    val entity = UsuarioEntity(
                        id = existingEntity.id,
                        nombre = usuario.nombre,
                        email = usuario.email,
                        password = usuario.password
                    )
                    repository.actualizarUsuario(entity)

                    _usuarioActual.postValue(usuario)

                    val currentList = _usuarios.value?.toMutableList() ?: mutableListOf()
                    val index = currentList.indexOfFirst { it.email == usuario.email }
                    if (index >= 0) {
                        currentList[index] = usuario
                        _usuarios.postValue(currentList)
                    }
                } else {
                    Log.e("UsuarioViewModel", "No se encontró usuario con email: ${usuario.email}")
                }
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error actualizando usuario: ${e.message}")
            }
        }
    }

    /**
     * Elimina un usuario por su correo electrónico.
     *
     * @param email Correo del usuario a eliminar.
     */
    fun eliminarUsuario(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entity = repository.obtenerUsuarioPorEmail(email)
                if (entity != null) {
                    repository.eliminarUsuario(email)
                    val currentList = _usuarios.value?.toMutableList() ?: mutableListOf()
                    currentList.removeAll { it.email == email }
                    _usuarios.postValue(currentList)

                    if (_usuarioActual.value?.email == email) {
                        _usuarioActual.postValue(null)
                    }
                }
            } catch (e: Exception) {
                Log.e("UsuarioViewModel", "Error eliminando usuario: ${e.message}")
            }
        }
    }

    /**
     * Busca un usuario por correo electrónico.
     *
     * @param email Correo a buscar.
     * @return Usuario encontrado o null si no existe.
     */
    fun buscarUsuarioPorEmail(email: String): Usuario? {
        return _usuarios.value?.find { it.email.equals(email, ignoreCase = true) }
    }

    /** Resetea el usuario actualmente logueado */
    fun resetUsuarioActual() {
        _usuarioActual.postValue(null)
    }
}
