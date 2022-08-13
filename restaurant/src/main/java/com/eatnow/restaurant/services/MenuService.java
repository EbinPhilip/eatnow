package com.eatnow.restaurant.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.restaurant.dtos.Item;
import com.eatnow.restaurant.dtos.Menu;
import com.eatnow.restaurant.entities.ItemEntity;
import com.eatnow.restaurant.entities.MenuEntity;
import com.eatnow.restaurant.repositories.MenuRepository;
import com.eatnow.restaurant.utils.Location;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantService restaurantService;

    public Menu getMenuByRestaurantId(String restaurantId) {

        MenuEntity menu = menuRepository.findByRestaurantId(restaurantId);
        List<Item> items = menu.getItems()
                .stream()
                .map((i) -> {
                    return itemToDto(i);
                }).collect(Collectors.toList());

        return Menu.builder()
                .restaurantId(restaurantId)
                .restaurantName(menu.getRestaurantName())
                .items(items)
                .build();
    }

    public List<Item> checkServiceabilityAndFetchItems(String restaurantId,
            Location location, Set<Integer> itemIndices) {

        if (!restaurantService.isRestaurantServiceable(restaurantId, location)) {

            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                    "Restaurant not serviceable");
        }

        return menuRepository.findByRestaurantIdAndIndices(restaurantId, itemIndices)
                .stream().map(
                        (i) -> {
                            return itemToDto(i);
                        })
                .collect(Collectors.toList());
    }

    public Item getItemByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        ItemEntity item = menuRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        Item dto = itemToDto(item);
        dto.setRestaurantId(restaurantId);
        dto.setAvailable(item.isAvailable());
        return dto;
    }

    public Item createItem(String restaurantId, Item dto) {

        ItemEntity item = dtoToItem(dto);
        menuRepository.createItem(restaurantId, item);
        return itemToDto(item);
    }

    public Item updateItem(String restaurantId, int itemIndex, Item dto) {

        dto.setItemIndex(itemIndex);
        ItemEntity item = dtoToItem(dto);
        item = menuRepository.updateItem(restaurantId, item);
        return itemToDto(item);
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        return menuRepository.deleteItem(restaurantId, itemIndex);
    }

    public boolean setItemAvailability(String restaurantId, int itemIndex, boolean isAvailable) {

        ItemEntity item = menuRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        item.setAvailable(isAvailable);
        return menuRepository.updateItem(restaurantId, item).isAvailable();
    }

    public boolean getItemAvailability(String restaurantId, int itemIndex) {
        ItemEntity item = menuRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        return item.isAvailable();
    }

    private ItemEntity dtoToItem(Item dto) {
        ItemEntity item = ItemEntity.builder()
                .name(dto.getName())
                .itemIndex(dto.getItemIndex())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .tags(dto.getTags())
                .build();
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return item;
    }

    private Item itemToDto(ItemEntity item) {
        Item dto = Item.builder()
                .itemIndex(item.getItemIndex())
                .name(item.getName())
                .price(item.getPrice())
                .description(item.getDescription())
                .tags(item.getTags())
                .available(item.isAvailable())
                .build();
        return dto;
    }
}
