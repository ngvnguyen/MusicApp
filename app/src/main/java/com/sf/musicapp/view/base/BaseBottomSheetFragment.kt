package com.sf.musicapp.view.base

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sf.musicapp.R


// class base cho bottom sheet
abstract class BaseBottomSheetFragment<T: ViewBinding>: BottomSheetDialogFragment() {
    private var _binding:T?=null

    protected val binding
        get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        _binding?.root?.setBackgroundColor(requireContext().getColor(R.color.app_background))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        initData()
        addEvent()
        addObservers()
    }

    // mở rộng bottomSheet full màn hình và bỏ khả năng đóng 1 phần
    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ActionBar.LayoutParams.MATCH_PARENT
            bottomSheet.background = null
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN){
                        dismiss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

            })
        }
    }

    abstract fun getViewBinding():T
    open fun initViewModel() {}

    open fun initView() {}

    open fun addEvent() {}

    open fun addObservers() {}

    open fun initData() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded.not()) {
            super.show(manager, tag)
        }
    }
}