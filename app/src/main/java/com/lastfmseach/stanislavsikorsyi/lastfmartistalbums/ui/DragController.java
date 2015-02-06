package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.adapters.AlbumAdapter;

/**
 * Created by stanislavsikorsyi on 06.02.15.
 */
public class DragController implements RecyclerView.OnItemTouchListener {
    public static final int ANIMATION_DURATION = 100;

    private RecyclerView recyclerView;
    private ImageView overlay;
    private final GestureDetectorCompat gestureDetector;

    private boolean isDragging = false;
    private View draggingView;
    private boolean isFirst = true;
    private long draggingId = -1;
    private float startY = 0f;
    private Rect startBounds = null;

    public DragController(RecyclerView recyclerView, ImageView overlay) {
        this.recyclerView = recyclerView;
        this.overlay = overlay;
        GestureDetector.SimpleOnGestureListener longClickGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                isDragging = true;
                dragStart(e.getX(), e.getY());
            }
        };
        this.gestureDetector = new GestureDetectorCompat(recyclerView.getContext(), longClickGestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (isDragging) {
            return true;
        }
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        int y = (int) e.getY();
        if (e.getAction() == MotionEvent.ACTION_UP) {
            dragEnd();
            isDragging = false;
        } else {
            drag(y);
        }
    }

    private void dragStart(float x, float y) {
        draggingView = recyclerView.findChildViewUnder(x, y);
        View first = recyclerView.getChildAt(0);
        isFirst = draggingView == first;
        startY = y - draggingView.getTop();
        paintViewToOverlay(draggingView);
        overlay.setTranslationY(y - startY);
        draggingView.setVisibility(View.INVISIBLE);
        draggingId = recyclerView.getChildItemId(draggingView);
        startBounds = new Rect(draggingView.getLeft(), draggingView.getTop(), draggingView.getRight(), draggingView.getBottom());
    }

    private void drag(int y) {
        overlay.setTranslationY(y - startY);
        if (!isInPreviousBounds()) {
            View view = recyclerView.findChildViewUnder(0, y);
            if (recyclerView.getChildPosition(view) != 0 && view != null) {
                swapViews(view);
            }
        }
    }

    private void swapViews(View current) {
        long replacementId = recyclerView.getChildItemId(current);
        AlbumAdapter adapter = (AlbumAdapter) recyclerView.getAdapter();
        int start = adapter.getPositionForId(replacementId);
        int end = adapter.getPositionForId(draggingId);
        adapter.moveItem(start, end);
        if (isFirst) {
            recyclerView.scrollToPosition(end);
            isFirst = false;
        }
        startBounds.top = current.getTop();
        startBounds.bottom = current.getBottom();
    }

    private void dragEnd() {
        overlay.setImageBitmap(null);
        draggingView.setVisibility(View.VISIBLE);
        float translationY = overlay.getTranslationY();
        draggingView.setTranslationY(translationY - startBounds.top);
        draggingView.animate().translationY(0f).setDuration(ANIMATION_DURATION).start();
    }

    private void paintViewToOverlay(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        overlay.setImageBitmap(bitmap);
        overlay.setTop(0);
    }

    public boolean isInPreviousBounds() {
        float overlayTop = overlay.getTop() + overlay.getTranslationY();
        float overlayBottom = overlay.getBottom() + overlay.getTranslationY();
        return overlayTop < startBounds.bottom && overlayBottom > startBounds.top;
    }

}