package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR(5200,"Sunucu Hatasi...", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4200,"Parametre Hatasi...", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4210,"Kullanici adi kullanilmaktadir" ,HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4211,"Kulanici bulunamadi..." , HttpStatus.BAD_REQUEST ),
    USER_NOT_CREATED(4212,"Kullanici profili olusturulamadi...",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4213,"Gecersiz token",HttpStatus.BAD_REQUEST)
    ;
    private int code;
    private String message;
    private HttpStatus httpStatus;
}
