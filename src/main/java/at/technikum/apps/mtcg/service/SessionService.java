package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.DatabaseUserRepository;

public class SessionService {

    private final DatabaseUserRepository databaseUserRepository;

    public SessionService(DatabaseUserRepository databaseUserRepository) {
        this.databaseUserRepository = databaseUserRepository;
    }
}
