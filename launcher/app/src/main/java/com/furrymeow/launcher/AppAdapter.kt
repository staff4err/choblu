package com.furrymeow.launcher

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.furrymeow.launcher.databinding.ItemAppBinding

/** Данные об одном приложении. */
data class AppItem(
    val label: String,
    val packageName: String,
    val icon: Drawable,
    val isSystem: Boolean
)

/** Адаптер сетки приложений с «пушистыми» иконками. */
class AppAdapter(
    private val apps: List<AppItem>,
    private val onClick: (AppItem) -> Unit
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    inner class AppViewHolder(private val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppItem) {
            binding.appIcon.setImageDrawable(app.icon)
            binding.appLabel.text = app.label
            binding.root.setOnClickListener { onClick(app) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AppViewHolder(ItemAppBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount(): Int = apps.size
}
