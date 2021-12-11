package jvm.pablohdz.myfilesapi.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jvm.pablohdz.myfilesapi.dto.UserRequest;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.UserRepository;
import jvm.pablohdz.myfilesapi.service.UserService;

@Service
public class LocalUserService implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public LocalUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(UserRequest request) {
        User user = new User();
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setUsername(request.getUsername());
        user.setNumberEmployee(Integer.valueOf(request.getNumEmployee()));

        User userSaved = userRepository.save(user);
        // TODO: 09/12/2021 create mapper
    }
}
