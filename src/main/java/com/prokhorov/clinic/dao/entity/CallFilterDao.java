package com.prokhorov.clinic.dao.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class CallFilterDao {
    private List<String> statuses;
    private List<Boolean> statusPay;
    private DateSituation registration;
    private DateSituation accepted;
    private DateSituation finished;

    @Getter
    @Setter
    public class DateSituation {
        private Timestamp beginTime;
        private Timestamp endTime;

        public boolean isBetween(Timestamp timestamp) {
            if (beginTime != null) {
                if (endTime != null) {
                    return timestamp.after(beginTime) && timestamp.before(endTime);
                } else {
                    return timestamp.after(beginTime);
                }
            } else {
                if (endTime != null) {
                    return timestamp.before(endTime);
                } else {
                    return true;
                }
            }
        }
    }
}
