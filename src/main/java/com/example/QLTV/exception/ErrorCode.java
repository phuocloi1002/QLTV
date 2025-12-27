package com.example.QLTV.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAM(1001, "Invalid parameter.", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED(1002, "Permission denied.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(1003, "Authentication failed or session expired.", HttpStatus.UNAUTHORIZED),
    RESOURCE_NOT_FOUND(1004, "Resource not found.", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(2006, "Unauthenticated", HttpStatus.UNAUTHORIZED),

    USER_EXISTED(2001, "User already existed.", HttpStatus.CONFLICT),
    USERNAME_INVALID(2002, "Invalid username format.", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(2003, "Invalid password.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2004, "User not found.", HttpStatus.NOT_FOUND),
    ACCOUNT_LOCKED(2005, "Account is locked.", HttpStatus.FORBIDDEN),


    BOOK_NOT_FOUND(3001, "Book title not found.", HttpStatus.NOT_FOUND),
    COPY_NOT_FOUND(3002, "Book copy not found.", HttpStatus.NOT_FOUND),
    ISBN_EXISTED(3003, "ISBN already existed.", HttpStatus.CONFLICT),
    COPY_NOT_AVAILABLE(3004, "Book copy is not available for loan.", HttpStatus.CONFLICT),

    COPY_CANNOT_DELETE_BORROWED(3005, "Cannot delete a book copy that is currently borrowed.", HttpStatus.BAD_REQUEST),


    MAX_LOAN_LIMIT_EXCEEDED(4001, "Maximum loan limit exceeded.", HttpStatus.BAD_REQUEST),
    OUTSTANDING_FINE_EXIST(4002, "Outstanding fine prevents new transaction.", HttpStatus.FORBIDDEN),
    LOAN_NOT_FOUND(4003, "Loan record not found.", HttpStatus.NOT_FOUND),
    RESERVATION_NOT_FOUND(4004, "Reservation request not found.", HttpStatus.NOT_FOUND),
    RESERVATION_EXPIRED(4005, "Reservation expired.", HttpStatus.BAD_REQUEST),


    FINE_NOT_FOUND(5001, "Fine record not found.", HttpStatus.NOT_FOUND),
    PAYMENT_FAILED(5002, "Payment processing failed.", HttpStatus.BAD_REQUEST),
    FINE_ALREADY_PAID(5003, "Fine has been fully paid.", HttpStatus.CONFLICT),


    ROLE_NOT_FOUND(6001, "Role not found.", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(6002, "Permission not found.", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(6005, "Permission already exists", HttpStatus.BAD_REQUEST),
    ROLE_NAME_EXISTED(6003, "Role name already existed.", HttpStatus.CONFLICT),
    INVALID_ROLE_NAME(6004, "Invalid role name.", HttpStatus.BAD_REQUEST),


    STAFF_NOT_EXISTED(7001, "Staff not found.", HttpStatus.NOT_FOUND),
    STAFF_CODE_EXISTED(7002, "Staff code already existed.", HttpStatus.CONFLICT),
    STAFF_ALREADY_DELETED(7003, "Staff has already been deleted.", HttpStatus.BAD_REQUEST),







    TOKEN_CREATION_FAILED(9001, "Failed to create security token.", HttpStatus.INTERNAL_SERVER_ERROR);




    int code;
    String message;
    HttpStatus status;
}