package com.example.ipctest;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public int bookId;
    public String bookName;
    public Book(int bookId,String bookName){
        this.bookId = bookId;
        this.bookName = bookName;

    }

    @Override
    public String toString(){
        return  bookId+": "+bookName;
    }
    public Book(Parcel source) {
        bookId = source.readInt();
        bookName = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int hashCode() {
        return bookId + bookName.hashCode();
    }
    public Boolean equals(Book book1,Book book2){
        if(book1.bookName == book2.bookName&&book1.bookId==book2.bookId){
            return true;
        }
        else {
            return false;
        }
    }
}
