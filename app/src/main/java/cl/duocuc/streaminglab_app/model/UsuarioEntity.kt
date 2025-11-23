package cl.duocuc.streaminglab_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un usuario en la base de datos local.
 *
 * Esta clase se utiliza con Room para persistir los usuarios en la tabla "usuarios".
 *
 * @property id Identificador único del usuario. Se genera automáticamente.
 * @property nombre Nombre completo del usuario.
 * @property email Correo electrónico del usuario.
 * @property password Contraseña del usuario.
 */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String
)
