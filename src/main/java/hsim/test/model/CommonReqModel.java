package hsim.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CommonReqModel {
    private long id;
    private String loginId;
    private String domain;
    private String name;
    private Child parent1;

    private String param1;
    private String param2;
    private List<Child> childs;

}

