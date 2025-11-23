package cl.duocuc.streaminglab_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cl.duocuc.streaminglab_app.R
import cl.duocuc.streaminglab_app.model.Plan
import com.bumptech.glide.Glide

/**
 * Adapter para mostrar una lista de planes de streaming en un RecyclerView.
 *
 * Este adapter maneja la visualización de cada plan con su información:
 * nombre, descripción, precio, resolución, dispositivos, perfiles, descargas offline y imagen.
 *
 * @property planes Lista de planes a mostrar.
 * @property onPlanSelect Función que se ejecuta cuando el usuario selecciona un plan.
 */
class PlansAdapter(
    private var planes: List<Plan>,
    private val onPlanSelect: (Plan) -> Unit
) : RecyclerView.Adapter<PlansAdapter.PlanViewHolder>() {

    /**
     * ViewHolder que contiene todos los elementos de la UI de un item de plan.
     *
     * @param view La vista inflada del item_plan.xml
     */
    inner class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvPlanName)
        val descripcion: TextView = view.findViewById(R.id.tvPlanDescription)
        val precio: TextView = view.findViewById(R.id.tvPlanPrice)
        val imagen: ImageView = view.findViewById(R.id.imgPlan)
        val resolucion: TextView = view.findViewById(R.id.tvPlanResolution)
        val dispositivos: TextView = view.findViewById(R.id.tvPlanDevices)
        val perfiles: TextView = view.findViewById(R.id.tvPlanProfiles)
        val descargasOffline: TextView = view.findViewById(R.id.tvPlanDownloads)
        val btnSeleccionar: Button = view.findViewById(R.id.btnSelectPlan)
    }

    /**
     * Inflar la vista del item y crear un ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(view)
    }

    /**
     * Vincula los datos de un Plan con el ViewHolder correspondiente.
     *
     * @param holder ViewHolder donde se mostrará la información.
     * @param position Posición del item en la lista.
     */
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = planes[position]

        holder.nombre.text = plan.nombre
        holder.descripcion.text = plan.descripcion
        holder.precio.text = plan.precio
        holder.resolucion.text = "Resolución: ${plan.resolucion}"
        holder.dispositivos.text = "Dispositivos: ${plan.dispositivos}"
        holder.perfiles.text = "Perfiles: ${plan.perfiles}"
        holder.descargasOffline.text = if (plan.descargasOffline) "Descargas offline: Sí" else "Descargas offline: No"

        // Carga de imagen usando Glide
        Glide.with(holder.imagen.context)
            .load(plan.imageResId)
            .into(holder.imagen)

        holder.btnSeleccionar.setOnClickListener { onPlanSelect(plan) }

        // Muestra un toast con información del plan al hacer long click
        holder.itemView.setOnLongClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Plan: ${plan.nombre}\nPrecio: ${plan.precio}",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }

    /**
     * Devuelve la cantidad de items en la lista de planes.
     */
    override fun getItemCount(): Int = planes.size

    /**
     * Actualiza toda la lista de planes y refresca el RecyclerView.
     *
     * @param newList Nueva lista de planes.
     */
    fun submitList(newList: List<Plan>) {
        planes = newList
        notifyDataSetChanged()
    }

    /**
     * Actualiza un solo item en una posición específica.
     *
     * @param position Posición del item a actualizar.
     * @param plan Nuevo objeto Plan que reemplaza al anterior.
     */
    fun updateItem(position: Int, plan: Plan) {
        if (position in planes.indices) {
            planes = planes.toMutableList().apply { set(position, plan) }
            notifyItemChanged(position)
        }
    }

    /**
     * Filtra los planes por nombre que contengan el texto de búsqueda.
     *
     * @param query Texto a buscar dentro del nombre del plan.
     */
    fun filterByName(query: String) {
        val filtered = planes.filter { it.nombre.contains(query, ignoreCase = true) }
        submitList(filtered)
    }

    /**
     * Filtra los planes según un rango de precios.
     *
     * Se asume que el precio está en formato "$4.990" y se extraen los dígitos.
     *
     * @param min Precio mínimo.
     * @param max Precio máximo.
     */
    fun filterByPriceRange(min: Int, max: Int) {
        val filtered = planes.filter {
            val priceInt = it.precio.replace("[^\\d]".toRegex(), "").toIntOrNull() ?: 0
            priceInt in min..max
        }
        submitList(filtered)
    }
}
