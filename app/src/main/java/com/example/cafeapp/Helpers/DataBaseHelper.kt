package com.example.cafeapp.Helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.cafeapp.Models.AdminModel
import com.example.cafeapp.Models.CustomerModel
import com.example.cafeapp.Models.OrderDetailsModel
import com.example.cafeapp.Models.OrderModel
import com.example.cafeapp.Models.PaymentModel
import com.example.cafeapp.Models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import java.time.format.DateTimeFormatter

/* Database Config*/
private const val DataBaseName = "Database.db"
private const val ver: Int = 1

class DataBaseHelper(context: Context) : IDataBaseHelper,
    SQLiteOpenHelper(context, DataBaseName, null, ver) {

    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")


    //Customer table//
    private val customerTableName = "Customer"
    private val column_CustmerId = "CusId"
    private val column_CustomerFullName = "CusFullName"
    private val column_CustomerEmail = "CusEmail"
    private val column_CustomerPhoneNo = "CusPhoneNo"
    private val column_CustomerUserName = "CusUserName"
    private val column_CustomerPassword = "CusPassword"
    private val column_CustomerIsActive = "CusIsActive"

    /*******************************/


    //Product Table////
    private val productTableName = "Product"
    private val productColumnId = "ProdId"
    private val column_productName = "ProdName"
    private val column_productPrice = "ProdPrice"
    private val column_productImage = "ProdImage"
    private val column_productAvailable = "ProdAvailable"

    /*************************/


    //Admin table//
    private val adminTableName = "Admin"
    private val column_AdminId = "AdminId"
    private val column_AdminFullName = "AdminFullName"
    private val column_AdminEmail = "AdminEmail"
    private val column_AdminPhoneNo = "AdminPhoneNo"
    private val column_AdminUserName = "AdminUserName"
    private val column_AdminPassword = "AdminPassword"
    private val column_AdminIsActive = "AdminIsActive"

    /*******************************/

    //Order table//
    private val orderTableName = "`Order`"
    private val column_OrderId = "OrderId"
    private val column_OrderCusId = "CusId"
    private val column_OrderOrderDate = "OrderDate"
    private val column_OrderOrderTime = "OrderTime"
    private val column_OrderOrderStatus = "OrderStatus"

    /*******************************/

    //OrderDetails  table//
    private val orderDetailsTableName = "OrderDetails"
    private val column_orderDetailsOrderDetailsId = "OrderDetailsId"
    private val column_orderDetailsOrderId = "OrderId"
    private val column_orderDetailsProdId = "ProdId"

    /*******************************/

    //Payment  table//
    private val paymentTableName = "Payment"
    private val column_PaymentPaymentId = "PaymentId"
    private val column_PaymentOrderId = "OrderId"
    private val column_PaymentPaymentType = "PaymentType"
    private val column_PaymentPaymentAmount = "Amount"
    private val column_PaymentPaymentDate = "PaymentDate"

    /*******************************/

    // This is called the first time a database is accessed
    // Create a new database
    override fun onCreate(db: SQLiteDatabase?) {
        try {


            //Customer Table
            val sqlCreateStatementCustomer: String =
                "CREATE TABLE " + customerTableName + " ( " + column_CustmerId +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_CustomerFullName + " TEXT NOT NULL, " +
                        column_CustomerEmail + " TEXT NOT NULL, " + column_CustomerPhoneNo + " INTEGER NOT NULL, " +
                        column_CustomerUserName + " TEXT NOT NULL, " + column_CustomerPassword + " TEXT, " +
                        column_CustomerIsActive + " INTEGER NOT NULL)"

            //ProductTable
            val sqlCreateStatementProduct: String =
                "CREATE TABLE " + productTableName + " ( " + productColumnId +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_productName + " TEXT NOT NULL, " +
                        column_productPrice + " REAL NOT NULL, " + column_productImage + " BLOB NOT NULL, " +
                        column_productAvailable + " INTEGER NOT NULL)"

            //AdminTable
            val sqlCreateStatementAdmin: String =
                "CREATE TABLE " + adminTableName + " ( " + column_AdminId +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_AdminFullName + " TEXT NOT NULL, " +
                        column_AdminEmail + " TEXT NOT NULL, " + column_AdminPhoneNo + " INTEGER NOT NULL, " +
                        column_AdminUserName + " TEXT NOT NULL, " + column_AdminPassword + " TEXT, " +
                        column_AdminIsActive + " INTEGER NOT NULL)"

            //OrderTable
            val sqlCreateStatementOrder: String =
                "CREATE TABLE " + orderTableName + " ( " + column_OrderId +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_OrderCusId + " INTEGER NOT NULL, " +
                        column_OrderOrderDate + " TEXT NOT NULL, " + column_OrderOrderTime + " TEXT NOT NULL, " +
                        column_OrderOrderStatus + " TEXT NOT NULL)"

            //OrderDetailsTable
            val sqlCreateStatementOrderDetails: String =
                "CREATE TABLE " + orderDetailsTableName + " ( " + column_orderDetailsOrderDetailsId +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_orderDetailsOrderId + " INTEGER NOT NULL," + column_orderDetailsProdId + " INTEGER NOT NULL)"

            //PaymentTable
            val sqlCreateStatementPayment: String =
                "CREATE TABLE " + paymentTableName + " ( " + column_PaymentPaymentId +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_PaymentOrderId + " INTEGER NOT NULL, " +
                        column_PaymentPaymentType + " TEXT NOT NULL, " + column_PaymentPaymentAmount + " REAL NOT NULL, " +
                        column_PaymentPaymentDate + " TEXT NOT NULL )"

            db?.execSQL(sqlCreateStatementCustomer)
            db?.execSQL(sqlCreateStatementProduct)
            db?.execSQL(sqlCreateStatementAdmin)
            db?.execSQL(sqlCreateStatementOrder)
            db?.execSQL(sqlCreateStatementPayment)
            db?.execSQL(sqlCreateStatementOrderDetails)
        } catch (e: SQLiteException) {
        }
    }

    // This is called if the database ver. is changed
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    //CUSTOMER//


    /**
     * return  1 : the new use has been add to the database successfully
     * return -1 : Error, adding new user
     * return -2 : can not Open/Create database
     * return -3 : user name is already exist
     *
     */


    override suspend fun addCustomerAsync(customer: CustomerModel): Int {
        return withContext(Dispatchers.IO) {
            val isUserNameAlreadyExists = checkUserName(customer)
            if (isUserNameAlreadyExists < 0)
                return@withContext isUserNameAlreadyExists

            val db = writableDatabase
            val cv = ContentValues()

            cv.put(column_CustomerFullName, customer.fullName)
            cv.put(column_CustomerEmail, customer.email)
            cv.put(column_CustomerPhoneNo, customer.phoneNo)
            cv.put(column_CustomerUserName, customer.username.lowercase())

            // Hash the password using bcrypt
            val hashedPassword = BCrypt.hashpw(customer.password, BCrypt.gensalt())
            cv.put(column_CustomerPassword, hashedPassword)

            cv.put(column_CustomerIsActive, customer.isActive)

            val success = db.insert(customerTableName, null, cv)

            db.close()

            if (success.toInt() == -1) success.toInt() else success.toInt()
        }
    }


    override fun checkUserName(customer: CustomerModel): Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            return -2
        }

        val userName = customer.username.lowercase()

        val sqlStatement = "SELECT * FROM $customerTableName WHERE $column_CustomerUserName = ?"
        val param = arrayOf(userName)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            // The user is found
            val n = cursor.getInt(0)
            cursor.close()
            db.close()
            return -3 // error the user name is already exist
        }

        cursor.close()
        db.close()
        return 0 //User not found

    }

    override fun getCustomer(customer: CustomerModel): Int {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            return -2
        }

        val userName = customer.username.lowercase()

        val sqlStatement = "SELECT * FROM $customerTableName WHERE $column_CustomerUserName = ?"
        val param = arrayOf(userName)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        return if (cursor.moveToFirst()) {
            // The user is found, now check the password
            val passwordColumnIndex = cursor.getColumnIndex(column_CustomerPassword)
            val customerIdColumnIndex = cursor.getColumnIndex(column_CustmerId)

            if (passwordColumnIndex != -1 && customerIdColumnIndex != -1) {
                val storedHashedPassword = cursor.getString(passwordColumnIndex)

                if (BCrypt.checkpw(customer.password, storedHashedPassword)) {
                    // Passwords match
                    val customerId = cursor.getInt(customerIdColumnIndex)
                    cursor.close()
                    db.close()
                    return customerId
                } else {
                    // Passwords do not match
                    cursor.close()
                    db.close()
                    return -1
                }
            } else {
                // Password or customer ID column not found
                cursor.close()
                db.close()
                return -1
            }
        } else {
            // User not found
            cursor.close()
            db.close()
            return -1
        }
    }


    ///////////************************//////////////

    // Modify the getAllProducts function to handle image data
    override fun getAllProducts(): ArrayList<ProductModel> {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            // Handle the exception or log an error
            return ArrayList()
        }

        val productList = ArrayList<ProductModel>()
        val sqlStatement =
            "SELECT $productColumnId, $column_productName, $column_productPrice, $column_productImage, $column_productAvailable FROM $productTableName"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(productColumnId)
                val nameIndex = cursor.getColumnIndex(column_productName)
                val priceIndex = cursor.getColumnIndex(column_productPrice)
                val availableIndex = cursor.getColumnIndex(column_productAvailable)
                val imageIndex = cursor.getColumnIndex(column_productImage)

                // Check if column indices are valid
                if (idIndex >= 0 && nameIndex >= 0 && priceIndex >= 0 && availableIndex >= 0 && imageIndex >= 0) {
                    val id: Int = cursor.getInt(idIndex)
                    val name: String = cursor.getString(nameIndex)
                    val price: Float = cursor.getFloat(priceIndex)
                    val available: Boolean = cursor.getInt(availableIndex) == 1
                    val image: ByteArray? = cursor.getBlob(imageIndex)

                    val product = ProductModel(
                        id,
                        name,
                        price,
                        image,
                        available
                    )
                    productList.add(product)
                } else {
                    // Handle the case where one or more column indices are not found
                    // Log an error or take appropriate action
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return productList
    }

    override fun getProductById(productId: Int): ProductModel? {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            return null
        }
        val sqlStatement =
            "SELECT $productColumnId, $column_productName, $column_productPrice, $column_productImage, $column_productAvailable FROM $productTableName WHERE $productColumnId = ?"

        val param = arrayOf(productId.toString())
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        return if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(productColumnId)
            val nameIndex = cursor.getColumnIndex(column_productName)
            val priceIndex = cursor.getColumnIndex(column_productPrice)
            val availableIndex = cursor.getColumnIndex(column_productAvailable)
            val imageIndex = cursor.getColumnIndex(column_productImage)

            // Check if column indices are valid
            if (idIndex >= 0 && nameIndex >= 0 && priceIndex >= 0 && availableIndex >= 0) {
                val id: Int = cursor.getInt(idIndex)
                val name: String = cursor.getString(nameIndex)
                val price: Float = cursor.getFloat(priceIndex)
                val available: Boolean = cursor.getInt(availableIndex) == 1
                val image: ByteArray? = cursor.getBlob(imageIndex)

                val product = ProductModel(
                    id,
                    name,
                    price,
                    image,
                    available
                )
                cursor.close()
                db.close()
                product
            } else {
                cursor.close()
                db.close()
                null
            }
        } else {
            cursor.close()
            db.close()
            null // Product not found
        }
    }

    override fun editProduct(product: ProductModel) {
        val db = writableDatabase
        val cv = ContentValues()

        // Update the relevant columns with the new values
        cv.put(column_productName, product.name)
        //only showing 2 decimal places
        cv.put(
            column_productPrice, product.price.toDouble().toFloat()
        )


        // Define the WHERE clause to update the specific product based on its ID
        val whereClause = "$productColumnId = ?"
        val whereArgs = arrayOf(product.id.toString())

        // Perform the update
        db.update(productTableName, cv, whereClause, whereArgs)

        db.close()
    }

    override fun editProductImageById(productId: Int, image: ByteArray) {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(column_productImage, image)

        val whereClause = "$productColumnId = ?"
        val whereArgs = arrayOf(productId.toString())

        // Perform the update
        db.update(productTableName, cv, whereClause, whereArgs)

        db.close()
    }


    //////////////////////////////
    override suspend fun addAdminAsync(admin: AdminModel): Int {
        return withContext(Dispatchers.IO) {
            val checkAdminUsernameExists = checkAdminUsernameExists(admin)
            if (checkAdminUsernameExists < 0)
                return@withContext checkAdminUsernameExists

            val db = writableDatabase
            val cv = ContentValues()

            cv.put(column_AdminFullName, admin.fullName)
            cv.put(column_AdminEmail, admin.email)
            cv.put(column_AdminPhoneNo, admin.phoneNo)
            cv.put(column_AdminUserName, admin.username.lowercase())

            // Hash the password using bcrypt
            val hashedPassword = BCrypt.hashpw(admin.password, BCrypt.gensalt())
            cv.put(column_AdminPassword, hashedPassword)

            cv.put(column_AdminIsActive, admin.isActive)

            val success = db.insert(adminTableName, null, cv)

            db.close()

            if (success.toInt() == -1) success.toInt() else success.toInt()
        }
    }

    override fun checkAdminUsernameExists(admin: AdminModel): Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            return -2
        }

        val userName = admin.username.lowercase()

        val sqlStatement = "SELECT * FROM $adminTableName WHERE $column_AdminUserName = ?"
        val param = arrayOf(userName)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            // The user is found
            val n = cursor.getInt(0)
            cursor.close()
            db.close()
            return -3 // error the user name is already exist
        }

        cursor.close()
        db.close()
        return 0 //User not found

    }

    override fun getAdmin(admin: AdminModel): Int {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            return -2
        }

        val userName = admin.username.lowercase()

        val sqlStatement =
            "SELECT * FROM $adminTableName WHERE $column_AdminUserName = ?"
        val param = arrayOf(userName)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)
        if (cursor.moveToFirst()) {
            // The admin is found, now check the password
            val passwordColumnIndex = cursor.getColumnIndex(column_AdminPassword)
            val adminIdColumnIndex = cursor.getColumnIndex(column_AdminId)

            if (passwordColumnIndex != -1 && adminIdColumnIndex != -1) {
                val storedHashedPassword = cursor.getString(passwordColumnIndex)

                return if (BCrypt.checkpw(admin.password, storedHashedPassword)) {
                    // Passwords match
                    val adminId = cursor.getInt(adminIdColumnIndex)
                    cursor.close()
                    db.close()
                    adminId
                } else {
                    // Passwords do not match
                    cursor.close()
                    db.close()
                    -1
                }
            } else {
                // Password or admin ID column not found
                cursor.close()
                db.close()
                return -1
            }
        } else {
            // Admin not found
            cursor.close()
            db.close()
            return -1
        }
    }

    override suspend fun addProductAsync(product: ProductModel) {
        return withContext(Dispatchers.IO) {
            val db = writableDatabase
            val cv = ContentValues()
            cv.put(column_productName, product.name)
            cv.put(column_productPrice, product.price)
            cv.put(column_productImage, product.image)
            cv.put(column_productAvailable, product.available)

            val success = db.insert(productTableName, null, cv)
            db.close()
        }

    }

    // Order table
    override suspend fun addOrderAsync(order: OrderModel): Long {
        return withContext(Dispatchers.IO) {
            val db = writableDatabase
            val cv = ContentValues()
            cv.put(column_OrderCusId, order.customerId)
            cv.put(column_OrderOrderDate, order.orderDate)
            cv.put(column_OrderOrderTime, order.orderTime)
            cv.put(column_OrderOrderStatus, order.orderStatus)

            val success = db.insert(orderTableName, null, cv)
            val orderId =
                if (success != -1L) success else throw SQLiteException("Failed to insert order")

            db.close()
            orderId
        }
    }

    // Payment table
    override suspend fun addPaymentAsync(paymentModel: PaymentModel) {
        return withContext(Dispatchers.IO) {
            val db = writableDatabase
            val cv = ContentValues()

            cv.put(column_PaymentOrderId, paymentModel.orderId)
            cv.put(column_PaymentPaymentType, paymentModel.paymentType)
            cv.put(column_PaymentPaymentAmount, paymentModel.paymentAmount)
            cv.put(column_PaymentPaymentDate, paymentModel.paymentDate)

            val success = db.insert(paymentTableName, null, cv)
            db.close()

            success
        }
    }

    // OrderDetails table
    override suspend fun addOrderDetailsAsync(orderDetailsModel: OrderDetailsModel): Long {
        return withContext(Dispatchers.IO) {
            val db = writableDatabase
            val cv = ContentValues()

            cv.put(column_orderDetailsOrderId, orderDetailsModel.orderId)
            cv.put(column_orderDetailsProdId, orderDetailsModel.productId)

            val success = db.insert(orderDetailsTableName, null, cv)
            db.close()

            success
        }
    }


    fun getAllOrdersByCustomerId(customerId: Int): List<OrderModel> {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            // Handle the exception or log an error
            return emptyList()
        }

        val orderList = mutableListOf<OrderModel>()
        val sqlStatement =
            "SELECT $column_OrderId, $column_OrderCusId, $column_OrderOrderDate, $column_OrderOrderTime, $column_OrderOrderStatus FROM $orderTableName WHERE $column_OrderCusId = ?"

        val param = arrayOf(customerId.toString())
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            do {
                val orderIdIndex = cursor.getColumnIndex(column_OrderId)
                val customerIdIndex = cursor.getColumnIndex(column_OrderCusId)
                val orderDateIndex = cursor.getColumnIndex(column_OrderOrderDate)
                val orderTimeIndex = cursor.getColumnIndex(column_OrderOrderTime)
                val orderStatusIndex = cursor.getColumnIndex(column_OrderOrderStatus)

                if (orderIdIndex >= 0 && customerIdIndex >= 0 && orderDateIndex >= 0 && orderTimeIndex >= 0 && orderStatusIndex >= 0) {
                    val orderId: Int = cursor.getInt(orderIdIndex)
                    val customerId: Int = cursor.getInt(customerIdIndex)
                    val orderDate: String = cursor.getString(orderDateIndex)
                    val orderTime: String = cursor.getString(orderTimeIndex)
                    val orderStatus: String = cursor.getString(orderStatusIndex)

                    val order = OrderModel(
                        orderId,
                        customerId,
                        orderDate,
                        orderTime,
                        orderStatus
                    )
                    orderList.add(order)
                } else {
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return orderList
    }

    fun getAllPreparingOrders(): List<OrderModel> {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            // Handle the exception or log an error
            return emptyList()
        }

        val preparingOrderList = mutableListOf<OrderModel>()
        val sqlStatement =
            "SELECT $column_OrderId, $column_OrderCusId, $column_OrderOrderDate, $column_OrderOrderTime, $column_OrderOrderStatus FROM $orderTableName WHERE $column_OrderOrderStatus = ?"

        val param = arrayOf("Preparing")
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            do {
                val orderIdIndex = cursor.getColumnIndex(column_OrderId)
                val customerIdIndex = cursor.getColumnIndex(column_OrderCusId)
                val orderDateIndex = cursor.getColumnIndex(column_OrderOrderDate)
                val orderTimeIndex = cursor.getColumnIndex(column_OrderOrderTime)
                val orderStatusIndex = cursor.getColumnIndex(column_OrderOrderStatus)

                if (orderIdIndex >= 0 && customerIdIndex >= 0 && orderDateIndex >= 0 && orderTimeIndex >= 0 && orderStatusIndex >= 0) {
                    val orderId: Int = cursor.getInt(orderIdIndex)
                    val customerId: Int = cursor.getInt(customerIdIndex)
                    val orderDate: String = cursor.getString(orderDateIndex)
                    val orderTime: String = cursor.getString(orderTimeIndex)
                    val orderStatus: String = cursor.getString(orderStatusIndex)

                    val order = OrderModel(
                        orderId,
                        customerId,
                        orderDate,
                        orderTime,
                        orderStatus
                    )
                    preparingOrderList.add(order)
                } else {
                    // Handle the case where one or more column indices are not found
                    // Log an error or take appropriate action
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return preparingOrderList
    }

    fun getProductIdsByOrderId(orderId: Int): List<Int> {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            return emptyList()
        }

        val productIds = mutableListOf<Int>()
        val sqlStatement =
            "SELECT $column_orderDetailsProdId FROM $orderDetailsTableName WHERE $column_orderDetailsOrderId = ?"

        val param = arrayOf(orderId.toString())
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            do {
                val productIdIndex = cursor.getColumnIndex(column_orderDetailsProdId)

                if (productIdIndex >= 0) {
                    val productId: Int = cursor.getInt(productIdIndex)
                    productIds.add(productId)
                } else {
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return productIds
    }

    fun markOrderForCollection(orderId: Int) {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(column_OrderOrderStatus, "Collect")

        val whereClause = "$column_OrderId = ?"
        val whereArgs = arrayOf(orderId.toString())

        db.update(orderTableName, cv, whereClause, whereArgs)

        db.close()
    }

}

