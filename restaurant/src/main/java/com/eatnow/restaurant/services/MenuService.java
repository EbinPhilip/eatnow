package com.eatnow.restaurant.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatnow.restaurant.dtos.ItemDto;
import com.eatnow.restaurant.dtos.MenuDto;
import com.eatnow.restaurant.entities.Item;
import com.eatnow.restaurant.entities.Menu;
import com.eatnow.restaurant.repositories.MenuRepository;
import com.eatnow.restaurant.utils.Location;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantService restaurantService;

    public MenuDto getMenuByRestaurantId(String restaurantId) {

        Menu menu = menuRepository.findByRestaurantId(restaurantId);
        List<ItemDto> items = menu.getItems()
                .stream()
                .map((i)->{
                    return itemToDto(i);
                }).collect(Collectors.toList());
        
        return MenuDto.builder()
                    .restaurantId(restaurantId)
                    .restaurantName(menu.getRestaurantName())
                    .items(items)
                    .build();
    }

    public List<ItemDto> checkServiceabilityAndFetchItems(String restaurantId,
            Location location, Set<Integer> itemIndices) {

        if (!restaurantService.isRestaurantServiceable(restaurantId, location)) {
            throw new RuntimeException();
        }

        return menuRepository.findByRestaurantIdAndIndices(restaurantId, itemIndices)
                .stream().map(
                        (i) -> {
                            return itemToDto(i);
                        })
                .collect(Collectors.toList());
    }

    public ItemDto getItemByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        Item item = menuRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        ItemDto dto = itemToDto(item);
        dto.setRestaurantId(restaurantId);
        dto.setAvailable(item.isAvailable());
        return dto;
    }

    public ItemDto createItem(String restaurantId, ItemDto dto) {

        Item item = dtoToItem(dto);
        menuRepository.createItem(restaurantId, item);
        return itemToDto(item);
    }

    public ItemDto updateItem(String restaurantId, int itemIndex, ItemDto dto) {

        dto.setItemIndex(itemIndex);
        Item item = dtoToItem(dto);
        item = menuRepository.updateItem(restaurantId, item);
        return itemToDto(item);
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        return menuRepository.deleteItem(restaurantId, itemIndex);
    }

    public boolean setItemAvailability(String restaurantId, int itemIndex, boolean isAvailable) {

        Item item = menuRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        item.setAvailable(isAvailable);
        return menuRepository.updateItem(restaurantId, item).isAvailable();
    }

    public boolean getItemAvailability(String restaurantId, int itemIndex) {
        Item item = menuRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        return item.isAvailable();
    }

    private Item dtoToItem(ItemDto dto) {
        Item item = Item.builder()
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

    private ItemDto itemToDto(Item item) {
        ItemDto dto = ItemDto.builder()
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
