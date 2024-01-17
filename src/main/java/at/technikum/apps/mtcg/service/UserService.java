package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private AuthorizationService authorizationService;
    private final DatabaseUserRepository databaseUserRepository;

    public UserService(DatabaseUserRepository databaseUserRepository) {
        this.databaseUserRepository = databaseUserRepository;
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public User findWithUsername(String username, String authorizationToken) {
        Optional<User> userOptional = databaseUserRepository.findWithUsername(username);
        if(userOptional.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, ExceptionMessage.NOT_FOUND_USER);
        }

        User user = userOptional.get();
        if(authorizationToken == null || user.getToken() == null || (!authorizationToken.equals(user.getToken()) && !user.getUsername().equals("admin"))) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_ACCESS);
        }

        return user;
    }

    public User findWithToken(String token) {
        Optional<User> optionalUser = databaseUserRepository.findWithToken(token);
        if(optionalUser.isEmpty()) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_ACCESS);
        }
        User user = optionalUser.get();

        Timestamp loginTimeStamp = user.getLoginTimestamp();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        long millisecondsDifference = currentTimestamp.getTime() - loginTimeStamp.getTime();

        if(millisecondsDifference > 30000) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_ACCESS);
        }
        return user;
    }

    public User update(String authorizationToken, String username, User updatedUser) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);
        if(!user.getUsername().equals(username)) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_ACCESS);
        }

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getBio() != null) {
            user.setBio(updatedUser.getBio());
        }
        if (updatedUser.getImage() != null) {
            user.setImage(updatedUser.getImage());
        }
        user.setCoins(user.getCoins());
        user.setElo(user.getElo());
        user.setWins(user.getWins());
        user.setLosses(user.getLosses());

        return databaseUserRepository.update(user);
    }

    public void updateCoins(User user, int sum) {
        user.setCoins(sum);
        this.databaseUserRepository.update(user);
    }

    public User save(User user) {
        if(this.databaseUserRepository.findWithUsername(user.getUsername()).isPresent()) {
            throw new HttpStatusException(HttpStatus.ALREADY_EXISTS, HttpContentType.TEXT_PLAIN, ExceptionMessage.ALREADY_EXISTS_USER);
        }
        user.setId(UUID.randomUUID().toString());
        user.setCoins(20);
        user.setElo(100);
        user.setWins(0);
        user.setLosses(0);
        return databaseUserRepository.save(user);
    }
}
