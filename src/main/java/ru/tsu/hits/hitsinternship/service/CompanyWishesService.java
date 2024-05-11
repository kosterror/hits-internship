package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.companyWishes.CompanyWishDto;
import ru.tsu.hits.hitsinternship.dto.companyWishes.NewCompanyWishDto;
import ru.tsu.hits.hitsinternship.entity.CompanyEntity;
import ru.tsu.hits.hitsinternship.entity.CompanyWishesEntity;
import ru.tsu.hits.hitsinternship.entity.ProgramLanguageEntity;
import ru.tsu.hits.hitsinternship.entity.SpecialityEntity;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.CompanyWishesMapper;
import ru.tsu.hits.hitsinternship.repository.CompanyRepository;
import ru.tsu.hits.hitsinternship.repository.CompanyWishesRepository;
import ru.tsu.hits.hitsinternship.repository.ProgramLanguageRepository;
import ru.tsu.hits.hitsinternship.repository.SpecialityRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyWishesService {

    private final CompanyWishesRepository companyWishesRepository;
    private final CompanyWishesMapper companyWishesMapper;
    private final CompanyRepository companyRepository;
    private final ProgramLanguageRepository programLanguageRepository;
    private final SpecialityRepository specialityRepository;


    public void deleteCompanyWish(UUID companyWishId) {
        if (!companyWishesRepository.existsById(companyWishId)) {
            throw new NotFoundException("");
        }
        companyWishesRepository.deleteById(companyWishId);
    }

    public CompanyWishDto createCompanyWish(NewCompanyWishDto newCompanyWishDto) {

        var company = findCompanyById(newCompanyWishDto.getCompanyId());
        var speciality = findSpecialityById(newCompanyWishDto.getSpecialityId());
        CompanyWishesEntity companyWish = companyWishesMapper.newDtoToEntity(newCompanyWishDto);

        if (newCompanyWishDto.getProgramLanguageId() != null) {
            var programLanguage = findProgramLanguageById(newCompanyWishDto.getProgramLanguageId());
            companyWish.setProgramLanguage(programLanguage);
        }

        companyWish.setCompany(company);
        companyWish.setSpeciality(speciality);

        companyWishesRepository.save(companyWish);
        return companyWishesMapper.entityToDto(companyWish);
    }

    public CompanyWishDto updateCompanyWish(UUID companyWishId, NewCompanyWishDto newCompanyWishDto) {

        var companyWish = companyWishesRepository.findById(companyWishId)
                .orElseThrow(() -> new NotFoundException("Пожелания компании с таким id не существует"));

        var company = findCompanyById(newCompanyWishDto.getCompanyId());

        if (newCompanyWishDto.getProgramLanguageId() != null) {
            var programLanguage = findProgramLanguageById(newCompanyWishDto.getProgramLanguageId());
            companyWish.setProgramLanguage(programLanguage);
        }

        var speciality = findSpecialityById(newCompanyWishDto.getSpecialityId());
        companyWish.setCompany(company);
        companyWish.setSpeciality(speciality);
        companyWish.setInternAmount(newCompanyWishDto.getInternAmount());
        companyWish.setComment(newCompanyWishDto.getComment());

        companyWish = companyWishesRepository.save(companyWish);
        return companyWishesMapper.entityToDto(companyWish);
    }

    public List<CompanyWishDto> getCompanyWishes(UUID companyId) {
        var companyWish = companyWishesRepository.findCompanyWishesEntitiesByCompanyId(companyId);
        return companyWish.stream()
                .map(companyWishesMapper::entityToDto)
                .toList();
    }

    private CompanyEntity findCompanyById(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Компании с таким id не существует"));
    }

    private SpecialityEntity findSpecialityById(UUID specialityId) {
        return specialityRepository.findById(specialityId)
                .orElseThrow(() -> new NotFoundException("Специальности с таким id не существует"));
    }

    private ProgramLanguageEntity findProgramLanguageById(UUID programLanguageId) {
        return programLanguageRepository.findById(programLanguageId)
                .orElseThrow(() -> new NotFoundException("Языка программирования с таким id не существует"));
    }

}
