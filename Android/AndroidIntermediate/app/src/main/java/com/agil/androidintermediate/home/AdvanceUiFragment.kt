package com.agil.androidintermediate.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agil.androidintermediate.advanced_ui.canvas.CanvasActivity
import com.agil.androidintermediate.advanced_ui.custom_view.CustomViewActivity
import com.agil.androidintermediate.advanced_ui.ticketing.TicketingActivity
import com.agil.androidintermediate.advanced_ui.webview.WebViewActivity
import com.agil.androidintermediate.advanced_ui.widget.WidgetActivity
import com.agil.androidintermediate.databinding.FragmentAdvanceUiBinding

class AdvanceUiFragment : Fragment(), View.OnClickListener {
    private var _binding : FragmentAdvanceUiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvanceUiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnCustomView.setOnClickListener(this@AdvanceUiFragment)
            btnTicketing.setOnClickListener(this@AdvanceUiFragment)
            btnCanvas.setOnClickListener(this@AdvanceUiFragment)
            btnWidget.setOnClickListener(this@AdvanceUiFragment)
            btnWebview.setOnClickListener(this@AdvanceUiFragment)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnCustomView.id -> startActivity(Intent(v.context, CustomViewActivity::class.java))
            binding.btnTicketing.id -> startActivity(Intent(v.context, TicketingActivity::class.java))
            binding.btnCanvas.id -> startActivity(Intent(v.context, CanvasActivity::class.java))
            binding.btnWidget.id -> startActivity(Intent(v.context, WidgetActivity::class.java))
            binding.btnWebview.id -> startActivity(Intent(v.context, WebViewActivity::class.java))
        }
    }
}