package com.example.jokesplash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.jokesplash.adapter.JokeAdapter
import com.example.jokesplash.databinding.RecyclerviewFragmentBinding
import com.example.jokesplash.model.JokesClass
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class RecyclerView_Fragment: Fragment() {

    private lateinit var binding: RecyclerviewFragmentBinding

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = RecyclerviewFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val party = Party(
            speed = 0f,
            maxSpeed = 50f,
            damping = 0.9f,
            spread = listOf(1000,1200,2000,3000).random(),
            colors = listOf(0x367C23, 0x012703, 0xFFFFFF, 0x000000, 0x00FF0D, 0x00FF0D),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )

        val jokeAdapter = JokeAdapter(viewModel)

        binding.jokeRecycler.adapter = jokeAdapter

        val cardViewClickListener = object : JokeAdapter.CardViewClickListener {
            override fun onCardViewClick(cardView: CardView, party: Party) {
                binding.konfettiViewRecycler.start(party)
            }
        }

        jokeAdapter.setCardViewClickListener(cardViewClickListener)

        val selectedLimit = RecyclerView_FragmentArgs.fromBundle(requireArguments()).selectedItem

        val animations = AnimationUtils.loadAnimation(requireContext(),R.anim.animation)
        val animation2 = AnimationUtils.loadAnimation(requireContext(),R.anim.animation2)

        binding.jokesRecyclerCardView.setOnClickListener {
            binding.konfettiViewRecycler.start(party)
            binding.jokesRecyclerCardView.startAnimation(animations)
            lifecycleScope.launch(Main) {
                delay(500)
                viewModel.getJokes(selectedLimit)
                binding.jokeRecycler.visibility = View.VISIBLE
                binding.jokesRecyclerCardView.visibility = View.INVISIBLE
                jokeAdapter.selectedLimit(selectedLimit)
                Log.e("LIMIT","$selectedLimit")
            }

        }

        binding.backButton.setOnClickListener {
            binding.backButton.startAnimation(animation2)
            lifecycleScope.launch(Main) {
                delay(1000)
                findNavController().navigate(RecyclerView_FragmentDirections.actionRecyclerViewFragmentToHomeFragment())
            }
        }

        viewModel.jokes.observe(viewLifecycleOwner, Observer {
            if ( it != null) {
                jokeAdapter.submitlist(it as MutableList<JokesClass>)
            }
        })
    }
}