package ru.tsu.hits.hitsinternship.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.mapper.ChangePracticeApplicationMapper;
import ru.tsu.hits.hitsinternship.repository.ChangePracticeApplicationRepository;

@Service
@RequiredArgsConstructor
public class ChangePracticeApplicationService {

    private final ChangePracticeApplicationRepository changePracticeApplicationRepository;
    private final ChangePracticeApplicationMapper changePracticeApplicationMapper;

//    public ChangePracticeApplicationDto deleteChangePracticeApplication(UUID positionId, UUID userId) {
//
//
//    }
//
//    public ChangePracticeApplicationDto takeApplicationToWork(UUID positionId, UUID checkingEmployeeId) {
//
//    }
//
//    public ChangePracticeApplicationDto createChangePracticeApplication(NewApplicationDto newApplicationDto, UUID authorId) {
//
//    }
//
//    public ChangePracticeApplicationDto makeDecisionOnApplication(@PathVariable UUID positionId, UUID checkingEmployeeId) {
//
//    }
//
//    public ChangePracticeApplicationDto getChangePracticeApplications() {
//
//    }

}
