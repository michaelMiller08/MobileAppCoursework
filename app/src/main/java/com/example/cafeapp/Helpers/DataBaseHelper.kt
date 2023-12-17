package com.example.cafeapp.Helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.cafeapp.Models.CustomerModel
import com.example.cafeapp.Models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/* Database Config*/
private val DataBaseName = "Database.db"
private val ver : Int = 1

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context,DataBaseName,null ,ver) {


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

    // This is called the first time a database is accessed
    // Create a new database
    override fun onCreate(db: SQLiteDatabase?) {
        try {


            //Customer Table
            val sqlCreateStatementCustomer: String = "CREATE TABLE " + customerTableName + " ( " + column_CustmerId +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_CustomerFullName + " TEXT NOT NULL, " +
                    column_CustomerEmail + " TEXT NOT NULL, " + column_CustomerPhoneNo + " INTEGER NOT NULL, " +
                    column_CustomerUserName + " TEXT NOT NULL, " + column_CustomerPassword + " TEXT, " +
                    column_CustomerIsActive + " INTEGER NOT NULL)"

            //ProductTable
            val sqlCreateStatementProduct: String = "CREATE TABLE " + productTableName + " ( " + productColumnId +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + column_productName + " TEXT NOT NULL, " +
                    column_productPrice + " REAL NOT NULL, " + column_productImage + " BLOB NOT NULL, " +
                    column_productAvailable + " INTEGER NOT NULL)"

            db?.execSQL(sqlCreateStatementCustomer)
            db?.execSQL(sqlCreateStatementProduct)
        }
        catch (e: SQLiteException) {}
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


    suspend fun addCustomerAsync(customer: CustomerModel): Int {
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
            cv.put(column_CustomerPassword, customer.password)
            cv.put(column_CustomerIsActive, customer.isActive)

            val success = db.insert(customerTableName, null, cv)

            db.close()

            if (success.toInt() == -1) success.toInt() else success.toInt()
        }
    }

    fun addCustomer(customer: CustomerModel) : Int {

        val db: SQLiteDatabase
        val isUserNameAlreadyExists = checkUserName(customer) // check if the username is already exist in the database
        if(isUserNameAlreadyExists < 0)
            return isUserNameAlreadyExists

        try {
            db = this.writableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val cv: ContentValues = ContentValues()

        cv.put(column_CustomerFullName,customer.fullName)
        cv.put(column_CustomerEmail, customer.email)
        cv.put(column_CustomerPhoneNo, customer.phoneNo)
        cv.put(column_CustomerUserName,customer.username.lowercase())
        cv.put(column_CustomerPassword, customer.password)
        cv.put(column_CustomerIsActive, customer.isActive)

        val success  =  db.insert(customerTableName, null, cv)

        db.close()
        if (success.toInt() == -1) return success.toInt() //Error, adding new user
        else return success.toInt() //1

    }

    private fun checkUserName(customer: CustomerModel): Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val userName = customer.username.lowercase()

        val sqlStatement = "SELECT * FROM $customerTableName WHERE $column_CustomerUserName = ?"
        val param = arrayOf(userName)
        val cursor: Cursor =  db.rawQuery(sqlStatement,param)

        if(cursor.moveToFirst()){
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

    fun getCustomer(customer: CustomerModel) : Int {

        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        }
        catch(e: SQLiteException) {
            return -2
        }

        val userName = customer.username.lowercase()
        val userPassword = customer.password
        //val sqlStatement = "SELECT * FROM $TableName WHERE $Column_UserName = $userName AND $Column_Password = $userPassword"

        val sqlStatement = "SELECT * FROM $customerTableName WHERE $column_CustomerUserName = ? AND $column_CustomerPassword = ?"
        val param = arrayOf(userName,userPassword)
        val cursor: Cursor =  db.rawQuery(sqlStatement,param)
        if(cursor.moveToFirst()){
            // The customer is found
            val n = cursor.getInt(0)
            cursor.close()
            db.close()
            return n
        }

        cursor.close()
        db.close()
        return -1 //User not found

    }

    fun getCustomerById(customerId: Int): CustomerModel? {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            return null
        }

        val sqlStatement = "SELECT * FROM $customerTableName WHERE $column_CustmerId = ?"
        val param = arrayOf(customerId.toString())
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        return if (cursor.moveToFirst()) {
            val customerIdIndex = cursor.getColumnIndex(column_CustmerId)
            val customerFullNameIndex = cursor.getColumnIndex(column_CustomerFullName)
            val customerEmailIndex = cursor.getColumnIndex(column_CustomerEmail)
            val customerPhoneNoIndex = cursor.getColumnIndex(column_CustomerPhoneNo)
            val customerUserNameIndex = cursor.getColumnIndex(column_CustomerUserName)
            val customerPasswordIndex = cursor.getColumnIndex(column_CustomerPassword)
            val customerIsActiveIndex = cursor.getColumnIndex(column_CustomerIsActive)

            if (customerIdIndex == -1) {
                // Handle the case where the column index is not found
                cursor.close()
                db.close()
                null
            } else {
                val customer = CustomerModel(
                    cursor.getInt(customerIdIndex),
                    cursor.getString(customerFullNameIndex),
                    cursor.getString(customerEmailIndex),
                    cursor.getString(customerPhoneNoIndex),
                    cursor.getString(customerUserNameIndex),
                    cursor.getString(customerPasswordIndex),
                    cursor.getInt(customerIsActiveIndex) == 1
                )
                cursor.close()
                db.close()
                customer
            }
        } else {
            cursor.close()
            db.close()
            null // Customer not found
        }
    }
///////////************************//////////////

    fun getAllProducts(): ArrayList<ProductModel> {
        val db: SQLiteDatabase
        try {
            db = this.readableDatabase
        } catch (e: SQLiteException) {
            // Handle the exception or log an error
            return ArrayList()
        }

        val productList = ArrayList<ProductModel>()
        val sqlStatement = "SELECT $productColumnId, $column_productName, $column_productPrice, $column_productAvailable FROM $productTableName"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
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
                    //Remember to add this back in
//                    val image: ByteArray? = cursor.getBlob(imageIndex)

                    val product = ProductModel(id, name, price, null, available) // Note: Set image to null as it is not loaded here
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






}

