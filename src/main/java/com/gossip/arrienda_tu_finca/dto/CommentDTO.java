package com.gossip.arrienda_tu_finca.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private String comment;
    private int rating;
    private String authorEmail;
}
