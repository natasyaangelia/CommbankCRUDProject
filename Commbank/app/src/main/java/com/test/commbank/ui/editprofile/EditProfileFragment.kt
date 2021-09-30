package com.test.commbank.ui.editprofile

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.test.commbank.R
import com.test.commbank.data.model.AddEmployee
import com.test.commbank.data.model.BrowseEmployee
import com.test.commbank.data.model.Status
import com.test.commbank.data.model.UpdateEmployee
import com.test.commbank.databinding.EditProfileFragmentBinding
import com.test.commbank.ui.base.BaseFragment
import com.test.commbank.ui.bottomsheet.GenderBottomSheet
import com.test.commbank.utils.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment : BaseFragment<EditProfileFragmentBinding, EditProfileViewModel>(false),
    EditProfileNavigator {

    private val controller: NavController? by lazy { view?.findNavController() }
    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var binding: EditProfileFragmentBinding
    private val args by navArgs<EditProfileFragmentArgs>()
    private var updateData: BrowseEmployee.Response.Data? = null

    override fun onInitialization() {
        super.onInitialization()
        binding = getViewDataBinding()
        binding.lifecycleOwner = this
        editProfileViewModel.navigator = this
        setData()
    }

    override fun setLayout(): Int {
        return R.layout.edit_profile_fragment
    }

    override fun getViewModels() = editProfileViewModel

    override fun onObserveAction() {
        observe(editProfileViewModel.addUser) {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        Loading.showLoading(requireActivity())
                    }
                    Status.SUCCESS -> {
                        Loading.hideLoading()
                        requireContext().showToast(requireContext().getString(R.string.success_add_user))
                        binding.formGender.setClearText()
                        binding.formName.setClearText()
                        binding.formEmail.setClearText()
                        requireContext().hideKeyboard(requireView())
                    }
                    Status.ERROR -> {
                        Loading.hideLoading()
                        it.message?.let { msg -> requireContext().showToast(msg, Toast_Error) }
                    }
                    else -> {
                        Loading.hideLoading()
                    }
                }
            }
        }

        observe(editProfileViewModel.updateUser) {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        Loading.showLoading(requireActivity())
                    }
                    Status.SUCCESS -> {
                        Loading.hideLoading()
                        requireContext().showToast(requireContext().getString(R.string.success_update_user))
                        requireActivity().onBackPressed()
                    }
                    Status.ERROR -> {
                        Loading.hideLoading()
                        it.message?.let { msg -> requireContext().showToast(msg, Toast_Error) }
                    }
                    else -> {
                        Loading.hideLoading()
                    }
                }
            }
        }
    }

    override fun onReadyAction() {
        onValidateForm()

        binding.formGender.setClicked()
        binding.formGender.editTextInstance.onClick {
            showGenre()
        }
    }

    private fun onValidateForm() {
        binding.formEmail.editTextInstance.onTextChangeListener {
            editProfileViewModel.validEmail.value = it.isEmailValid()
            binding.formEmail.setError(!it.isEmailValid())
            validInput()
        }

        binding.formName.editTextInstance.onTextChangeListener {
            binding.formName.setError(it.isEmpty())
            editProfileViewModel.validName.value = it.isNotEmpty()
            validInput()
        }
    }

    private fun validInput() {
        editProfileViewModel.validInput.value =
            ((editProfileViewModel.validName.value == true) &&
                    (editProfileViewModel.validEmail.value == true) &&
                    (editProfileViewModel.validGender.value == true))
    }

    private fun setData() {
        updateData = args.data
        updateData?.let {
            binding.formName.textValue = updateData?.name.toString()
            binding.formGender.textValue = updateData?.gender.toString()
            binding.formEmail.textValue = updateData?.email.toString()

            binding.btnToBrowse.visibility = View.GONE
            binding.btnSubmit.text = requireContext().getString(R.string.edit)
            editProfileViewModel.isEdit.value = true
            editProfileViewModel.validGender.value = true
            editProfileViewModel.validName.value = true
            editProfileViewModel.validEmail.value = true
        }
    }

    private fun showGenre() {
        GenderBottomSheet {
            binding.formGender.textValue = it
            editProfileViewModel.validGender.value = true
            validInput()
        }.show(childFragmentManager, GenderBottomSheet.TAG)
    }

    override fun goToBrowseProfileFragment() {
        val directions = EditProfileFragmentDirections.actionEditProfileFragmentToBrowseProfileFragment()
        controller?.navigate(directions)
    }

    override fun addUser() {
        editProfileViewModel.addUserAsync(
            AddEmployee.Request(
                binding.formName.editTextInstance.text.toString(),
                binding.formGender.editTextInstance.text.toString(),
                binding.formEmail.editTextInstance.text.toString(),
                "active"
            )
        )
    }

    override fun updateUser() {
        updateData?.let {
            editProfileViewModel.updateUserAsync(
                it.id,
                UpdateEmployee.Request(
                    binding.formName.editTextInstance.text.toString(),
                    binding.formEmail.editTextInstance.text.toString(),
                    "active"
                )
            )
        }
    }
}