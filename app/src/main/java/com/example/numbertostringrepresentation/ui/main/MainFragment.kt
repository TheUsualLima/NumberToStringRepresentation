package com.example.numbertostringrepresentation.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.numbertostringrepresentation.R
import com.example.numbertostringrepresentation.databinding.FragmentMainBinding
import com.example.numbertostringrepresentation.domain.numtotextparser.NumToTextRepParserFactory
import kotlinx.coroutines.launch
import java.util.*

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(NumToTextRepParserFactory.createParser(Locale.getDefault()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
//        binding.mainInputField.setText(viewModel.uiState.value.inputText)
        binding.mainPrimaryCta.setOnClickListener {
            viewModel.convertInput()
        }
        binding.mainInputField.doOnTextChanged { text, _, _, _ ->
            viewModel.updateInput(text.toString())
        }
        binding.mainOutputLabel.setOnLongClickListener {
            val clipManager =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipManager.setPrimaryClip(ClipData.newPlainText("label", (it as TextView).text))
            Toast.makeText(context, getString(R.string.clipboard_copy), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(this@MainFragment::updateUi)
            }
        }
    }

    private fun updateUi(state: MainState) {
        binding.apply {
            mainLoader.root.visibility = if (state.loading) View.VISIBLE else View.GONE
            mainOutputLabel.text = state.label
        }
    }
}
