package com.gophagi.nanugi.groupbuying.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;

@Repository
public interface GroupbuyingBoardRepository extends JpaRepository<GroupbuyingBoard, Long> {
	Page<GroupbuyingBoard> findByCategory(Category category, Pageable pageable);
}
