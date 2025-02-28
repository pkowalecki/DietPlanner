package pl.kowalecki.dietplanner.service.register;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kowalecki.dietplanner.client.auth.AuthClient;
import pl.kowalecki.dietplanner.controller.helper.RegisterHelper;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.service.WebPage.IWebPageResponseBuilder;


@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private IWebPageResponseBuilder responseBuilder;

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private RegisterHelper registerHelper;

    private RegistrationRequestDTO registrationRequestDTO;

    @BeforeEach
    void setUp() {
        registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setNickname("aaaaa");
        registrationRequestDTO.setName("aaaaa");
        registrationRequestDTO.setSurname("aaaaa");
        registrationRequestDTO.setEmailReg("test@test.pl");
        registrationRequestDTO.setPasswordReg("!@#$%QWERTy12345");
        registrationRequestDTO.setPasswordReg2("!@#$%QWERTy12345");
    }

//    @Test
//    void registerUser_shouldReturnSuccessMessage_whenAuthClientRegistersUserSuccessfully() {
//        //given
//        Mockito.when(registerHelper.checkRegistrationData(registrationRequestDTO))
//                .thenReturn(Collections.emptyMap());
//
//        Mockito.when(authClient.registerUser(registrationRequestDTO))
//                .thenReturn(Mono.just(res));
//
//        CustomResponse expectedResponse = CustomResponse.builder()
//                .status(ActionType.MESSAGE)
//                .message("User registered successfully")
//                .icon("success")
//                .build();
//
//        Mockito.when(responseBuilder.buildMessage("User registered successfully"))
//                .thenReturn(expectedResponse);
//
//        //when
//        Mono<CustomResponse> responseMono = registerService.registerUser(registrationRequestDTO);
//
//        //then
//        StepVerifier.create(responseMono)
//                .expectNext(expectedResponse)
//                .verifyComplete();
//
//        Mockito.verify(authClient).registerUser(registrationRequestDTO);
//        Mockito.verify(responseBuilder).buildMessage("User registered successfully");
//    }
}