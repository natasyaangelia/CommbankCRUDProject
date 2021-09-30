package com.test.commbank.ui.bottomsheet

import com.test.commbank.R
import com.test.commbank.databinding.BottomSheetGenderBinding
import com.test.commbank.ui.base.BottomSheetBaseFragment
import com.test.commbank.ui.editprofile.EditProfileViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GenderBottomSheet(
    private val listener: (String) -> Unit
) : BottomSheetBaseFragment<BottomSheetGenderBinding, EditProfileViewModel>(),
    GenderBottomSheetNavigator {

    companion object {
        const val TAG = "filter_gender"
    }

    private val minipackViewModel: EditProfileViewModel by sharedViewModel(from = { requireParentFragment() })
    private lateinit var binding: BottomSheetGenderBinding
    private lateinit var choosenGenre: String

    override fun getTheme() = R.style.BottomSheetPrimaryStyle

    override fun setLayout() = R.layout.bottom_sheet_gender

    override fun getViewModels() = minipackViewModel

    override fun onInitialization() {
        binding = getViewDataBinding()
        binding.lifecycleOwner = this
    }

    override fun onReadyAction() {
        binding.tvFemale.onClick {
            binding.tvFemale.isSelected = true
            binding.tvMale.isSelected = false
            choosenGenre = "female"
        }

        binding.tvMale.onClick {
            binding.tvFemale.isSelected = false
            binding.tvMale.isSelected = true
            choosenGenre = "male"
        }

        binding.btnSubmit.onClick {
            listener(choosenGenre)
            dismiss()
        }
    }

    override fun onObserveAction() = Unit

    override fun finishFilter(data: String) {
        listener(data)
        dismiss()
    }
}