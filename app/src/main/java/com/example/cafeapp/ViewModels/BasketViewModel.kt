import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cafeapp.Helpers.BasketManager
import com.example.cafeapp.Helpers.DataBaseHelper
import com.example.cafeapp.Models.ProductModel

class BasketViewModel(application: Application) : AndroidViewModel(application) {

    private val basket: BasketManager = BasketManager(application.applicationContext)

    private val db: DataBaseHelper = DataBaseHelper(application.applicationContext)


    fun addToBasketById(id: Int) {
        basket.addIdToBasket(id)
    }

    fun getItemsInBasket(): List<ProductModel> {
        val basketList: MutableList<ProductModel> = mutableListOf()
        for (id in basket.getBasketIds()) {
            val product = productFromID(id)
            if (product != null) {
                basketList.add(product)
            }
        }
        return basketList.toList()
    }

    fun clearBasket() {
        basket.clearBasket()
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
