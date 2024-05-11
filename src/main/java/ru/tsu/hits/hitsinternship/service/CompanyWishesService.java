package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.companyWishes.CompanyWishDto;
import ru.tsu.hits.hitsinternship.dto.companyWishes.NewCompanyWishDto;
import ru.tsu.hits.hitsinternship.entity.CompanyWishesEntity;
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

    public CompanyWishDto createCompanyWish(NewCompanyWishDto newCompanyWishDto) {

        CompanyWishesEntity companyWishes = companyWishesMapper.newDtoToEntity(newCompanyWishDto);

        companyWishesRepository.save(companyWishes);
        return companyWishesMapper.entityToDto(companyWishes);
    }

    public void deleteCompanyWish(UUID companyWishId) {
        if (!companyWishesRepository.existsById(companyWishId)) {
            throw new NotFoundException("");
        }
        companyWishesRepository.deleteById(companyWishId);
    }

    public CompanyWishDto updateCompanyWish(UUID companyWishId, NewCompanyWishDto newCompanyWishDto) {

        var companyWish = companyWishesRepository.findById(companyWishId)
                .orElseThrow(() -> new NotFoundException("Пожелания компании с таким id не существует"));

        var company = companyRepository.findById(newCompanyWishDto.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Компании с таким id не существует"));

        var programLanguage = programLanguageRepository.findById(newCompanyWishDto.getProgramLanguageId())
                .orElseThrow(() -> new NotFoundException("Языка программирования с таким id не существует"));

        var speciality = specialityRepository.findById(newCompanyWishDto.getSpecialityId())
                .orElseThrow(() -> new NotFoundException("Специальности с таким id не существует"));

        companyWish.setCompany(company);
        companyWish.setProgramLanguage(programLanguage);
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
}
