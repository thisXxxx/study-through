package team.weilai.studythrough.pojo.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gwj
 * @date 2024/12/4 21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoExam {
    private String name;
    private Long userId;
}
