package com.comsultant.domain.account.api;

import com.comsultant.domain.account.dto.AccountDto;
import com.comsultant.domain.account.dto.FindPasswordDto;
import com.comsultant.domain.account.dto.PasswordDto;
import com.comsultant.domain.account.service.AccountService;
import com.comsultant.global.common.response.DtoResponse;
import com.comsultant.global.common.response.MessageResponse;
import com.comsultant.global.config.security.AccountDetails;
import com.comsultant.global.properties.ResponseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountApi {
    private final AccountService accountService;

    private final ResponseProperties responseProperties;

    @PostMapping("")
    public ResponseEntity<MessageResponse> registerAccount(@RequestBody AccountDto accountDto) {
        boolean result = accountService.registerAccount(accountDto);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MessageResponse> checkDuplicatedEmail(@PathVariable("email") String inputEmail) {
        boolean result = accountService.checkDuplicatedEmail(inputEmail);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @GetMapping("/name/{nickname}")
    public ResponseEntity<MessageResponse> checkDuplicatedNickname(@PathVariable("nickname") String inputNickname) {
        boolean result = accountService.checkDuplicatedNickname(inputNickname);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<MessageResponse> sendVerifyEmail(@RequestBody Map<String, String> inputBody) {
        String mailAddress = inputBody.get("email");
        if(mailAddress != null) {
            accountService.sendVerifyEmail(mailAddress);
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @GetMapping("/verify-email/{code}")
    public ResponseEntity<MessageResponse> verifyAuthToken(@PathVariable("code") String authToken, @RequestParam("email") String email) {
        if(email == null) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }

        boolean result = accountService.verifyAuthToken(authToken, email);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<DtoResponse<AccountDto>> getProfile(@AuthenticationPrincipal AccountDetails accountDetails) {
        AccountDto result = accountService.getProfile(accountDetails);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), result));
    }

    @PutMapping("")
    public ResponseEntity<MessageResponse> modifyAccount(@AuthenticationPrincipal AccountDetails accountDetails, @RequestBody AccountDto accountDto) {
        boolean result = accountService.modifyAccount(accountDetails, accountDto);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<MessageResponse> modifyPassword(@AuthenticationPrincipal AccountDetails accountDetails, @RequestBody PasswordDto passwordDto) {
        boolean result = accountService.modifyPassword(accountDetails, passwordDto);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @GetMapping("/send-verify-link/{email}")
    public ResponseEntity<MessageResponse> sendFindPasswordLink(@PathVariable("email") String email) {
        boolean result = accountService.sendFindPasswordLink(email);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }

    @GetMapping("/verify-token/{token}")
    public ResponseEntity<DtoResponse<FindPasswordDto>> verifyFindPasswordToken(@PathVariable("token") String token) {
        FindPasswordDto result = accountService.verifyFindPasswordToken(token);
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getSuccess(), result));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(DtoResponse.of(HttpStatus.OK, responseProperties.getFail(), result));
        }
    }

    @PatchMapping("/reset-password/{token}")
    public ResponseEntity<MessageResponse> resetPassword(@PathVariable("token") String token, @RequestBody PasswordDto passwordDto) {
        if(passwordDto == null || passwordDto.getNewPassword() == null || passwordDto.getNewPassword().length() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
        boolean result = accountService.resetPassword(token, passwordDto.getNewPassword());
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getSuccess()));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(HttpStatus.OK, responseProperties.getFail()));
        }
    }
}
