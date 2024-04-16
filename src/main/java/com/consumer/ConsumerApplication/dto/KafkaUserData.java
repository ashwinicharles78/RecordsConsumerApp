package com.consumer.ConsumerApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.consumer.ConsumerApplication.dto.Roles;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaUserData {
    private String firstName;
    private String lastName;
    private String email;
    private List<Roles> roles;
}
