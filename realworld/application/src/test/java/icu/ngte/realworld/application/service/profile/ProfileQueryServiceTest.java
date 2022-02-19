package icu.ngte.realworld.application.service.profile;

import static org.junit.Assert.assertTrue;
import icu.ngte.realworld.application.service.ApplicationTestConfig;
import icu.ngte.realworld.application.service.ProfileQueryService;
import icu.ngte.realworld.domain.user.UserDO;
import icu.ngte.realworld.domain.user.UserRepository;
import icu.ngte.realworld.infra.dmr.data.ProfileData;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ApplicationTestConfig.class})
public class ProfileQueryServiceTest {
    @Autowired
    private ProfileQueryService profileQueryService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_fetch_profile_success() {
        UserDO currentUser = new UserDO("a@test.com", "a", "123", "", "");
        UserDO profileUser = new UserDO("p@test.com", "p", "123", "", "");
        userRepository.saveUser(profileUser);

        Optional<ProfileData> optional = profileQueryService.findByUsername(profileUser.getUsername(), currentUser);
        assertTrue(optional.isPresent());
    }
}