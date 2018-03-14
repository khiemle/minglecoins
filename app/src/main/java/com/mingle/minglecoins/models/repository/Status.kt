package com.mingle.minglecoins.models.repository

enum class Status {
    SUCCESS,
    ERROR,
    LOADING;
    fun isLoading() = this == LOADING
}