package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Collection;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CollectionDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.*;
import ru.otus.hw.security.acl.AclConfig;
import ru.otus.hw.security.configs.SecurityConfig;
import ru.otus.hw.security.repositories.LibraryRoleRepository;
import ru.otus.hw.security.repositories.LibraryUserRepository;
import ru.otus.hw.security.services.*;
import ru.otus.hw.services.*;

import javax.sql.DataSource;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.List;

@DisplayName("Tests for Collection Controller")
@WebMvcTest
@Import({
        CollectionController.class,
        CollectionServiceImpl.class,
        AdminController.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class,
        CommentServiceImpl.class,
        AclWrapperServiceImpl.class,
        LibraryRoleServiceImpl.class,
        LibraryUserServiceImpl.class,
        BookServiceImpl.class,
        SecurityConfig.class,
        AclConfig.class
})
public class CollectionControllerTests {
    private final List<AuthorDto> testAuthors = List.of(
            new AuthorDto(1L, "Author_1"),
            new AuthorDto(2L, "Author_2")
    );

    private final List<GenreDto> testGenres = List.of(
            new GenreDto(1L, "Genre_1"),
            new GenreDto(2L, "Genre_2")
    );

    private final List<BookDto> testBooks = List.of(
            new BookDto(1L, "Title_1",
                    testAuthors.get(0),
                    testGenres),
            new BookDto(2L, "Title_2",
                    testAuthors.get(1),
                    testGenres)
    );

    private final List<Collection> testCollection = List.of(
            new Collection(8L, "collection_user_2", "for user 2",
                    testBooks.stream().map(BookDto::toDomainObject).toList()),
            new Collection(9L, "collection_user_2", "for user 2",
                    testBooks.stream().map(BookDto::toDomainObject).toList()),
            new Collection(10L, "collection_user_1", "for user 1",
                    testBooks.stream().map(BookDto::toDomainObject).toList())
    );

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private CollectionRepository collectionRepository;

    @MockitoBean
    private LibraryRoleRepository libraryRoleRepository;

    @MockitoBean
    private LibraryUserRepository libraryUserRepository;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private GenreRepository genreRepository;

    @MockitoBean
    private CommentRepository commentRepository;

    @MockitoBean
    private DataSource dataSource;

    @DisplayName("should return all collections for admin")
    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    public void shouldReturnAllCollectionsForAdmin() throws Exception {
        given(collectionRepository.findAll()).willReturn(testCollection);

        mvc.perform(get("/collections"))
                .andExpect(status().isOk())
                .andExpect(view().name("collection"))
                .andExpect(model().attributeExists("collections"))
                .andExpect(model().attribute("collections", testCollection.stream().map(CollectionDto::fromDomainObject).toList()));
    }

//    @DisplayName("should return collection for user_1")
//    @Test
//    @WithMockUser(
//            username = "user_1",
//            authorities = {"ROLE_USER"}
//    )
//    public void shouldReturnAllCollectionsForUser1() throws Exception {
//        given(collectionRepository.findAll()).willReturn(testCollection);
//
//        List<Collection> expectedCollection = testCollection.stream()
//                .filter(c -> c.getId() != 8L && c.getId() != 9L).toList();
//
//        mvc.perform(get("/collections"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("collection"))
//                .andExpect(model().attributeExists("collections"))
//                .andExpect(model().attribute("collections", expectedCollection.stream().map(CollectionDto::fromDomainObject).toList()));
//    }
}
