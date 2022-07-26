package com.ebin.eatnow.repositories.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.ebin.eatnow.entities.Item;
import com.ebin.eatnow.repositories.ItemRepository;

@Repository
public class ItemRepositoryDummy implements ItemRepository {

    private HashMap<String, Item> items;

    public ItemRepositoryDummy()
    {
        items = new HashMap<>();

        Item i1 = Item.builder()
                    .id("i1")
                    .restaurantId("r1")
                    .itemIndex(1)
                    .name("i1")
                    .restaurantName("r1")
                    .category("")
                    .price(100.0)
                    .build();
        Item i2 = Item.builder()
                    .id("i2")
                    .restaurantId("r1")
                    .itemIndex(2)
                    .name("i2")
                    .restaurantName("r1")
                    .category("")
                    .price(100.0)
                    .build();
        Item i3 = Item.builder()
                    .id("i3")
                    .restaurantId("r2")
                    .itemIndex(1)
                    .name("i3")
                    .restaurantName("r2")
                    .category("")
                    .price(100.0)
                    .build();
        Item i4 = Item.builder()
                    .id("i4")
                    .restaurantId("r2")
                    .itemIndex(2)
                    .name("i4")
                    .restaurantName("r2")
                    .category("")
                    .price(100.0)
                    .build();

        items.put(i1.getId(), i1);
        items.put(i2.getId(), i2);
        items.put(i3.getId(), i3);
        items.put(i4.getId(), i4);
    }

    public Item findById(String id)
    {
        return Optional.ofNullable(items.get(id))
                .orElseThrow(RuntimeException::new);
    }

    public List<Item> findById(List<String> ids)
    {
        return new ArrayList<Item>();
    }

    public List<Item> findByRestaurantId(String restaurantId)
    {
        List<Item> itemList = items.values().stream().filter(
                (i) -> (
                    i.getRestaurantId().equals(restaurantId)
                )
            ).collect(Collectors.toList());

        return itemList;
    }

    public Item findByRestaurantIdAndIndex(String restaurantId, int itemIndex)
    {
        Item item = items.values().stream().filter(
                (i) -> (
                    i.getRestaurantId().equals(restaurantId)
                    && i.getItemIndex().equals(itemIndex)
                )
            ).findFirst()
            .orElseThrow(RuntimeException::new);

        return item;
    }

    public boolean existsById(String id)
    {
        return items.containsKey(id);
    }

    public Item create(Item item)
    {
        item.setId(
            String.valueOf(items.keySet().stream().count() + 1)
        );
        item.setItemIndex(
            (int)(items.values().stream().filter(
                (i)->(
                    i.getRestaurantId().equals(item.getRestaurantId())
                ))
                .count() + 1)
        );

        return save(item);
    }

    public Item update(Item item)
    {
        Item old = items.values().stream().filter(
            (i) -> (
                i.getRestaurantId().equals(item.getRestaurantId()) &&
                i.getItemIndex().equals(item.getItemIndex())
            )
        ).findFirst()
        .orElseThrow(NoSuchElementException::new);
        item.setId(old.getId());
        return save(item);
    }

    public boolean delete(String restaurantId, int itemIndex)
    {
        Item old = items.values().stream().filter(
            (i) -> (
                i.getRestaurantId().equals(restaurantId) &&
                i.getItemIndex().equals(itemIndex)
            )
        ).findFirst()
        .orElseThrow(RuntimeException::new);
        items.remove(old.getId());
        return true;
    }

    private Item save(Item item)
    {
        items.put(item.getId(), item);
        return item;
    }
}
