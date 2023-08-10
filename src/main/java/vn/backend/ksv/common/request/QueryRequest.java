package vn.backend.ksv.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.backend.ksv.common.anotation.NotNullAndEmpty;

import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:00 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    @NotNullAndEmpty
    private String what; //which table query?
    private Integer type; //inner condition

    // Status
    private Integer status;
    private String statusField;

    private Integer from; //start index
    private Integer limit; //limit the record
    private String filter; //filter by
    private String filterValue; // filter value
    private String search; //search by
    private String searchValue; // search value
    private Integer count; //how many record after from
    private String order; //order by what field?
    private String by; //asc or desc
    private Long fromDate;
    private Long toDate;
    private List<Integer> filterStatus;

    //
    private String donateId;
}
