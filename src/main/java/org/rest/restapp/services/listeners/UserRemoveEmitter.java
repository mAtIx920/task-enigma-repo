package org.rest.restapp.services.listeners;

import lombok.Builder;

import java.util.List;

@Builder
public record UserRemoveEmitter(List<Integer> removedUserIds) {

}
