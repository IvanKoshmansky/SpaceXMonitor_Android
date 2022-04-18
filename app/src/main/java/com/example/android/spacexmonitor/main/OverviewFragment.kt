package com.example.android.spacexmonitor.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.spacexmonitor.R
import com.example.android.spacexmonitor.databinding.FragmentOverviewBinding
import com.example.android.spacexmonitor.main.viewpaging.*
import javax.inject.Inject

class OverviewFragment : Fragment() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private var pagingAdapter: LaunchesPagingDataAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    // перед созданием вью
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentOverviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_overview,
            container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mainViewModel

        pagingAdapter = LaunchesPagingDataAdapter(LaunchesClickListener { launchId ->
            mainViewModel.onItemClicked(launchId)
        })

        // перейти по навигации: LiveData содержит Id миссии,
        // при изменениях LiveData отрабатывает обсервер и запускает переход по навигации
        mainViewModel.navigateToLaunchDetail.observe(viewLifecycleOwner) { launchId ->
            launchId?.let {
                mainViewModel.localRepository.invalidateOneLaunchDetailCache()
                findNavController().navigate(
                    OverviewFragmentDirections.actionMainFragmentToDetailFragment(launchId)
                )
                mainViewModel.toLaunchDetailNavigated()
            }
        }

        binding.overviewList.adapter = pagingAdapter

        setHasOptionsMenu(true)

        return binding.root
    }

    // сразу после создания вью
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // настройка обозревания LiveData
        // Activities can use getLifecycle() directly, but Fragments should instead use
        // getViewLifecycleOwner().getLifecycle().
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.overviewLiveData.observe(viewLifecycleOwner) { pagingData ->
            pagingAdapter?.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        mainViewModel.needToRefreshAdapter.observe(viewLifecycleOwner) { trigger ->
            if (trigger) {
                mainViewModel.needToRefreshAdapterReset()
                pagingAdapter?.refresh()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainViewModel.clearOverviewCache()
        return super.onOptionsItemSelected(item)
    }
}
