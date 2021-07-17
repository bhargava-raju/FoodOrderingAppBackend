package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.*;

import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthorizationFailedException.class)
  public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.FORBIDDEN);
  }


  @ExceptionHandler(UpdateCustomerException.class)
  public ResponseEntity<ErrorResponse> updateCustomerException(UpdateCustomerException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(AddressNotFoundException.class)
  public ResponseEntity<ErrorResponse> addressNotFoundException(AddressNotFoundException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(RestaurantNotFoundException.class)
  public ResponseEntity<ErrorResponse> restaurantNotFoundException(RestaurantNotFoundException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CouponNotFoundException.class)
  public ResponseEntity<ErrorResponse> couponNotFoundException(CouponNotFoundException exception) {
    return new ResponseEntity<>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(SaveAddressException.class)
  public ResponseEntity<ErrorResponse> saveAddressException(
      SaveAddressException exception) {
    return new ResponseEntity<>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(PaymentMethodNotFoundException.class)
  public ResponseEntity<ErrorResponse> saveAddressException(PaymentMethodNotFoundException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ItemNotFoundException.class)
  public ResponseEntity<ErrorResponse> itemNotFoundException(ItemNotFoundException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidRatingException.class)
  public ResponseEntity<ErrorResponse> invalidRatingExcpetion(InvalidRatingException exception) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),
        HttpStatus.BAD_REQUEST);
  }
}

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> AuthenticationFailedException(AuthenticationFailedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> AuthorizationFailedException(AuthorizationFailedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> UpdateCustomerException(UpdateCustomerException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> CouponNotFoundException(CouponNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> SaveAddressException(SaveAddressException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> AddressNotFoundException(AddressNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> RestaurantNotFoundException(RestaurantNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<ErrorResponse> PaymentMethodNotFoundException(PaymentMethodNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> ItemNotFoundException(ItemNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> CategoryNotFoundException(CategoryNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> InvalidRatingException(InvalidRatingException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

}

