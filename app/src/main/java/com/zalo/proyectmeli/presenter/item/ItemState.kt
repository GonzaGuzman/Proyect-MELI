package com.zalo.proyectmeli.presenter.item

import androidx.databinding.ObservableField

class ItemState {
    val numberItem: ObservableField<Int?> = ObservableField<Int?>(0)
    val id: ObservableField<String?> = ObservableField<String?>()
    val title: ObservableField<String?> = ObservableField<String?>()
    val thumbnail: ObservableField<String?> = ObservableField<String?>()
    val condition: ObservableField<String?> = ObservableField<String?>()
    val price: ObservableField<Double?> = ObservableField<Double?>(0.0)
    val soldQuantity: ObservableField<Int?> = ObservableField<Int?>(0)
    val permaLink: ObservableField<String?> = ObservableField<String?>()
}
