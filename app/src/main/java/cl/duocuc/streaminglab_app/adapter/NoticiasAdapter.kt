package cl.duocuc.streaminglab_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.model.Noticia
import com.bumptech.glide.Glide

/**
 * Adapter para mostrar una lista de noticias en un RecyclerView.
 *
 * @property noticias Lista de objetos [Noticia] que se mostrarán.
 */
class NoticiasAdapter(private var noticias: List<Noticia>) :
    RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder>() {

    /**
     * ViewHolder que representa cada item de noticia en el RecyclerView.
     *
     * @property imgNoticia ImageView que muestra la imagen de la noticia.
     * @property tvTitulo TextView que muestra el título de la noticia.
     * @property tvDescripcion TextView que muestra la descripción de la noticia.
     */
    inner class NoticiaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgNoticia: ImageView = view.findViewById(R.id.imgNoticia)
        val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
    }

    /**
     * Infla el layout de cada item de noticia y devuelve un ViewHolder.
     *
     * @param parent El ViewGroup padre donde se inflará la vista.
     * @param viewType Tipo de vista (no se usa en este caso).
     * @return Un [NoticiaViewHolder] con la vista inflada.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noticia, parent, false)
        return NoticiaViewHolder(view)
    }

    /**
     * Asocia los datos de una noticia al ViewHolder correspondiente.
     *
     * @param holder ViewHolder que contendrá los datos.
     * @param position Posición del item en la lista.
     */
    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        val noticia = noticias[position]

        holder.tvTitulo.text = noticia.title

        val descripcion = noticia.description ?: ""
        holder.tvDescripcion.text =
            if (descripcion.length > 500) {
                descripcion.substring(0, 500) + "..."
            } else {
                descripcion
            }

        Glide.with(holder.imgNoticia.context)
            .load(noticia.urlToImage)
            .placeholder(R.drawable.carga)
            .error(R.drawable.fondo)
            .into(holder.imgNoticia)
    }


    /**
     * Devuelve la cantidad de noticias en la lista.
     *
     * @return Número de elementos en [noticias].
     */
    override fun getItemCount(): Int = noticias.size

    /**
     * Actualiza la lista de noticias y notifica al RecyclerView para refrescar la UI.
     *
     * @param nuevasNoticias Nueva lista de objetos [Noticia].
     */
    fun setNoticias(nuevasNoticias: List<Noticia>) {
        noticias = nuevasNoticias
        notifyDataSetChanged()
    }
}
