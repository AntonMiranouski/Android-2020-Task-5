package anton.miranouski.cats.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import anton.miranouski.cats.databinding.FragmentCatListBinding
import kotlinx.coroutines.flow.collectLatest

class CatListFragment : Fragment() {

    private lateinit var binding: FragmentCatListBinding
    private lateinit var catListViewModel: CatListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatListBinding.inflate(layoutInflater, container, false)

        val adapter = CatListAdapter(this)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        catListViewModel = ViewModelProvider(this).get(CatListViewModel::class.java)

        lifecycleScope.launchWhenCreated {
            catListViewModel.getCats().collectLatest {
                adapter.submitData(it)
            }
        }

        return binding.root
    }
}