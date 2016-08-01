package com.augmentis.ayp.crimin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Amita on 7/18/2016.
 */
public class Crime {
    private UUID id;
    private  String title;
    private Date crimeDate;
    private boolean solved;

    public Date getCrimedate() {
        return crimeDate;
    }

    public void setCrimedate(Date crimeDate) {
        this.crimeDate = crimeDate;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID uuid){
        this.id = uuid;
        crimeDate = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UUID= ").append(id);
        builder.append(",Title= ").append(title);
        builder.append(",Crime Date= ").append(crimeDate);
        builder.append(",Solved= ").append(solved);
        return builder.toString();
    }
}
