package ru.tsu.hits.hitsinternship.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {

    private UUID id;

    private String name;

}
