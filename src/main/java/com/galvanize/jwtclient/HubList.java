package com.galvanize.jwtclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HubList {

    private List<Hub> hubs;

    public HubList() {hubs = new ArrayList<>();}

    public HubList(List<Hub> hubs) {this.hubs = hubs;}

    public List<Hub> getHubs() {
        return hubs;
    }

    public void setHubs(List<Hub> hubs) {
        this.hubs = hubs;
    }

    public boolean isEmpty() {
        return hubs.isEmpty();
    }

    @Override
    public String toString(){
        return "HubList{" +
                "hubs="+ hubs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HubList hubList = (HubList) o;
        return Objects.equals(hubs, hubList.hubs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hubs);
    }


}
