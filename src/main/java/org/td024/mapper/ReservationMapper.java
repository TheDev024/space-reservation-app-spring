package org.td024.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.td024.dto.ReservationDto;
import org.td024.entity.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "reserver", source = "reservedBy.username")
    ReservationDto toDto(Reservation reservation);
}
