package com.eatnow.search.exchanges;

import java.util.List;

import com.eatnow.search.dtos.ItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSearchResponse {

    private long matches;

    private int pages;

    private int pageSize;

    @NonNull
    private List<ItemDto> items;
}

