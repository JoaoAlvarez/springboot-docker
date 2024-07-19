package com.projeto.api.repository;

import com.projeto.api.domain.Role;
import com.projeto.api.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "pass";
    public static final String ROLE_SELECT = "ROLE_SELECT";
    public static final String ROLE_INSERT = "ROLE_INSERT";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void before() {
        // Prepare roles
        Role role1 = new Role();
        role1.setName(ROLE_SELECT);
        Role role2 = new Role();
        role2.setName(ROLE_INSERT);

        testEntityManager.persist(role1);
        testEntityManager.persist(role2);

        // Prepare user
        Usuario user = new Usuario();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
        user.setRoles(roles);

        testEntityManager.persist(user);
        testEntityManager.flush();
    }

    @Test
    @DisplayName("Should get User successfully from DB")
    void findByUsername_success() {
        //when
        Usuario finded = usuarioRepository.findByUsername(USERNAME);

        //then
        assertThat(finded).isNotNull();
        assertThat(finded.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    @DisplayName("Should not get User successfully from DB")
    void findByUsername_failure() {
        //given
        String WRONG_USERNAME = "ZZZ";

        //when
        Usuario finded = usuarioRepository.findByUsername(WRONG_USERNAME);

        //then
        assertThat(finded).isNull();
    }

    @Test
    @DisplayName("Should get User with roles successfully from DB")
    void findByUsernameFetchRoles_success() {
        //when
        Usuario finded = usuarioRepository.findByUsernameFetchRoles(USERNAME);

        //then
        assertThat(finded).isNotNull();
        assertThat(finded.getUsername()).isEqualTo(USERNAME);
        assertThat(finded.getRoles()).isNotEmpty();
        assertThat(finded.getRoles()).extracting("name").contains(ROLE_INSERT, ROLE_SELECT);
    }

    @Test
    @DisplayName("Should not get User with roles successfully from DB")
    void findByUsernameFetchRoles_failure() {
        //given
        String WRONG_USERNAME = "ZZZ";

        //when
        Usuario finded = usuarioRepository.findByUsernameFetchRoles(WRONG_USERNAME);

        //then
        assertThat(finded).isNull();
    }
}
