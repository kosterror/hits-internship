package ru.tsu.hits.hitsinternship.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.companyWishes.CompanyWishDto;
import ru.tsu.hits.hitsinternship.dto.companyWishes.NewCompanyWishDto;
import ru.tsu.hits.hitsinternship.entity.CompanyWishesEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CompanyWishesMapper {

    CompanyWishesEntity newDtoToEntity(NewCompanyWishDto dto);

    CompanyWishDto entityToDto(CompanyWishesEntity companyWishes);
}