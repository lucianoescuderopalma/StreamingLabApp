package cl.duocuc.streaminglab_app.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duocuc.streaminglab_app.model.PlanEntity
import cl.duocuc.streaminglab_app.model.UsuarioEntity

/**
 * Base de datos local de la aplicación utilizando Room.
 *
 * Contiene las entidades [PlanEntity] y [UsuarioEntity] y sus correspondientes DAOs.
 *
 * @property planDao Proporciona acceso a las operaciones sobre la tabla de planes.
 * @property usuarioDao Proporciona acceso a las operaciones sobre la tabla de usuarios.
 */
@Database(
    entities = [PlanEntity::class, UsuarioEntity::class],
    version = 2, // Incrementa cuando agregues o modifiques entidades
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /** DAO para operaciones sobre la tabla de planes. */
    abstract fun planDao(): PlanDao

    /** DAO para operaciones sobre la tabla de usuarios. */
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia singleton de la base de datos.
         *
         * @param context Contexto de la aplicación.
         * @return Instancia de [AppDatabase].
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Evita crash por cambios en la versión
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
