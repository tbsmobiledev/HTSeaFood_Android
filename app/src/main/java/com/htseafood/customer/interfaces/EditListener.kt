package com.htseafood.customer.interfaces

import com.htseafood.customer.model.responses.SalesOrderLinesItem

interface EditListener {
    fun editQty(salesOrderLinesItem: SalesOrderLinesItem)
}