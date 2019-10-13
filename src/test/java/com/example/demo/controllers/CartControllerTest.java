package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

        Item i = new Item();
        i.setId(1L);
        i.setPrice(BigDecimal.valueOf(100));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(i));

        User u = new User();
        u.setId(0L);
        u.setUsername("Test");

        Cart c = new Cart();
        c.setId(0L);
        c.setUser(u);
        c.addItem(i);

        u.setCart(c);

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
    }

    @Test
    public void addToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(3);

        final ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(modifyCartRequest);
        assertNotNull(cartResponseEntity);
        assertEquals(200, cartResponseEntity.getStatusCodeValue());

        Cart c = cartResponseEntity.getBody();
        assertEquals(4, c.getItems().size());
        assertEquals(BigDecimal.valueOf(400), c.getTotal());
    }

    @Test
    public void removeFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        final ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(cartResponseEntity);
        assertEquals(200, cartResponseEntity.getStatusCodeValue());

        Cart c = cartResponseEntity.getBody();
        assertEquals(0, c.getItems().size());
        assertEquals(BigDecimal.valueOf(0), c.getTotal());
    }
}
