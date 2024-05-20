package com.example.jokesplash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.jokesplash.adapter.JokeAdapter
import com.example.jokesplash.databinding.HomeFragmentBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment: Fragment() {

    private lateinit var binding : HomeFragmentBinding

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //TODO Spinner maximale Anzahl an 5 Elementen festsetzen, den rest Scrollbar.

        val options = listOf(1,2,3,4,5,6,7,8,9,10)

        val limitSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)

        limitSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerHome.adapter = limitSpinner

        val animations = AnimationUtils.loadAnimation(requireContext(),R.anim.animation2)

        binding.spinnerHome.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedItem = parent?.getItemIdAtPosition(position).toString()
                val selectedLimit = selectedItem.toIntOrNull()?.plus(1) // +1 Anpassung des Ausgewählten Elements

                binding.go4Jokes.setOnClickListener {
                    binding.go4Jokes.startAnimation(animations)

                    lifecycleScope.launch(Main) {
                        delay(1000)
                        if (selectedLimit != null) {
                            if(selectedLimit > 1) {
                                val actionRecycler = HomeFragmentDirections.actionHomeFragmentToRecyclerViewFragment(selectedLimit)
                                findNavController().navigate(actionRecycler)
                                Log.e("LIMITHOME","$selectedLimit")

                            } else {
                                val actionDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedLimit)
                                findNavController().navigate(actionDetail)
                            }
                        } else {
                            Toast.makeText(requireContext(), "Das Limit ist nicht gesetzt", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(),"Please select Limit",Toast.LENGTH_LONG).show()
            }
        }

        val drawableElements = arrayOf(
            R.drawable.funny1,
            R.drawable.funny2,
            R.drawable.funny4,
            R.drawable.funny5,
            R.drawable.funny6,
            R.drawable.funny8,
            R.drawable.funny9,
            R.drawable.funny10,
            R.drawable.funny11
        )

        Glide
            .with(requireContext())
            .load(drawableElements.random())
            .centerCrop()
            .placeholder(drawableElements.random())
            .into(binding.glideCoverHome)
    }

}