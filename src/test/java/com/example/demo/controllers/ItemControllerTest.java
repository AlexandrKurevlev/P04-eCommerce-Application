package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        Item i = new Item();
        i.setId(1L);
        i.setPrice(BigDecimal.valueOf(100));
        i.setName("Test");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(i));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(i));
        when(itemRepository.findByName("Test")).thenReturn(Collections.singletonList(i));
    }

    @Test
    public void getItems() {
        final ResponseEntity<List<Item>> items = itemController.getItems();
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
        assertEquals(1, items.getBody().size());
    }

    @Test
    public void getItemById() {
        final ResponseEntity<Item> items = itemController.getItemById(1L);
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
    }

    @Test
    public void getItemsByName() {
        final ResponseEntity<List<Item>> items = itemController.getItemsByName("Test");
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
        assertEquals(1, items.getBody().size());
    }

}
