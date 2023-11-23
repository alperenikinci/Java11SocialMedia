package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.UserManager;
import com.bilgeadam.mapper.AuthMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.AuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import com.bilgeadam.utility.enums.ERole;
import com.bilgeadam.utility.enums.EStatus;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth,Long> {

    private final AuthRepository authRepository;
    private final UserManager userManager;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManager;
    private final RegisterProducer registerProducer;

    public AuthService(AuthRepository authRepository, UserManager userManager, JwtTokenManager jwtTokenManager, CacheManager cacheManager, RegisterProducer registerProducer) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager = userManager;
        this.jwtTokenManager = jwtTokenManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
    }

    @Transactional //Metotta herhangi bir yerden exception donuyorsa metot icerisinde yapilan butun degisiklikleri geri alir. (Rollback)
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        try {
            save(auth);
            userManager.createUser(AuthMapper.INSTANCE.fromAuthToUserCreateRequestDto(auth));
            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        } catch (Exception e){
//            delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }

    @Transactional
    public RegisterResponseDto registerWithRabbitMQ(RegisterRequestDto dto) {
        Auth auth = AuthMapper.INSTANCE.fromRegisterRequestToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        try {
            save(auth);

            //rabbitmq ile haberlesme saglayacagiz.
            System.out.println("oncesi");
            registerProducer.sendNewUser(AuthMapper.INSTANCE.fromAuthToRegisterModel(auth));

            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        } catch (Exception e){
//            delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
        return AuthMapper.INSTANCE.fromAuthToRegisterResponse(auth);
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> authOptional =  authRepository.findOptionalByUsernameAndPassword(dto.getUsername(),dto.getPassword());
        if(authOptional.isEmpty()){
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if(!authOptional.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        Optional<String> token = jwtTokenManager.createToken(authOptional.get().getId(),authOptional.get().getRole());
//        if(token.isEmpty()){
//            throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
//        }

        return jwtTokenManager.createToken(authOptional.get().getId(),authOptional.get().getRole())
                .orElseThrow(()->{
                    throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
    }

    public Boolean activateStatus(ActivationRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if(auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(dto.getActivationCode().equals(auth.get().getActivationCode())){
            auth.get().setStatus(EStatus.ACTIVE);
            update(auth.get());
//            userManager.activateStatus(auth.get().getId());
            userManager.activateStatus2(ActivateStatusRequestDto.builder().authId(dto.getId()).build());
            //            auth.get().setUpdateDate(System.currentTimeMillis());
            //            authRepository.save(auth.get());
            return true;
        } else {
            throw new AuthManagerException(ErrorType.ACTIVATION_CODE_ERROR);
        }

    }

    public Boolean updateEmailOrUsername(UpdateEmailOrUsernameRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setUsername(dto.getUsername());
        auth.get().setEmail(dto.getEmail());
        update(auth.get());
        return true;
    }

    public Boolean delete(Long id) {
        Optional<Auth> auth = findById(id);
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.delete(id);
        return true;
    }

    public Boolean deleteByToken(String token) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(token);
        Optional<Auth> auth = findById(authId.get());
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.delete(authId.get());
        return true;
    }

    public List<Long> findByRole(String role) {
        ERole myRole;
        try {
            myRole = ERole.valueOf(role.toUpperCase(Locale.ENGLISH)); //admin -> ADMİN -> ADMIN
        } catch (Exception e){
            throw new AuthManagerException(ErrorType.ROLE_NOT_FOUND);
        }
        return authRepository.findAllByRole(myRole).stream().map(x->x.getId()).collect(Collectors.toList());
    }
}
