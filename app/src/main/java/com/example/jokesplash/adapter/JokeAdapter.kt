package com.example.jokesplash.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesplash.*
import com.example.jokesplash.model.JokesClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class JokeAdapter(private val viewModel: MainViewModel, private var cardViewClickListener: CardViewClickListener? = null):  RecyclerView.Adapter<JokeAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<JokesClass>()

    private var selectedLimit = 0

    interface CardViewClickListener {
        fun onCardViewClick(cardView: CardView, party: Party)
    }

    fun submitlist(jokesList: MutableList<JokesClass>) {
        dataset = jokesList
        notifyDataSetChanged()
    }

    fun selectedLimit(limit : Int) {
        selectedLimit = limit
        notifyDataSetChanged()
    }

    fun setCardViewClickListener(listener: CardViewClickListener) {
        cardViewClickListener = listener
    }

    class ItemViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val jokesText = view.findViewById<TextView>(R.id.item_text)
        val cardView = view.findViewById<CardView>(R.id.jokes_CardView_Recycler)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemlayout = LayoutInflater.from(parent.context).inflate(R.layout.item_jokes,parent,false)
        return ItemViewHolder(itemlayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            val party = Party(
                speed = 0f,
                maxSpeed = 50f,
                damping = 0.9f,
                spread = listOf(1000,1200,2000,3000).random(),
                colors = listOf(0x367C23, 0x012703, 0xFFFFFF, 0x000000, 0x00FF0D, 0x00FF0D),
                position = Position.Relative(0.5, 0.3),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
            )

        val data : JokesClass = dataset[position]
        holder.jokesText.text = data.joke

        holder.cardView.setOnClickListener {

            cardViewClickListener?.onCardViewClick(holder.cardView,party) // Hier wird das Konfetti durch die CardView Klickbar gemacht.

            val animations = AnimationUtils.loadAnimation(holder.itemView.context,R.anim.animation)
            holder.cardView?.startAnimation(animations)
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // Hier starten wir die Verzögerung für die Animation von der CardView
                viewModel.getJokes(selectedLimit)
            }
            Log.e("LIMITADAPTER","$selectedLimit")
        }
    }

    override fun getItemCount(): Int {
       return dataset.size
    }
}


