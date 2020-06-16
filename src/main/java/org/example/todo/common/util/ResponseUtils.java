package org.example.todo.common.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ResponseUtils {

	//TODO: Investigate if ResponseContainer<T> class can be entirely removed now
	private static final ModelMapper modelMapper = new ModelMapper();

	static {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
	}

	private ResponseUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static <R, T, D> D convertToDtoResponseContainer(Page<R> page, Class<T> dtoClazz, Class<D> responseClazz) {
		List<R> elements = page.getContent();
		List<T> dtoElements = convertToDtoList(elements, dtoClazz);
		ResponseContainer<T> responseContainer = new ResponseContainer<>(true, null, dtoElements);
		responseContainer.setTotalElements(page.getTotalElements());
		responseContainer.setTotalPages(page.getTotalPages());
		responseContainer.setLast(page.isLast());
		responseContainer.setPage(page.getPageable().getPageNumber());
		responseContainer.setPageSize(page.getPageable().getPageSize());

		return convertToDto(responseContainer, responseClazz);
	}

	public static <R, T, D> D convertToDtoResponseContainer(List<R> elements, Class<T> dtoClazz, Class<D> responseClazz) {
		List<T> dtoElements = convertToDtoList(elements, dtoClazz);
		ResponseContainer<T> responseContainer = new ResponseContainer<>(true, null, dtoElements);
		responseContainer.setTotalElements(dtoElements.size());
		responseContainer.setTotalPages(1L);
		responseContainer.setLast(true);
		responseContainer.setPage(0L);

		return convertToDto(responseContainer, responseClazz);
	}

	public static <R, T, D> D convertToDtoResponseContainer(R element, Class<T> clazz, Class<D> responseClazz) {
		List<T> dtoElements = Collections.singletonList(convertToDto(element, clazz));
		ResponseContainer<T> responseContainer = new ResponseContainer<>(true, null, dtoElements);
		responseContainer.setTotalElements(dtoElements.size());
		responseContainer.setTotalPages(1L);
		responseContainer.setLast(true);
		responseContainer.setPage(0L);

		return convertToDto(responseContainer, responseClazz);
	}

	public static <R, T> List<T> convertToDtoList(List<R> objects, Class<T> clazz) {
		return objects.stream()
				.map(obj -> convertToDto(obj, clazz))
				.collect(Collectors.toList());
	}

	public static <R, T> T convertToDto(R object, Class<T> clazz) {
		return modelMapper.map(object, clazz);
	}
}
