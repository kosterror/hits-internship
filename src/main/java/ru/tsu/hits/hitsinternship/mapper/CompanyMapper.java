package ru.tsu.hits.hitsinternship.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.company.NewCompanyDto;
import ru.tsu.hits.hitsinternship.entity.CompanyEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CompanyMapper {


    CompanyEntity newDtoToEntity(NewCompanyDto dto);

    CompanyDto entityToDto(CompanyEntity companyWishes);
}
