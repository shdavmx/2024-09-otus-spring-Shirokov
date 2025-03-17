package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.security.acl.AclConfig;
import ru.otus.hw.security.configs.SecurityConfig;

@DisplayName("Tests for AuthorController")
@WebMvcTest({CollectionController.class})
@Import({SecurityConfig.class, AclConfig.class})
public class CollectionControllerTests {

}
