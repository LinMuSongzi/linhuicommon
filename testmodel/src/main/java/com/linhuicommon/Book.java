package com.linhuicommon;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    private String name;
    private int id;
    private String organization;
    private String company;
    private String author;
    private long update_time;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(update_time);
        dest.writeString(name);
        dest.writeString(organization);
        dest.writeString(company);
        dest.writeString(author);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    protected Book(Parcel dest) {
        id = dest.readInt();
        update_time = dest.readLong();
        name =  dest.readString();
        organization =  dest.readString();
        company =  dest.readString();
        author =  dest.readString();
    }

    public Book() {
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public void readFromParcel(Parcel dest){
        id = dest.readInt();
        update_time = dest.readLong();
        name =  dest.readString();
        organization =  dest.readString();
        company =  dest.readString();
        author =  dest.readString();
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", organization='" + organization + '\'' +
                ", company='" + company + '\'' +
                ", author='" + author + '\'' +
                ", update_time=" + update_time +
                '}';
    }
}
