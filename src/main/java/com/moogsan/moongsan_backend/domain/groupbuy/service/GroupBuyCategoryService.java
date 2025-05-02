package com.moogsan.moongsan_backend.domain.groupbuy.service;

import com.moogsan.moongsan_backend.domain.groupbuy.entity.Category;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuyCategory;
import com.moogsan.moongsan_backend.domain.groupbuy.repository.GroupBuyCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupBuyCategoryService {
    private final GroupBuyCategoryRepository gbcRepo;

    public List<Category> getCategoriesByGroupBuy(Long groupBuyId) {
        return gbcRepo.findByGroupBuyId(groupBuyId).stream()
                .map(GroupBuyCategory::getCategory)
                .collect(Collectors.toList());
    }

    public List<GroupBuy> getGroupBuysByCategory(Long categoryId) {
        return gbcRepo.findByCategoryId(categoryId).stream()
                .map(GroupBuyCategory::getGroupBuy)
                .collect(Collectors.toList());
    }
}
