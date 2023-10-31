package com.htseafood.customer.interfaces

interface PDFListener {
    fun sendPDF(orderId: String)
}