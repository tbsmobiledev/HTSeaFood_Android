package com.example.htseafood.utils


import android.content.Context
import android.content.SharedPreferences

class SharedHelper {
    companion object {
        var sharedPreferences: SharedPreferences? = null
        var editor: SharedPreferences.Editor? = null

        fun putKey(context: Context, Key: String?, Value: String?) {
            sharedPreferences =
                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
            editor = sharedPreferences!!.edit()
            editor!!.putString(Key, Value)
            editor!!.apply()
        }




    

        fun getKey(context: Context, Key: String?): String {
            var data = ""
            sharedPreferences =
                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
            data = sharedPreferences!!.getString(Key, "").toString()
            return data
        }


//        fun getUser(context: Context): UserResponse? {
//            sharedPreferences =
//                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
//            return GsonBuilder()
//                .setLenient()
//                .create()
//                .fromJson(
//                    sharedPreferences!!.getString(Constants.UserData, ""),
//                    UserResponse::class.java
//                )
//        }


        fun putKey(context: Context, Key: String?, value: Int?) {
            sharedPreferences =
                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
            editor = sharedPreferences!!.edit()
            editor!!.putInt(Key, value!!)
            editor!!.apply()
        }

//        fun getIntKey(context: Context, Key: String?): Int {
//            sharedPreferences =
//                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
//            return sharedPreferences!!.getInt(Key, -1)
//        }


        fun putKey(context: Context, Key: String?, Value: Boolean?) {
            sharedPreferences =
                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
            editor = sharedPreferences!!.edit()
            editor!!.putBoolean(Key, Value!!)
            editor!!.apply()
        }

        fun getBoolKey(context: Context, Key: String?): Boolean {
            sharedPreferences =
                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
            return sharedPreferences!!.getBoolean(Key, false)
        }


        fun clearSharedPreferences(context: Context) {
            sharedPreferences =
                context.getSharedPreferences("htseafood_preferances", Context.MODE_PRIVATE)
            sharedPreferences!!.edit().clear().apply()
        }

    }
}