package com.sf.musicapp.view.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

import androidx.viewbinding.ViewBinding
import com.sf.musicapp.R
import com.sf.musicapp.utils.setNavigationBarColor
import com.sf.musicapp.utils.setStatusBarColor

// class base của activity
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    private var _binding: T? = null
    private lateinit var dialog: Dialog

    protected val binding: T
        get() = checkNotNull(_binding) {
            "Activity $this binding cannot be accessed before onCreateView() or after onDestroyView()"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = getDataBinding()
        setContentView(binding.root)
        setupLoading()
        initViewModel()
        initView()
        addEvent()
        addObservers()
        initData()
    }
    // lấy dataBinding
    abstract fun getDataBinding(): T
    // khởi tạo viewModel nếu cần
    open fun initViewModel() {}
    // khởi tạo giao diện
    open fun initView() {
        setNavigationBarColor(R.color.app_background)
        setStatusBarColor(R.color.app_background)
    }
    // thêm các sự kiện
    open fun addEvent() {}
    // thêm theo dõi với liveData, stateFlow
    open fun addObservers() {}
    // khởi tạo dữ liệu
    open fun initData() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupLoading() {
        dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(ProgressBar(this))
    }

    fun showLoading() {
        dialog.show() // to show this dialog
    }

    fun hideLoading() {
        dialog.dismiss() // to hide this dialog
    }
}