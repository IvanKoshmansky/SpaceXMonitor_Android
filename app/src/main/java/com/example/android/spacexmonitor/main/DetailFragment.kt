package com.example.android.spacexmonitor.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.spacexmonitor.R
import com.example.android.spacexmonitor.databinding.FragmentDetailBinding
import javax.inject.Inject

class DetailFragment : Fragment() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private var crewListAdapter: CrewListAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail,
            container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mainViewModel

        crewListAdapter = CrewListAdapter()
        binding.detailCrewList.adapter = crewListAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            // первый запуск фрагмента (не поворот экрана)
            val arguments = DetailFragmentArgs.fromBundle(requireArguments())
            mainViewModel.setupOneLaunchDetails(arguments.launchId)
        }
        // CrewMemvers приходит корректный (от 08.04.2022)
        mainViewModel.detailLiveData.observe(viewLifecycleOwner) { oneLaunchModel ->
            crewListAdapter?.submitList(oneLaunchModel.crewMembers)
        }
    }
}
