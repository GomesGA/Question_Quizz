package br.com.ufu.question_quizz.dto

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null, message: String? = null): ApiResponse<T> {
            return ApiResponse(
                success = true,
                message = message,
                data = data
            )
        }
        
        fun <T> error(message: String, data: T? = null): ApiResponse<T> {
            return ApiResponse(
                success = false,
                message = message,
                data = data
            )
        }
    }
} 