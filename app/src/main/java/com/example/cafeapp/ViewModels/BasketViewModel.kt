import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Models.ProductModel

class BasketViewModel(application: Application) : AndroidViewModel(application) {

    private val _basket = MutableLiveData<List<ProductModel>>(emptyList())
    val basket: LiveData<List<ProductModel>> get() = _basket

    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)

    fun addToBasketById(id: Int) {
        val product = productFromID(id)
        if (product != null) {
            _basket.value = (_basket.value ?: emptyList()) + product
        }
    }

    fun productFromID(productID: Int): ProductModel? {
        for (product in getProducts()) {
            if (product.id == productID) {
                return product
            }
        }
        return null
    }

    private fun getProducts(): ArrayList<ProductModel> {
        return db.getAllProducts()
    }
}
