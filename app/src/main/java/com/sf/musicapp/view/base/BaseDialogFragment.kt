package com.sf.musicapp.view.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding


// class base cho dialog
abstract class BaseDialogFragment<T : ViewBinding> : DialogFragment() {
    private var _binding: T? = null
    private lateinit var dialogLoad: Dialog

    protected val binding: T
        get() = checkNotNull(_binding) {
            "Fragment $this binding cannot be accessed before onCreateView() or after onDestroyView()"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.custom_dialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getDataBinding()
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setupLoading()
        initViewModel()
        initView()
        addEvent()
        addObservers()
        initData()
    }

    abstract fun getDataBinding(): T

    open fun initViewModel() {}

    open fun initView() {}

    open fun addEvent() {}

    open fun addObservers() {}

    open fun initData() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupLoading() {
        dialogLoad = Dialog(requireContext())
        dialogLoad.setCancelable(false)
        dialogLoad.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoad.setContentView(ProgressBar(requireContext()))
    }

    fun showLoading() {
        dialogLoad.show() // to show this dialog
    }

    fun hideLoading() {
        dialogLoad.dismiss() // to hide this dialog
    }
}