package com.leverx.learning_management_system.util;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ControllerResponse {
  public static <T> ResponseEntity<List<T>> handleList(List<T> data) {
    return isEmpty(data)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(data);
  }

  public static <T> ResponseEntity<T> handleItemOrNotFound(T data) {
    return isEmpty(data)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(data);
  }
}