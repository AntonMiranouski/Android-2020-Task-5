package anton.miranouski.cats.fragments.single

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import anton.miranouski.cats.R
import anton.miranouski.cats.databinding.FragmentCatSingleBinding
import anton.miranouski.cats.util.sdk29AndUp
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.IOException

class CatSingleFragment : Fragment() {

    private lateinit var binding: FragmentCatSingleBinding
    private val args by navArgs<CatSingleFragmentArgs>()
    private lateinit var bitmap: Bitmap
    private var writePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatSingleBinding.inflate(layoutInflater, container, false)

        lifecycleScope.launchWhenCreated {
            bitmap = getBitmap()
            binding.ivCatFull.setImageBitmap(bitmap)
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }

        binding.ibSave.setOnClickListener {
            updateOrRequestPermission()

            if (writePermissionGranted) {
                savePhotoToExternalStorage("${args.imageUrl}", bitmap)
                Toast.makeText(requireContext(), "Image saved in pictures", Toast.LENGTH_LONG)
                    .show()
            } else {
                updateOrRequestPermission()
            }
        }

        return binding.root
    }

    private suspend fun getBitmap(): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(args.imageUrl)
            .error(R.drawable.ic_baseline_error_outline)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }
        return try {
            val contentResolver = activity?.contentResolver
            contentResolver?.insert(imageCollection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, outputStream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            true
        } catch (e: IOException) {
            Log.e(TAG, e.stackTraceToString())
            false
        }
    }

    private fun updateOrRequestPermission() {
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        writePermissionGranted = hasWritePermission || minSdk29

        if (!writePermissionGranted) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    companion object {

        private const val TAG = "CatSingleFragment"
        private const val COMPRESS_QUALITY = 100
    }
}
