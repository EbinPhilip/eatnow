package com.ebin.eatnow.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebin.eatnow.dtos.ItemDto;
import com.ebin.eatnow.entities.Item;
import com.ebin.eatnow.repositories.ItemRepository;
import com.ebin.eatnow.utils.Location;

@Service
public class MenuService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RestaurantService restaurantService;

    public List<ItemDto> getItemsByRestaurantId(String restaurantId) {
        restaurantService.getRestaurantbyId(restaurantId);
        return itemRepository.findByRestaurantId(restaurantId).stream().map(
                (i) -> {
                    return itemToDto(i);
                }).collect(Collectors.toList());
    }

    public List<ItemDto> checkServiceabilityAndFetchItems(String restaurantId,
            Location location, Set<Integer> itemIndices) {

        if (!restaurantService.isRestaurantServiceable(restaurantId, location)) {
            throw new RuntimeException();
        }

        return itemRepository.findByRestaurantIdAndIndices(restaurantId, itemIndices)
                .stream().map(
                        (i) -> {
                            return itemToDto(i);
                        })
                .collect(Collectors.toList());
    }

    public ItemDto getItemByRestaurantIdAndIndex(String restaurantId, int itemIndex) {
        Item item = itemRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        ItemDto dto = itemToDto(item);
        dto.setAvailable(item.isAvailable());
        return dto;
    }

    public ItemDto createItem(ItemDto dto) {
        restaurantService.getRestaurantbyId(dto.getRestaurantId());
        Item item = dtoToItem(dto);
        itemRepository.create(item);
        return itemToDto(item);
    }

    public ItemDto updateItem(ItemDto dto) {
        Item old = itemRepository
                .findByRestaurantIdAndIndex(dto.getRestaurantId(), dto.getItemIndex());
        Item item = dtoToItem(dto);
        item.setRestaurantName(old.getName());
        item = itemRepository.update(item);
        return itemToDto(item);
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {
        return itemRepository.delete(restaurantId, itemIndex);
    }

    public boolean setItemAvailability(String restaurantId, int itemIndex, boolean isAvailable) {
        Item item = itemRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        item.setAvailable(isAvailable);
        return itemRepository.update(item).isAvailable();
    }

    public boolean getItemAvailability(String restaurantId, int itemIndex) {
        Item item = itemRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        return item.isAvailable();
    }

    private Item dtoToItem(ItemDto dto) {
        Item item = Item.builder()
                .restaurantId(dto.getRestaurantId())
                .itemIndex(dto.getItemIndex())
                .name(dto.getName())
                .restaurantName("")
                .category(dto.getCategory())
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
                .restaurantId(item.getRestaurantId())
                .itemIndex(item.getItemIndex())
                .name(item.getName())
                .restaurantName(item.getRestaurantName())
                .category(item.getCategory())
                .price(item.getPrice())
                .description(item.getDescription())
                .tags(item.getTags())
                .build();
        return dto;
    }
}
