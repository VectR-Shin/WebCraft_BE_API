package com.team22.webcraft.Service;

import com.team22.webcraft.DTO.UserData.UserDataDTO;
import com.team22.webcraft.Domain.UserData;
import com.team22.webcraft.Exception.UserData.DuplicatedIdException;
import com.team22.webcraft.Exception.UserData.SignInException;
import com.team22.webcraft.Repository.UserDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDataService {
    private final UserDataRepository userDataRepository;

    public UserData signIn(UserDataDTO userDataDTO) {
        if (!userDataRepository.checkUserIdExists(userDataDTO.getUserId()))
            throw new SignInException("ID or PW is incorrect.");

        UserData userData = new UserData(userDataDTO.getUserId(), userDataDTO.getUserPw());

        if (!userDataRepository.checkPW(userData))
            throw new SignInException("ID or PW is incorrect.");

        return userDataRepository.findOneUserDataById(userDataDTO.getUserId());
    }

    public void signUp(UserDataDTO userDataDTO) {
        if (userDataRepository.checkUserIdExists(userDataDTO.getUserId()))
            throw new DuplicatedIdException("This ID is already in use.");

        UserData userData = new UserData(userDataDTO.getUserId(), userDataDTO.getUserPw());
        userDataRepository.saveUserData(userData);
    }
}
