
package ru.malakhov.nytimes.ui;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorationNews extends RecyclerView.ItemDecoration {

    private final int mSpacing;
    private final int mOrientation;
    private final int mSpanCount;

    public ItemDecorationNews(int spacing, int orientation) {
        mSpacing = spacing;
        mOrientation = orientation;
        mSpanCount = 0;
    }

    public ItemDecorationNews(int spanCount, int spacing, int orientation) {
        mSpacing = spacing;
        mOrientation = orientation;
        mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
            @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        // задаем одинаковое расстояние между элементаци
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (position == 0){
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing;
            outRect.left = mSpacing;
            outRect.right = mSpacing;
        } else {
            int column = position % mSpanCount;
            outRect.left = mSpacing - column * mSpacing / mSpanCount;
            outRect.right = (column + 1) * mSpacing / mSpanCount;
            if (position < mSpanCount) {
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing;
        }
    }
}
