package jvm.pablohdz.myfilesapi.service.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.UserRepository;
import jvm.pablohdz.myfilesapi.service.UserService;

@Service
public class LocalUserService implements UserService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.toString());

    @Autowired
    public LocalUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(UserRequest request) {
        // TODO: 10/12/2021 hash password user before to save
        User user = new User();
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setUsername(request.getUsername());
        user.setNumberEmployee(Integer.valueOf(request.getNumEmployee()));
        user.setEmail(request.getEmail());

        createNewUser(user);
        logger.info("a new user was created with name: {} and email: {}",
            user.getFirstname(), user.getEmail());
    }

    private void createNewUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
