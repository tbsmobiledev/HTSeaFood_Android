package com.example.htseafood.interfaces

import com.example.htseafood.model.responses.SalesOrderLinesItem

interface EditListener {
    fun editQty(salesOrderLinesItem: SalesOrderLinesItem)
}