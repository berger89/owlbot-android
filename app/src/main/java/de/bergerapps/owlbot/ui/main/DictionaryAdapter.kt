package de.bergerapps.owlbot.ui.main

import android.content.Context
import android.text.Html
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.bergerapps.owlbot.R
import de.bergerapps.owlbot.service.model.OwlBotResponse
import kotlinx.android.synthetic.main.dictionary_item.view.*

class DictionaryAdapter(
    private var owlBotResponse: OwlBotResponse,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            from(context).inflate(R.layout.dictionary_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return owlBotResponse.definitions.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        initViews(p0 as ViewHolder, owlBotResponse, owlBotResponse.definitions[p1])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val word: TextView = view.word
        val pronunciation: TextView = view.pronunciation
        val definition: TextView = view.definition
        val example: TextView = view.example
        val imageUrl: TextView = view.image_url
        val imageView: ImageView = view.imageView
    }

    private fun initViews(
        viewHolder: ViewHolder,
        owlBotResponse: OwlBotResponse,
        def: OwlBotResponse.OwlBotDefinitionResponse
    ) {
        // word
        viewHolder.word.text = owlBotResponse.word
        // pronunciation
        viewHolder.pronunciation.text = owlBotResponse.pronunciation

        // definition
        viewHolder.definition.text = def.definition
        // example
        if (def.example != null && def.example.isNotEmpty()) {
            viewHolder.example.text = Html.fromHtml(def.example)
        } else {
            viewHolder.example.text = ""
        }
        // type
        if (def.type != null && def.type.isNotEmpty()) {
            viewHolder.word.text = viewHolder.word.text as String + " (" + def.type + ")"
        }
        // image_url
        viewHolder.imageUrl.text = def.image_url
        if (def.image_url != null && def.image_url.isNotEmpty()) {
            viewHolder.imageView.imageTintList = null
            viewHolder.imageView.colorFilter = null
            Picasso.get().load(def.image_url).into(viewHolder.imageView)
        } else {
            viewHolder.imageView.setColorFilter(
                ContextCompat.getColor(context, R.color.colorSecond),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            Picasso.get().load(R.drawable.owlbot).into(viewHolder.imageView)
        }

    }

    fun update(owlBotResponse: OwlBotResponse) {
        this.owlBotResponse = owlBotResponse
        notifyDataSetChanged()
    }

}