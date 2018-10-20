package com.zohaltech.app.grewords.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.activities.VocabulariesActivity;
import com.zohaltech.app.grewords.classes.App;
import com.zohaltech.app.grewords.classes.LearningStatus;

import java.util.ArrayList;

import widgets.CircleProgress;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private static final long DURATION = 300;

    Activity activity;
    ArrayList<Integer> lessons = new ArrayList<>();
    ArrayList<ProgressDetailStatus> progressDetailStatuses;

    public LessonAdapter(Activity activity, ArrayList<Integer> lessons) {
        this.activity = activity;
        this.lessons = lessons;
        this.progressDetailStatuses = new ArrayList<>();
        for (int i = 0; i < lessons.size(); i++) {
            progressDetailStatuses.add(new ProgressDetailStatus(i, false));
        }
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                                             ? ViewGroup.LayoutParams.WRAP_CONTENT
                                             : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(DURATION);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(DURATION);
        v.startAnimation(a);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int lesson = lessons.get(position);
        holder.txtLessonNumber.setText("" + lesson);
        holder.txtLesson.setText("Lesson " + lesson);

        holder.layoutRoot.setOnClickListener(v -> {
            Intent intent = new Intent(App.currentActivity, VocabulariesActivity.class);
            intent.putExtra(VocabulariesActivity.LESSON, lesson);
            App.currentActivity.startActivity(intent);
        });
        holder.layoutProgressDetail.setVisibility(View.GONE);

        LearningStatus status = LearningStatus.getLearningStatusByLesson(lesson);
        if (status != null) {
            holder.layoutDivider.setVisibility(View.VISIBLE);
            holder.layoutCircleProgress.setVisibility(View.VISIBLE);
            holder.circleProgress.setProgress(status.getProgress());

            if (progressDetailStatuses.get(position).visible) {
                holder.layoutProgressDetail.setVisibility(View.VISIBLE);
            } else {
                holder.layoutProgressDetail.setVisibility(View.GONE);
            }

            holder.txtVocabProgress.setText(String.format("Vocab %d/%d", status.getVocabIndex(), status.getVocabCount()));

            holder.layoutCircleProgress.setOnClickListener(v -> {
                ProgressDetailStatus status1 = progressDetailStatuses.get(position);
                if (status1.visible) {
                    collapse(holder.layoutProgressDetail);
                    App.handler.postDelayed(() -> holder.layoutProgressDetail.setVisibility(View.GONE), DURATION);
                    status1.visible = false;
                } else {
                    holder.layoutProgressDetail.setVisibility(View.VISIBLE);
                    //ViewCompat.animate(holder.layoutProgressDetail).scaleYBy(12).setDuration(1000).start();
                    expand(holder.layoutProgressDetail);
                    status1.visible = true;
                }
            });
        } else {
            holder.layoutDivider.setVisibility(View.GONE);
            holder.layoutCircleProgress.setVisibility(View.GONE);
            holder.layoutProgressDetail.setVisibility(View.GONE);
            progressDetailStatuses.set(position, new ProgressDetailStatus(position, false));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout   layoutRoot;
        public TextView       txtLessonNumber;
        public TextView       txtLesson;
        public CircleProgress circleProgress;
        public LinearLayout   layoutDivider;
        public LinearLayout   layoutCircleProgress;
        public LinearLayout   layoutProgressDetail;
        public TextView       txtVocabProgress;

        public ViewHolder(View view) {
            super(view);
            layoutRoot = view.findViewById(R.id.layoutRoot);
            txtLessonNumber = view.findViewById(R.id.txtLessonNumber);
            txtLesson = view.findViewById(R.id.txtLesson);
            layoutDivider = view.findViewById(R.id.layoutDivider);
            layoutCircleProgress = view.findViewById(R.id.layoutCircleProgress);
            circleProgress = view.findViewById(R.id.circleProgress);
            layoutProgressDetail = view.findViewById(R.id.layoutProgressDetail);
            txtVocabProgress = view.findViewById(R.id.txtVocabProgress);
        }
    }

    private class ProgressDetailStatus {
        public int     position;
        public boolean visible;

        public ProgressDetailStatus(int position, boolean visible) {
            this.position = position;
            this.visible = visible;
        }
    }
}
