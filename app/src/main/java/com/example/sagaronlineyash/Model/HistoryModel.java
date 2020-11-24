package com.example.sagaronlineyash.Model;

public class HistoryModel {
    String searched , search_id;

    public HistoryModel(String searched, String search_id) {
        this.searched = searched;
        this.search_id = search_id;
    }

    public String getSearched() {
        return searched;
    }

    public void setSearched(String searched) {
        this.searched = searched;
    }

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }
}
