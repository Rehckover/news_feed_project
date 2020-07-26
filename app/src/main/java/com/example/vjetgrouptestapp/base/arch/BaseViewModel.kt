package digital.cvlt.app.core.platform

import androidx.lifecycle.ViewModel
import com.example.vjetgrouptestapp.base.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope

abstract class BaseViewModel : ViewModel() {

    val progressVisibility = SingleLiveEvent<Boolean>()

    protected fun showLoading() {
        progressVisibility.postValue(true)
    }

    protected fun hideLoading() {
        progressVisibility.postValue(false)
    }

    fun handleErrors(
        exception: Exception,
        retryListener: (() -> Unit)? = null
    ) {

    }
}
