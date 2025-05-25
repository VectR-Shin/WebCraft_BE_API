package com.team22.webcraft.Service;

import com.team22.webcraft.DTO.UserData.UserDataDTO;
import com.team22.webcraft.Exception.UserData.DuplicatedIdException;
import com.team22.webcraft.Exception.UserData.SignInException;
import com.team22.webcraft.Repository.UserDataRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// do not use embedded db in test
public class UserDataServiceTest {
    @Autowired UserDataService userDataService;
    @Autowired UserDataRepository userDataRepository;

    @Test
    @DisplayName(value = "UserDataServiceTest - signUp")
    void signUp() {
        UserDataDTO dto1 = new UserDataDTO("id1", "pw1");
        UserDataDTO dto2 = new UserDataDTO("id2", "pw2");

        userDataService.signUp(dto1);
        userDataService.signUp(dto2);

        Assertions.assertTrue(userDataRepository.checkUserIdExists(dto1.getUserId()));
        Assertions.assertTrue(userDataRepository.checkUserIdExists(dto2.getUserId()));

        // check ID not exist
        Assertions.assertFalse(userDataRepository.checkUserIdExists("id4"));

        // check duplicated ID
        Assertions.assertThrows(DuplicatedIdException.class, () -> {userDataService.signUp(dto1);});
    }

    @Test
    @DisplayName(value = "UserDataServiceTest - signIn")
    void signIn() {
        UserDataDTO dto1 = new UserDataDTO("id1", "pw1");
        UserDataDTO dto2 = new UserDataDTO("id2", "pw2");

        userDataService.signUp(dto1);
        userDataService.signUp(dto2);

        Assertions.assertDoesNotThrow(() -> {userDataService.signIn(dto1);});
        Assertions.assertDoesNotThrow(() -> {userDataService.signIn(dto2);});

        // check ID not exist
        Assertions.assertThrows(SignInException.class, () -> {userDataService.signIn(new UserDataDTO("id3", "pw3"));});

        // check incorrect PW
        Assertions.assertThrows(SignInException.class, () -> {userDataService.signIn(new UserDataDTO("id2", "pw3"));});
    }
}
