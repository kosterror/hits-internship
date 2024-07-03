package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.company.CompanyDto;
import ru.tsu.hits.hitsinternship.dto.company.NewCompanyDto;
import ru.tsu.hits.hitsinternship.entity.CompanyEntity;
import ru.tsu.hits.hitsinternship.entity.Role;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
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
    private final UserService userService;

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
        return companyRepository.findAllByOrderByNameAsc()
                .stream()
                .map(companyMapper::entityToDto)
                .toList();
    }

    public CompanyEntity findCompanyById(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with id " + companyId + " not found"));
    }

    public CompanyDto updateCompanyCurator(UUID companyId, UUID curatorId) {
        var company = getCompanyEntity(companyId);

        if (curatorId == null) {
            company.setCurator(null);
        } else {
            var curator = userService.getUserEntityById(curatorId);
            if (curator.getRoles().size() == 1 && curator.getRoles().getFirst().equals(Role.STUDENT)) {
                throw new ConflictException("Студент не может стать куратором компании");
            }

            company.setCurator(curator);
        }
        company = companyRepository.save(company);
        return companyMapper.entityToDto(company);
    }

    public CompanyDto updateCompanyOfficer(UUID companyId, UUID officerId) {
        var company = getCompanyEntity(companyId);

        if (officerId == null) {
            company.setOfficer(null);
        } else {
            var officer = userService.getUserEntityById(officerId);
            var companies = companyRepository.findAllByOfficer(officer);

            if (companies.size() == 1 && !companies.getFirst().getId().equals(companyId) || companies.size() > 1) {
                throw new ConflictException(
                        "%s уже является представителем другой компании".formatted(officer.getFullName())
                );
            }

            if (officer.getRoles().size() == 1 && officer.getRoles().getFirst().equals(Role.STUDENT)) {
                throw new ConflictException("Студент не может стать представителем компании");
            }

            company.setOfficer(officer);
        }
        company = companyRepository.save(company);
        return companyMapper.entityToDto(company);
    }

    public CompanyEntity getCompanyEntity(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Компания с id '%s' не найдена".formatted(companyId)));
    }
}
