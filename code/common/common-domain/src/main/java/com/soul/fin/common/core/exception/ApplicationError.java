package com.soul.fin.common.core.exception;

public sealed interface ApplicationError permits DomainError, SystemError {
}