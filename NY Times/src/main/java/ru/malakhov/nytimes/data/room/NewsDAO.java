package ru.malakhov.nytimes.data.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NewsDAO {

    @Query("SELECT * FROM news")
    List<NewsEntity> getAll();

    @Query("SELECT * FROM news WHERE section = :section")
    List<NewsEntity> getAll(String section);

    @Query("SELECT * FROM news WHERE id = :id")
    NewsEntity getNewsById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NewsEntity... newsEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsEntity newsEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void edit(NewsEntity newsEntity);

    @Delete
    void delete(NewsEntity newsEntity);

    @Query("DELETE FROM news WHERE section = :section")
    void deleteAll(String section);
}
