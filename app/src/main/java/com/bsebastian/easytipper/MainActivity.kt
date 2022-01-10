package com.bsebastian.easytipper

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bsebastian.easytipper.databinding.ActivityMainBinding

private const val INITIAL_TIP_PERCENTAGE = 15

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.seekBarTip.progress = INITIAL_TIP_PERCENTAGE
        binding.tvTipPercentage.text = "$INITIAL_TIP_PERCENTAGE%"
        binding.tvTipDescription.text = updateTipDescription(INITIAL_TIP_PERCENTAGE)

        binding.seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("ResourceAsColor")
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                // Solves issue of user moving seekbar before base amount is entered
                if (binding.etBaseAmount.text.isEmpty()) {
                    binding.tvTipPercentage.text = "$p1%"
                    binding.tvTipAmount.text = ""
                    binding.tvTotalAmount.text = ""
                    binding.tvTipDescription.text = updateTipDescription(p1)
                    return
                }

                binding.tvTipPercentage.text = "$p1%"

                val tip = calculateTip(binding.etBaseAmount.text.toString().toDouble(),
                    binding.seekBarTip.progress.toDouble())

                val total = calculateTotal(binding.etBaseAmount.text.toString().toDouble(), tip, binding.seekBarSplit.progress)

                binding.tvTipAmount.text = "%.2f".format(tip)
                binding.tvTotalAmount.text = "%.2f".format(total)

                binding.tvTipDescription.text = updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        binding.etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                // Solves issue of base amount deleted by user
                if (binding.etBaseAmount.text.isEmpty()) {
                    binding.tvTipAmount.text = ""
                    binding.tvTotalAmount.text = ""
                    return
                }

                val tip = calculateTip(binding.etBaseAmount.text.toString().toDouble(),
                    binding.seekBarTip.progress.toDouble())

                val total = calculateTotal(binding.etBaseAmount.text.toString().toDouble(), tip, binding.seekBarSplit.progress)

                binding.tvTipAmount.text = "%.2f".format(tip)
                binding.tvTotalAmount.text = "%.2f".format(total)
            }
        })


        binding.seekBarSplit.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                // Solves issue of user moving seekbar before base amount is entered
                if (binding.etBaseAmount.text.isEmpty()) {
                    binding.tvTotalAmount.text = ""
                    binding.tvSplitBy.text = updateSplit(p1)
                    return
                }

                binding.tvSplitBy.text = updateSplit(p1)

                val total = calculateTotal(binding.etBaseAmount.text.toString().toDouble(), binding.tvTipAmount.text.toString().toDouble(), p1)

                binding.tvTotalAmount.text = "%.2f".format(total)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

    }

    private fun calculateTip(baseAmount: Double, tipPercent: Double): Double {
        return baseAmount * tipPercent / 100
    }

    private fun calculateTotal(baseAmount: Double, tip: Double, split: Int): Double {
        return if (split > 1) {
            (baseAmount + tip) / split
        } else {
            (baseAmount + tip)
        }
    }

    private fun updateTipDescription(tipPercent: Int): CharSequence {
        return when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..19 -> "Good"
            in 20..29 -> "Great"
            else -> "Amazing"
        }
    }

    private fun updateSplit(split: Int): CharSequence {
        return when (split) {
            0 -> "Split By 1"
            1 -> "Split By 1"
            2 -> "Split By 2"
            3 -> "Split By 3"
            4 -> "Split By 4"
            5 -> "Split By 5"
            6 -> "Split By 6"
            7 -> "Split By 7"
            8 -> "Split By 8"
            9 -> "Split By 9"
            10 -> "Split By 10"
            11 -> "Split By 11"
            12 -> "Split By 12"
            13 -> "Split By 13"
            14 -> "Split By 14"
            else -> "Split By 15"
        }
    }
}