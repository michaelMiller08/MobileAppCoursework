import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cafeapp.Helpers.BasketManager
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Models.ProductModel

class BasketViewModel(application: Application) : AndroidViewModel(application) {

    private val basketManager: BasketManager = BasketManager(application.applicationContext)

    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)

    private val _basket = MutableLiveData<List<ProductModel>>()

    private val _totalPrice = MutableLiveData<Float>()
    val totalPrice: LiveData<Float> get() = _totalPrice
    val basket: LiveData<List<ProductModel>> get() = _basket

    fun addToBasketById(id: Int) {
        basketManager.addIdToBasket(id)
        updateBasketLiveData()
    }

init {
    updateTotalPrice()

}

    fun getItemsInBasket(): List<ProductModel> {

        val basketList: MutableList<ProductModel> = mutableListOf()
        for (id in basketManager.getBasketIds()) {
            val product = productFromID(id)
            if (product != null) {
                basketList.add(product)
            }
        }
        return basketList.toList()
    }

    fun clearBasket() {
        basketManager.clearBasket()
        updateBasketLiveData()

    }

    fun removeItemFromBasket(id: Int) {
        basketManager.removeIdFromBasket(id)
        updateBasketLiveData()

    }

    fun productFromID(productID: Int): ProductModel? {
        for (product in getProducts()) {
            if (product.id == productID) {
                return product
            }
        }
        return null
    }

    private fun updateBasketLiveData() {
        // Update LiveData with the current basket data
        _basket.value = getItemsInBasket()
        updateTotalPrice()

    }

    private fun getProducts(): ArrayList<ProductModel> {
        return db.getAllProducts()
    }

    private fun updateTotalPrice() {
        // Calculate the total price from the current basket
        val currentBasket = getItemsInBasket()
        val total = currentBasket.sumOf { it.price.toDouble() }.toFloat()
        _totalPrice.value = total
    }
}
