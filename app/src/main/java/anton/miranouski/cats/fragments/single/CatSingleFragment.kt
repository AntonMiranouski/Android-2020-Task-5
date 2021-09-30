package anton.miranouski.cats.fragments.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import anton.miranouski.cats.R
import anton.miranouski.cats.databinding.FragmentCatSingleBinding
import coil.load

class CatSingleFragment : Fragment() {

    private lateinit var binding: FragmentCatSingleBinding
    private val args by navArgs<CatSingleFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatSingleBinding.inflate(layoutInflater, container, false)

        binding.ivCatFull.load(args.imageUrl) {
            error(R.drawable.ic_baseline_error_outline)
        }

        return binding.root
    }
}