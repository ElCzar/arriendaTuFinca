package com.gossip.arrienda_tu_finca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String phone;
    private boolean isHost;
    private boolean isRenter;
    private int imageId;
    private boolean rating;
}
