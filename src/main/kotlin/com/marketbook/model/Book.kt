package com.marketbook.model

import com.marketbook.enums.BookStatus
import com.marketbook.enums.Errors
import com.marketbook.exception.BadRequestException
import javax.persistence.*

@Entity(name = "book")
data class Book(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column
    val name: String,

    @Column
    val price: Double,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: Customer? = null
) {

    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
            when (field) {
                BookStatus.CANCELED,
                BookStatus.DELETED -> throw BadRequestException(Errors.MB102.message.format(field), Errors.MB102.code)

                else -> field = value
            }
        }

    constructor(
        id: Int? = null,
        name: String,
        price: Double,
        customer: Customer? = null,
        status: BookStatus?
    ) : this(id, name, price, customer) {
        this.status = status
    }

}
