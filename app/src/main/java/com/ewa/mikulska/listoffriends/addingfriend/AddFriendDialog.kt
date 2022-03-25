package com.ewa.mikulska.listoffriends.addingfriend

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.ewa.mikulska.listoffriends.BitmapConverter
import com.ewa.mikulska.listoffriends.databinding.DialogAddFriendBinding
import org.joda.time.LocalDate
import org.koin.androidx.viewmodel.ext.android.viewModel

val TAG = "ADD FRIEND DIALOG"
private val REQUEST_IMAGE_CAPTURE = 1

class AddFriendDialog : DialogFragment(), DatePickerDialog.Callback {

    private lateinit var binding: DialogAddFriendBinding
    private val viewModel: AddFriendViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddFriendBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takePhotoButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (
                e: ActivityNotFoundException
            ) {
                Toast.makeText(context, "Cant't find camera device", Toast.LENGTH_SHORT).show()
            }
        }

        binding.switchFriend.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isFriend = isChecked
        }

        binding.writeName.doAfterTextChanged {
            val name = binding.writeName.text.toString()
            viewModel.name = name
        }

        binding.writeSurname.doAfterTextChanged {
            val surname = binding.writeSurname.text.toString()
            viewModel.surname = surname
        }

        binding.confirmButton.setOnClickListener {
            viewModel.addNewFriend()
        }

        binding.pickDateButton.setOnClickListener {
            DatePickerDialog().apply {
                callback = this@AddFriendDialog
            }.show(childFragmentManager, "Date picker")
        }

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is AddFriendViewModel.MyEvent.Error -> {
                    val stringHint = it.message
                    binding.writeSurname.hint = stringHint
                    binding.writeName.hint = stringHint
                }
                is AddFriendViewModel.MyEvent.ErrorToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    dismiss()
                }
            }
        }
    }

    override fun onDateSet(date: LocalDate) {
        viewModel.birthDate = date
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageByteArray = BitmapConverter.fromBitmaptoByteArray(imageBitmap)
            viewModel.imageURL = imageByteArray
        }
    }
}

