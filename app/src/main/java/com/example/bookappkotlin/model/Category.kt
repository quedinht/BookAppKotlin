package com.example.bookappkotlin.model

class Category {

    var id: String = ""
    var name: String = ""
    private var uid: String = ""
    private var timeCreate: Long = 0

    constructor()

    constructor(id: String, name: String, uid: String, timeCreate: Long) {
        this.id = id
        this.name = name
        this.uid = uid
        this.timeCreate = timeCreate
    }
}