package com.example.greenspot.presentation.cleaner.sign

object Validator { //rules used to verify if the data insert into signup are valid or not

    fun validateCompanyName(companyName:String) : ValidationResult {
        return ValidationResult(
            (!companyName.isNullOrEmpty() && companyName.length >= 6) //if the companyName is not null or empty and is greater than 6
        )
    }

    fun validateEmail(email:String) : ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) //used to see if the email is valid, equivalent to regex
        )
    }

    fun validatePassword(password:String) : ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length >= 8)
        )
    }

}

data class ValidationResult(
    val status : Boolean = false
)