package com.example.androidmvvm.core.platform

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.androidmvvm.R
import com.example.androidmvvm.core.error.ApiFailure
import com.example.androidmvvm.core.error.Failure
import com.example.androidmvvm.core.extension.dismissIfAdded
import com.example.androidmvvm.core.extension.isAvailable
import com.example.androidmvvm.core.extension.showIfNotExist
import com.example.androidmvvm.core.livedata.autoCleared
import com.example.androidmvvm.feature.dialog.LoadingDialogFragment
import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    abstract val viewModel: VM?

    abstract fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    open fun onBackPressed(): Boolean = true

    var viewBinding: VB by autoCleared()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (onBackPressed()) {
                isEnabled = false
                activity?.onBackPressed()
            }
        }
    }

    private var loadingDialogFragment: LoadingDialogFragment? = null
    
    // Cache the class name to avoid repeated reflection calls
    private val logTag by lazy { "${this::class.simpleName}" }

    override fun onAttach(context: Context) {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onCreate")
        super.onCreate(savedInstanceState)
        addBackPressedCallback()
        observeBaseViewModel()
    }

    private fun observeBaseViewModel() {
        val baseViewModel = viewModel as? BaseViewModel ?: return
        baseViewModel.failure.observe(this) { failure ->
            onError(failure)
        }

        baseViewModel.isLoading.observe(this) { isLoading ->
            onLoading(isLoading)
        }
    }

    private fun addBackPressedCallback() {
        activity?.onBackPressedDispatcher?.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onCreateView")
        viewBinding = onCreateViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onStart")
        super.onStart()

        onBackPressedCallback.isEnabled = true
    }

    override fun onResume() {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onResume")
        super.onResume()
    }

    override fun onPause() {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onPause")
        super.onPause()
    }

    override fun onStop() {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Timber.tag(LIFECYCLE_TAG).i("$logTag onDetach")
        super.onDetach()
    }

    open fun onError(failure: Failure) {
        val message = when (failure) {
            is ApiFailure.Connection -> {
                getString(R.string.msg_no_internet_error)
            }
            is ApiFailure.Server -> {
                failure.errorMessage.ifEmpty {
                    getString(R.string.msg_unexpected_error)
                }
            }
            else -> {
                getString(R.string.msg_unknown_error)
            }
        }
        ErrorDialogFragment.newInstance(message).showIfNotExist(
            fragmentManager = childFragmentManager,
            tag = ErrorDialogFragment.TAG,
        )
    }

    open fun onLoading(isLoading: Boolean) {
        if (isLoading && activity.isAvailable()) {
            if (loadingDialogFragment == null) {
                loadingDialogFragment = LoadingDialogFragment.newInstance()
            }
            loadingDialogFragment?.showIfNotExist(childFragmentManager, LoadingDialogFragment.TAG)
        } else {
            loadingDialogFragment?.dismissIfAdded()
            loadingDialogFragment = null
        }
    }

    companion object {
        private const val LIFECYCLE_TAG = "FragmentLifecycle"
    }
}