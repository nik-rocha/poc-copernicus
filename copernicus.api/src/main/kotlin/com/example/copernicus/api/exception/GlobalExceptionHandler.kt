package com.example.copernicus.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

import com.example.copernicus.api.exception.ResourceNotFoundException
import com.example.copernicus.api.exception.ForbiddenActionException
import com.example.copernicus.api.exception.ConflictException
import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.TransactionSystemException
import org.springframework.web.bind.MethodArgumentNotValidException

data class ErrorResponse(val message: String, val status: Int)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val message = ex.constraintViolations.firstOrNull()?.message ?: "Dados inválidos fornecidos."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message, 400))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Dados de requisição inválidos."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message, 400))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        val causeMessage = ex.mostSpecificCause.message ?: ""

        val userMessage = when {
            causeMessage.contains("duplicate key", ignoreCase = true) -> "Já existe um registro cadastrado com esses dados."
            causeMessage.contains("foreign key", ignoreCase = true) -> "Não é possível alterar ou remover: o registro está vinculado a outros dados."
            else -> "Erro de integridade de dados no banco."
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(userMessage, 409))
    }

    @ExceptionHandler(TransactionSystemException::class)
    fun handleTransactionSystem(ex: TransactionSystemException): ResponseEntity<ErrorResponse> {
        val rootCause = ex.rootCause

        val userMessage = if (rootCause is ConstraintViolationException) {
            rootCause.constraintViolations.firstOrNull()?.message ?: "Dados inválidos."
        } else {
            rootCause?.message ?: "Falha ao processar a transação no banco."
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(userMessage, 400))
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(ex.message ?: "Recurso não encontrado", 404))

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(ex.message ?: "Conflito de dados", 409))

    @ExceptionHandler(ForbiddenActionException::class)
    fun handleForbidden(ex: ForbiddenActionException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(ex.message ?: "Ação não permitida", 403))

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(ex.message ?: "Erro inesperado no servidor.", 500))
    }
}