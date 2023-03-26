package study.toy.everythingshop.repository;

import study.toy.everythingshop.dto.SignInDTO;
import study.toy.everythingshop.entity.UserMEntity;

import java.util.Optional;

/**
 * fileName : UserDAO
 * author   : pilming
 * date     : 2023-03-08
 */
public interface UserDAO {
    int save(UserMEntity userMEntity);

    UserMEntity findByUserId(String userId);

    public Optional<SignInDTO> findById(String userId);

    public int join(SignInDTO userMEntity);
}
