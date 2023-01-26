package com.gophagi.nanugi.groupbuying.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingThumbnailDTO;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardCommandService;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GroupbuyingController {
	private final GroupbuyingBoardCommandService commandService;
	private final GroupbuyingBoardQueryService queryService;

	public GroupbuyingController(GroupbuyingBoardCommandService commandService,
		GroupbuyingBoardQueryService queryService) {
		this.commandService = commandService;
		this.queryService = queryService;
	}

	@PostMapping(value = "${groupbuying.create-url}",
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public void create(@RequestPart GroupbuyingBoardDTO dto,
		@RequestPart List<MultipartFile> files,
		@CookieValue String token) {

		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.create(dto, files, userId);
	}

	@PostMapping(value = "${groupbuying.update-url}",
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public void update(@RequestPart GroupbuyingBoardDTO dto,
		@RequestPart(required = false) List<MultipartFile> files,
		@RequestPart(required = false) List<Long> deletePhotoIdList,
		@CookieValue String token) {

		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.update(dto, files, deletePhotoIdList, userId);

	}

	@PostMapping("${groupbuying.order-url}/{id}")
	public void order(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.order(userId, id);
	}

	@PostMapping("${groupbuying.cancel-url}/{id}")
	public void cancel(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.cancel(userId, id);
	}

	@PostMapping("${groupbuying.remove-url}/{id}")
	public List<Long> remove(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		return commandService.remove(userId, id);
	}

	@GetMapping("${groupbuying.retrieve-url}/{id}")
	public GroupbuyingBoardDTO retrieve(@PathVariable("id") Long id) {
		commandService.updateView(id);
		return queryService.retrieve(id);
	}

	@GetMapping("${groupbuying.retrieve-list-url}/{page}")
	public Page<GroupbuyingThumbnailDTO> retrieveList(@PathVariable("page") int page) {
		return queryService.retrieveList(page);
	}

	@GetMapping("${groupbuying.retrieve-url}")
	public Page<GroupbuyingThumbnailDTO> retrieveCategoryList(Category category, int page) {
		return queryService.retrieveCategoryList(category, page);
	}
}
