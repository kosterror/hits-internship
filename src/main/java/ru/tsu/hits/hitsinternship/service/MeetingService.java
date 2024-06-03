package ru.tsu.hits.hitsinternship.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.dto.meeting.MeetingDto;
import ru.tsu.hits.hitsinternship.dto.meeting.MeetingsGroupedByNumber;
import ru.tsu.hits.hitsinternship.dto.meeting.NewMeetingDto;
import ru.tsu.hits.hitsinternship.entity.MeetingEntity;
import ru.tsu.hits.hitsinternship.entity.PairNumber;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.mapper.MeetingMapper;
import ru.tsu.hits.hitsinternship.repository.MeetingRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingMapper meetingMapper;
    private final GroupService groupService;
    private final CompanyService companyService;

    public MeetingDto createMeeting(NewMeetingDto newMeetingDto) {
        var meeting = meetingMapper.toEntity(newMeetingDto);

        var groups = groupService.getGroups(newMeetingDto.getGroupIds());
        meeting.setGroups(groups);

        var company = companyService.findCompanyById(newMeetingDto.getCompanyId());
        meeting.setCompany(company);

        meeting.setDayOfWeek(DayOfWeek.from(newMeetingDto.getDate().getDayOfWeek()));

        meeting = meetingRepository.save(meeting);

        return meetingMapper.toDto(meeting);
    }

    public MeetingDto updateMeeting(UUID id, NewMeetingDto newMeetingDto) {
        var meeting = getMeeting(id);

        meeting.setPairNumber(newMeetingDto.getPairNumber());
        meeting.setDate(newMeetingDto.getDate());
        meeting.setAudience(newMeetingDto.getAudience());

        var groups = groupService.getGroups(newMeetingDto.getGroupIds());
        meeting.setGroups(groups);

        var company = companyService.findCompanyById(newMeetingDto.getCompanyId());
        meeting.setCompany(company);

        meeting = meetingRepository.save(meeting);

        return meetingMapper.toDto(meeting);
    }

    public void deleteMeeting(UUID id) {
        meetingRepository.deleteById(id);
    }

    private MeetingEntity getMeeting(UUID id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meeting with id '%s' not found".formatted(id)));
    }

    //🙃🙃🙃
    public List<MeetingsGroupedByNumber> getMeetings(List<UUID> groupIds,
                                                     LocalDate from,
                                                     LocalDate to) {
        return List.of(
                getMeetingsGroupedByNumber(groupIds, from, to, PairNumber.FIRST),
                getMeetingsGroupedByNumber(groupIds, from, to, PairNumber.SECOND),
                getMeetingsGroupedByNumber(groupIds, from, to, PairNumber.THIRD),
                getMeetingsGroupedByNumber(groupIds, from, to, PairNumber.FOURTH),
                getMeetingsGroupedByNumber(groupIds, from, to, PairNumber.FIFTH),
                getMeetingsGroupedByNumber(groupIds, from, to, PairNumber.SIXTH),
                getMeetingsGroupedByNumber(groupIds, from, to, PairNumber.SEVENTH)
        );
    }

    private MeetingsGroupedByNumber getMeetingsGroupedByNumber(List<UUID> groupIds,
                                                               LocalDate from,
                                                               LocalDate to,
                                                               PairNumber pairNumber) {
        return new MeetingsGroupedByNumber(
                pairNumber,
                getMeetings(groupIds, from, to, pairNumber, DayOfWeek.MONDAY),
                getMeetings(groupIds, from, to, pairNumber, DayOfWeek.TUESDAY),
                getMeetings(groupIds, from, to, pairNumber, DayOfWeek.WEDNESDAY),
                getMeetings(groupIds, from, to, pairNumber, DayOfWeek.THURSDAY),
                getMeetings(groupIds, from, to, pairNumber, DayOfWeek.FRIDAY),
                getMeetings(groupIds, from, to, pairNumber, DayOfWeek.SATURDAY)
        );
    }

    private List<MeetingDto> getMeetings(List<UUID> groupIds,
                                         LocalDate from,
                                         LocalDate to,
                                         PairNumber pairNumber,
                                         DayOfWeek dayOfWeek) {
        Specification<MeetingEntity> spec = (root, query, cb) -> {
            Predicate inGroups = root.get("groups").get("id").in(groupIds);
            Predicate betweenDates = cb.between(root.get("date"), from, to);
            Predicate byPairNumber = cb.equal(root.get("pairNumber"), pairNumber);
            Predicate byDayOfWeek = cb.equal(root.get("dayOfWeek"), dayOfWeek);
            return cb.and(inGroups, betweenDates, byPairNumber, byDayOfWeek);
        };

        List<MeetingEntity> meetings = meetingRepository.findAll(spec);

        return meetings.stream()
                .map(meetingMapper::toDto)
                .toList();
    }
}
