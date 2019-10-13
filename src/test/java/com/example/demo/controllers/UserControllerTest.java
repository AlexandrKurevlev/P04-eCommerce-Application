package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserSuccess() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void findByIdSuccess() throws Exception {
        User expectedUser = new User();
        expectedUser.setId(0);
        expectedUser.setUsername("Test");
        expectedUser.setPassword("testpass");
        when(userRepository.findById(0L)).thenReturn(Optional.of(expectedUser));

        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("testpass", u.getPassword());
    }

    @Test
    public void findByUsernameSuccess() throws Exception {
        User expectedUser = new User();
        expectedUser.setId(0);
        expectedUser.setUsername("Test");
        expectedUser.setPassword("testpass");
        when(userRepository.findByUsername("Test")).thenReturn(expectedUser);

        final ResponseEntity<User> response = userController.findByUserName("Test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("testpass", u.getPassword());
    }
}
