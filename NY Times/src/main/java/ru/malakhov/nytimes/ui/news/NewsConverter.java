package ru.malakhov.nytimes.ui.news;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.malakhov.nytimes.data.network.dto.ResultDto;
import ru.malakhov.nytimes.data.room.AppDatabase;
import ru.malakhov.nytimes.data.room.NewsEntity;

public class NewsConverter {

    private final static int LIST_IMAGE_SIZE = 1;

    public static List<NewsEntity> dtoToDao(List<ResultDto> listDto, String newsCategory){
        List<NewsEntity> listDao = new ArrayList<>();
        for (ResultDto dto : listDto){
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setId(dto.getUrl()+newsCategory);
            newsEntity.setUrl(dto.getUrl());
            newsEntity.setSection(newsCategory);
            newsEntity.setCategory(dto.getCategory());
            newsEntity.setTitle(dto.getTitle());
            newsEntity.setPublishedDate(dto.getPublishedDate());
            newsEntity.setText(dto.getText());
            if (dto.getMultimedia().size() != 0){
                newsEntity.setImageUrl(dto.getMultimedia().get(LIST_IMAGE_SIZE).getUrl());
            } else {
                newsEntity.setImageUrl("");
            }
            listDao.add(newsEntity);
        }
        return listDao;
    }

    public static NewsEntity getNewsById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.newsDao().getNewsById(id);
    }

    public static List<NewsEntity> loadNewsFromDb(Context context, String section) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.newsDao().getAll(section);
    }

    public static void saveAllNewsToDb(Context context, List<NewsEntity> list, String section){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.newsDao().deleteAll(section);

        NewsEntity mas[] = list.toArray(new NewsEntity[list.size()]);
        db.newsDao().insertAll(mas);
    }

    public static void editNewsToDb(Context context, NewsEntity newsEntity){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.newsDao().edit(newsEntity);
    }

    public static void deleteNewsFromDb(Context context, NewsEntity newsEntity){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.newsDao().delete(newsEntity);
    }
}
