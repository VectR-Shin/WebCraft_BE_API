package com.team22.webcraft.Repository;

import com.team22.webcraft.Domain.UserData;
import com.team22.webcraft.SHA256.SHA256Generator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDataRepository {
    private final EntityManager em;

    public void saveUserData(UserData userData) {
        // sha256
        String pw = SHA256Generator.generateSHA256(userData.getUserPw());
        userData.setUserPw(pw);

        em.persist(userData);
    }

    // compare encrypt userData.userPw and user_pw in table (encrypted)
    public boolean checkPW(UserData userData) {
        TypedQuery<String> query = em.createQuery("SELECT UD.userPw FROM UserData AS UD WHERE UD.userId = :userId", String.class);
        query.setParameter("userId", userData.getUserId());

        try {
            String userPw = query.getSingleResult();
            return userPw.equals(SHA256Generator.generateSHA256(userData.getUserPw()));
        } catch (NoResultException e) {
            return false;
        }
    }

    public UserData findOneUserData(Long userDataId) {return em.find(UserData.class, userDataId);}

    public UserData findOneUserDataById(String userId) {
        return em.createQuery("SELECT UD FROM UserData AS UD WHERE UD.userId = :userId", UserData.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    public boolean checkUserIdExists(String userId) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM UserData AS UD WHERE UD.userId = :userId", Long.class);
        query.setParameter("userId", userId);

        Long count = query.getSingleResult();
        if (count == null)
            count = 0l;

        // if exists return true / not exists return false
        return (count >= 1);
    }

    // for test
    public void deleteAll() {
        em.createQuery("DELETE FROM UserData").executeUpdate();
    }
}
