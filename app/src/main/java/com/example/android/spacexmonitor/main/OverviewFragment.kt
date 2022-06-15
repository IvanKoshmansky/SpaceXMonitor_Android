package com.example.android.spacexmonitor.main

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.android.spacexmonitor.R
import com.example.android.spacexmonitor.databinding.FragmentOverviewBinding
import com.example.android.spacexmonitor.main.viewpaging.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

// @AndroidEntryPoint - использовать компонент Hilt для этого фрагмента
@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.navigation)
    private var pagingAdapter: LaunchesPagingDataAdapter? = null

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
                mainViewModel.localRepository.invalidateOneLaunchDetail()
                findNavController().navigate(
                    OverviewFragmentDirections.actionMainFragmentToDetailFragment(launchId)
                )
                mainViewModel.toLaunchDetailNavigated()
            }
        }

        binding.overviewList.adapter = pagingAdapter

        // здесь используется стандартная логика скроллинга от LinearLayoutManager
        // переопределять что-либо в LayoutManager не нужно
        mainViewModel.scrollToTop.observe(viewLifecycleOwner) { scroll ->
            if (scroll) {
                mainViewModel.scrollToTopReset()
                binding.overviewList.smoothScrollToPosition(0)
            }
        }

        // анонимный объект-наследник OnScrollListener с одним переопределенным методом
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                // состояние скроллинга изменилось, новое состояние - скроллинг не производится (окончен)
                if (newState == SCROLL_STATE_IDLE) {
                    // чтобы узнать номер первого видимого элемента в списке нужно обращаться к LayoutManager
                    val layoutManager = (binding.overviewList.layoutManager) as LinearLayoutManager
                    val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()
                    // если позиция 10 и больше - то показать FAB, если меньше, то убрать
                    binding.fab.isVisible = firstVisiblePos >= 9
                }
            }
        }
        binding.overviewList.addOnScrollListener(onScrollListener)

        setHasOptionsMenu(true)

        // проверить что при смене конфигурации сохраняется одна и таже ссылка на вью модел
        // кроме того, одна и таже ссылка на вью модел должна быть на все фрагменты (shared)
        // чтобы достичь такого поведения используется функция расширения hiltNavGraphViewModels(R.id.navigation)
        Timber.d("ViewModel HashCode = ${mainViewModel.hashCode()}")

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
