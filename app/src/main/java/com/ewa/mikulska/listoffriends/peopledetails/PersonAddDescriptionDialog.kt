package com.ewa.mikulska.listoffriends.peopledetails

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ewa.mikulska.listoffriends.databinding.DialogAddDescriptionBinding
import kotlinx.android.synthetic.main.dialog_add_description.*

class PersonAddDescriptionDialog : DialogFragment() {

    private lateinit var binding: DialogAddDescriptionBinding
    var callback: Callback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddDescriptionBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            val descriptionText = editTextTextPersonName.text.toString()
            callback?.sendDescription(descriptionText)
            dismiss()
        }
    }

    interface Callback {
        fun sendDescription(descrption: String)
    }
}