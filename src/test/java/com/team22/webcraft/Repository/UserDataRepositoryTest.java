package com.team22.webcraft.Repository;

import com.team22.webcraft.Domain.UserData;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserDataRepositoryTest {
    @Autowired
    private UserDataRepository userDataRepository;

    @BeforeEach
    void init() {
        // initialize before test
        UserData init1 = new UserData("init1", "pw1");
        UserData init2 = new UserData("init2", "pw2");

        userDataRepository.saveUserData(init1);
        userDataRepository.saveUserData(init2);
    }

    @Test
    @DisplayName("UserDataRepository Test - saveUserData")
    void testSaveUserData() {
        UserData data1 = new UserData("id1", "pw1");
        UserData data2 = new UserData("id2", "pw2");

        userDataRepository.saveUserData(data1);
        userDataRepository.saveUserData(data2);
    }

    @Test
    @DisplayName("UserDataRepository Test - findOneUserData")
    void testFindOneUserData() {
        UserData data1 = userDataRepository.findOneUserData(1l);
        UserData data2 = userDataRepository.findOneUserData(2l);

        Assertions.assertEquals(data1.getUserId(), "init1");
        Assertions.assertEquals(data2.getUserId(), "init2");
    }

    @Test
    @DisplayName("UserDataRepository Test - checkUserIdExists")
    void testCheckUserIdExists() {
        Assertions.assertTrue(userDataRepository.checkUserIdExists("init1"));
        Assertions.assertTrue(userDataRepository.checkUserIdExists("init2"));
        Assertions.assertFalse(userDataRepository.checkUserIdExists("notExists"));
    }

    @Test
    @DisplayName("UserDataRepository Test - checkPW")
    void testCheckPW() {
        UserData true1 = new UserData("init1", "pw1");
        UserData true2 = new UserData("init2", "pw2");

        UserData false1 = new UserData("init1", "pw2");
        UserData false2 = new UserData("new1", "pw1");

        Assertions.assertTrue(userDataRepository.checkPW(true1));
        Assertions.assertTrue(userDataRepository.checkPW(true2));
        Assertions.assertFalse(userDataRepository.checkPW(false1));
        Assertions.assertFalse(userDataRepository.checkPW(false2));
    }
}
