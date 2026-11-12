package com.example.konekin.model

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("employee_name")
    val employeeName: String,
    @SerializedName("employee_salary")
    val employeeSalary: Int,
    @SerializedName("employee_age")
    val employeeAge: Int
)