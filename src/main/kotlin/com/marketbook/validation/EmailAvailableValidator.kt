package com.marketbook.validation

import com.marketbook.service.CustomerService
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EmailAvailableValidator(
    private val customerService: CustomerService
) : ConstraintValidator<EmailAvailable, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean =
        if (value.isNullOrEmpty()) false else customerService.emailAvailable(value)

}
