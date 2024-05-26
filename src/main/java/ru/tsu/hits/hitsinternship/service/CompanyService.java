package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.company.NewCompanyDto;
import ru.tsu.hits.hitsinternship.entity.CompanyEntity;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.CompanyMapper;
import ru.tsu.hits.hitsinternship.repository.CompanyRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyDto createCompany(NewCompanyDto newCompanyDto) {
        CompanyEntity company = companyMapper.newDtoToEntity(newCompanyDto);
        return companyMapper.entityToDto(companyRepository.save(company));
    }

    public void deleteCompany(UUID companyId) {
        companyRepository.deleteById(companyId);
    }

    public CompanyDto updateCompany(UUID companyId, NewCompanyDto newCompanyDto) {

        CompanyEntity company = findCompanyById(companyId);
        company.setName(newCompanyDto.getName());
        company.setIsVisible(newCompanyDto.getIsVisible());
        company.setWebsiteLink(newCompanyDto.getWebsiteLink());

        return companyMapper.entityToDto(companyRepository.save(company));
    }

    public List<CompanyDto> getCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(companyMapper::entityToDto)
                .toList();
    }

    public CompanyEntity findCompanyById(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id " + companyId + " not found"));
    }

}
