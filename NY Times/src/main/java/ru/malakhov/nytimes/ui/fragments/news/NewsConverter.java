package ru.malakhov.nytimes.ui.fragments.news;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.malakhov.nytimes.data.network.dto.ResultDto;
import ru.malakhov.nytimes.data.room.AppDatabase;
import ru.malakhov.nytimes.data.room.NewsEntity;

public class NewsConverter {

    private final static int LIST_IMAGE_SIZE = 1;
    public final static String KEY_NO_IMAGE = "no";

    public static List<NewsEntity> dtoToDao(List<ResultDto> listDto, String newsSection){
        List<NewsEntity> listDao = new ArrayList<>();
        for (ResultDto dto : listDto){
            NewsEntity newsEntity = new NewsEntity();

            newsEntity.setId(dto.getUrl()+newsSection);
            newsEntity.setUrl(dto.getUrl());
            newsEntity.setSection(newsSection);
            newsEntity.setCategory(dto.getCategory());
            newsEntity.setTitle(dto.getTitle());
            newsEntity.setPublishedDate(dto.getPublishedDate());
            newsEntity.setText(dto.getText());
            if (dto.getImage().size() != 0){
                newsEntity.setImage(dto.getImage().get(LIST_IMAGE_SIZE).getUrl());
            } else {
                newsEntity.setImage(KEY_NO_IMAGE);
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
