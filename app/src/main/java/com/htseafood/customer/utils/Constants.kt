package com.htseafood.customer.utils

class Constants {
    companion object {

        // Base URL
        const val BASE_URL = "https://htseafood.com/htseafood/api/" //live

        const val SIGN_IN = "login.php"
        const val INVOICE_LIST = "postedsalesinvoice.php"
        const val INVOICE_DETAIL = "postedsalesinvoicedetail.php"
        const val SHIPMENT_LIST = "postedsalesshipment.php"
        const val SHIPMENT_DETAIL = "postedsalesshipmentdetail.php"
        const val ORDER_DETAIL = "postedsalesorderdetail.php"
        const val ORDER_LIST = "postedsalesorder.php"
        const val CREATE_ORDER = "createsalesorder.php"
        const val DELETE_ORDER = "deletesalesorder.php"
        const val DELETE_ITEM = "deletesalesorderline.php"
        const val SEARCH_UPC = "searchdescription.php"
        const val SEARCH_ITEMNO = "searchitemno.php"
        const val ADD_ITEM = "createsalesorderline.php"
        const val UPDATE_ITEM = "modifysalesorderline.php"
        const val SEND_ORDER_PDF = "sendOrderPDF.php"

        // Constants Key
        const val IS_LOGIN = "is_login"
        const val CustmerNo = "custmerNo"
        const val CustmerName = "custmerName"
        const val CustmerEmail = "custmerEmail"

    }


}