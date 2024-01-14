package com.example.blog.exception;

import com.example.blog.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    // lỗi này bắt khi nhập id không tồn tại ví dụ localhost:8080:api/v1/posts/10000 --> id =10000
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handlerResourceNotFoundException(ResourceNotFoundException resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.NOT_FOUND.value(),new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // lỗi này bắt khi nhập url đúng định dạng không đúng ví dụ localhost:8080:api/v1/posts/10000abc --> id =10000abc
    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handlerBlogAPIException(BlogAPIException resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.BAD_REQUEST.value(), new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // nhập sai định dạng url ví dụ: http://localhost:8080/api/v1/posts/comments/123abc
    // vì yêu câù phải là sô 123
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> handlerArgumentTypeMismatchException(MethodArgumentTypeMismatchException resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.BAD_REQUEST.value(),new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // nhập sai định dạng url ví dụ: http://localhost:8080/api/v1/posts/comments/123abc
    // vì yêu câù phải là sô 123
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDetails> handlerNoResourceFoundException(NoResourceFoundException resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.NOT_FOUND.value(),new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDetails> handlerNullPointerException(NullPointerException resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.NOT_FOUND.value(),new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // bắt lỗi nhập sai validation cho các trường input
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerNotValidException(MethodArgumentNotValidException exception, WebRequest webRequest){
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error ->
                {
                    String field = ((FieldError)error).getField();  // trường như name,age bị lôix
                    String message = error.getDefaultMessage();
                    errors.put(field,message);
                });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // lỗi phân quyền nghĩa là không có quyền truy cập
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handlerAccessDeniedException(AccessDeniedException resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.UNAUTHORIZED.value(),new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // đăng nhập tài khoản mật khẩu sai
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handlerBadCredentialsException(BadCredentialsException resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.UNAUTHORIZED.value(),new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // nếu không vào 2 lỗi trên thì sẽ được đưa vào exception này, bắt hết exception luôn
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handlerException(Exception resource, WebRequest webRequest){
        ErrorDetails errorDetails =new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(),new Date(),resource.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
