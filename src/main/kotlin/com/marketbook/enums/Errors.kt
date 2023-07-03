package com.marketbook.enums

enum class Errors(val  code: String, val message: String) {

    //GENERIC
    MB000(code = "MB-000", message = "Access Denied"),
    MB001(code = "MB-001", message = "Invalid Request"),

    //BOOK
    MB101(code = "MB-101", message = "Book [%s] not exists"),
    MB102(code = "MB-102", message = "Cannot update book with status [%s]"),
    MB103(code = "MB-103", message = "Cannot sell book with status [%s]"),

    //CUSTOMER
    MB201(code = "MB-201", message = "Customer [%s] not exists"),
}