package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivationRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.AuthMapper;
import com.bilgeadam.repository.AuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.ServiceManager;
import com.bilgeadam.utility.enums.EStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth,Long> {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        super(authRepository);
        this.authRepository = authRepository;
    }

    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }

    public Boolean login(LoginRequestDto dto) {
        Optional<Auth> authOptional =  authRepository.findOptionalByUsernameAndPassword(dto.getUsername(),dto.getPassword());
        if(authOptional.isEmpty()){
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        return true;
    }

    public Boolean activateStatus(ActivationRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if(auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(dto.getActivationCode().equals(auth.get().getActivationCode())){
            auth.get().setStatus(EStatus.ACTIVE);
            update(auth.get());
            //            auth.get().setUpdateDate(System.currentTimeMillis());
            //            authRepository.save(auth.get());
            return true;
        } else {
            throw new AuthManagerException(ErrorType.ACTIVATION_CODE_ERROR);
        }

    }
}
