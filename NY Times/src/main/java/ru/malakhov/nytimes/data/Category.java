package ru.malakhov.nytimes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Category implements Parcelable {
    private final int mId;
    private final String mName;

    public Category(int id, String name) {
        mId = id;
        mName = name;
    }

    protected Category(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        return mId == category.mId && Objects.equals(mName, category.mName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mName);
    }

    @Override
    public String toString() {
        return "Category{"
                + "mId=" + mId
                + ", mName='" + mName + '\''
                + '}';
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
    }
}
