package org.td024.entity;

import jakarta.persistence.Embeddable;
import org.td024.exception.InvalidTimeIntervalException;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class Interval implements Serializable {
    private Date startTime;
    private Date endTime;

    protected Interval() {
    }

    Interval(IntervalBuilder builder) {
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static boolean isOverlap(Interval interval1, Interval interval2) {
        return !(interval1.endTime.before(interval2.startTime) || interval2.endTime.before(interval1.startTime));
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public static final class IntervalBuilder {
        private Date startTime;
        private Date endTime;

        public IntervalBuilder startTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public IntervalBuilder endTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public Interval build() throws InvalidTimeIntervalException {
            if (startTime.after(endTime))
                throw new InvalidTimeIntervalException("Start time must be before the end time!");

            return new Interval(this);
        }
    }
}
