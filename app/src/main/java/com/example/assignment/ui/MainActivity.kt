package com.example.assignment.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.ComponentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import com.example.UserApi.UserApi
import com.example.assignment.databinding.ActivityBinding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm by viewModel { UsersViewModel(it, UserApi) }
        vm.load()

        ActivityBinding.inflate(layoutInflater).run {
            setContentView(root)
            refresh.setOnRefreshListener {
                vm.load()
            }
            retry.setOnClickListener {
                vm.load()
            }

            vm.users.observe(this@MainActivity) { result: Result<List<User>>? ->
                result?.fold(
                    onSuccess = { users ->
                        refresh.isRefreshing = false
                        failure.visibility = GONE
                        val adapter = UserAdapter()
                        items.adapter = adapter
                        adapter.show(users)
                    },
                    onFailure = {
                        failure.visibility = VISIBLE
                    }
                )
            }

        }
    }
}
/**
 * Helper function that enables us to directly call constructor of our ViewModel but also
 * provides access to SavedStateHandle.
 * Usually generated by Hilt
 */
inline fun <reified VM: ViewModel> ComponentActivity.viewModel(crossinline create: (SavedStateHandle) -> VM) =
    ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            object: AbstractSavedStateViewModelFactory(this@viewModel, null) {
                override fun <T : ViewModel> create(key: String, type: Class<T>, handle: SavedStateHandle): T =
                    create(handle) as T
            }
        })