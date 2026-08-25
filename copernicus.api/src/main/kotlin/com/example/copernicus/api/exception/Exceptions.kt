package com.example.copernicus.api.exception

class ResourceNotFoundException(message: String) : RuntimeException(message)
class ConflictException(message: String) : RuntimeException(message)
class ForbiddenActionException(message: String) : RuntimeException(message)
class MissingDataException(message: String) : RuntimeException(message)