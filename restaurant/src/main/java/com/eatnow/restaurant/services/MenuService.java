package com.eatnow.restaurant.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eatnow.restaurant.dtos.Item;
import com.eatnow.restaurant.dtos.Menu;
import com.eatnow.restaurant.dtos.Restaurant;
import com.eatnow.restaurant.dtos.RestaurantItemsInfo;
import com.eatnow.restaurant.entities.ItemElasticEntity;
import com.eatnow.restaurant.entities.ItemEntity;
import com.eatnow.restaurant.entities.MenuEntity;
import com.eatnow.restaurant.repositories.MenuRepository;
import com.eatnow.restaurant.repositories.elasticsearch.ItemElasticRepository;
import com.eatnow.restaurant.utils.Location;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ItemElasticRepository itemElasticRepository;

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

    public RestaurantItemsInfo checkServiceabilityAndFetchItems(String restaurantId,
            Location location, Set<Integer> itemIndices) {

        if (!restaurantService.isRestaurantServiceable(restaurantId, location)) {

            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                    "Restaurant not serviceable");
        }
    
        Restaurant restaurant = restaurantService.getRestaurantbyId(restaurantId);
        List<Item> items = menuRepository.findByRestaurantIdAndIndices(restaurantId, itemIndices)
                .stream().map(
                        (i) -> {
                            if (i.isAvailable()) {
                                return itemToDto(i);
                            } else {
                                throw new ResponseStatusException(
                                        HttpStatus.PRECONDITION_FAILED,
                                        String.format("Item: %s is not available currently", i.getName()));
                            }
                        })
                .collect(Collectors.toList());
        if (items.size() != itemIndices.size()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "some of the requested items could not be found");
        }

        RestaurantItemsInfo details = RestaurantItemsInfo
                .builder()
                .restaurantName(restaurant.getName())
                .items(items)
                .build();

        return details;
    }

    public Item getItemByRestaurantIdAndIndex(String restaurantId, int itemIndex) {

        ItemEntity item = menuRepository.findByRestaurantIdAndIndex(restaurantId, itemIndex);
        Item dto = itemToDto(item);
        dto.setAvailable(item.isAvailable());
        return dto;
    }

    public Item createItem(String restaurantId, Item dto) {

        ItemEntity item = dtoToItem(dto);
        item = menuRepository.createItem(restaurantId, item);

        Restaurant restaurant = restaurantService.getRestaurantbyId(restaurantId);
        Location location = restaurantService.getRestaurantLocation(restaurantId);
        ItemElasticEntity entity = ItemElasticEntity.builder()
                .name(item.getName())
                .itemIndex(item.getItemIndex())
                .restaurantId(restaurantId)
                .restaurantName(restaurant.getName())
                .location(new GeoPoint(location.getLatitude(), location.getLongitude()))
                .price(item.getPrice())
                .description(item.getDescription())
                .tags(String.join(" ", item.getTags()))
                .available(item.isAvailable())
                .build();
        itemElasticRepository.save(entity);

        return itemToDto(item);
    }

    public Item updateItem(String restaurantId, int itemIndex, Item dto) {

        dto.setItemIndex(itemIndex);
        ItemEntity item = dtoToItem(dto);
        item = menuRepository.updateItem(restaurantId, item);

        Restaurant restaurant = restaurantService.getRestaurantbyId(restaurantId);
        ItemElasticEntity currentEntity = itemElasticRepository
                .findByRestaurantIdAndItemIndex(restaurantId, item.getItemIndex())
                .orElseThrow(()->(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "item not found")));
        ItemElasticEntity entity = ItemElasticEntity.builder()
                .id(currentEntity.getId())
                .name(item.getName())
                .itemIndex(item.getItemIndex())
                .restaurantId(restaurantId)
                .restaurantName(restaurant.getName())
                .location(currentEntity.getLocation())
                .price(item.getPrice())
                .description(item.getDescription())
                .tags(String.join(" ", item.getTags()))
                .available(item.isAvailable())
                .build();
        itemElasticRepository.save(entity);

        return itemToDto(item);
    }

    public boolean deleteItem(String restaurantId, int itemIndex) {

        itemElasticRepository.deleteByRestaurantIdAndItemIndex(restaurantId, itemIndex);
        return menuRepository.deleteItem(restaurantId, itemIndex);
    }

    public boolean setItemAvailability(String restaurantId, int itemIndex, boolean isAvailable) {

        Item item = getItemByRestaurantIdAndIndex(restaurantId, itemIndex);
        item.setAvailable(isAvailable);
        return updateItem(restaurantId, itemIndex, item).getAvailable();
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
