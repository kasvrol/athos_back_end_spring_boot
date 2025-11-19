# Exception Handling Implementation Summary

## Overview
Comprehensive exception handling system implemented for ms-campeonato microservice.

## Files Created

### 1. Exception Classes (/src/main/java/br/ufpr/athos/campeonato/exception/)

#### ResourceNotFoundException.java
- Used for: Resource not found errors (404)
- Constructors:
  - `ResourceNotFoundException(String message)` - Custom message
  - `ResourceNotFoundException(String resource, String id)` - Formatted message

#### ValidationException.java
- Used for: Input validation errors (400)
- Constructor: `ValidationException(String message)`

#### BusinessRuleException.java
- Used for: Business logic violations (422)
- Constructor: `BusinessRuleException(String message)`

#### ForbiddenException.java
- Used for: Access denied errors (403)
- Constructor: `ForbiddenException(String message)`

### 2. Error Response DTO (/src/main/java/br/ufpr/athos/campeonato/dto/)

#### ErrorResponse.java
- Fields:
  - `code` - Error code (e.g., "RESOURCE_NOT_FOUND")
  - `message` - Error message
  - `timestamp` - Auto-generated timestamp
  - `errors` - Map for field validation errors

- Constructors:
  - `ErrorResponse(String code, String message)` - Simple errors
  - `ErrorResponse(String code, Map<String, String> errors)` - Field validation errors

### 3. Global Exception Handler (/src/main/java/br/ufpr/athos/campeonato/config/)

#### GlobalExceptionHandler.java
- Annotated with `@RestControllerAdvice`
- Handlers:
  - `handleResourceNotFound()` - Returns 404 NOT_FOUND
  - `handleValidation()` - Returns 400 BAD_REQUEST
  - `handleBusinessRule()` - Returns 422 UNPROCESSABLE_ENTITY
  - `handleForbidden()` - Returns 403 FORBIDDEN
  - `handleMethodArgumentNotValid()` - Returns 400 for @Valid violations
  - `handleGenericException()` - Returns 500 INTERNAL_SERVER_ERROR

## Services Updated

### 1. PartidaService.java
**Changes made:**
- Added imports: `ResourceNotFoundException`, `ValidationException`, `BusinessRuleException`
- `criarPartida()`:
  - Campeonato not found → `ResourceNotFoundException("Campeonato", id)`
  - Equipe not found → `ResourceNotFoundException("Equipe", id)`
  - Same team validation → `ValidationException`
  - Team ownership validation → `ValidationException`
- `buscarPorId()`: → `ResourceNotFoundException("Partida", id)`
- `listarPorCampeonato()`: → `ResourceNotFoundException("Campeonato", id)`
- `atualizarPlacar()`:
  - Not found → `ResourceNotFoundException("Partida", id)`
  - Already finished → `BusinessRuleException`
- `atualizarStatus()`: → `ResourceNotFoundException("Partida", id)`
- `deletar()`: → `ResourceNotFoundException("Partida", id)`
- `gerarTabelaPontosCorridos()`:
  - Campeonato not found → `ResourceNotFoundException("Campeonato", id)`
  - Wrong format → `ValidationException`
  - Min teams validation → `ValidationException`
  - Existing matches → `BusinessRuleException`

### 2. MembroEquipeService.java
**Changes made:**
- Added imports: `ResourceNotFoundException`, `ValidationException`, `BusinessRuleException`
- `adicionarMembro()`:
  - Equipe not found → `ResourceNotFoundException("Equipe", id)`
  - Duplicate member → `BusinessRuleException`
- `removerMembro()`:
  - Member not found → `ResourceNotFoundException("Membro", id)`
  - Wrong team → `ValidationException`
  - Captain removal → `BusinessRuleException`

### 3. CampeonatoService.java
**Changes made:**
- Added import: `ResourceNotFoundException`
- `buscarPorId()`: → `ResourceNotFoundException("Campeonato", id)`
- `atualizarStatus()`: → `ResourceNotFoundException("Campeonato", id)`

### 4. EquipeService.java
**Changes made:**
- Added import: `ResourceNotFoundException`
- `criarEquipe()`: → `ResourceNotFoundException("Campeonato", id)`
- `buscarPorId()`: → `ResourceNotFoundException("Equipe", id)`

## HTTP Status Code Mapping

| Exception | Status Code | HTTP Status |
|-----------|-------------|-------------|
| ResourceNotFoundException | 404 | NOT_FOUND |
| ValidationException | 400 | BAD_REQUEST |
| BusinessRuleException | 422 | UNPROCESSABLE_ENTITY |
| ForbiddenException | 403 | FORBIDDEN |
| MethodArgumentNotValidException | 400 | BAD_REQUEST |
| Exception (generic) | 500 | INTERNAL_SERVER_ERROR |

## Error Response Format

### Simple Error
```json
{
  "code": "RESOURCE_NOT_FOUND",
  "message": "Campeonato não encontrado com ID: abc123",
  "timestamp": "2025-11-18T20:39:00"
}
```

### Field Validation Error
```json
{
  "code": "VALIDATION_ERROR",
  "timestamp": "2025-11-18T20:39:00",
  "errors": {
    "nome": "não deve estar em branco",
    "email": "deve ser um e-mail válido"
  }
}
```

## Benefits

1. **Standardized Error Responses**: All errors follow the same format
2. **Clear Error Codes**: Easy to identify error types programmatically
3. **Proper HTTP Status**: Correct status codes for different error types
4. **Portuguese Messages**: User-friendly error messages in Portuguese
5. **Validation Support**: Automatic handling of @Valid annotation errors
6. **Centralized Handling**: All exception handling in one place
7. **Type Safety**: Specific exception classes for different error types
8. **Maintainability**: Easy to add new exception types

## Usage Examples

### Throwing Exceptions in Service Layer
```java
// Resource not found
campeonatoRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Campeonato", id));

// Validation error
if (equipe1Id.equals(equipe2Id)) {
    throw new ValidationException("Uma equipe não pode jogar contra si mesma");
}

// Business rule violation
if (partida.getStatus() == StatusPartida.FINALIZADA) {
    throw new BusinessRuleException("Não é possível atualizar o placar de uma partida já finalizada");
}

// Access denied (when needed)
if (!user.hasPermission()) {
    throw new ForbiddenException("Acesso negado a este recurso");
}
```

## Testing the Exception Handling

Test the endpoints to see the error responses:

```bash
# Resource not found (404)
curl http://localhost:8082/api/campeonatos/invalid-id

# Validation error (400)
curl -X POST http://localhost:8082/api/partidas \
  -H "Content-Type: application/json" \
  -d '{"equipe1Id": "123", "equipe2Id": "123"}'

# Business rule violation (422)
curl -X PUT http://localhost:8082/api/partidas/{id}/placar \
  -H "Content-Type: application/json" \
  -d '{"placarEquipe1": 3, "placarEquipe2": 2}'
  # (on already finished match)
```

## Next Steps

1. Add logging to exception handlers for monitoring
2. Consider adding more specific exception types as needed
3. Implement exception handling in other microservices
4. Add integration tests for error scenarios
5. Document API errors in OpenAPI/Swagger specification

